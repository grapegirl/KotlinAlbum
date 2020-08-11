package com.kiki.myalbum.ui.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import com.kiki.myalbum.R
import com.kiki.myalbum.entity.DisplayOption
import com.kiki.myalbum.imageloader.ImageLoaderFactory
import com.kiki.myalbum.imageloader.ImageLoaderWrapper
import com.kiki.myalbum.test.album.ui.activity.ImageSelectActivity
import com.kiki.myalbum.ui.activity.base.BaseActivity
import kotlinx.android.synthetic.main.activity_image_select.*
import java.io.File




class ImageSelectActivity : BaseActivity(), View.OnClickListener {

    val EXTRA_SELECTED_IMAGE_LIST = "selectImage"

    private var mSelectedImageGridView: GridView? = null
    private var mSelectedImageList: List<File>? = null

    fun getSelectedImageList() : List<File>? {
        return mSelectedImageList
    }

    private var mImageLoaderWrapper: ImageLoaderWrapper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image_select)

        iv_back.setOnClickListener(this)

        mImageLoaderWrapper = ImageLoaderFactory.getInstance()
        mSelectedImageList = intent.getSerializableExtra(EXTRA_SELECTED_IMAGE_LIST) as List<File>

        mSelectedImageGridView = gv_image_selected as GridView
        mSelectedImageGridView!!.setAdapter(SelectedImageGridAdapter())

    }

    override fun onClick(v: View?) {
        if (v != null) {
            if(v.id == iv_back.id){
                onBackPressed()
            }
        }
    }

    private class SelectedImageHolder {
        var selectedImageView: ImageView? = null
    }

    private class SelectedImageGridAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return if (mSelectedImageList == null) {
                0
            } else
                mSelectedImageList.size
        }

        override fun getItem(position: Int): Any {
            return mSelectedImageList.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(
            position: Int,
            convertView: View,
            parent: ViewGroup
        ): View {
            var convertView = convertView
            var holder: SelectedImageHolder? =
                null
            if (convertView == null) {
                holder = SelectedImageHolder()
                convertView = View.inflate(
                    parent.context,
                    R.layout.selected_image_item,
                    null
                )
                val gridItemSpacing = RuleUtils.convertDp2Px(parent.context, 2) as Int
                val gridEdgeLength: Int =
                    (RuleUtils.getScreenWidth(parent.context) - gridItemSpacing * 2) / 3
                val layoutParams =
                    AbsListView.LayoutParams(gridEdgeLength, gridEdgeLength)
                convertView.layoutParams = layoutParams
                holder.selectedImageView =
                    convertView.findViewById<View>(R.id.iv_selected_item) as ImageView
                convertView.tag = holder
            } else {
                holder =
                    convertView.tag as SelectedImageHolder
            }
            val displayOption = DisplayOption(R.mipmap.img_default, R.mipmap.img_error)

            mImageLoaderWrapper.displayImage(
                holder.selectedImageView,
                mSelectedImageList.get(position),
                displayOption
            )
            return convertView
        }
    }

}