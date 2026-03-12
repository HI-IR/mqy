package com.mqy.mqy.post.service.impl

import com.mqy.mqy.common.config.filter.CustomUserDetail
import com.mqy.mqy.common.koltinmapper.ktQuery
import com.mqy.mqy.common.koltinmapper.ktUpdate
import com.mqy.mqy.common.utils.upload.OssUtil
import com.mqy.mqy.common.utils.upload.StsProvider
import com.mqy.mqy.database.entity.cat.CatEntity
import com.mqy.mqy.database.entity.posts.MediaType
import com.mqy.mqy.database.entity.posts.PostEntity
import com.mqy.mqy.database.entity.posts.PostMediaEntity
import com.mqy.mqy.database.entity.user.PostLikeEntity
import com.mqy.mqy.database.entity.user.UserEntity
import com.mqy.mqy.post.mapper.*
import com.mqy.mqy.post.pojo.dto.AddPostsDTO
import com.mqy.mqy.post.pojo.dto.MediasUploadUrlDTO
import com.mqy.mqy.post.pojo.vo.*
import com.mqy.mqy.post.service.PostsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class PostsServiceImpl(
	private val ossUtil: OssUtil,
	private val stsProvider: StsProvider,
	private val mediaMapper: PostMediaMapper,
	private val postMapper: PostMapper,
	private val userMapper: UserMapper,
	private val catMapper: CatMapper,
	private val postsLikeMapper: PostsLikeMapper
) : PostsService {
	override fun getMediasUploadUrl(dto: MediasUploadUrlDTO): MediasUploadUrlVO {
		if (dto.files.isEmpty()) return MediasUploadUrlVO(emptyList())
		// 记得复用一个sts凭证，减轻先阿里云请求sts凭证的压力
		val sts = stsProvider.getCredentials(sessionName = "PostFilesUpload")
		val dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
		val file = dto.files.map { fileInfo ->
			val uuid = UUID.randomUUID().toString().replace("-", "")
			val objectKey = "posts/$dateStr-$uuid-${fileInfo.filename}"
			val presignedUrl = ossUtil.generatePresignedPutUrl(
				objectKey = objectKey,
				contentType = fileInfo.contentType,
				accessKeyId = sts.accessKeyId,
				accessKeySecret = sts.accessKeySecret,
				securityToken = sts.securityToken
			)
			Credential(
				fileInfo.filename,
				objectKey,
				presignedUrl
			)
		}
		return MediasUploadUrlVO(file)
	}

	@Transactional
	override fun addPosts(dto: AddPostsDTO): AddPostsVO {
		val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUserDetail).id.toLong()
		val postEntity = PostEntity().apply {
			this.userId = userId
			this.catId = dto.catId
			this.title = dto.title
			this.content = dto.content
		}
		postMapper.insert(postEntity)
		val postId = postEntity.id ?: throw RuntimeException("帖子无法生成id，请稍后重试")
		if (dto.medias.isEmpty()) {
			return AddPostsVO(postId)
		}
		val mediasEntity = dto.medias.map { media ->
			val url = ossUtil.getTheCompleteURL(media.objectKey)
			val thumbnailUrl = if (media.mediaType == MediaType.VIDEO) ossUtil.getVideoThumbnailUrl(url) else url
			PostMediaEntity().apply {
				this.postId = postId
				this.mediaType = media.mediaType
				this.url = url
				this.thumbnailUrl = thumbnailUrl
				this.width = media.width
				this.height = media.height
			}
		}
		mediaMapper.insertBatch(mediasEntity)
		postMapper.increasePostCount(userId)
		return AddPostsVO(postId)
	}

	override suspend fun getPostDetail(userId: Long, postId: Long): PostDetailVO {
		// 没在spring中试过协程，但是因为这个函数需要查5次表格，使用协程优化一下性能
		val postEntity = postMapper.selectById(postId) ?: throw RuntimeException("未找到该帖子")
		return supervisorScope {
			val userDeferred = async(Dispatchers.IO) {
				runCatching {
					userMapper.selectById(postEntity.userId) ?: UserEntity()
				}.getOrDefault(UserEntity())
			}
			val catDeferred = async(Dispatchers.IO) {
				runCatching {
					catMapper.selectById(postEntity.catId) ?: CatEntity()
				}.getOrDefault(CatEntity())
			}
			val mediaDeferred = async(Dispatchers.IO) {
				runCatching {
					val mediaSelectWrapper = ktQuery<PostMediaEntity>().eq(PostMediaEntity::postId, postId)
					val mediaEntities = mediaMapper.selectList(mediaSelectWrapper) ?: emptyList()
					mediaEntities.map { media ->
						DetailMedia(
							width = media.width ?: 0,
							height = media.height ?: 0,
							url = media.url.orEmpty(),
							mediaType = media.mediaType,
							thumbnailURL = media.thumbnailUrl.orEmpty()
						)
					}
				}.getOrDefault(emptyList())
			}

			val isLikeDeferred = async(Dispatchers.IO) {
				runCatching {
					if (userId > 0) {
						val isLikeWrapper = ktQuery<PostLikeEntity>()
							.eq(PostLikeEntity::postId, postId)
							.eq(PostLikeEntity::userId, userId)
						postsLikeMapper.selectCount(isLikeWrapper) > 0
					} else {
						false
					}
				}.getOrDefault(false)
			}
			val userEntity = userDeferred.await()
			val catEntity = catDeferred.await()
			val mediaList = mediaDeferred.await()
			val isLike = isLikeDeferred.await()

			PostDetailVO(
				author = Author(
					avatarURL = userEntity.avatarUrl.orEmpty(),
					userID = userEntity.id ?: 0L,
					username = userEntity.username ?: "已注销用户"
				),
				cat = Cat(
					avatar = catEntity.avatar.orEmpty(),
					catID = catEntity.id ?: 0L,
					name = catEntity.name ?: "未知猫咪"
				),
				content = postEntity.content.orEmpty(),
				createTime = postEntity.createTime?.toString().orEmpty(),
				medias = mediaList,
				postID = postId,
				stats = PostStats(
					isLiked = isLike,
					likeCount = postEntity.likeCount ?: 0
				),
				title = postEntity.title.orEmpty()
			)
		}
	}

	override fun postsSearchCat(
		keyword: String,
		limit: Long
	): List<PostsSearchCatVO> {
		val wrapper = ktQuery<CatEntity>()
			.like(CatEntity::name, keyword)
			.last("LIMIT $limit")
		val entities = catMapper.selectList(wrapper) ?: emptyList()
		return entities.map {
			PostsSearchCatVO(
				it.id ?: 0,
				it.name ?: ""
			)
		}
	}

	@Transactional
	override fun likePost(id: Long) {
		val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUserDetail).id.toLong()
		val postEntity = postMapper.selectById(id) ?: throw RuntimeException("未找到帖子")

		val existLike = postsLikeMapper.findAnyStatusLikeRecord(id, userId)
		if (existLike != null && existLike.deleted == 0) {
			throw RuntimeException("已点赞，请勿重复操作")
		}

		if (existLike != null) {
			postsLikeMapper.restoreLikeStatus(existLike.id!!)
		} else {
			val postLikeEntity = PostLikeEntity().apply {
				this.userId = userId
				this.postId = id
				this.deleted = 0
			}
			postsLikeMapper.insert(postLikeEntity)
		}

		val likeCountWrapper = ktUpdate<PostEntity>().eq(PostEntity::id, id).setSql("like_count = like_count + 1")
		postMapper.update(PostEntity(), likeCountWrapper)

		postsLikeMapper.increaseLikeCount(userId)
		postsLikeMapper.increaseLikeReceivedCount(postEntity.userId!!)
	}

	@Transactional
	override fun unLikePost(id: Long) {
		val user = SecurityContextHolder.getContext().authentication.principal as CustomUserDetail
		val userId = user.id.toLong()

		val postEntity = postMapper.selectById(id) ?: throw RuntimeException("未找到帖子")
		val existLike = postsLikeMapper.findLikedRecord(id, userId) ?: throw RuntimeException("你还没点赞哟")

		// 更新 点赞记录表
		val postLikeWrapper = ktUpdate<PostLikeEntity>().eq(PostLikeEntity::id, existLike.id)
			.set(PostLikeEntity::deleted, 1)
		postsLikeMapper.update(PostLikeEntity(), postLikeWrapper)

		// 更新posts表上的点赞数据
		val likeCountWrapper =
			ktUpdate<PostEntity>().eq(PostEntity::id, id).gt(PostEntity::likeCount, 0).setSql("like_count = like_count - 1")
		postMapper.update(PostEntity(), likeCountWrapper)

		postsLikeMapper.decreaseLikeCount(userId)
		postsLikeMapper.decreaseLikeReceivedCount(postEntity.userId!!)
	}

}