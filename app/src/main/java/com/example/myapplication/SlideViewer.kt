package com.example.myapplication


import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.utils.*
import android.os.Bundle
import android.view.Choreographer
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.SurfaceView
import android.widget.TextView
import com.google.android.filament.Skybox
import java.nio.ByteBuffer



class SlideViewer : AppCompatActivity(),GestureDetector.OnGestureListener{
    companion object {
        const val MIN_DISTANCE=150
        init {
            Utils.init()
        }
    }

    lateinit var gestureDetector: GestureDetector
    var x2:Float=0.0f
    var x1:Float=0.0f
    var y2:Float=0.0f
    var y1:Float=0.0f
    var y3:Float=0.0f

    private lateinit var textView: TextView
    private lateinit var surfaceView: SurfaceView
    private lateinit var choreographer: Choreographer
    private lateinit var modelViewer: ModelViewer
    private lateinit var fileNames:ArrayList<String>
    private  var i:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_renderer1)
        surfaceView = findViewById(R.id.SurView)
        choreographer = Choreographer.getInstance()
        modelViewer = ModelViewer(surfaceView)
        surfaceView.setOnTouchListener(modelViewer)
        textView=findViewById(R.id.textview)
        modelViewer.scene.skybox = Skybox.Builder().build(modelViewer.engine)
        fileNames= intent.getStringArrayListExtra("name") as ArrayList<String>
        callof1(fileNames,i)
        loadEnvironment("venetian_crossroads_2k")
        val asset = modelViewer.asset!!
        val rm = modelViewer.engine.renderableManager
        for (entity in asset.entities) {
            val renderable = rm.getInstance(entity)
            if (renderable == 0) {
                continue
            }
            if (asset.getName(entity) == "Scheibe_Boden_0") {
                rm.setLayerMask(renderable, 0xff, 0x00)
            }
            val material = rm.getMaterialInstanceAt(renderable, 0)
            material.setParameter("emissiveFactor", 0f, 0f, 0f)
        }
        gestureDetector= GestureDetector(this,this)

    }

    private fun callof1(fileNames:ArrayList<String>,i:Int) {

            textView.setText(fileNames.get(i).substring(0,fileNames.get(i).length-4))
            loadGlb(fileNames.get(i))

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event != null) {
            gestureDetector.onTouchEvent(event)
        }
        when(event?.action){
            0->{
                x1=event.x
                y1=event.y
            }
            1->{
                x2=event.x
                y2=event.y

                val valueX:Float=x2-x1
                if(Math.abs(valueX) > SlideViewer.MIN_DISTANCE){

                    if(x2<x1) {
                        if (i + 1 < fileNames.size){
                            i+=1
                            callof1(fileNames, i)
                        }
                        else{
                            i=0
                            callof1(fileNames, i)
                        }
                    }
                    else{
                        if (i - 1 >= 0){
                            i-=1
                            callof1(fileNames, i)
                        }
                        else{
                            i=fileNames.size-1
                            callof1(fileNames, i)
                        }
                    }

                }

            }
        }

        return super.onTouchEvent(event)
    }

    private fun loadEnvironment(ibl: String) {
        // Create the indirect light source and add it to the scene.
        var buffer = readAsset("envs/$ibl/${ibl}_ibl.ktx")
        KtxLoader.createIndirectLight(modelViewer.engine, buffer).apply {
            intensity = 100_000f      //For inc the intensity
            modelViewer.scene.indirectLight = this
        }

        // Create the sky box and add it to the scene.
        buffer = readAsset("envs/$ibl/${ibl}_skybox.ktx")
        KtxLoader.createSkybox(modelViewer.engine, buffer).apply {
            modelViewer.scene.skybox = this
        }
    }

    private val frameCallback = object : Choreographer.FrameCallback {
        private val startTime = System.nanoTime()
        override fun doFrame(currentTime: Long) {
            val seconds = (currentTime - startTime).toDouble() / 1_000_000_000
            choreographer.postFrameCallback(this)
            modelViewer.animator?.apply {
                if (animationCount > 0) {
                    applyAnimation(0, seconds.toFloat())
                }
                updateBoneMatrices()
            }
            modelViewer.render(currentTime)

        }
    }
//    private fun Int.getTransform(): Mat4 {
//        val tm = modelViewer.engine.transformManager
//        return Mat4.of(*tm.getTransform(tm.getInstance(this), null))
//    }
//
//    private fun Int.setTransform(mat: Mat4) {
//        val tm = modelViewer.engine.transformManager
//        tm.setTransform(tm.getInstance(this), mat.toFloatArray())
//    }

    private fun loadGlb(name: String) {
        val buffer = readAsset("models/${name}")
        modelViewer.loadModelGlb(buffer)
        modelViewer.transformToUnitCube()
    }

    private fun readAsset(assetName: String): ByteBuffer {
        val input = assets.open(assetName)

        val bytes = ByteArray(input.available())
        input.read(bytes)
        return ByteBuffer.wrap(bytes)
    }

    override fun onResume() {
        super.onResume()
        choreographer.postFrameCallback(frameCallback)
    }

    override fun onPause() {
        super.onPause()
        choreographer.removeFrameCallback(frameCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        choreographer.removeFrameCallback(frameCallback)
    }

    override fun onDown(p0: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent) {

    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent) {

    }

    override fun onFling(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }
}

