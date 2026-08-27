package com.dayron.neondodge

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.view.*
import android.content.Context
import kotlin.math.*
import kotlin.random.Random

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); window.setFlags(1024,1024); setContentView(GameView(this)) }
}

data class Enemy(var x:Float,var y:Float,var r:Float,var speed:Float)

class GameView(ctx: Context): View(ctx) {
    private val p=Paint(Paint.ANTI_ALIAS_FLAG); private val enemies=mutableListOf<Enemy>(); private var px=0f; private var py=0f; private var pr=34f; private var score=0; private var best=ctx.getSharedPreferences("game",0).getInt("best",0); private var over=false; private var last=System.nanoTime(); private var spawn=0f
    init { p.typeface=Typeface.create("sans",Typeface.BOLD); setBackgroundColor(Color.rgb(5,5,16)) }
    override fun onSizeChanged(w:Int,h:Int,ow:Int,oh:Int){ px=w/2f; py=h*.78f }
    override fun onDraw(c:Canvas){ super.onDraw(c); val now=System.nanoTime(); val dt=((now-last)/1e9).toFloat().coerceAtMost(.04f); last=now; if(!over) update(dt); drawGame(c); postInvalidateOnAnimation() }
    private fun update(dt:Float){ spawn-=dt; if(spawn<=0){ val r=Random.nextInt(20,48).toFloat(); enemies+=Enemy(Random.nextFloat()*(width-2*r)+r,-r,r,250f+score*.8f); spawn=(.7f-score*.002f).coerceAtLeast(.18f) }; val it=enemies.iterator(); while(it.hasNext()){ val e=it.next(); e.y+=e.speed*dt; if(hypot(e.x-px,e.y-py)<e.r+pr){ over=true; if(score>best){best=score; context.getSharedPreferences("game",0).edit().putInt("best",best).apply()} }; if(e.y>height+e.r){it.remove();score++} } }
    private fun drawGame(c:Canvas){ p.color=Color.rgb(0,229,255); c.drawCircle(px,py,pr,p); p.color=Color.rgb(255,45,110); enemies.forEach{c.drawCircle(it.x,it.y,it.r,p)}; p.textSize=48f;p.color=Color.WHITE;c.drawText("SCORE $score",28f,65f,p);p.textSize=30f;c.drawText("BEST $best",28f,105f,p); if(over){p.textAlign=Paint.Align.CENTER;p.textSize=66f;c.drawText("GAME OVER",width/2f,height*.42f,p);p.textSize=32f;c.drawText("TAP TO RESTART",width/2f,height*.49f,p);p.textAlign=Paint.Align.LEFT} }
    override fun onTouchEvent(e:MotionEvent):Boolean { if(e.action==MotionEvent.ACTION_DOWN && over){ enemies.clear();score=0;over=false;px=width/2f;py=height*.78f;return true }; if(!over && (e.action==MotionEvent.ACTION_DOWN||e.action==MotionEvent.ACTION_MOVE)){px=e.x.coerceIn(pr,width-pr);py=e.y.coerceIn(pr,height-pr)}; return true }
}
