# Introduction #

Mesh4x FeedSync server is a java servlet that supports RESTfull API to synchronize data with uploaded mesh4x feeds through the basic HTTP GET and POST method. For connect to the server [HttpSyncAdapter](HowToUseHTTPAdapter.md) is avaialble.

Current implementation in java can be download from Mesh4xDownloads section.

Test server is hosted on http://sync.staging.instedd.org/mesh4x

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4xFeedSyncRestfullServer.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4xFeedSyncRestfullServer.png)

# Details #
The information in the server is organized with the following logic structure:
  * Mesh groups: is the first level, is a group of data sets. Each item is a dataset in the mesh group with the information of schema and mappings.
  * Datasets: is the second level, represents the dataset content, each item has a payload with business information as rdf instance or plain xml and feedSync information.
All information is stored in the server as Feed file, Database or S3 amazon depending of server configuration.


## GET protocol ##
In order to describe the protocol we assume a mesh group named Epiinfo with and a dataset named Oswego in this mesh group. This is a real mesh4x use case , Oswego is a MsAccess table of Epiinfo application. For more information see [Taha Kass-Hout's (taha) Blog](http://taha.instedd.org/search/label/Mesh)


#### Get protocol for Mesh Groups ####
  * Get all mesh groups, feed rss20 with all mesh groups in the server
```
http://sync.staging.instedd.org/mesh4x/feeds
```

  * Get all datasets for mesh group named Epiinfo, feed rss20 with all dataset in mesh group Epiinfo
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo
```

#### Get protocol for Datasets ####
  * Get datatset schema, rdfxml of Oswego dataset schema(Oswego table structure expressed as RDF
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego/schema
```

  * Get dataset schema as xform, XForm of Oswego dataset generated from Oswego rdf schema
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego/schema?content=xform
```

  * Get dataset mappings, Xml mappings of Oswego dataset
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego/mappings
```

#### Get protocol for Datasets content ####
  * Get all items, feed rss20, each item is a Oswego dataset patient with payload as rdf
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego	
```

  * Get all items as rss syndication protocol, feed rss20, each item is a Oswego dataset patient with payload as rdf
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego?format=rss20
```

  * Get all items as atom syndication protocol, feed atom10, each item is a Oswego dataset patient with payload as rdf
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego?format=atom10	
```

#### Get protocol for Datasets content translation to other formats ####
  * Get kml, each placemark is a Oswego dataset patient, rdf schema and mappings are used to translate placemarks title and geo lat/lon data
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego?format=kml	
```

  * Get xform, feed rss20, each item is a Oswego dataset patient with payload as xform instance (translated from rdf schema)
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego?content=xform	
```

  * Get xform without feed sync information, feed rss20, each item is a Oswego dataset patient with payload as xform instance (translated from rdf schema) without feedSync information
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego?content=plain_xform	
```

  * Get plain xml without feed sync information, feed rss20, each item is a Oswego dataset patient with payload as plain xml instance (translated from rdf schema) without feedSync information
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego?content=plain_xml	
```

#### Get protocol for Datasets content filtering ####
  * Get items filtering by rdf instance attribute, feed rss20, each item is a Oswego dataset patient that Country attribute is equals to 'Argentine', payload as rdf instance
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego?filter=Country=Argentine

http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego?filter=Country=Argentine or (Ill=true and dateOnSet>2009-01-01T10:30:00Z)
```

#### Get protocol for Datasets content history ####
  * Get history changes for an Item, feed rss20, each item is a history change of Oswego dataset patient information for the item with syncId equals to '1234'
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego/history?syncId=1234	
```

#### Util URLs, Java Rosa example ####
  * Java rosa: feed atom10, each item is a Oswego dataset patient where Country attribute is equals to 'Argentine', payload as xform instance (translated from rdf schema)
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego?format=atom10&content=xform&filter=Country=Argentine
```


## POST protocol ##
#### Create a new Mesh Group ####
  * mesh administration, create/update/clean a mesh group
> Required parameters:
  1. action=uploadMeshDefinition
  1. format=rss20|atom10
  1. newSourceId=mesh group name, example 'Epiinfo'
  1. description=<a description>
  1. schema=<rdfxml or null>
  1. mappings=<mappings xml or null>
```
http://sync.staging.instedd.org/mesh4x/feeds 
```


#### Create a new Dataset into a mesh group ####
  * dataset administration, create/update/clean a dataset
> Required parameters:
  1. action=uploadMeshDefinition
  1. format=rss20|atom10
  1. newSourceId=meshgroup/dataset name, example 'Epiinfo/Oswego'
  1. description=<a description>
  1. schema=<rdfxml or null>
  1. mappings=<mappings xml or null>
  1. xform=<xform xml>
  1. by=user


```
http://sync.staging.instedd.org/mesh4x/feeds	
```

#### Synchronize data ####
  * execute sync process, post a feed rss20 with payload as rdfxml instance and sync information
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego	
```

  * execute sync process, post a feed atom10 with payload as xform instance and sync information
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego?format=atom10&content=xform	
```

  * add a new item, post a xml payload as rdf
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo/Oswego/add	
```

  * execute multi sync process, post a feed rss20 with payload as rdfxml instance and sync information, items can be an union of all items of each dataset in mesh group
```
http://sync.staging.instedd.org/mesh4x/feeds/Epiinfo
```