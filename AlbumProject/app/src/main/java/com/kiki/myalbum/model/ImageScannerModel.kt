package com.kiki.myalbum.model

import android.content.Context
import androidx.loader.app.LoaderManager
import com.kiki.myalbum.entity.AlbumViewData
import com.kiki.myalbum.entity.ImageScanResult

interface ImageScannerModel {

    fun startScanImage(context: Context, loaderManger : LoaderManager, onScanImageFinish: OnScanImageFinish)

    fun archiveAlbumInfo(context: Context, imageFinish: ImageScanResult) : AlbumViewData?


    interface OnScanImageFinish {
        fun onFinish(imageScanResult: ImageScanResult?);
    }
}

