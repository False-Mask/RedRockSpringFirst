package com.example.springfirst.imageloader

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.springfirst.tools.MyApplication
import com.example.springfirst.tools.NetTool
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap


private const val TAG = "RequestBuilder"
class RequestBuilder private constructor(){


    private var imageView:ImageView? = null

    private var view:View? = null


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

        fun create(view: View): RequestBuilder {
            builder.view = view
            return RequestBuilder()
        }
    }

    fun load(url:String,hashMap: HashMap<String,String> = HashMap()):RequestBuilder{
        //加载图片
        //异步发送消息获取

        //在一级缓存中读取内存

        val observable = Observable.create<String> {
            it.onNext("1")
            it.onComplete()
        }
            .observeOn(io())
            .map { t ->
                var bitmap =ImageCache.getBitmapFromCache(t)
                //当缓存中没有读取到的时候就在复用池中寻找是否存在该CaChe
                //如果还是没有
                if (bitmap == null){
                    val reusable  = ImageCache.getReUsable("1",660,600,1)
                    bitmap = getImageFromNet(url,hashMap,reusable)
                    //ImageCache.putBitmapToCathe("1",reusable)
                    Log.e(TAG, "三级缓存：从网络缓存中获取Bitmap" )
                }else{
                    Log.e(TAG, "load: 从LruCathe中获取到变量" )
                }
                bitmap
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t -> imageView?.setImageBitmap(t) }

        return this

    }

    //从网络加载数据
    private fun getImageFromNet(
        url: String,
        hashMap: java.util.HashMap<String, String>,
        reusable: Bitmap?
    ): Bitmap {
            val input = NetTool.sendGet(url, hashMap)
            val option = BitmapFactory.Options()
            option.apply {
                if (reusable!=null){
                    inBitmap = reusable
                }
                inMutable = true
                //inDensity = Bitmap.DENSITY_NONE
                //inScaled = false
            }
            val bitmap = BitmapFactory.decodeStream(input,null,option)!!
            ImageCache.putBitmapToCathe("1",bitmap)
        return bitmap
        //imageView?.setImageBitmap(bitmap)
    }


    fun into(view:ImageView):RequestBuilder{
        //
        //对加载的图片的位置进行处理
        this.imageView= view
        return  this
    }

}