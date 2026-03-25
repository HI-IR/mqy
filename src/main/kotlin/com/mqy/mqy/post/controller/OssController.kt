package com.mqy.mqy.controller

import com.mqy.mqy.common.core.response.ApiResponse
import com.mqy.mqy.common.utils.upload.OssUtil
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/oss")
class OssController(
    private val ossUtil: OssUtil
) {
    @DeleteMapping("/delete")
    fun deleteFile(@RequestParam fileKey: String): ApiResponse {
        ossUtil.deleteFile(fileKey)
        return ApiResponse.success("文件删除成功")
    }
}
