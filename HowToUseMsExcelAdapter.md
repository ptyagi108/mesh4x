# Introduction #
Mesh4x ms-excel adapter supports both .xls and .xlsx file format. Ms-excel adapter is based on SplitAdapter concept in mesh4x core. The concept of SplitAdapter is separate the data and sync information. Idea is your business data and sync information stores in separate ms-excel worksheet or in two different ms-excel file. You can sync multiple worksheet of a ms-excel file or you can sync two ms-excel file contains multiple worksheet where your source file might be .xls and target might be .xlsx format.Mesh4x of course support sync operation between heterogeneous repository, for example you can sync between Google spreadsheet and ms-excel or it could be ms-excel to mysql(any RDBMS).

# Building Ms-excel adapter #
Ms-excel adapter supports RDF schema and plain xml. With the help of RDF schema ms-excel adapter is capable to create repository (excel file) automatically. Adapter can be used in two following ways,
  * Plain xml based MsExcel adapter.
  * Rdf based MsExcel adapter.

Both type of adapter creation is the process of creating SplitAdapter based on MsExcelSyncRepository  and  MsExcelContentAdapter. There's another easy alternative way of creating ms-excel adapter which is use the adapter factory. Example code here represents the adapter factory MsExcelSyncAdapterFactory  and  MsExcelRDFSyncAdapterFactory.Both Plain xml and Rdf MsExcel adapter works in single mode and multi mode.

  * Single mode:Sync between two sheet of a excel file or two sheet of two different excel file.
  * Milti mode:Sync between multiple sheet of  excel file.

**Plain xml based MsExcel adapter**
  * Single mode:Single mode plain xml based MsExcel adapter can be created with the following method  of MsExcelSyncAdapterFactory

```
createSyncAdapter(String excelFileName, String sheetName, String idColumnName, IIdentityProvider identityProvider)
```
parameter definition of above method:
  * **excelFileName:**  The excel file name(with its absolute path)which contains the sheet.
  * **sheetName:**  The sheet name which needs to be synchronized,this is the entity name.
  * **idColumnName:**  idColumnName field is called entity id which is for identify each row/item in the sheet of excel file.
  * **identityProvider:** Identity of the user.Please see `org.mesh4j.sync.security.LoggedInIdentityProvider` and `org.mesh4j.sync.security.NullIdentityProvider` in mesh4x core.LoggedInIdentityProvider provides logged in user and NullIdentityProvider provides default "admin" as identity.

Lets create single mode palin xml based adapter:

```
String excelFileName ="c:/content.xls";
String sheetName = "user";
String idColumnName = "id";
MsExcelSyncAdapterFactory adapterFactory = new MsExcelSyncAdapterFactory();
ISyncAdapter msExcelAdapter = adapterFactory.createSyncAdapter(excelFileName, sheetName, idColumnName, NullIdentityProvider.INSTANCE);
```

  * Multi mode:Multi mode plain xml based MsExcel adapter can be created with the following method of MsExcelSyncAdapterFactory

```
createSyncAdapterForMultiSheets(String excelFileName, IIdentityProvider identityProvider, Map<String, String> sheets, ISyncAdapter opaqueAdapter)
```

parameter definition of above method:
  * **excelFileName:**  The excel file name(with its absolute path)which contains the all the sheet.
  * **identityProvider:** Identity of the user.Please see `org.mesh4j.sync.security.LoggedInIdentityProvider` and `org.mesh4j.sync.security.NullIdentityProvider` in mesh4x core.LoggedInIdentityProvider provides logged in user and NullIdentityProvider provides default "admin" as identity.
  * **sheets:** Collection of all sheet in excel file with sheet and id column name as key value pair.
  * **opaqueAdapter:** Instance of ISyncAdapter which could be instance of InMemorySyncAdapter

Lets create multi mode palin xml based adapter:
```
String sourceExcelFile = "c:/composite_MsExcel_source.xls";
Map<String, String> sheets = new HashMap<String, String>();
sheets.put("sheet1", "id");
sheets.put("sheet2", "id");
sheets.put("sheet3", "id");

MsExcelSyncAdapterFactory factory = new MsExcelSyncAdapterFactory();
	
InMemorySyncAdapter opaqueAdapter = new InMemorySyncAdapter("opaque", NullIdentityProvider.INSTANCE);
ISyncAdapter msExcelAdapter = factory.createSyncAdapterForMultiSheets(sourceExcelFile, NullIdentityProvider.INSTANCE, sheets,opaqueAdapter);

```

**Rdf based MsExcel adapter**
  * Single mode:Single mode Rdf base MsExcel can be created with the following method of MsExcelRDFSyncAdapterFactory

```
createSyncAdapter(String excelFileName, String sheetName, String idColumnName, IIdentityProvider identityProvider)
```

Lets create single mode rdf based adapter:

```
String excelFileName ="c:/content.xls";
String sheetName = "user";
String idColumnName = "id";
String ontologyBaseUri = "http://localhost:8080/mesh4x/myExample";

MsExcelRDFSyncAdapterFactory factory = new MsExcelRDFSyncAdapterFactory(ontologyBaseUri);
ISyncAdapter msExcelAdapter = factory.createSyncAdapter(excelFileName, sheetName, idColumnName, NullIdentityProvider.INSTANCE);
```

The adapter you created with the above code is rdf adapter and let’s say this is source adapter. So you can extract the RDF schema of your excel file and pass it to the target adapter.

Extract the RDF schema,
```
IRDFSchema rdfSchema = (IRDFSchema)((ISupportReadSchema)msExcelAdapter .getContentAdapter()).getSchema();
```

Now you pass the rdf schema to the target excel adapter with the following code
```
String targetExcelFileName ="c:/target.xls";
ISyncAdapter targetMsExcelAdapter = factory.createSyncAdapter(targetExcelFileName , "user", "id", NullIdentityProvider.INSTANCE, rdfSchema);
```

This rdf based target excel adapter ensures that target excel file must have same schema of source excel file.

  * Multi mode:Multi mode Rdf adapter can be created with following method of MsExcelRDFSyncAdapterFactory

```
createSyncAdapterForMultiSheets(String excelFileName, IIdentityProvider identityProvider, Map<String, String> sheets, ISyncAdapter opaqueAdapter)
```
parameter defination is same as multi mode plain xml based MsExcel adapter.

Lets create multi mode Rdf based MsExcel adapter:

```
String sourceExcelFile = "c:/composite_MsExcel_source.xls";
String rdfURL = "http://localhost:8080/mesh4x/feeds";	
MsExcelRDFSyncAdapterFactory factory = new MsExcelRDFSyncAdapterFactory(rdfURL);
	
Map<String, String> sheets = new HashMap<String, String>();
sheets.put("sheet1", "id");
sheets.put("sheet2", "id");
sheets.put("sheet3", "id");
		
InMemorySyncAdapter opaqueAdapter = new InMemorySyncAdapter("opaque", NullIdentityProvider.INSTANCE);
ISyncAdapter msExcelAdapter = factory.createSyncAdapterForMultiSheets(sourceExcelFile, NullIdentityProvider.INSTANCE, sheets,opaqueAdapter);

```

here created msExcelAdapter is a rdf based MsExcel adapter.So you can extract the schema of all sheet of excel file.
Important information is, your msExcelAdapter is the collection of multiple adapter.
It means for each sheet of excel file it creates a adapter.So let's say your source excel file contains 3 sheet that means your multi mode msExcelAdapter must contain 3 instance of IIdentifiableSyncAdapter which is actually a ISyncAdapter.To hold all 3 adapters instance multi mode MsExcel adapter uses CompositeSyncAdapter.IIdentifiableSyncAdapter is for identify each adapter along with specific sheet.Please see the `CompositeSyncAdapter` and `IIdentifiableSyncAdapter ` in Mesh4x core.

following code represents extraction of the schema of each adapter along with specific sheet.

```
for(IIdentifiableSyncAdapter identifiableAdapter :((CompositeSyncAdapter)msExcelAdapter ).getAdapters()){
SplitAdapter adapter = (SplitAdapter)((IdentifiableSyncAdapter)identifiableAdapter).getSyncAdapter();
IRDFSchema rdfSchema = (IRDFSchema)((ISupportReadSchema)adapter.getContentAdapter()).getSchema();
}
```

After extract schema of each source sheet you pass the schema to the target multi mode MsExcel Adapter sothat target ensures the same schema of source.Following code represents the  scenario.

```
Map<IRDFSchema, String> targetSheets = new HashMap<IRDFSchema, String>();
for(IIdentifiableSyncAdapter identifiableAdapter :((CompositeSyncAdapter)msExcelAdapter ).getAdapters()){
SplitAdapter adapter = (SplitAdapter)((IdentifiableSyncAdapter)identifiableAdapter).getSyncAdapter();
IRDFSchema rdfSchema = (IRDFSchema)((ISupportReadSchema)adapter.getContentAdapter()).getSchema();
String idColumnName = ((MsExcelContentAdapter)((SplitAdapter)adapter).getContentAdapter()).getMapping().getIdColumnName();
targetSheets.put(rdfSchema, idColumnName);
}
InMemorySyncAdapter opaqueAdapterTarget = new InMemorySyncAdapter("opaque", NullIdentityProvider.INSTANCE);
ISyncAdapter adapterTarget = factory.createSyncAdapterForMultiSheets(targetExcelFile, NullIdentityProvider.INSTANCE, opaqueAdapterTarget,targetSheets);
```


# Synchronization #

**Single mode**
  * Sync two excel file based on RDF

```
String sourceExcelFile ="c:/source.xls";
String targetExcelFile ="c:/target.xls";

String ontologyBaseUri = "http://localhost:8080/mesh4x/myExample";
MsExcelRDFSyncAdapterFactory factory = new MsExcelRDFSyncAdapterFactory(ontologyBaseUri);
		
	
SplitAdapter sourceAdapter = factory.createSyncAdapter(sourceExcelFile , "user", "id", NullIdentityProvider.INSTANCE);
	
ISyncAdapter targetAdapter = factory.createSyncAdapter(targetExcelFile , "user", "id", NullIdentityProvider.INSTANCE);
		
		
SyncEngine engine = new SyncEngine(sourceAdapter,targetAdapter);
engine.synchronize();
```

  * Sync two excel file based on RDF (checking source and target rdf compatibility)

```
String sourceExcelFile ="c:/source.xls";
String targetExcelFile ="c:/target.xls";

String ontologyBaseUri = "http://localhost:8080/mesh4x/myExample";
MsExcelRDFSyncAdapterFactory factory = new MsExcelRDFSyncAdapterFactory(ontologyBaseUri);
		
	
SplitAdapter sourceAdapter = factory.createSyncAdapter(sourceExcelFile , "user", "id", NullIdentityProvider.INSTANCE);
IRDFSchema rdfSchema = (IRDFSchema)((ISupportReadSchema)sourceAdapter.getContentAdapter()).getSchema();
		
	
ISyncAdapter targetAdapter = factory.createSyncAdapter(targetExcelFile , "user", "id", NullIdentityProvider.INSTANCE, rdfSchema);
		
		
SyncEngine engine = new SyncEngine(sourceAdapter,targetAdapter);
engine.synchronize();
```

  * Sync two excel file based on plain xml

```
String sourceExcelFile ="c:/source.xls";
String targetExcelFile ="c:/target.xls";
String sheetName = "user";
String idColumnName = "id";
		
MsExcelSyncAdapterFactory adapterFactory = new MsExcelSyncAdapterFactory();
ISyncAdapter sourceAdapter = adapterFactory.createSyncAdapter(sourceExcelFile, sheetName, idColumnName, NullIdentityProvider.INSTANCE);
		
ISyncAdapter targetAdapter = adapterFactory.createSyncAdapter(targetExcelFile, sheetName, idColumnName, NullIdentityProvider.INSTANCE);
		
SyncEngine engine = new SyncEngine(sourceAdapter,targetAdapter);
engine.synchronize();
```

**Multi mode**
  * Sync two excel file based on RDF

```
String sourceExcelFile = "c:/composite_MsExcel_source.xls";
String targetExcelFile = "c:/composite_MsExcel_target.xls";

		
MsExcelRDFSyncAdapterFactory factory = new MsExcelRDFSyncAdapterFactory("http://localhost:8080/mesh4x/feeds");
		
	
Map<String, String> sheets = new HashMap<String, String>();
sheets.put("sheet1", "id");
sheets.put("sheet2", "id");
sheets.put("sheet3", "id");
		
InMemorySyncAdapter opaqueAdapterSource = new InMemorySyncAdapter("opaque", NullIdentityProvider.INSTANCE);
ISyncAdapter adapterSource = factory.createSyncAdapterForMultiSheets(sourceExcelFile, NullIdentityProvider.INSTANCE, sheets,opaqueAdapterSource);
	
		
InMemorySyncAdapter opaqueAdapterTarget = new InMemorySyncAdapter("opaque", NullIdentityProvider.INSTANCE);
ISyncAdapter adapterTarget = factory.createSyncAdapterForMultiSheets(targetExcelFile, NullIdentityProvider.INSTANCE, sheets,opaqueAdapterTarget);
		
SyncEngine syncEngine = new SyncEngine(adapterSource, adapterTarget);
syncEngine.synchronize();
```

  * Sync two excel file based on RDF (checking source and target rdf compatibility)

```
String sourceExcelFile = "c:/composite_MsExcel_source.xlsx";
String targetExcelFile = "c:/composite_MsExcel_target.xlsx";
		
MsExcelRDFSyncAdapterFactory factory = new MsExcelRDFSyncAdapterFactory("http://localhost:8080/mesh4x/feeds");
		
Map<String, String> sourceSheets = new HashMap<String, String>();
sourceSheets.put("sheet1", "id");
sourceSheets.put("sheet2", "id");
sourceSheets.put("sheet3", "id");
Map<IRDFSchema, String> targetSheets = new HashMap<IRDFSchema, String>();
		
InMemorySyncAdapter opaqueAdapterSource = new InMemorySyncAdapter("opaque", NullIdentityProvider.INSTANCE);
ISyncAdapter adapterSource = factory.createSyncAdapterForMultiSheets(sourceExcelFile, NullIdentityProvider.INSTANCE,sourceSheets,opaqueAdapterSource);
	
for(IIdentifiableSyncAdapter identifiableAdapter :((CompositeSyncAdapter)adapterSource).getAdapters()){
SplitAdapter adapter = (SplitAdapter)((IdentifiableSyncAdapter)identifiableAdapter).getSyncAdapter();
IRDFSchema rdfSchema = (IRDFSchema)((ISupportReadSchema)adapter.getContentAdapter()).getSchema();
String idColumnName = ((MsExcelContentAdapter)((SplitAdapter)adapter).getContentAdapter()).getMapping().getIdColumnName();
targetSheets.put(rdfSchema, idColumnName);
}
		
InMemorySyncAdapter opaqueAdapterTarget = new InMemorySyncAdapter("opaque", NullIdentityProvider.INSTANCE);
ISyncAdapter adapterTarget = factory.createSyncAdapterForMultiSheets(targetExcelFile, NullIdentityProvider.INSTANCE, opaqueAdapterTarget,targetSheets);
		
SyncEngine syncEngine = new SyncEngine(adapterSource, adapterTarget);
syncEngine.synchronize();

```

  * Sync two excel file based on plain xml

```
String sourceExcelFile = "c:/composite_MsExcel_source.xlsx";
String targetExcelFile = "c:/composite_MsExcel_target.xlsx";
		
Map<String, String> sheets = new HashMap<String, String>();
sheets.put("sheet1", "id");
sheets.put("sheet2", "id");
sheets.put("sheet3", "id");
		
MsExcelSyncAdapterFactory factory = new MsExcelSyncAdapterFactory();
		
InMemorySyncAdapter opaqueAdapterSource = new InMemorySyncAdapter("opaque", NullIdentityProvider.INSTANCE);
ISyncAdapter adapterSource = factory.createSyncAdapterForMultiSheets(sourceExcelFile, NullIdentityProvider.INSTANCE, sheets,opaqueAdapterSource);
		
InMemorySyncAdapter opaqueAdapterTarget = new InMemorySyncAdapter("opaque", NullIdentityProvider.INSTANCE);
ISyncAdapter adapterTarget = factory.createSyncAdapterForMultiSheets(targetExcelFile, NullIdentityProvider.INSTANCE, sheets,opaqueAdapterTarget);
		
SyncEngine syncEngine = new SyncEngine(adapterSource, adapterTarget);
syncEngine.synchronize();
```