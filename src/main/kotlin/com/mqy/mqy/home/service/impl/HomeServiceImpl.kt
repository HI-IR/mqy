package com.mqy.mqy.home.service.impl

import com.mqy.mqy.common.koltinmapper.ktQuery
import com.mqy.mqy.database.entity.cat.CatEntity
import com.mqy.mqy.database.entity.posts.PostEntity
import com.mqy.mqy.database.entity.posts.PostMediaEntity
import com.mqy.mqy.database.entity.user.PostLikeEntity
import com.mqy.mqy.database.entity.user.UserEntity
import com.mqy.mqy.home.mapper.HomeCatMapper
import com.mqy.mqy.home.mapper.HomeMediaMapper
import com.mqy.mqy.home.mapper.HomePostLikeMapper
import com.mqy.mqy.home.mapper.HomePostsMapper
import com.mqy.mqy.home.pojo.vo.*
import com.mqy.mqy.home.service.HomeService
import com.mqy.mqy.post.mapper.UserMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import org.springframework.stereotype.Service

@Service
class HomeServiceImpl(
	private val postMapper: HomePostsMapper,
	private val mediaMapper: HomeMediaMapper,
	private val catMapper: HomeCatMapper,
	private val postLikeMapper: HomePostLikeMapper,
	private val userMapper: UserMapper
) : HomeService {
	override suspend fun getHomePosts(
		userId: Long,
		catId: Long?,
		cursor: Long?,
		limit: Int,
		keyword: String?
	): GetPostsVO {
		// 先主表查询
		// 按照最新发送排序
		// 预查询，查询limit + 1 个数据，通过查到了limit+1，说明has_more
		val prevPosts = postMapper.selectList(
			ktQuery<PostEntity>()
				.eq(catId != null, PostEntity::catId, catId)
				.lt(cursor != null, PostEntity::id, cursor)//小于cursor
				.like(keyword != null, PostEntity::title, keyword)
				.orderByDesc(PostEntity::id)
				.last("LIMIT ${limit + 1}")
		)
		if (prevPosts.isEmpty()) {
			return GetPostsVO(
				hasMore = false,
				nextCursor = -1,
				posts = emptyList()
			)
		}
		val hasMore = prevPosts.size == limit + 1
		//去掉最后一个
		val posts = if (hasMore) prevPosts.dropLast(1) else prevPosts
		val nextCursor = posts.last().id!!

		val postIds = posts.mapNotNull { it.id }
		val catIds = posts.map { it.catId }.distinct()
		val authIds = posts.map { it.userId }.distinct()

		return supervisorScope {
			val mediaDeferred = async(Dispatchers.IO) {
				runCatching {
					mediaMapper.selectList(ktQuery<PostMediaEntity>().`in`(PostMediaEntity::postId, postIds)).groupBy {
						it.postId!!
					}
				}.getOrDefault(emptyMap())
			}

			val likeSetDeferred = async(Dispatchers.IO) {
				runCatching {
					// 找出这批postIds中和用户相关的有效的点赞记录
					if (userId > 0) {
						val isLikeWrapper = ktQuery<PostLikeEntity>()
							.eq(PostLikeEntity::userId, userId)
							.`in`(PostLikeEntity::postId, postIds)
							.eq(PostLikeEntity::deleted, 0)
						postLikeMapper.selectList(isLikeWrapper).map { it.postId!! }.toSet()
					} else {
						emptySet()
					}
				}.getOrDefault(emptySet())
			}

			// 拿到涉及到的动物信息
			val catDeferred = async(Dispatchers.IO) {
				runCatching {
					catMapper.selectByIds(catIds).associateBy { it.id!! }
				}.getOrDefault(emptyMap())
			}

			val authInfoDeferred = async(Dispatchers.IO) {
				runCatching {
					userMapper.selectList(ktQuery<UserEntity>().`in`(UserEntity::id, authIds)).associateBy { it.id!! }
				}.getOrDefault(emptyMap())
			}

			// postId: Media
			val mediaMap = mediaDeferred.await()

			// 该用户点赞过的集合
			val likeInfo = likeSetDeferred.await()

			// catId: Cat(可以通过post实例找到对应的catId，进而找到Cat)
			val catMap = catDeferred.await()

			// authId: User(可以通过post实例找到对应的authId，进而找到User)
			val authInfoMap = authInfoDeferred.await()

			val postsListVO = posts.map { it ->
				val cat = catMap[it.catId] ?: CatEntity()
				val auth = authInfoMap[it.userId] ?: UserEntity()
				val media = mediaMap[it.id] ?: emptyList()
				val isLike = likeInfo.contains(it.id)

				val mediaItemList = media.map {
					MediaItem(
						mediaType = it.mediaType,
						thumbnailURL = it.thumbnailUrl.orEmpty(),
						url = it.url.orEmpty(),
					)
				}
				PostItem(
					cat = CatItem(
						avatar = cat.avatar.orEmpty(),
						catID = cat.id ?: 0,
						name = cat.name ?: "未知猫咪"
					),
					content = it.content.orEmpty(),
					createTime = it.createTime.toString(),
					interaction = Interaction(
						isLiked = isLike,
						likeCount = it.likeCount ?: 0
					),
					medias = mediaItemList,
					postID = it.id!!,
					publisher = Publisher(
						avatarURL = auth.avatarUrl.orEmpty(),
						userID = auth.id ?: 0,
						username = auth.username ?: "已注销用户"
					),
					title = it.title.orEmpty()
				)
			}
			GetPostsVO(
				hasMore = hasMore,
				nextCursor = nextCursor,
				posts = postsListVO
			)
		}
	}

	override suspend fun getGallery(
		cursor: Long?,
		limit: Int,
		state: Int?,
		keyword: String?
	): GalleryVO {
		val prevCats = catMapper.selectList(
			ktQuery<CatEntity>()
				.lt(cursor != null, CatEntity::id, cursor)//小于cursor
				.eq(state != null, CatEntity::state, state)
				.like(keyword != null, CatEntity::name, keyword)
				.orderByDesc(CatEntity::id)
				.last("LIMIT ${limit + 1}")
		)
		if (prevCats.isEmpty()) {
			return GalleryVO(
				hasMore = false,
				nextCursor = -1,
				cats = emptyList()
			)
		}

		val hasMore = prevCats.size == limit + 1
		//去掉最后一个
		val catsList = if (hasMore) prevCats.dropLast(1) else prevCats
		val nextCursor = catsList.last().id!!

		val cats = catsList.map {
			CatInfo(
				avatar = it.avatar.orEmpty(),
				catID = it.id ?: 0,
				coatColor = it.coatColor!!,
				gender = it.gender!!,
				name = it.name ?: "未知猫咪",
				position = it.position.orEmpty(),
				state = it.state!!
			)
		}
		return GalleryVO(
			cats = cats,
			hasMore = hasMore,
			nextCursor = nextCursor
		)
	}
}