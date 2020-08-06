package com.kiki.myalbum.view

import com.kiki.myalbum.entity.AlbumFolderInfo
import com.kiki.myalbum.entity.AlbumViewData

interface AlbumView {

    fun refreshAlbumData(albumData: AlbumViewData?)

    fun switchAlbumFolder(albumFolderInfo: AlbumFolderInfo?)
}