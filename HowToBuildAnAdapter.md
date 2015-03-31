#How to build an adapter for a specific storage or transport

# Introduction #

The mesh4x sync framework allows data meshes that store information on multiple endpoints and works over multiple protocols. As data meshes are symmetric, building an adapter for one or the other is quite similar.

# Details #

Building an adapter

  * Decide which language you will build your adapter in.
  * Get the sync libraries of that language from the source tree
  * (Futures idea: Get the adapter automated functionality tests?)
  * Create a project implementing the right interfaces
  * Code away! you need to map the content of the items to your adapter
  * Make sure the integration tests pass

## Choosing the right interfaces ##
In order for the sychronization engine to work, your repository items (i.e. customers, situation reports, etc.) need to be augmented with versioning metadata.

If you want to control how that metadata is stored and where, or if your repository already provides versioning information that you want to reuse, you will want to implement an ISyncAdapter. This interface exposes simple CRUD methods that work with items composed of both content AND sync metadata.

This versioning metadata information is, however, very general in nature and can be easily externalized from your repository (i.e. keep it in a separate database/table). Hence, we also provide a standard SyncRepository that takes care of managing this metadata, and a simplified interface you can implement for your actual content items: IContentAdapter. This interface works with the raw item content of your repository, so you don't need to worry about the versioning information, which is coordinated automatically by a SplitAdapter with the standard SyncRepository and your IContentAdapter. The IContentAdapter exposes simple CRUD methods that work with just your content payloads. See [the source](http://code.google.com/p/mesh4x/source/browse/Mesh4j/trunk/src/org/mesh4j/sync/adapters/split/IContentAdapter.java)

## For more specific information you can see ##
HowToUseMsExcelAdapter

HowToUseHibernateAdapater

HowToUseMsAccessAdapter

[HowToUseHTTPAdapter](http://code.google.com/p/mesh4x/wiki/HowToUseHTTPAdapter)

[HowToUseKMLAdapter](http://code.google.com/p/mesh4x/wiki/HowToUseKMLAdapter)

[HowToUseDOMAdapter](http://code.google.com/p/mesh4x/wiki/HowToUseDOMAdapter)

HowToUseFeedAdapter

HowToUseGoogleSpreadsheetAdapter

HowToUseFolderAdapter