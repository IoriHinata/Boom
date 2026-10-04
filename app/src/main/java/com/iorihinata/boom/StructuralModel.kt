package com.iorihinata.boom

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

data class V3(var x: Float, var y: Float, var z: Float) {
    operator fun plus(o: V3) = V3(x + o.x, y + o.y, z + o.z)
    operator fun times(s: Float) = V3(x*s,y*s,z*s)
}
enum class ElementType { FOUNDATION, COLUMN, BEAM, SLAB, WALL, ROOF, STAIR }
enum class Material(val density:Float,val strengthPa:Float,val shearPa:Float) {
    REINFORCED_CONCRETE(2500f,30_000_000f,4_000_000f), STEEL(7850f,250_000_000f,145_000_000f),
    BRICK(1800f,12_000_000f,2_000_000f), GLASS(2500f,40_000_000f,8_000_000f)
}
enum class Disaster { NONE, EARTHQUAKE, WIND, TORNADO, FLOOD }

data class StructuralElement(
    val id:String,val type:ElementType,val material:Material,var position:V3,val size:V3,
    val mass:Float,val strength:Float,val shearStrength:Float,val floor:Int,val primary:Boolean
) {
    var stressPa=0f; var strain=0f; var failed=false; var supported=true
    var velocity=V3(0f,0f,0f); var rotation=V3(0f,0f,0f); var loadN=0f
    fun area()=max(0.01f,size.x*size.z)
}
data class StructuralJoint(
    val id:String,val a:String,val b:String,val shearStrength:Float,
    val tensionStrength:Float,val rotationalStiffness:Float
) { var failed=false; var forceN=0f }
data class Building(
    val id:String,val name:String,val origin:V3,val floors:Int,val width:Float,val depth:Float,
    val floorHeight:Float,val elements:MutableList<StructuralElement>,val joints:MutableList<StructuralJoint>
)

class StructuralWorld(private val context:Context) {
    val buildings=mutableListOf<Building>()
    var disaster=Disaster.NONE
    var disasterIntensity=3f
    var simulationTime=0f
    var seed=1337L
    init { loadCity("city.json") }

    fun reset(){ loadCity("city.json"); simulationTime=0f; disaster=Disaster.NONE }

    private fun loadCity(asset:String) {
        buildings.clear()
        val text=context.assets.open(asset).bufferedReader().use(BufferedReader::readText)
        val root=JSONObject(text); seed=root.optLong("seed",1337L)
        val array=root.getJSONArray("buildings")
        for(i in 0 until array.length()) buildings += BuildingFactory.create(array.getJSONObject(i))
    }

    fun update(dtInput:Float) {
        val dt=min(dtInput,0.033f); simulationTime += dt
        if(disaster==Disaster.NONE) return
        for(b in buildings){ applyLoads(b); evaluateFailures(b); integrateFailedElements(b,dt) }
    }

    private fun applyLoads(b:Building) {
        val g=9.81f
        val quake=if(disaster==Disaster.EARTHQUAKE) disasterIntensity*sin(simulationTime*2.2f) else 0f
        val wind=if(disaster==Disaster.WIND || disaster==Disaster.TORNADO) disasterIntensity*0.12f else 0f
        val columns=b.elements.filter{it.type==ElementType.COLUMN}
        for(e in b.elements){
            if(e.failed){e.supported=false;continue}
            val share=max(1,b.floors-e.floor)
            val gravity=e.mass*g*share
            val lateral=e.mass*abs(quake+wind)*share
            e.loadN=gravity+lateral
            e.stressPa=if(e.type==ElementType.COLUMN)
                gravity/e.area()+lateral/max(0.02f,e.size.x*e.size.y)
            else lateral/max(0.02f,e.size.y*e.size.z)
            e.strain=e.stressPa/max(1f,e.strength)
        }
        for(floor in b.floors-1 downTo 0){
            val live=columns.filter{it.floor<=floor && !it.failed}
            val dead=columns.count{it.floor==floor && it.failed}
            if(dead>0 && live.isNotEmpty()){
                val extra=(dead.toFloat()/live.size)*0.65f
                live.forEach{it.stressPa*=1f+extra;it.strain=it.stressPa/max(1f,it.strength)}
            }
        }
        for(j in b.joints){
            if(j.failed) continue
            val a=b.elements.firstOrNull{it.id==j.a} ?: continue
            val c=b.elements.firstOrNull{it.id==j.b} ?: continue
            j.forceN=abs(a.loadN-c.loadN)+abs(quake)*(a.mass+c.mass)
        }
    }

    private fun evaluateFailures(b:Building) {
        for(e in b.elements){
            if(!e.failed && e.stressPa/max(1f,e.strength) > if(e.primary) 1f else 1.15f) e.failed=true
        }
        for(j in b.joints){
            if(!j.failed && j.forceN>j.shearStrength){
                j.failed=true
                b.elements.firstOrNull{it.id==j.a}?.supported=false
                b.elements.firstOrNull{it.id==j.b}?.supported=false
            }
        }
        for(e in b.elements.filter{it.primary && !it.failed}){
            if(!b.joints.any{!it.failed && (it.a==e.id || it.b==e.id)} && e.type!=ElementType.FOUNDATION) e.supported=false
        }
    }

    private fun integrateFailedElements(b:Building,dt:Float){
        for(e in b.elements){
            if(!e.failed && e.supported) continue
            e.supported=false
            e.velocity.y-=9.81f*dt
            e.position += e.velocity*dt
            e.rotation.x += e.velocity.z*dt*0.15f
            e.rotation.z -= e.velocity.x*dt*0.15f
            if(e.position.y < -1f) e.position.y=-1f
        }
    }
}

object BuildingFactory {
    fun create(json:JSONObject):Building {
        val id=json.getString("id"); val name=json.optString("name",id)
        val o=json.getJSONObject("origin")
        val origin=V3(o.getDouble("x").toFloat(),o.getDouble("y").toFloat(),o.getDouble("z").toFloat())
        val floors=json.getInt("floors"); val width=json.getDouble("width").toFloat()
        val depth=json.getDouble("depth").toFloat(); val fh=json.getDouble("floor_height").toFloat()
        val elements=mutableListOf<StructuralElement>(); val joints=mutableListOf<StructuralJoint>()
        val density=Material.REINFORCED_CONCRETE.density
        elements += StructuralElement(id+"-foundation",ElementType.FOUNDATION,Material.REINFORCED_CONCRETE,
            origin+V3(0f,-0.25f,0f),V3(width,0.5f,depth),width*depth*0.5f*density,
            Material.REINFORCED_CONCRETE.strengthPa,Material.REINFORCED_CONCRETE.shearPa,0,true)
        for(floor in 0 until floors){
            val y=origin.y+floor*fh+fh*0.5f
            val offsets=listOf(V3(-width*0.42f,0f,-depth*0.42f),V3(width*0.42f,0f,-depth*0.42f),
                V3(-width*0.42f,0f,depth*0.42f),V3(width*0.42f,0f,depth*0.42f))
            for((ci,p) in offsets.withIndex()){
                val idc=id+"-c"+floor+"c"+ci
                elements += StructuralElement(idc,ElementType.COLUMN,Material.REINFORCED_CONCRETE,
                    origin+p+V3(0f,y-origin.y,0f),V3(0.35f,fh,0.35f),0.35f*0.35f*fh*density,
                    Material.REINFORCED_CONCRETE.strengthPa,Material.REINFORCED_CONCRETE.shearPa,floor,true)
                val below=if(floor>0) id+"-c"+(floor-1)+"c"+ci else id+"-foundation"
                joints += StructuralJoint(id+"-jc-"+floor+"-"+ci,idc,below,180_000f,120_000f,8_000f)
            }
            val slab=id+"-slab-"+floor
            elements += StructuralElement(slab,ElementType.SLAB,Material.REINFORCED_CONCRETE,
                origin+V3(0f,floor*fh,0f),V3(width,0.22f,depth),width*depth*0.22f*density,
                Material.REINFORCED_CONCRETE.strengthPa,Material.REINFORCED_CONCRETE.shearPa,floor,true)
            joints += StructuralJoint(id+"-js-"+floor,slab,id+"-c"+floor+"c0",90_000f,80_000f,5_000f)
            joints += StructuralJoint(id+"-js2-"+floor,slab,id+"-c"+floor+"c3",90_000f,80_000f,5_000f)
        }
        elements += StructuralElement(id+"-roof",ElementType.ROOF,Material.REINFORCED_CONCRETE,
            origin+V3(0f,floors*fh+0.15f,0f),V3(width+0.2f,0.3f,depth+0.2f),
            (width+0.2f)*(depth+0.2f)*0.3f*density,Material.REINFORCED_CONCRETE.strengthPa,
            Material.REINFORCED_CONCRETE.shearPa,floors,true)
        return Building(id,name,origin,floors,width,depth,fh,elements,joints)
    }
}
