using PhysicsCity.Buildings;
namespace PhysicsCity.Simulation;
public sealed class LoadPropagation { public void Recompute(IReadOnlyDictionary<string,StructuralElement> elements,IReadOnlyDictionary<string,StructuralJoint> joints){ foreach(var e in elements.Values){e.Supports.Clear();e.SupportedElements.Clear();} foreach(var j in joints.Values) if(!j.Failed&&elements.ContainsKey(j.A)&&elements.ContainsKey(j.B)){elements[j.A].SupportedElements.Add(j.B);elements[j.B].Supports.Add(j.A);} } }
