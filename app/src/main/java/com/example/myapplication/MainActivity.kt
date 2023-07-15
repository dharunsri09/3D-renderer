package com.example.myapplication
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userArrayList: ArrayList<Mydata>
    private lateinit var fileNames:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val assetManager = this.assets

        // Get a list of all the files in the assets folder.
        val taleFile = assetManager.list("models/")

        fileNames=ArrayList()

        if (taleFile != null) {
            for(f in taleFile){
                if(f.contains(".glb")){
                    fileNames.add(f)
                }
            }
        }

        binding.floatingActionButton1.setOnClickListener{
            val intent=Intent(this,SlideViewer::class.java)
            intent.putExtra("name",fileNames)
            startActivity(intent)
        }

        userArrayList= ArrayList()
        for (fileName in fileNames) {
                userArrayList.add(Mydata(R.drawable.download,fileName))
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