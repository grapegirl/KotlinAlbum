package com.kiki.myalbum.manager

import android.os.Environment
import java.io.File

class FolderManager {
    private val APP_FOLDER_NAME = "album"
    private val CRASH_LOG_FOLDER_NAME = "crash"

    fun getAppFolder(): File? {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val appFolder = File(
                Environment.getExternalStorageDirectory(),
                APP_FOLDER_NAME
            )
            createOnNotFound(appFolder)
        } else {
            null
        }
    }

    fun getCrashLogFolder(): File? {
        val appFolder =
            FolderManager.getAppFolder()
        return if (appFolder != null) {
            val crashLogFolder = File(
                appFolder,
                CRASH_LOG_FOLDER_NAME
            )
            createOnNotFound(crashLogFolder)
        } else {
            null
        }
    }

    private fun createOnNotFound(folder: File?): File? {
        if (folder == null) {
            return null
        }
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return if (folder.exists()) {
            folder
        } else {
            null
        }
    }
}