package com.example.springfirst.imageloader

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.util.LruCache
import java.io.File
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.HashSet
import kotlin.concurrent.thread
private const val TAG = "ImageCache"
object ImageCache {
    //memoryClass/8*1024*1024
    var lruCache:LruCache<String,Bitmap> = object : LruCache<String,Bitmap>(5000000){
        override fun sizeOf(key: String?, value: Bitmap?): Int {
            //进行版本适配
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
                return  value?.allocationByteCount ?: 0
            }
            return value?.byteCount ?: 0
        }

        //当LruCache最少使用的内容被移除缓存区的时候调用
        override fun entryRemoved(
            evicted: Boolean,
            key: String?,
            oldValue: Bitmap?,
            newValue: Bitmap?
        ) {
            //如果Bitmap式可以复用的
            if (oldValue?.isMutable == true){
                //把Bitmap加入到复用池
                //weakReference.put(key, WeakReference(oldValue, getReferenceQue()))
                (weakReference as MutableSet<Bitmap>).add(oldValue)
                Log.e(TAG, "some just added to the reusablePool" )
                Log.e(TAG, "The Pool now length is ${weakReference?.size}" )
            }else{
                //如果不可复用就直接转到native层处理回收掉
                oldValue?.recycle()
                Log.e(TAG, "the bitmap can't be reuse" )
            }
        }
    }

    //val diskLruCache:DiskLruCache

    //复用池
    var weakReference:Set<WeakReference<Bitmap>>?=Collections.synchronizedSet(HashSet<WeakReference<Bitmap>>())


    //初始化LruCathe池
    fun init(context: Context,dir:String):ImageCache{
        //复用池
        //弱引用
        //初始化
        weakReference = Collections.synchronizedSet(HashSet<WeakReference<Bitmap>>())
        val activityManager:ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //为LruCache提供一个最大的大小
        val memoryClass = activityManager.memoryClass

        return this
    }

    //将一个key-value放入LruCache内
    fun putBitmapToCathe(key: String,value:Bitmap):ImageCache{
        lruCache?.put(key, value)
        Log.e(TAG, "this:  $this" )
        return this
    }

    //将一个key-value移除
    fun removeBitmapFromCache(key: String):ImageCache{
        lruCache?.remove(key)
        return this
    }
    //获取一个Bitmap(通过一个key)
    fun getBitmapFromCache(key: String): Bitmap? {
        Log.e(TAG, "Cache Length is ${lruCache?.size()}" )
        return lruCache?.get(key)
    }
    //清空Cache
    fun clearTheCache():ImageCache{
        lruCache?.evictAll()
        return  this
    }

    //引用队列
    var referenceQueue:ReferenceQueue<Bitmap> ? = null

    const val boolean=true

    //监听gc回收
    //主动加快回收
    private fun getReferenceQue():ReferenceQueue<Bitmap>{
        if (referenceQueue==null){
            referenceQueue = ReferenceQueue()
        }else{
            thread {
                while (!boolean){
                    val bitmapReference = referenceQueue!!.remove()
                    val bitmap = bitmapReference.get()
                    //bitmap没有被回收 并且不为空
                    if (bitmap!=null && !bitmap.isRecycled){
                        //清理掉bitmap
                        bitmap.recycle()
                        Log.e(TAG, "Gc Get References" )
                    }
                }
            }
        }
        return referenceQueue!!
    }

    fun getReUsable(key:String, width:Int, height:Int, scalePercent:Int): Bitmap? {
        //当Api小于11时候bitmap貌似不支持缓存
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB){
            return null
        }
        val iterator:Iterator<WeakReference<Bitmap>> ?= weakReference?.iterator()
        var reusable:Bitmap? = null
        while (iterator?.hasNext() == true){
            val bitmap = iterator.next().get()
            if (checkIsReusable(bitmap,660,600,1)){
                reusable = bitmap
                break
            }
            (weakReference as MutableSet<Bitmap>).remove(bitmap)
        }
        return reusable
    }

    private fun checkIsReusable(
        bitmap: Bitmap?,
        width: Int,
        height: Int,
        scalePercent: Int
    ): Boolean {
        return bitmap?.allocationByteCount!! >= width*height*(if (bitmap.config == Bitmap.Config.ARGB_8888) 4 else 2)
    }


    //地址
    private lateinit var path:String

    fun setDirPath(dir: String?): ImageCache {
        path = dir?:""
        return this
    }

    //读取bitmap
    fun write(bitmap: Bitmap,key:String): Boolean {
        val file = File("$path/$key")
        val out = file.outputStream().buffered()
        val boolean = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        return boolean
    }

    //写入
    fun read(key:String):Bitmap?{
        val bitmap = BitmapFactory.decodeFile("$path/$key")
        if (bitmap!=null){
            putBitmapToCathe(key,bitmap)
        }
        return bitmap
    }

}