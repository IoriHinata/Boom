# Architecture

World -> City -> District -> Sector -> Block -> Building -> Floor -> Room -> Object.

A building is a graph of structural elements connected by joints. Runtime failure changes this graph and forces load/support/stability recomputation. No building HP or scripted collapse sequence.

Modules: core, physics, materials, buildings, rooms, interior, city, disasters, simulation, save, maps, rendering, ui.

Physics activation tiers: FULL_PHYSICS, REDUCED_PHYSICS, STATIC_PROXY. Sleeping/streaming may reduce active bodies but must preserve logical structural state.
