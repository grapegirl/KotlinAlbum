package com.kiki.myalbum.view

import com.kiki.myalbum.entity.ImageInfo

interface ImageChooseView {
    fun refreshSelectedCounter(imageInfo: ImageInfo?)
}