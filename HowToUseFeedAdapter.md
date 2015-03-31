# Introduction #
Mesh4x FeedAdapter supports Rss 2.0 and Atom 1.0 feed.FeedAdapter operates on Feed class.Feed class represents standard Rss 2.0 and Atom 1.0 feed format.You can sync two Rss 2.0 feed or two Atom 1.0 feed or it could be synchronization between Rss and Atom.

# Building FeedAdapter #

FeedAdapter can be created in two ways.one is FeedSyncAdapterFactory class and other is by
FeedAdapter class itself.Following code represensts both way of creating FeedAdapter

Create  FeedAdapter  with the help of FeedAdapter class:
```
//creating the feed file and then load it or you can specify your feed file path
File file = new File(TestHelper.fileName(IdGenerator.INSTANCE.newID()+".xml"));
FeedAdapter feedAdapter = new FeedAdapter(file, RssSyndicationFormat.INSTANCE, NullIdentityProvider.INSTANCE, IdGenerator.INSTANCE, new Feed());
//after provide the feed file you will be able to get the feed in the format of Feed class
//instance
Feed feed = repo.getFeed();//standard rss or atom feed

```

Create  FeedAdapter  with the help of FeedSyncAdapterFactory class:

```
String feedFileName = "c:/rss.xml"//assume that you have a rss feed file in this location
IIdentityProvider identityProvider = NullIdentityProvider.INSTANCE;
ISyncAdapter feedAdapter = FeedSyncAdapterFactory.createSyncAdapter(feedFileName, identityProvider)
```

# Synchronization #

For better understanding Following code creates rss file automatically and then add item into the feed file.After creating the source feed file and target feed file  mesh4x sync process incorporates changes between two feed file.


```
Element element = TestHelper.makeElement("<payload><user><id>SyncId123</id><name>SyncId123</name><pass>123</pass></user></payload>");
XMLContent content = new XMLContent("SyncId123", "SyncId123", "SyncId123", element);
Item item = new Item(content, new Sync("SyncId123", "jmt", TestHelper.now(), false));
		
Feed feed = new Feed();
feed.addItem(item);
		
File fileSource = new File(TestHelper.fileName(IdGenerator.INSTANCE.newID()+".xml"));
FeedAdapter source = new FeedAdapter(fileSource, RssSyndicationFormat.INSTANCE, NullIdentityProvider.INSTANCE, IdGenerator.INSTANCE, feed);
		
File fileTarget = new File(TestHelper.fileName(IdGenerator.INSTANCE.newID()+".xml"));
FeedAdapter target = new FeedAdapter(fileTarget, RssSyndicationFormat.INSTANCE, NullIdentityProvider.INSTANCE, IdGenerator.INSTANCE, new Feed());
		
SyncEngine syncEngine = new SyncEngine(source, target);
syncEngine.synchronize();

```

With the above code to create element you can use any xml library instead of TestHelper class in mesh4x.You could use dom4j DocumentHelper class also.Following code represents how to create xml Element object from string by DocumentHelper class of dom4j.

```
Element element  = DocumentHelper.parseText("<payload><user><id>SyncId123</id><name>SyncId123</name><pass>123</pass></user></payload>").getRootElement();
```

If you have Rss or Atom feed file you could synchronize with the following way

```
String sourceFeedFile = "c:/rss1.xml";
String targetFeedFile = "c:/rss2.xml";
ISyncAdapter sourceAdapter = FeedSyncAdapterFactory.createSyncAdapter(sourceFeedFile, NullIdentityProvider.INSTANCE);
ISyncAdapter targetAdapter = FeedSyncAdapterFactory.createSyncAdapter(targetFeedFile, NullIdentityProvider.INSTANCE);
		
SyncEngine engine = new SyncEngine(sourceAdapter,targetAdapter);
engine.synchronize();
```