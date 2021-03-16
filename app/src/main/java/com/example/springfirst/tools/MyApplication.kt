package com.example.springfirst.tools

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    companion object{
        var context:Context? = null
    }

    override fun getApplicationContext(): Context {
        context = super.getApplicationContext()
        return context!!
    }
}