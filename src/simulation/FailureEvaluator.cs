using PhysicsCity.Buildings;
namespace PhysicsCity.Simulation;
public sealed class FailureEvaluator { public bool Evaluate(StructuralElement e,double compressionPa,double tensionPa,double shearPa){ if(e.FailureState==FailureState.Failed)return true; var s=Math.Max(compressionPa,Math.Max(tensionPa,shearPa)); e.StressPa=s; return false; } public bool EvaluateJoint(StructuralJoint j){ if(j.Failed)return true; if(j.ShearStressPa>j.ShearStrengthPa||j.TensionStressPa>j.TensionStrengthPa)return j.Failed=true; return false; } }
