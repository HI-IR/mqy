package com.mqy.mqy.user.service.impl

import com.mqy.mqy.common.config.filter.CustomUserDetail
import com.mqy.mqy.user.mapper.UserInfoMapper
import com.mqy.mqy.user.mapper.UserStatisticInfoMapper
import com.mqy.mqy.user.service.UserInfoService
import com.mqy.mqy.user.vo.UserInfoVO
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserInfoServiceImpl(
	private val userInfoMapper: UserInfoMapper,
	private val userStatisticInfoMapper: UserStatisticInfoMapper
) : UserInfoService {
	override fun getUserInfo(): UserInfoVO {
		val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUserDetail).id.toLong()
		val user = userInfoMapper.selectById(userId) ?: throw RuntimeException("未找到该用户")
		val userStatisticInfo = userStatisticInfoMapper.selectById(userId) ?: throw RuntimeException("未找到该用户")
		return UserInfoVO(
			avatarURL = user.avatarUrl ?: "",
			name = user.username ?: "爱心人士",
			userID = userId,
			userFollowCount = userStatisticInfo.totalFollowingCount ?: 0,
			userLikeCount = userStatisticInfo.totalLikesCount ?: 0,
			userLikeReceived = userStatisticInfo.totalLikesReceived ?: 0,
			userPostCount = userStatisticInfo.totalPostCount ?: 0
		)
	}
}