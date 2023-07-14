package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class Mycustomadabter(private val ctx: Context,private val arrayList:ArrayList<Mydata>): ArrayAdapter<Mydata>(ctx,R.layout.list_item,arrayList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
       val inflater:LayoutInflater= LayoutInflater.from(ctx);
        val view:View=inflater.inflate(R.layout.list_item,null)

        val imageView:ImageView=view.findViewById(R.id.list_item)
        val txt:TextView=view.findViewById(R.id.list_text)

        imageView.setImageResource(arrayList[position].img)
        txt.text=arrayList[position].name

        return view
    }
}