package com.example.springfirst.imageloader

import android.app.Activity
import androidx.fragment.app.Fragment

class ImageLoader {
    companion object{
        fun with(activity: Activity):RequestBuilder{
            return RequestBuilder.create(activity)
        }
        fun with(fragment: Fragment):RequestBuilder{
            return RequestBuilder.create(fragment)
        }
    }
}