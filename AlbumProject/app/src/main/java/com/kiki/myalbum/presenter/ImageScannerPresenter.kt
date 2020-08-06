package com.kiki.myalbum.presenter

import android.content.Context
import androidx.loader.app.LoaderManager

interface ImageScannerPresenter {
    fun startScanImage(
        context: Context,
        loaderManager: LoaderManager
    )
}