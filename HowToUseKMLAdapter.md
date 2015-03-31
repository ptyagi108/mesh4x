# Introduction #

The purpose of Mesh4x KMLAdapter? is to sync a KML/KMZ file with other KML/KMZ file or other supported repository say Cloud or Feed (RSS/Atom). It is actually an extended version of Mesh4x DOMAdapter?.

## what is KML/KMZ file? ##

KML, or Keyhole Markup Language, is an XML grammar and file format for modeling and storing geographic features such as points, lines, images, polygons, and models for display in Google Earth, Google Maps and other applications. You can use KML to share places and information with other users of these applications.

KMZ files are Zip compressed archives which can be opened using any application with an unzipping utility. A typical KMZ file will contain a KML document, which is an XML-based representation of information which can be overlaid on 2D or 3D maps. This will include a geographical location based on longitude and latitude, as well as points, lines or text which can be used to describe or illustrate a location. KML files can also contain paths to external images or 3D textured objects which can also be used to illustrate an area or point on a map. These resources will usually be distributed in subfolders contained within the KMZ file, which can then be imported in their entirety using any geo browsing software which supports KML.

# Build a KML adapter for sync #

Factory class:
```
org.mesh4j.sync.adapters.kml.KMLDOMLoaderFactory
```

Methods for creating adapter instance:
```
public static ISyncAdapter createKMLAdapter(String kmlFileName, IIdentityProvider identityProvider);

public ISyncAdapter createSyncAdapter(String sourceAlias, String sourceDefinition, IIdentityProvider identityProvider);	
```

Example:
```
String kmlFileName = "kmlSyncTestsPlacemark.kml";
String sourceDefinition = KMLDOMLoaderFactory.createSourceDefinition(kmlFileName);
IIdentityProvider identityProvider = NullIdentityProvider.INSTANCE;

//static version
ISyncAdapter kmlAdapter = KMLDOMLoaderFactory.createKMLAdapter(kmlFileName, identityProvider);

//non-static version
KMLDOMLoaderFactory kmlFactory = new KMLDOMLoaderFactory();
String sourceAlias= "kml:myfile.kml"; //right now this parameter is of no use, but will be used in future for applying new feature.  
ISyncAdapter kmlAdapter = kmlFactory.createSyncAdapter(sourceAlias, sourceDefinition, identityProvider);

```