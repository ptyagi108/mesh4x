# Introduction #

The purpose of Mesh4x GoogleSpreadsheet adapter is to sync spreadsheet data stored in cloud environment (http://docs.google.com). This adapter is based on SplitAdapter? concept of Mesh4x core. The concept of SplitAdapter? is to separate the business data and sync information; that is business data and sync information stores in different worksheet. GoogleSpreadsheet adapter support sync operation with other heterogeneous data repository, for example you can sync a Google spreadsheet with Ms-excel, MsAccess or MySql (any RDBMS) etc.


# Build a GoogleSpreadsheetAdapter for sync #

GoogleSpreadsheetAdapter can interact with content data in either in
> XML form: to sync data between 2 google spreadsheet.
or
> RDF form: to sync data between google spreadsheet and other different media type for example MsExcel, MySql etc.

Following are more details on how to use the adapter factory classes to create appropriate adapter instance.

### Plain xml based GoogleSpreadsheet? adapter ###

Factory class:
```
org.mesh4j.sync.adapters.googlespreadsheet.GoogleSpreadSheetSyncAdapterFactory
```

Constructor:
```
public GoogleSpreadSheetSyncAdapterFactory(); 
```

Method for creating adapter instance:
```
public SplitAdapter createSyncAdapter(String username, String password,
		String spreadsheetName, String contentSheetName,
		String idColumnName, String lastUpdateColumnName,
		IIdentityProvider identityProvider, String sourceAlias);
```

Example:

```
GoogleSpreadSheetSyncAdapterFactory factory = new GoogleSpreadSheetSyncAdapterFactory();

String username = "username@gmail.com";
String password = "password";
String spreadsheetName = "My test spreadsheet";
String cotentWorksheetName = "user";
String idColumnName = "user_id";
String lastUpdateColumnName = null;
IIdentityProvider identityProvider = NullIdentityProvider.INSTANCE;
String sourceAlias = "user";

ISyncAdapter adapter = factory.createSyncAdapter(username, password, spreadsheetName,
		cotentWorksheetName, idColumnName, lastUpdateColumnName, identityProvider,
		sourceAlias);
```


### RDF based GoogleSpreadsheet? adapter ###

Factory class:
```
org.mesh4j.sync.adapters.googlespreadsheet.GoogleSpreadSheetRDFSyncAdapterFactory
```

Constructor:
```
public GoogleSpreadSheetRDFSyncAdapterFactory(String rdfBaseURL);
```

Method for creating adapter instance:
```

public SplitAdapter createSyncAdapter(String username, String password,
		String spreadsheetName, String contentSheetName,
		String idColumnName, String lastUpdateColumnName,
		IIdentityProvider identityProvider, String sourceAlias);


public SplitAdapter createSyncAdapter(String username, String password,
		String spreadsheetName, String cotentSheetName, String 
		idColumnName, String lastUpdateColumnName, IIdentityProvider
		identityProvider,IRDFSchema rdfSchema, String sourceAlias);

```

Example:

```
String ontologyBaseUri = "http://localhost:8080/mesh4x/myExample";
GoogleSpreadSheetRDFSyncAdapterFactory factory = new GoogleSpreadSheetRDFSyncAdapterFactory(ontologyBaseUri);

ISyncAdapterBuilder builder = new SyncAdapterBuilder(new PropertiesProvider());
ISyncAdapter sourceAdapter = builder.createMsExcelAdapter("source.xls", "user", "user_id", true);

SplitAdapter splitAdapterSource = ((SplitAdapter)sourceAsExcel);
ISchema sourceSchema = ((MsExcelContentAdapter)splitAdapterSource.getContentAdapter()).getSchema();

String username = "username@gmail.com";
String password = "password";
String spreadsheetName = "My test spreadsheet";
String cotentWorksheetName = "user";
String idColumnName = "user_id";
String lastUpdateColumnName = null;
IIdentityProvider identityProvider = NullIdentityProvider.INSTANCE;
IRDFSchema rdfSchema = (IRDFSchema)sourceSchema;
String sourceAlias = "user";

//Scenario 1:
/*
when the spreadsheet/worksheet is not available, user need to provide a schema in
_sourceSchema_ that contains schema of other data repository 
with which user attempts to perform sync operation. Using which this schema 
GoogleSpreadsheetAdapter can automatically create new spreadsheet/worksheet before 
sync operation. If the spreadsheet as well as worksheet both available it will 
extract its own schema and compare with the given schema if both are compatible for sync.
Sync is only possible with 2 different repositories when the corresponding schemas 
of the repositories are compatible with each other. 

This is mainly used when the adapter acts as a target adapter in the sync operation.
*/

ISyncAdapter targetAdapter = factory.createSyncAdapter(username,
		password, spreadsheetName, cotentWorksheetName, idColumnName,
		lastUpdateColumnName, identityProvider, sourceSchema, sourceAlias);


//Scenario 2:
/*
when the spreadsheet/worksheet is available and the schema is compatible with 
other repository to sync, user no need to provide an external schema and hence can 
call this method.

This is mainly used when the adapter acts as a source adapter in the sync operation.
*/

ISyncAdapter sourceAdapter = factory.createSyncAdapter(username, password,
		spreadsheetName,cotentSheetName, idColumnName, lastUpdateColumnName,
		identityProvider, sourceAlias);

//Note, the only change here is no external schema is provided in the method.
```