package com.example.springfirst.tools

import android.util.Log
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object NetTool{

    private const val TAG = "NetTool"

    fun sendGet(url:String , hashMap: HashMap<String,String> = hashMapOf()): InputStream? {
        //定义一个字符串拼接对象
        val mUrl = StringBuilder("$url?")
        for (x in hashMap){
            mUrl.append("${x.key}=${x.value}&")
        }

        //删除mUrl的最后一个‘&’符号

        mUrl.delete(mUrl.length-1,mUrl.length)

        val connection : HttpURLConnection = URL(mUrl.toString())
            .openConnection() as HttpURLConnection

        try {
            connection.apply {
                doOutput=true
                connectTimeout=5000
                readTimeout=5000
                requestMethod="GET"
                defaultUseCaches=false
            }
        }catch (e:Exception){
            Log.e(TAG, "sendGet: 连接配置异常" )
        }


        //测试代码 ： 把请求数据先打印出来

//        try{
//            connection.inputStream.bufferedReader().forEachLine {
//                Log.e(TAG, "sendGet: $it" )
//            }
//        }catch (e:java.lang.Exception){
//            Log.e(TAG, "sendGet: 发送请求出错")
//        }

        return connection.inputStream
    }


    fun sendPost(url: String, hashMap: HashMap<String, String> = hashMapOf()): InputStream? {
        val mUrl = StringBuilder("$url?")
        for (x in hashMap){
            mUrl.append("${x.key}=${x.value}&")
        }
        //去掉mUrl最后的一个&保证url的合法性
        mUrl.delete(mUrl.length-1,mUrl.length)

        val connection : HttpURLConnection = URL(mUrl.toString())
            .openConnection() as HttpURLConnection

        try {
            connection.apply {
                connectTimeout=5000
                readTimeout=5000
                requestMethod="POST"

                doInput=true
                doOutput=true
            }
        }catch (e:Exception){
            Log.e(TAG, "sendPost: 连接配置异常" )
        }


        //测试代码 ： 把请求数据先打印出来

//        try{
//            connection.inputStream.bufferedReader().forEachLine {
//                Log.e(TAG, "sendGet: $it" )
//            }
//        }catch (e:java.lang.Exception){
//            Log.e(TAG, "sendGet: 发送请求出错")
//        }


        //返回一个字节流
        return connection.inputStream
    }
}