using PhysicsCity.Core;
using PhysicsCity.Physics;
namespace PhysicsCity.Disasters;
public sealed class EarthquakeDisaster : IDisaster {
 private Vec3 _origin; private uint _seed; private IPhysicsBackend? _physics;
 public double HorizontalAccelerationMps2{get;init;}=2; public double VerticalAccelerationMps2{get;init;}=.8; public double FrequencyHz{get;init;}=1.5; public double DurationS{get;init;}=20; public Vec3 Direction{get;init;}=new(1,0,0); public double TimeS{get;private set;}
 public void BindPhysics(IPhysicsBackend physics)=>_physics=physics;
 public void Initialize(uint seed,Vec3 origin){_seed=seed;_origin=origin;TimeS=0;}
 public void ApplyForces(double dt){ if(_physics is null) throw new InvalidOperationException("EarthquakeDisaster requires a bound physics backend."); if(TimeS>=DurationS)return; var phase=2*Math.PI*FrequencyHz*TimeS; var horizontal=HorizontalAccelerationMps2*Math.Sin(phase); var vertical=VerticalAccelerationMps2*Math.Sin(phase+Math.PI/2); _=horizontal; _=vertical; TimeS+=dt; }
 public void Update(double dt){TimeS=Math.Min(DurationS,TimeS+dt);} public void Propagate(double dt){} public (Vec3 min,Vec3 max) GetAffectedArea()=>(new(_origin.X-100,_origin.Y-20,_origin.Z-100),new(_origin.X+100,_origin.Y+100,_origin.Z+100)); public void Stop(){TimeS=DurationS;}
}
