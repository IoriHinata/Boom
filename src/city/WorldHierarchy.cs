namespace PhysicsCity.City;
public abstract class Node { public required string Id{get;init;} public List<Node> Children{get;}=new(); }
public sealed class World:Node{} public sealed class City:Node{} public sealed class District:Node{} public sealed class Sector:Node{} public sealed class Block:Node{}
