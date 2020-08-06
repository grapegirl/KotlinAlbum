package com.kiki.myalbum.model

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.kiki.myalbum.R
import com.kiki.myalbum.entity.AlbumFolderInfo
import com.kiki.myalbum.entity.AlbumViewData
import com.kiki.myalbum.entity.ImageInfo
import com.kiki.myalbum.entity.ImageScanResult
import java.io.File
import java.util.*

class ImageScannerModelImpl : ImageScannerModel {

    private val TAG =
        ImageScannerModelImpl::class.java.simpleName

    /**
     * Loader的唯一ID号
     */
    private val IMAGE_LOADER_ID = 1000

    /**
     * 加载数据的映射
     */
    private val IMAGE_PROJECTION = arrayOf(
        MediaStore.Images.Media.DATA,  //图片路径
        MediaStore.Images.Media.DISPLAY_NAME,  //图片文件名，包括后缀名
        MediaStore.Images.Media.TITLE //图片文件名，不包含后缀
    )

    private var mOnScanImageFinish: ImageScannerModel.OnScanImageFinish? = null


    private val mRefreshHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val imageScanResult = msg.obj as ImageScanResult
            if (mOnScanImageFinish != null && imageScanResult != null) {
                mOnScanImageFinish!!.onFinish(imageScanResult)
            }
        }
    }


    override fun archiveAlbumInfo(
        context: Context,
        imageScanResult: ImageScanResult
    ): AlbumViewData? {


        return if (imageScanResult != null) {

            val albumFolderList: List<File> = imageScanResult.albumFolderList
            val albumImageListMap: Map<String, ArrayList<File>> = imageScanResult.albumImageListMap

            if (albumFolderList != null && albumFolderList.size > 0 && albumImageListMap != null) {

                val albumFolderInfoList: MutableList<AlbumFolderInfo> =
                    ArrayList<AlbumFolderInfo>()


                val allImageFolder: AlbumFolderInfo? =
                    createAllImageAlbum(context, albumImageListMap)
                if (allImageFolder != null) {
                    albumFolderInfoList.add(allImageFolder)
                }
                val albumFolderSize = albumFolderList.size
                for (albumFolderPos in 0 until albumFolderSize) {

                    val albumFolder = albumFolderList[albumFolderPos]
                    val folderName = albumFolder.name
                    val albumPath = albumFolder.absolutePath
                    val albumImageList: List<File> =
                        albumImageListMap[albumPath]!!
                    val frontCover = albumImageList[0]

                    val imageInfoList: List<ImageInfo>? = ImageInfo(null, false).buildFromFileList(albumImageList)

                    allImageFolder!!.imageInfoList = imageInfoList!!
                    val albumFolderInfo = AlbumFolderInfo(folderName, imageInfoList, frontCover)

                    albumFolderInfoList.add(albumFolderInfo)
                }
                val albumViewData = AlbumViewData(albumFolderInfoList)
                return albumViewData
            }
            null
        } else {
            null
        }
    }

    private fun createAllImageAlbum(
        context: Context,
        albumImageListMap: Map<String, ArrayList<File>>
    ): AlbumFolderInfo? {

        if (albumImageListMap != null) {

            val folderName = context.getString(R.string.all_image)
            val totalImageInfoList: List<ImageInfo> =
                ArrayList<ImageInfo>()

            var isFirstAlbum = true //是否是第一个目录
            val albumKeySet = albumImageListMap.keys

            var albumFolderInfo: AlbumFolderInfo? = null
            for (albumKey in albumKeySet) { //每个目录的图片
                val albumImageList: List<File> =
                    albumImageListMap[albumKey]!!
                if (isFirstAlbum == true) {
                    val frontCover = albumImageList[0]
                    albumFolderInfo = AlbumFolderInfo(folderName, totalImageInfoList, frontCover)
                    isFirstAlbum = false
                }
            }
            return albumFolderInfo
        } else {
            return null
        }

    }

    private fun sortByFileLastModified(files: ArrayList<File>) {
        Collections.sort(
            files,
            object : Comparator<File?> {

                override fun compare(lhs: File?, rhs: File?): Int {
                    if (lhs!!.lastModified() > rhs!!.lastModified()) {
                        return -1
                    } else if (lhs.lastModified() < rhs.lastModified()) {
                        return 1
                    }
                    return 0
                }
            })
    }

    override fun startScanImage(
        context: Context,
        loaderManger: LoaderManager,
        onScanImageFinish: ImageScannerModel.OnScanImageFinish
    ) {

        mOnScanImageFinish = onScanImageFinish

        val loaderCallbacks: LoaderManager.LoaderCallbacks<Cursor> =
            object : LoaderManager.LoaderCallbacks<Cursor> {

                override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                    Log.i(TAG, "-----onCreateLoader-----")
                    return CursorLoader(
                        context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER
                    )
                }

                override fun onLoaderReset(loader: Loader<Cursor>) {
                    Log.i(TAG, "-----onLoaderReset-----")
                }

                override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
                    Log.i(TAG, "-----onLoadFinished-----")
                    if (data!!.count == 0) {
                        onScanImageFinish?.onFinish(null)
                    } else {
                        val dataColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DATA)
                        //int displayNameColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                        //int titleColumnIndex = data.getColumnIndex(MediaStore.Images.Media.TITLE);
                        val albumFolderList =
                            ArrayList<File>()
                        val albumImageListMap =
                            HashMap<String, ArrayList<File>>()
                        while (data.moveToNext()) {
                            val imageFile =
                                File(data.getString(dataColumnIndex)) //图片文件
                            val albumFolder = imageFile.parentFile //图片目录
                            if (!albumFolderList.contains(albumFolder)) {
                                albumFolderList.add(albumFolder)
                            }
                            val albumPath = albumFolder.absolutePath
                            var albumImageFiles =
                                albumImageListMap[albumPath]
                            if (albumImageFiles == null) {
                                albumImageFiles = ArrayList()
                                albumImageListMap[albumPath] = albumImageFiles
                            }
                            albumImageFiles.add(imageFile) //添加到对应的相册目录下面
                        }



                        sortByFileLastModified(albumFolderList) //对图片目录做排序
                        val keySet: Set<String> = albumImageListMap.keys
                        for (key in keySet) { //对图片目录下所有的图片文件做排序
                            val albumImageList =
                                albumImageListMap[key]!!
                            sortByFileLastModified(albumImageList)
                        }
                        val imageScanResult =
                            ImageScanResult(albumFolderList, albumImageListMap)

                        //Fix CursorLoader Bug
                        //http://stackoverflow.com/questions/7746140/android-problems-using-fragmentactivity-loader-to-update-fragmentstatepagera
                        val message = mRefreshHandler.obtainMessage()
                        message.obj = imageScanResult
                        mRefreshHandler.sendMessage(message)
                    }
                }
            }
        loaderManger.initLoader(IMAGE_LOADER_ID, null, loaderCallbacks) //初始化指定id的Loader
    }
}

