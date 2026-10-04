# MVP status

Implemented repository foundation:
- SI unit conventions and structural roles.
- JSON Schema for building definitions.
- Configured material library with approximate SI values.
- Structural element and joint state models.
- Replaceable physics backend interface.
- Load/support graph recomputation.
- Disaster interface and earthquake parameter model.
- City hierarchy and map-provider boundary.
- Initial test contracts.

Not yet complete:
- Concrete JSON deserialization + JSON Schema runtime validation.
- BuildingFactory and procedural segment selection.
- A real physics backend adapter (Unity/PhysX or another engine).
- Reaction-force/stress solver and joint failure coupling to the backend.
- Runtime earthquake force dispatch to every active rigid body.
- Room/interior generators, roads, vehicles, NPCs, streaming/LOD, debug renderer, save/load, and full executable tests.

These are intentionally reported as incomplete rather than hidden behind fake implementations.
