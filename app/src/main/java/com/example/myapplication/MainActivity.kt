package com.example.myapplication

import android.content.Intent
import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.utils.*;
import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.view.LayoutInflater
import android.view.SurfaceView
import android.widget.ListView
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.filament.Skybox
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userArrayList: ArrayList<Mydata>
    private lateinit var fileNames:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val myList= mutableListOf<Mydata>()

        val assetManager = this.assets

        // Get a list of all the files in the assets folder.
        val taleFile = assetManager.list("models/")

        fileNames=ArrayList()

        if (taleFile != null) {
            for(f in taleFile){
                if(f.contains(".glb")||f.contains(".gltf")){
                    fileNames.add(f)
                }
            }
        }

        userArrayList= ArrayList()
        if (fileNames != null) {
            for (fileName in fileNames) {
                    userArrayList.add(Mydata(R.drawable.download,fileName))
            }
        }
        binding.listview.isClickable=true
        binding.listview.adapter=Mycustomadabter(this,userArrayList)
        binding.listview.setOnItemClickListener{parent,view,position,id->
            val imageView=R.drawable.download
            val name=fileNames?.get(position)
            val i=Intent(this,Renderer::class.java)
            i.putExtra("name",name)
            startActivity(i)
        }

    }


}