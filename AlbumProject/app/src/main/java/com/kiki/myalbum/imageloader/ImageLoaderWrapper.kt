package com.kiki.myalbum.imageloader

import android.widget.ImageView
import java.io.File
import com.kiki.myalbum.entity.DisplayOption

interface ImageLoaderWrapper {

    fun displayImage(
        imageView: ImageView?,
        imageFile: File?,
        option: DisplayOption?
    )

    fun displayImage(
        imageView: ImageView?,
        imageUrl: String?,
        option: DisplayOption?
    )
}