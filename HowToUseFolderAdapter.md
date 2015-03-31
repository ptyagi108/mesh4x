# Introduction #
Mesh4x folder adapter supports synchronization between folders or directory where directory or folder contains sub-directory/sub-folder or any kind of file.



# Building Folder adapter #
Creation of Folder adapter is the process of creating SplitAdapter based on FolderContentAdapter and FileSyncRepository.Another easy alternative way of creating folder adapter is use the adapter factory.Example code here represents the adapter factory FolderSyncAdapterFactory.Folder adapter can be created with the following method of FolderSyncAdapterFactory.
```
createFolderAdapter(String folderName, IIdentityProvider identityProvider, IIdGenerator idGenerator)
```

parameter definition of above method:
  * **folderName:** Absolute path of the folder or directory which needs to be synchronized
  * **identityProvider:** Identity of the user.Please see `org.mesh4j.sync.security.LoggedInIdentityProvider` and `org.mesh4j.sync.security.NullIdentityProvider` in mesh4x core.LoggedInIdentityProvider provides logged in user and NullIdentityProvider provides default "admin" as identity.
  * **idGenerator:** instance of IIdGenerator class to generate unique id.

Now create the adapter
```
String sourcefolder = "c:/test/";
ISyncAdapter folderAdapter = FolderSyncAdapterFactory.createFolderAdapter(sourcefolder, NullIdentityProvider.INSTANCE, IdGenerator.INSTANCE);
```

# Synchronization #

```
String sourcefolder = "c:/source/";
String targetFolder = "c:/target/";
ISyncAdapter sourceAdapter = FolderSyncAdapterFactory.createFolderAdapter(sourcefolder, NullIdentityProvider.INSTANCE, IdGenerator.INSTANCE);
ISyncAdapter targetAdapter = FolderSyncAdapterFactory.createFolderAdapter(targetFolder, NullIdentityProvider.INSTANCE, IdGenerator.INSTANCE);
		
SyncEngine engine = new SyncEngine(sourceAdapter,targetAdapter);
engine.synchronize();
```