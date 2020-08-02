package com.kiki.myalbum.entity

import java.io.File

data class ImageScanResult(
    var albumFolderList : List<File>,
    var albumImageListMap : Map<String, ArrayList<File>>
)
