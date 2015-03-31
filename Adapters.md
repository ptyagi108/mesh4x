# Adapters for Productivity Apps #
## KML Adapter <b title='How To Use KMLAdapter'><a href='http://code.google.com/p/mesh4x/wiki/HowToUseKMLAdapter'><img src='http://mesh4x.googlecode.com/svn/wiki/files/howTo.png' /></a></b> ##
#The Mesh4x KML adapter allows you to synchronize KML / KMZ files, including all their placemarks (pushpins, polygons, lines, etc) styles and for KMZ files the embedded resources (photos, icons).

[Build maps collaboratively with new Mesh4x KML adapter](http://edjez.instedd.org/2008/05/build-maps-collaboratively-with-new.html)

[Improvements to Mesh4x KML adapter ("Mesh4Maps"?)](http://edjez.instedd.org/2008/05/improvements-to-mesh4x-kml-adapter.html)

We have also set up a demo server to show how you can sync to the HTTP sync server and back. If you use the KML adapter sample application you can sync between two local KML files, a local KML and an http server we are hosting, or 2 http servers.


## Google Spreadsheets Adapter <b title='How To Use GoogleSpreadsheetAdapter'><a href='http://code.google.com/p/mesh4x/wiki/HowToUseGoogleSpreadsheetAdapter'><img src='http://mesh4x.googlecode.com/svn/wiki/files/howTo.png' /></a></b> ##
The Google Spreadsheets Adapter helps synchronize data kept in Google Spreadsheets. It takes a simple approach to taking a Sheet act as a table, where the first row is assumed to be the titles of the columns, and every row is taken as the entity to be shared (i.e. conflicts happen at granularities of rows, not cells). Also this adapter takes the contents of the cells as data i.e. it doesn't preserve formulas or conditional computations.

## Microsoft Excel Adapter <b title='How To Use MsExcelAdapter'><a href='http://code.google.com/p/mesh4x/wiki/HowToUseMsExcelAdapter'><img src='http://mesh4x.googlecode.com/svn/wiki/files/howTo.png' /></a></b> ##
The Excel Adapter helps synchronize data kept in Excel. It takes a simple approach to taking a Sheet in excel act as a table, where the first row is assumed to be the titles of the columns, and every row is taken as the entity to be shared (i.e. conflicts happen at granularities of rows, not cells). Also this adapter takes the contents of the cells as data i.e. it doesn't preserve formulas or conditional computations.
See HowToUseMsExcelAdapter for more information.

## Microsoft Access Adapter <b title='How To Use MsAccessAdapter'><a href='http://code.google.com/p/mesh4x/wiki/HowToUseMsAccessAdapter'><img src='http://mesh4x.googlecode.com/svn/wiki/files/howTo.png' /></a></b> ##
The Access Adapter allows you to synchronize to Access databases. It uses Hibernate's XML to table mappings, and accesses the MDB via ODBC. This has the drawback that the MDB has to be closed to be able to write to it. This will be addressed in future versions depending on user demand (ideally this adapter should be hosted in-process in the access application itself as a plugin, instead of a separate standalone application 'on the side')
See HowToUseMsAccessAdapter for more information.


# Generic Purpose Persistence Adapters #
## Hibernate OR/M for Databases Adapter <b title='How To Use HibernateAdapater'><a href='http://code.google.com/p/mesh4x/wiki/HowToUseHibernateAdapater'><img src='http://mesh4x.googlecode.com/svn/wiki/files/howTo.png' /></a></b> ##
The Hibernate Adapter allows you to synchronize to any database through the Hibernate
OR/M library, which allows support for pretty much most databases available today.
See HowToUseHibernateAdapater for more information (example of Hibernate adapter for <b>MySQL</b> databse)


## Amazon Web Services S3 Adapter ##
This adapter can be used in conjunction with the storage web service (hosted in EC2) for a highly scalable cloud-based storage node.


# Adapters used as Transports #
Transport adapters are designed to help synchronization happen in a distributed system.

## File Adapter <b title='How To Use File Adapter '><a href='http://code.google.com/p/mesh4x/wiki/HowToUseFeedAdapter'><img src='http://mesh4x.googlecode.com/svn/wiki/files/howTo.png' /></a></b> ##
The file adapter allows you to sync from/to a file. The file is really an XML (RSS/Atom) file that you can save on a USB drive and then sync with again. This file is also what you would transfer ("beam") to sync over Infrared or Bluetooth in a peer-to-peer fashion between mobiles.

## Folder Adapter <b title='How To Use FolderAdapter'><a href='http://code.google.com/p/mesh4x/wiki/HowToUseFolderAdapter'><img src='http://mesh4x.googlecode.com/svn/wiki/files/howTo.png' /></a></b> ##
The folder adapter allows you to sync from/to a folder. The folder is really an XML (RSS/Atom) file that you can save on a USB drive and then sync with again. This file is also what you would transfer ("beam") to sync over Infrared or Bluetooth in a peer-to-peer fashion between mobiles.
See HowToUseFolderAdapter for more information.

## HTTP Adapter <b title='How To Use HTTPAdapter'><a href='http://code.google.com/p/mesh4x/wiki/HowToUseHTTPAdapter'><img src='http://mesh4x.googlecode.com/svn/wiki/files/howTo.png' /></a></b> ##
The HTTP adapter synchronizes to a server that supports the REST APIs for Feedsync. We have an implementation of such server in the source tree too (it's what you are synchronizing to when using the KML demo).
For more information see [Mesh4x FeedSync RESTfull Server](FeedSyncRESTfulServer.md)

## SMS Adapter ##
The SMSAdapter allows you to synchronize over SMS messages. It implements an optimized version of the FeedSync protocol specifically designed to minimize the number of messages exchanged. For example you could use this to synchronize J2ME RMS records with a PC or server or two PCs (with phones plugged in). Read more here: [SMSAdapter](SMSAdapter.md).

[Epi info a real use case](http://taha.instedd.org/search/label/Mesh)


# Adapters for developer extensibility #
## Split Adapter ##
The Split Adapter can be used by developeers to synchronize with a data soruce that does not have awareness/support of mesh4x synchronziation (like a database whose schema you can't modify). The split adapter keeps track of all versioning metadata, and interacts with the datasource only through an class that has a simple CRUD API. See HowToBuildAnAdapter for more information.


_Would you like to see a specific adapter not listed here? Please leave a comment below or add it to the Issues List!_