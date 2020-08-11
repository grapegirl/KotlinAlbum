package com.kiki.myalbum.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kiki.myalbum.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_image_loader.setOnClickListener(this)
        btn_system_album.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val viewId = v.id
        if (viewId == R.id.btn_system_album) { //系统相册
            val albumIntent = Intent(this, AlbumActivity::class.java)
            startActivity(albumIntent)
        } else if (viewId == R.id.btn_image_loader) { //网络图片加载（各大加载图片框架的实现）
        }
    }
}
