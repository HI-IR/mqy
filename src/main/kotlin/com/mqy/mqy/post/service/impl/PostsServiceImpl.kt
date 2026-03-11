package com.mqy.mqy.post.service.impl

import com.mqy.mqy.common.config.filter.CustomUserDetail
import com.mqy.mqy.common.utils.upload.OssUtil
import com.mqy.mqy.common.utils.upload.StsProvider
import com.mqy.mqy.database.entity.posts.MediaType
import com.mqy.mqy.database.entity.posts.PostEntity
import com.mqy.mqy.database.entity.posts.PostMediaEntity
import com.mqy.mqy.post.mapper.PostMapper
import com.mqy.mqy.post.mapper.PostMediaMapper
import com.mqy.mqy.post.pojo.dto.AddPostsDTO
import com.mqy.mqy.post.pojo.dto.MediasUploadUrlDTO
import com.mqy.mqy.post.pojo.vo.AddPostsVO
import com.mqy.mqy.post.pojo.vo.Credential
import com.mqy.mqy.post.pojo.vo.MediasUploadUrlVO
import com.mqy.mqy.post.service.PostsService
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
	private val postMapper: PostMapper
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
		return AddPostsVO(postId)
	}

}