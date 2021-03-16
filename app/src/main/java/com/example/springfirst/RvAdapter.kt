package com.example.springfirst

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.springfirst.imageloader.ImageLoader
import kotlinx.android.synthetic.main.item.view.*

class RvAdapter : RecyclerView.Adapter<RvAdapter.MyViewHolder>() {
    var list:MutableList<Test> = mutableListOf()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView:ImageView = itemView.findViewById(R.id.item_image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false))
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val picture1="http://p1.music.126.net/6ua9l_ZP6OEbaIalaGxnrQ==/109951165679670878.jpg"

        holder.itemView.apply {
           ImageLoader.with(this).load(picture1).into(item_image_view)
        }
    }
}