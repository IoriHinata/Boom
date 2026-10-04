namespace PhysicsCity.Core;
public static class Units { public const double GravityMps2=9.80665; }
public readonly record struct Vec3(double X,double Y,double Z);
public enum StructuralRole { Foundation,Primary,Secondary,Facade,Partition,Decorative,Interior }
public enum FailureState { Intact,Deformed,Failed }
