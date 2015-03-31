# Introduction #

MS-Access adapter is basically an extended hibernate adapter specifically designed for Ms Access database.


# Details #
Like Hibernate adapter this adapter can also be used in two modes:

  1. Single mode: For sync a single table in a sync operation.
  1. Multi mode: For sync multiple tables in a sync operation.

The adapter factory class `org.mesh4j.sync.adapters.msaccess.MsAccessSyncAdapterFactory` can be configured for creating adapter instance to work with content data in either XML form or RDF form (necessary to sync data with different media type for example MsExcel, GoogleSpreadsheet etc). So while creating an instance of adapter factory using the constructor:

```
MsAccessSyncAdapterFactory(String baseDirectory, String rdfBaseUri);
```

an OntologyBaseUri has to pass in parameter _rdfBaseUri_ to create factory for data in RDF format, for example:
```
String ontologyBaseUri = "http://localhost:8080/mesh4x/myExample";
String mappingDirectory = baseDirectory + File.separator + databaseName;

MsAccessSyncAdapterFactory factory = new MsAccessSyncAdapterFactory(
				mappingDirectory, ontologyBaseUri);
```

and a null value in parameter _rdfBaseUri_ to create factory for data in plain XML format, for example:

```
String mappingDirectory = baseDirectory + File.separator + databaseName;
MsAccessSyncAdapterFactory factory = new MsAccessSyncAdapterFactory(
				mappingDirectory, null);
```

Once factory has been instantiated, following methods can be used to get instance of MsAccess adapter to work in Single or Multi mode sync operation:

### #Single Mode ###

Following methods are used for creating Single mode adapter:

```
createSyncAdapterFromFile(String sourceAlias, String mdbFileName, String tableName, IIdentityProvider identityProvider);
```

Here is another one which is a small variation of previous one:
```
String sourceDefinition = createSourceDefinition(String mdbFileName, String tableName);
createSyncAdapter(String sourceAlias, String sourceDefinition, IIdentityProvider identityProvider);
```

Following is an example of using this method to get an instance of MsAccess adapter for single mode sync.

```
...

String mdbFileName = baseDirectory + "ms-access/myExample.mdb";
String tableName = "example_table";

ISyncAdapter syncAdapter = factory.createSyncAdapterFromFile(tableName, mdbFileName, tableName, NullIdentityProvider.INSTANCE);

or 

String sourceDefinition = createSourceDefinition(mdbFileName, tableName);
ISyncAdapter syncAdapter = factory.createSyncAdapter(tableName, sourceDefinition, identityProvider);
...
```

### #Multi Mode ###

Following method is used for creating adapter for Multi mode sync:

```
createSyncAdapterForMultiTables(String mdbFileName, String[] tables, IIdentityProvider identityProvider, ISyncAdapter adapterOpaque);
```

The differences here with respect to the previous method are in this mode user need to provide an instance of  `ISyncAdapter` (for example `org.mesh4j.sync.adapters.InMemorySyncAdapter`) as _opaqueAdapter_ and an array of string as _tables_ containing the table names to sync.

Following is an examle of using this method to get an instance of MsAccess adapter for multi mode sync.

```
...
String ontologyBaseUri = "http://localhost:8080/mesh4x/myExample";
String mappingDirectory = baseDirectory + File.separator + databaseName;

InMemorySyncAdapter adapterOpaque = new InMemorySyncAdapter("opaque",  NullIdentityProvider.INSTANCE);
		
String[] tables = new String[]{"table_name_1", "table_name_2", "table_name_x"};
			
String mdbFileName = baseDirectory + "ms-access/myExample.mdb";

MsAccessSyncAdapterFactory factory = new MsAccessSyncAdapterFactory(mappingDirectory, ontologyBaseUri);
		
CompositeSyncAdapter adapter = factory.createSyncAdapterForMultiTables(mdbFileName, tables, NullIdentityProvider.INSTANCE, adapterOpaque);		
...
```