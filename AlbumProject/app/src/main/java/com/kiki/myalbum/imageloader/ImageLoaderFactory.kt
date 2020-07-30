package com.kiki.myalbum.imageloader

class ImageLoaderFactory private constructor() {

    companion object {
        @Volatile private var instance : ImageLoaderWrapper? = null

        @JvmStatic fun getInstance() : Any =
            instance ?: synchronized(this){
                instance ?: ImageLoaderFactory().also {
                    instance = UniversalAndroidImageLoader()//<link>https://github.com/nostra13/Android-Universal-Image-Loader</link>
                }
            }
    }

    //HOW TO
    //val instance = ImageLoaderFactory.getInstance()
}