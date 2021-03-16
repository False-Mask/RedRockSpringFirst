package com.example.springfirst
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.springfirst.imageloader.ImageCache
import com.example.springfirst.imageloader.ImageLoader
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.lang.ref.ReferenceQueue

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val string1="http://111.200.241.244:57806/?a=1&b=2"

        val picture1="http://p1.music.126.net/6ua9l_ZP6OEbaIalaGxnrQ==/109951165679670878.jpg"

        val string2 = "http://sandyz.ink:3000/cloudsearch"

        val job= Job()


        val mutableList= mutableListOf<Test>()

        recycler_view.adapter = RvAdapter()

        recycler_view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        val thread = CoroutineScope(job)
        //ImageLoader.with(this).load(picture1).into(findViewById(R.id.i))

    }

    companion object{
        const val TAG = "MainActivity"
    }
}