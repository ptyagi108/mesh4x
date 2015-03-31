# Introduction #

Mesh4j API has been upgraded to allow sync repository with different schema. On a similar schema this feature does facilitate choosing a subset of fields/columns which are to be involved in a sync operation rather than considering all the fields/columns in repository.

A source adapter can work with any target adapters as usual with a compatible schema along with this new feature just by providing a "schema conversion map" as per its local (source) repository schema and a "common sync schema" for sync engine. This "common sync schema" is shared by target adapter as well and the target adapter requires its own "schema conversion map" for this "common sync schema" according to its own local (target) repository schema.


# Details #

Following diagram shows an example scenario where 2 tables with different schema are synched on selected fields.

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4j_schema_mapped_sync.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4j_schema_mapped_sync.png)


### Sync example between a Mysql and MsAccess table ###

Adapter Factory methods for creating adapter instance to work with different schema:

Mysql:
```
org.mesh4j.sync.adapters.hibernate.HibernateSyncAdapterFactory.createHibernateAdapter(String connectionURL, String user, 
String password, Class<T> driverClass, Class<F> dialectClass, String tableName, String rdfBaseURL, String baseDirectory, 
IIdentityProvider identityProvider, File propertiesFile, Map<String, Resource> syncSchema, Map<String, String> schemaConversionMap);
```


MsAccess:
```
org.mesh4j.sync.adapters.hibernate.msaccess.MsAccessHibernateSyncAdapterFactory.createSyncAdapterFromFile(String sourceAlias, String 
mdbFileName, String tableName, IIdentityProvider identityProvider, Map<String, Resource> syncSchema, Map<String, String> schemaConversionMap);
```


Schema Mapping:
```
	Map<String, Resource> SYNC_SCHEMA = new HashMap<String, Resource>();
	Map<String, String> SCHEMA_CONVERT_MAP_MYSQL = new HashMap<String, String>();
	Map<String, String> SCHEMA_CONVERT_MAP_MSACCESS = new HashMap<String, String>();
	
	SYNC_SCHEMA.put("User", XSD.ENTITY);
	SYNC_SCHEMA.put("userId", XSD.xlong);
	SYNC_SCHEMA.put("name", XSD.xstring);
	SYNC_SCHEMA.put("phone", XSD.xstring);
		
	SCHEMA_CONVERT_MAP_MYSQL.put("User","User");
	SCHEMA_CONVERT_MAP_MYSQL.put("userId","userId");
	SCHEMA_CONVERT_MAP_MYSQL.put("name","name");
	SCHEMA_CONVERT_MAP_MYSQL.put("mobilephone","phone");
		
	SCHEMA_CONVERT_MAP_MSACCESS.put("Person","User");
	SCHEMA_CONVERT_MAP_MSACCESS.put("id","userId");
	SCHEMA_CONVERT_MAP_MSACCESS.put("firstname","name");
	SCHEMA_CONVERT_MAP_MSACCESS.put("cellphone","phone");
```


Sync Example:
```
SplitAdapter adapterMysql = HibernateSyncAdapterFactory.createHibernateAdapter(
				"jdbc:mysql:///mesh4xdb", 
				"root", 
				"admin", 
				com.mysql.jdbc.Driver.class,
				org.hibernate.dialect.MySQLDialect.class,
				"User", 
				"http://mesh4x", 
				TestHelper.baseDirectoryRootForTest(),
				NullIdentityProvider.INSTANCE,
				null, 
				SYNC_SCHEMA,
				SCHEMA_CONVERT_MAP_MYSQL);
		
		
String sourceFileName = "/mesh4j_access_SchemaMappedRDFTest.mdb";
MsAccessHibernateSyncAdapterFactory factory = new MsAccessHibernateSyncAdapterFactory(TestHelper.baseDirectoryForTest(), "http://mesh4x");

SplitAdapter adapterMsaccess = factory.createSyncAdapterFromFile(
				"Person", 
				sourceFileName, 
				"Person", 
				NullIdentityProvider.INSTANCE, 
				SYNC_SCHEMA, 
				SCHEMA_CONVERT_MAP_MSACCESS);
		
SyncEngine syncEngine = new SyncEngine(adapterMysql , adapterMsaccess);
...
```


Updated code with this feature is available here at:
http://mesh4x.googlecode.com/svn/Mesh4j/branches/Mesh4j-SchemaMap