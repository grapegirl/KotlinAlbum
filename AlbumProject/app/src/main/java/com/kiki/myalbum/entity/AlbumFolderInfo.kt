package com.kiki.myalbum.entity

import java.io.File

data class AlbumFolderInfo (
    var folderName : String,
    var imageInfoList : List<ImageInfo>,
    var frontCover : File )
{
    override fun toString() = "AlbumFolderInfo"

    override fun hashCode(): Int {
        var result = if (folderName != null) folderName.hashCode() else 0
        result = 31 * result + if (imageInfoList != null) imageInfoList.hashCode() else 0
        result = 31 * result + if (frontCover != null) frontCover.hashCode() else 0
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as AlbumFolderInfo

        if (if (folderName != null) folderName != that.folderName else that.folderName != null) return false
        return if (if (imageInfoList != null) imageInfoList != that.imageInfoList else that.imageInfoList != null) false
        else !if (frontCover != null) frontCover != that.frontCover else that.frontCover != null

        return super.equals(other)
    }
}