# Introduction #
Mesh4x HttpSyncAdapter basically communicates with the mesh4x FeedSync server to sync its feed. Mesh4x has its own HTTP [FeedSync RESTful server(Feedsync server)](FeedSyncRESTfulServer.md). This server supports synchoronization with uploaded feed through the basic HTTP GET and POST method.So HttpSyncAdapter actually make request to sync server to add/update/get  mesh or feed.HttpSyncAdapter supports its own merge implementation through ISupportMerge.


# Building HttpSyncAdapter #
Creating HttpSyncAdapter is very easy way through the factory class HttpSyncAdapterFactory.
With the factory you can create the adapter in many different ways. Here we represent the efficient way of creating adapter.Following method of HttpSyncAdapterFactory class creates the adapter.

```
createSyncAdapter(String url, IIdentityProvider identityProvider){
```

parameter definition:

  * **url:** The url of the feed sync server specifying with mesh name and feed name.
  * **identityProvider:** Identity of the user.Please see `org.mesh4j.sync.security.LoggedInIdentityProvider` and `org.mesh4j.sync.security.NullIdentityProvider` in mesh4x core.LoggedInIdentityProvider provides logged in user and NullIdentityProvider provides default "admin" as identity.

Lets create the adapter:

```
String meshGroup = "EktooMesh";
String dataSetId = "EktooFeed";
String serverUrl = "http://sync.staging.instedd.org:8080/mesh4x/feeds"

String url = serverUrl + "/" + meshGroup + "/" + dataSetId;

HttpSyncAdapter httpSyncAdapter = HttpSyncAdapterFactory.createSyncAdapter(url, identityProvider);
```

So after creating the HttpSyncAdapter you need to make sure that cloud server must contain
rdf schema.
check the colud server rdf schema
```
ISchema cloudSchema = adapter.getSchema();
```
If the colud schema is null then it means server doesnt have rdf schema and you have to upload the schema before you start synchronization with the server.HttpSyncAdapter does  support RDF schema.Following code does upload mesh,dataset,schema and mapping.

```
String url = "http://sync.staging.instedd.org:8080/mesh4x/feeds";
//first upload the mesh
HttpSyncAdapter.uploadMeshDefinition(url, "EktooMesh", RssSyndicationFormat.NAME, "Ektoo mesh", null, null, "jmt");

//creating schema
Element schemaElement = XMLHelper.parseElement("<mySchema><id>33</id></mySchema>");
ISchema schema = new Schema(schemaElement);
//creating mappings		
Element elementMappings = XMLHelper.parseElement("<mappings><title>title</title></mappings>");
IMapping mapping = new Mapping(elementMappings);
//finally upload the dataset or feed,schema and mapping.
HttpSyncAdapter.uploadMeshDefinition(path, "EktooMesh/EktooFeed", RssSyndicationFormat.NAME, "ektoo feed description", schema, mapping, "jmt");

```

As you uploaded schema and mapping into the sync server you will get the schema and mapping from the server by just following code

```
String url = "http://sync.staging.instedd.org:8080/mesh4x/feeds/EktooMesh/EktooFeed";
String schema = HttpSyncAdapter.getSchema(url);
```

```
String url = "http://sync.staging.instedd.org:8080/mesh4x/feeds/EktooMesh/EktooFeed";
String mappings = HttpSyncAdapter.getMappings(url);
```

Above url mentioned mesh name and dataset name,because you could have several dataset under one mesh.So to get specific schema/mapping of a dataset in a mesh you have to mention the complete url.You can also filter your feed by name with the following url
```
String path = "http://sync.staging.instedd.org:8080/mesh4x/feeds/EktooMesh/EktooFeed?filter=Name=JMT";
```


You could also do the all task(upload mesh,schema,mapping,dataset) with the following method of HttpSyncAdapterFactory class.This method is very useful because if the server doesnt have schema it uploads the schema.



```
createSyncAdapterAndCreateOrUpdateMeshGroupAndDataSetOnCloudIfAbsent(String serverUrl, String meshGroup, String dataSetId, IIdentityProvider identityProvider, ISchema schema)
```

parameter defination:
  * **serverUrl:** The base url of the sync server(ex,http://sync.staging.instedd.org:8080/mesh4x/feeds)
  * **meshGroup:** The mesh name in the server(ex,EktooMesh)
  * **dataSetId:** The data set name.Under one mesh you may have multiple data set(ex,under EktooMesh you may have user,address,customer data set(ex,EktooFeed).
  * **identityProvider:** Identity of the user.Please see `org.mesh4j.sync.security.LoggedInIdentityProvider` and `org.mesh4j.sync.security.NullIdentityProvider` in mesh4x core.LoggedInIdentityProvider provides logged in user and NullIdentityProvider provides default "admin" as identity.
  * **schema:** The RDF schema which cloud server will follow to validate mesh.


Lets jump into the code part of how to upload mesh,dataset and schema at the same time when adapter is created,here we create a simple rdf schema for explanation but you dont need to create schema always.If you are going to sync between ms-excel to cloud then definitely you create RDF based ms-excel adapter and then  extract the schema to upload it to the server.

```
String meshGroup = "EktooMesh";
String dataSetId = "EktooFeed";
String serverUrl = "http://sync.staging.instedd.org:8080/mesh4x/feeds"
String url = serverUrl + "/" + meshGroup + "/" + dataSetId;
IIdentityProvider identityProvider = NullIdentityProvider.INSTANCE;

//creating demo rdf schema
RDFSchema rdfSchema = new RDFSchema("example", url+"#", dataSetId);
rdfSchema.addStringProperty("string", "string", "en");
rdfSchema.addIntegerProperty("integer", "int", "en");
rdfSchema.addBooleanProperty("boolean", "boolean", "en");
rdfSchema.addDateTimeProperty("datetime", "datetime", "en");
rdfSchema.addDoubleProperty("double", "double", "en");
rdfSchema.addLongProperty("long", "long", "en");
rdfSchema.addDecimalProperty("decimal", "decimal", "en");  

HttpSyncAdapter httpSyncAdapter = HttpSyncAdapterFactory.createSyncAdapterAndCreateOrUpdateMeshGroupAndDataSetOnCloudIfAbsent(serverUrl, meshGroup, dataSetId , identityProvider, rdfSchema);

```

Now the created instance of HttpSyncAdapter can be used as target adapter in synchronization process of  **SyncEngine**