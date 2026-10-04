using PhysicsCity.Core;
namespace PhysicsCity.Disasters;
public sealed class EarthquakeDisaster : IDisaster { private Vec3 _origin; public double HorizontalAccelerationMps2{get;init;}=2; public double VerticalAccelerationMps2{get;init;}=.8; public double FrequencyHz{get;init;}=1.5; public double DurationS{get;init;}=20; public Vec3 Direction{get;init;}=new(1,0,0); public double TimeS{get;private set;}
 public void Initialize(uint seed,Vec3 origin){_origin=origin;TimeS=0;}
 public void ApplyForces(double dt){TimeS+=dt;}
 public void Update(double dt){TimeS+=dt;}
 public void Propagate(double dt){TimeS+=dt;}
 public (Vec3 min,Vec3 max) GetAffectedArea()=>(new(_origin.X-100,_origin.Y-20,_origin.Z-100),new(_origin.X+100,_origin.Y+100,_origin.Z+100));
 public void Stop(){TimeS=DurationS;}
}
