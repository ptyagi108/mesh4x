# Introduction #

DOM stands for the Document Object Model, and it's a way of representing a document (be it XML or HTML) using Object Oriented programming.  It’s a platform and language neutral interface that will allow programs and scripts to dynamically access and update the content, structure and style of documents. The document can be further processed and the results of that processing can be incorporated back into the presented page.
The purpose of Mesh4x DOMAdapter is to sync 2 DOM file with compatible structure for example KML/KMZ files.


# Details #

Following are the details of how to create a DOMAdapter.

Adapter Class:
```
org.mesh4j.sync.adapters.dom.DOMAdapter
```

Constructor:
```
public DOMAdapter(IDOMLoader domLoader);
```
Here in the constructor the important thing to consider is to get a DOM loader instance for specific type of DOM file. `org.mesh4j.sync.adapters.kml.KMLDOMLoaderFactory` provides the following static method to get file specific DOM loader for KML and KMZ file.

```
public static DOMLoader createDOMLoader(String fileName, IIdentityProvider identityProvider);
```

Example:
```
String kmlFileName = "testPlacemark.kml";
IIdentityProvider identityProvider = NullIdentityProvider.INSTANCE;
IDOMLoader domLoader = KMLDOMLoaderFactory.createDOMLoader(kmlFileName, identityProvider);
DOMAdapter domAdapter = new DOMAdapter(domLoader); 
```