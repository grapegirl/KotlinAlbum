package com.kiki.myalbum.entity

import java.io.File
import java.util.*

class ImageInfo(it: File?, b: Boolean)
{
    private val serialVersionUID : Long = -3753345306395582567L
    lateinit var imageFile : File
    var isSelected : Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val imageInfo = other as ImageInfo

        return if (isSelected != imageInfo.isSelected) false
        else imageFile == imageInfo.imageFile
    }

    override fun hashCode(): Int {
        var result: Int = imageFile.hashCode()
        result = 31 * result + if (isSelected) 1 else 0
        return result
    }


    /**
     * @param imageFileList
     * @return
     */
    fun buildFromFileList(imageFileList: List<File?>): List<ImageInfo>? {
        return if (imageFileList != null) {
            val imageInfoArrayList: MutableList<ImageInfo> =
                ArrayList()
            for (imageFile in imageFileList) {
                val imageInfo = imageFile?.let { ImageInfo(it, false ) }
                if (imageInfo != null) {
                    imageInfoArrayList.add(imageInfo)
                }
            }
            imageInfoArrayList
        } else {
            null
        }
    }
}