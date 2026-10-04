using PhysicsCity.Core;
namespace PhysicsCity.Physics;
public interface IPhysicsBackend { object CreateRigidBody(double mass,Vec3 position,Vec3 dimensions); object CreateStaticBody(Vec3 position,Vec3 dimensions); object CreateJoint(string type,object a,object b,double stiffness); void ApplyForce(object body,Vec3 force); void ApplyImpulse(object body,Vec3 impulse); void ApplyTorque(object body,Vec3 torque); void DestroyBody(object body); void Simulate(double dt); bool Raycast(Vec3 origin,Vec3 direction,double distance); }
