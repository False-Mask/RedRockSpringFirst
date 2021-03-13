package com.example.springfirst.tools

import org.json.JSONObject
import java.util.*

object ParseJson {
    fun <T> fromJson(beanClass:Class<T>){
        var mClass = beanClass.newInstance()
        for(i in beanClass.declaredFields){
            val name = i.name
            val iClass = i.javaClass
            if (iClass is List<*>){

            }else if (i.javaClass.declaredClasses.isEmpty()){
//                i.set(mClass)
            }
        }
    }
}