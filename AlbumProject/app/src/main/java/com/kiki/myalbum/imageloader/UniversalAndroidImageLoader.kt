package com.kiki.myalbum.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.kiki.myalbum.R
import com.kiki.myalbum.entity.DisplayOption
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.core.download.ImageDownloader
import java.io.File

class UniversalAndroidImageLoader  : ImageLoaderWrapper {

    override fun displayImage(imageView: ImageView?, imageFile: File?, option: DisplayOption?) {

        var imageLoadingResId: Int = R.mipmap.img_default
        var imageErrorResId: Int = R.mipmap.img_error
        if (option != null) {
            imageLoadingResId = option.loadingResId
            imageErrorResId = option.loadErrorResId
        }
        val options: DisplayImageOptions = DisplayImageOptions.Builder()
            .showImageOnLoading(imageLoadingResId)
            .showImageForEmptyUri(imageErrorResId)
            .showImageOnFail(imageErrorResId)
            .cacheInMemory(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build()

        val uri: String
        uri = if (imageFile == null) { ""
        } else {
            ImageDownloader.Scheme.FILE.wrap(imageFile.absolutePath)
        }
        ImageLoader
            .getInstance().displayImage(uri, imageView, options)



    }

    override fun displayImage(imageView: ImageView?, imageUrl: String?, option: DisplayOption?) {

        val HTTP : String = "http"
        val HTTPS : String = "https"

        var imageLoadingResId: Int = R.mipmap.img_default
        var imageErrorResId: Int = R.mipmap.img_error
        if (option != null) {
            imageLoadingResId = option.loadingResId
            imageErrorResId = option.loadErrorResId
        }
        val options: DisplayImageOptions = DisplayImageOptions.Builder()
            .showImageOnLoading(imageLoadingResId)
            .showImageForEmptyUri(imageErrorResId)
            .showImageOnFail(imageErrorResId)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build()
        if (imageUrl!!.startsWith(HTTPS)) {
            val uri: String = ImageDownloader.Scheme.HTTPS.wrap(imageUrl)
            ImageLoader.getInstance().displayImage(uri, imageView, options)
        } else if (imageUrl!!.startsWith(HTTP)) {
            val uri: String = ImageDownloader.Scheme.HTTP.wrap(imageUrl)
            ImageLoader.getInstance().displayImage(uri, imageView, options)
        }
    }



    fun init(context: Context?) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        val config = ImageLoaderConfiguration.Builder(context)
        config.threadPriority(Thread.NORM_PRIORITY - 2)
        config.denyCacheImageMultipleSizesInMemory()
        config.diskCacheFileNameGenerator(Md5FileNameGenerator())
        config.diskCacheSize(50 * 1024 * 1024) // 50 MiB

        config.tasksProcessingOrder(QueueProcessingType.LIFO)
        config.writeDebugLogs() // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build())
    }


}