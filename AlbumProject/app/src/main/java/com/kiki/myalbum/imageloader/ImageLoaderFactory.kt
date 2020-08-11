package com.kiki.myalbum.imageloader

class ImageLoaderFactory private constructor() {

    companion object {
        @Volatile private var instance : ImageLoaderWrapper? = null

        @JvmStatic fun getInstance() : ImageLoaderWrapper  =
            instance ?: synchronized(this){
                instance ?: ImageLoaderFactory().also {
                    instance = UniversalAndroidImageLoader()//<link>https://github.com/nostra13/Android-Universal-Image-Loader</link>
                }
            } as ImageLoaderWrapper
    }

    //HOW TO
    //val instance = ImageLoaderFactory.getInstance()
}