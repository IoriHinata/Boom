package com.iorihinata.boom
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Locale

class MainActivity:Activity(){
    private lateinit var world:StructuralWorld;private lateinit var game:BoomGameView;private lateinit var status:TextView
    override fun onCreate(state:Bundle?){super.onCreate(state);world=StructuralWorld(this);game=BoomGameView(this,world)
        val root=FrameLayout(this);root.addView(game,FrameLayout.LayoutParams(-1,-1))
        val hud=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(18,14,18,14);setBackgroundColor(0xCC10151D.toInt())}
        hud.addView(TextView(this).apply{text="BOOM  •  STRUCTURAL PHYSICS";textSize=19f;setTextColor(Color.WHITE)})
        status=TextView(this).apply{textSize=13f;setTextColor(0xFFD7E2EE.toInt())};hud.addView(status)
        root.addView(hud,FrameLayout.LayoutParams(-2,-2).apply{gravity=Gravity.TOP or Gravity.START;leftMargin=16;topMargin=16})
        val controls=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(10,10,10,10);setBackgroundColor(0xCC10151D.toInt())}
        fun addButton(label:String,action:()->Unit){controls.addView(Button(this).apply{text=label;setOnClickListener{action();updateStatus()}})}
        addButton("START / PAUSE"){game.renderer.running=!game.renderer.running}
        addButton("EARTHQUAKE"){world.disaster=if(world.disaster==Disaster.EARTHQUAKE)Disaster.NONE else Disaster.EARTHQUAKE}
        addButton("WIND"){world.disaster=if(world.disaster==Disaster.WIND)Disaster.NONE else Disaster.WIND}
        addButton("TORNADO"){world.disaster=if(world.disaster==Disaster.TORNADO)Disaster.NONE else Disaster.TORNADO}
        addButton("FLOOD"){world.disaster=if(world.disaster==Disaster.FLOOD)Disaster.NONE else Disaster.FLOOD}
        addButton("RESET"){world.reset();game.renderer.selectedBuilding=0}
        addButton("DEBUG"){game.renderer.debug=!game.renderer.debug}
        root.addView(controls,FrameLayout.LayoutParams(-2,-2).apply{gravity=Gravity.END or Gravity.CENTER_VERTICAL;rightMargin=16})
        setContentView(root);updateStatus()
    }
    private fun updateStatus(){
        val b=world.buildings.getOrNull(game.renderer.selectedBuilding);val failed=b?.elements?.count{it.failed}?:0;val stressed=b?.elements?.count{it.stressPa>it.strength*0.7f}?:0
        status.text="BUILDING "+(b?.name?:"-")+"  •  "+world.disaster+"  •  t="+String.format(Locale.US,"%.1f",world.simulationTime)+"s\nfailed="+failed+"  stressed="+stressed+"  • drag=orbit  pinch=zoom  tap=select"
    }
}
