# Physics City / Destruction Simulator

Procedural structural-physics city simulator. Buildings are assembled from JSON-defined structural segments and connections; destruction is an emergent result of loads, constraints, material limits, and disaster forces.

## MVP backend
Unity-oriented C# architecture with a replaceable `IPhysicsBackend`. The first vertical slice implements JSON validation, materials, structural elements/joints, load propagation, an earthquake disaster, city hierarchy, save-state interfaces, and tests.

## Rules
- SI units: kg, m, N, Pa, s.
- 1 Unity unit = 1 metre.
- No building HP, scripted collapse animations, timed destruction, or whole-building rigidbodies.
- Runtime failure is computed from element/joint state.

See `docs/architecture.md` and `data/buildings/building_small_house.json`.
