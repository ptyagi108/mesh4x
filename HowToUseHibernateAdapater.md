# Introduction #

Hibernate adapter is highly configurable to sync data stored in almost all popular relational DBMS in the industry.

# Details #

This adapter can be used in two ways:

  1. Single mode: Sync a single table in a sync operation
  1. Multi mode: Sync multiple table in a sync operation

Let's see in detail how hibernate adapter can be used in the above mentioned modes.

### 1) Single Mode ###

For this mode user can use the following method of factory class `org.mesh4j.sync.adapters.hibernate.HibernateSyncAdapterFactory`

```
createHibernateAdapter(String connectionURL, String user, String password, Class<T> driverClass, Class<F> dialectClass, String tableName, String rdfBaseURL, String baseDirectory, IIdentityProvider identityProvider)
```

Following is an examle of using this method to get an instance of Hibernate adapter for MySQL databse in single mode.

```

...
String ontologyBaseUri = "http://localhost:8080/mesh4x/myExample";
String mappingDirectory = baseDirectory + File.separator + databaseName;

ISyncAdapter adapter = HibernateSyncAdapterFactory.createHibernateAdapter(
			"jdbc:mysql:///databaseName", 
			"username", 
			"password", 
			com.mysql.jdbc.Driver.class,
			org.hibernate.dialect.MySQLDialect.class,
			"table_name", 
			ontologyBaseUri, 
			mappingDirectory,
			NullIdentityProvider.INSTANCE);
...

```




### 2) Multi Mode ###

For this mode user can use the following method of factory class `org.mesh4j.sync.adapters.hibernate.HibernateSyncAdapterFactory`

```
createSyncAdapterForMultiTables(String connectionURL, String user, String password, Class<T> driverClass, Class<F> dialectClass, String[] tables, String rdfBaseURL, String baseDirectory, IIdentityProvider identityProvider, ISyncAdapter opaqueAdapter)
```

The differences here with respect to the previous method are in this mode user need to provide an instance of `ISyncAdapter` (for example `org.mesh4j.sync.adapters.InMemorySyncAdapter`) as **opaqueAdapter** and an array of string as **tables** containing the table names to sync.

Following is an examle of using this method to get an instance of Hibernate adapter for MySQL databse in multi mode.

```

...
String ontologyBaseUri = "http://localhost:8080/mesh4x/myExample";
String mappingDirectory = baseDirectory + File.separator + databaseName;

InMemorySyncAdapter adapterOpaque = new InMemorySyncAdapter("opaque",  NullIdentityProvider.INSTANCE);
		
String[] tables = new String[]{"table_name_1", "table_name_2", "table_name_x"};
		
ISyncAdapter adapter = HibernateSyncAdapterFactory.createSyncAdapterForMultiTables(
			"jdbc:mysql:///databaseName", 
			"username", 
			"password", 
			com.mysql.jdbc.Driver.class,
			org.hibernate.dialect.MySQLDialect.class,
			tables, 
			ontologyBaseUri, 
			TestHelper.baseDirectoryRootForTest(),
			NullIdentityProvider.INSTANCE,
			adapterOpaque);

```