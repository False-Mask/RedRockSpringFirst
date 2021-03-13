package com.example.springfirst.imageloader

import android.app.Activity
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.springfirst.tools.NetTool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestBuilder private constructor(){


    private var view:ImageView? = null


    private val job = Job()
    private val netThread = CoroutineScope(job)

    var activity:Activity? = null
    var fragment:Fragment? = null
    companion object {
        private val builder = RequestBuilder()
        fun create(activity: Activity): RequestBuilder {
            builder.activity = activity
            return RequestBuilder()
        }

        fun create(fragment: Fragment): RequestBuilder {
            builder.fragment = fragment
            return RequestBuilder()
        }
    }

    fun load(url:String,hashMap: HashMap<String,String> = HashMap()):RequestBuilder{
        //加载图片
        //异步发送消息获取
        getImageFromNet(url,hashMap)


        return this

    }

    //从网络加载数据
    private fun getImageFromNet(url: String, hashMap: java.util.HashMap<String, String>) {
        netThread.launch (IO) {
            val input = NetTool.sendGet(url, hashMap)
            val bitmap = BitmapFactory.decodeStream(input)
            withContext(Main){
                view?.setImageBitmap(bitmap)
            }
        }
    }


    fun into(view:ImageView):RequestBuilder{
        //
        //对加载的图片的位置进行处理
        this.view= view
        return  this
    }
}