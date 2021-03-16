package com.example.springfirst.imageloader

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment

class ImageLoader {
    companion object{
        fun with(activity: Activity):RequestBuilder{
            return RequestBuilder.create(activity)
        }
        fun with(fragment: Fragment):RequestBuilder{
            return RequestBuilder.create(fragment)
        }

        fun with(view: View): RequestBuilder {
            return RequestBuilder.create(view)
        }
    }
}