package com.mqy.mqy.cats.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.mqy.mqy.cats.mapper.AdoptMapper
import com.mqy.mqy.cats.mapper.CatsMapper
import com.mqy.mqy.cats.mapper.FollowMapper
import com.mqy.mqy.cats.pojo.dto.AddCatsDTO
import com.mqy.mqy.cats.pojo.dto.AdoptCatDTO
import com.mqy.mqy.cats.pojo.vo.CatDetailVO
import com.mqy.mqy.cats.pojo.vo.CatsAddVO
import com.mqy.mqy.cats.pojo.vo.CatsAvatarUploadVO
import com.mqy.mqy.cats.pojo.vo.UserStats
import com.mqy.mqy.cats.service.CatsService
import com.mqy.mqy.common.config.filter.CustomUserDetail
import com.mqy.mqy.common.utils.upload.OssUtil
import com.mqy.mqy.common.utils.upload.StsProvider
import com.mqy.mqy.database.entity.cat.CatAdoptEntity
import com.mqy.mqy.database.entity.cat.CatEntity
import com.mqy.mqy.database.entity.user.FollowEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class CatsServiceImpl(
	private val stsProvider: StsProvider,
	private val ossUtil: OssUtil,
	private val catMapper: CatsMapper,
	private val followMapper: FollowMapper,
	private val catAdoptMapper: AdoptMapper
) : CatsService {
	override fun getAvatarUploadUrl(): CatsAvatarUploadVO {
		val dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
		val uuid = UUID.randomUUID().toString().replace("-", "")
		val objectKey = "cats/avatar/$dateStr-$uuid.jpg"
		//获取凭证
		val credentials = stsProvider.getCredentials(sessionName = "CatsAvatarUpload")
		val presignedUrl = ossUtil.generatePresignedPutUrl(
			objectKey = objectKey,
			contentType = "image/jpeg",
			accessKeyId = credentials.accessKeyId,
			accessKeySecret = credentials.accessKeySecret,
			securityToken = credentials.securityToken
		)
		return CatsAvatarUploadVO(
			uploadUrl = presignedUrl,
			objectKey = objectKey
		)
	}

	// 添加猫咪
	@Transactional
	override fun addCats(addCatsDTO: AddCatsDTO): CatsAddVO {
		val authentication = SecurityContextHolder.getContext().authentication
		val user = authentication.principal as CustomUserDetail
		catMapper.selectUserByUserId(user.id.toLong()) ?: throw RuntimeException("未找到该用户信息")
		try {
			val targetKey = addCatsDTO.avatarKey
			val catEntity = CatEntity().apply {
				name = addCatsDTO.name
				avatar = ossUtil.getTheCompleteURL(targetKey)
				gender = addCatsDTO.gender
				neuteredStatus = addCatsDTO.neuteredStatus
				birthDate = addCatsDTO.birthData
				arrivalDate = addCatsDTO.arrivalDate
				state = addCatsDTO.state
				coatColor = addCatsDTO.coatColor
				position = addCatsDTO.position
				story = addCatsDTO.story
				creatorId = user.id.toLong()
			}
			catMapper.insert(catEntity)
			val insertedId = catEntity.id!!
			return CatsAddVO(insertedId)
		} catch (e: Exception) {
			throw RuntimeException("添加失败：${e.message}")
		}

	}

	@Transactional
	override fun followCat(id: Long) {
		val cat = catMapper.selectCatByCatId(id) ?: throw RuntimeException("未找到猫咪,请检查传入id是否正确")
		val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUserDetail).id.toLong()

		val existFollow = followMapper.findAnyStatusFollowRecord(id, userId)

		if (existFollow != null) {
			if (existFollow.deleted == 0) {
				throw RuntimeException("您已经关注过该猫咪，请勿重复操作")
			} else {
				followMapper.restoreFollowStatus(existFollow.id!!)
			}
		} else {
			// 从未关注过
			val followEntity = FollowEntity().apply {
				this.userId = userId
				this.catId = cat.id!!
			}
			followMapper.insert(followEntity)
		}
		followMapper.increaseFollowCatCount(userId)
	}

	@Transactional
	override fun unfollowCat(id: Long) {
		val user = SecurityContextHolder.getContext().authentication.principal as CustomUserDetail
		val userId = user.id.toLong()

		catMapper.selectCatByCatId(id) ?: throw RuntimeException("未找到猫咪,请检查传入id是否正确")
		followMapper.findUserFollowCatByCatId(id, user.id.toLong()) ?: throw RuntimeException("未关注该猫咪")

		val wrapper = KtUpdateWrapper(FollowEntity()).eq(FollowEntity::catId, id)
			.eq(FollowEntity::userId, userId).set(FollowEntity::deleted, 1)
		val updateRecord = FollowEntity()
		followMapper.update(updateRecord, wrapper)
		followMapper.decreaseFollowCatCount(user.id.toLong())
	}

	override fun getCatDetail(id: Long): CatDetailVO {
		val catEntity = catMapper.selectCatByCatId(id) ?: throw RuntimeException("未找到猫咪，请检查id是否正确")
		val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUserDetail).id.toLong()
		val followEntity = followMapper.findUserFollowCatByCatId(catId = id, userId = userId)
		val isFollow = followEntity != null
		return CatDetailVO(
			arrivalDate = catEntity.arrivalDate.toString(),
			avatar = catEntity.avatar!!,
			birthDate = catEntity.birthDate.toString(),
			catID = id,
			coatColor = catEntity.coatColor!!,
			gender = catEntity.gender!!,
			name = catEntity.name!!,
			neuteredStatus = catEntity.neuteredStatus!!,
			position = catEntity.position.toString(),
			status = catEntity.state!!,
			userInfo = UserStats(isFollow),
			story = catEntity.story.toString()
		)

	}


	override fun adoptCat(adoptCatDTO: AdoptCatDTO) {
		val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUserDetail).id.toLong()
		val catId = adoptCatDTO.catID
		val catEntity = catMapper.selectCatByCatId(catId) ?: throw RuntimeException("未找到该猫咪，请检查id")
		if (catEntity.state!! != 1) throw RuntimeException("该猫咪已经有主人啦")
		val adoptEntity = CatAdoptEntity().apply {
			this.userId = userId
			this.catId = adoptCatDTO.catID
			this.contactInfo = adoptCatDTO.contactInfo
			this.reason = adoptCatDTO.reason
			this.info = adoptCatDTO.info
			this.isAccept = 0
		}
		catAdoptMapper.insert(adoptEntity)
	}
}
