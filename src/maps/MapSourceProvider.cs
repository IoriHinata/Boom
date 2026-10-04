namespace PhysicsCity.Maps;
public interface MapSourceProvider { string SourceId{get;} Task<MapData> LoadAsync(CancellationToken cancellationToken=default); }
public sealed record MapData(string Id,double Confidence,object Data);
public sealed class JsonMapProvider:MapSourceProvider { public string SourceId=>"json"; public Task<MapData> LoadAsync(CancellationToken cancellationToken=default)=>Task.FromResult(new MapData("json",1.0,new object())); }
public sealed class ImageMapProvider:MapSourceProvider { public string SourceId=>"image"; public Task<MapData> LoadAsync(CancellationToken cancellationToken=default)=>Task.FromResult(new MapData("image",0.0,new object())); }
public sealed class GoogleMapProvider:MapSourceProvider { public string SourceId=>"google"; public Task<MapData> LoadAsync(CancellationToken cancellationToken=default)=>throw new NotSupportedException("Google Maps provider requires an external API adapter and environment-provided credentials."); }
