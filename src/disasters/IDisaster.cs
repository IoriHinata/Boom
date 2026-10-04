using PhysicsCity.Core;
namespace PhysicsCity.Disasters;
public interface IDisaster { void Initialize(uint seed,Vec3 origin); void ApplyForces(double dt); void Update(double dt); void Propagate(double dt); (Vec3 min,Vec3 max) GetAffectedArea(); void Stop(); }
