package com.kiki.myalbum.presenter

import android.content.Context
import androidx.loader.app.LoaderManager
import com.kiki.myalbum.entity.AlbumViewData
import com.kiki.myalbum.entity.ImageScanResult
import com.kiki.myalbum.model.ImageScannerModel
import com.kiki.myalbum.model.ImageScannerModelImpl
import com.kiki.myalbum.view.AlbumView

class ImageScannerPresenterImpl : ImageScannerPresenter {

    private var mScannerModel: ImageScannerModel? = null
    private var mAlbumView: AlbumView? = null

    constructor(albumView: AlbumView ){
        mAlbumView = albumView
        mScannerModel = ImageScannerModelImpl()
    }
    override fun startScanImage(context: Context, loaderManager: LoaderManager) {
        mScannerModel!!.startScanImage(context, loaderManager, object :
            ImageScannerModel.OnScanImageFinish {

            override fun onFinish(imageScanResult: ImageScanResult?) {
                if (mAlbumView != null) {
                    val albumData: AlbumViewData? =
                        mScannerModel!!.archiveAlbumInfo(context, imageScanResult!!)
                    mAlbumView!!.refreshAlbumData(albumData)
                }
            }
        })

    }
}