package com.iorihinata.boom
import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.ScaleGestureDetector
class BoomGameView(context:Context,val world:StructuralWorld):GLSurfaceView(context){
val renderer=BoomRenderer(world);private var x=0f;private var y=0f;private var moved=false
private val scale=ScaleGestureDetector(context,object:ScaleGestureDetector.SimpleOnScaleGestureListener(){override fun onScale(d:ScaleGestureDetector):Boolean{renderer.zoom(d.scaleFactor);return true}})
init{setEGLContextClientVersion(2);setRenderer(renderer);renderMode=RENDERMODE_CONTINUOUSLY;isFocusable=true}
override fun onTouchEvent(e:MotionEvent):Boolean{scale.onTouchEvent(e);when(e.actionMasked){MotionEvent.ACTION_DOWN->{x=e.x;y=e.y;moved=false;return true};MotionEvent.ACTION_MOVE->if(e.pointerCount==1){val dx=e.x-x;val dy=e.y-y;if(kotlin.math.abs(dx)+kotlin.math.abs(dy)>8)moved=true;renderer.orbit(dx,dy);x=e.x;y=e.y};MotionEvent.ACTION_UP->{if(!moved)renderer.selectNext();performClick();return true}};return true}
override fun performClick():Boolean{super.performClick();return true}
}
