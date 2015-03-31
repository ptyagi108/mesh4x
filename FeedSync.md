[FeedSync](http://dev.live.com/feedsync/) is a protocol licensed under Creative Commons that specifies XML data formats and algorithms for version tracking and conflict management in a data mesh

You can see it here
http://dev.live.com/feedsync/spec/v1.htm

## More Information ##
[Live Mesh FeedSync: an overview of the protocol under the hood (by kzu)](http://www.clariusconsulting.net/blogs/kzu/archive/2008/04/29/62598.aspx)

[Overview of FeedSync support in the Microsoft Sync Framework (by cibrax)](http://weblogs.asp.net/cibrax/archive/2008/04/22/overview-of-feedsync-support-in-the-microsoft-sync-framework.aspx)

## Advantages of using Mesh-Friendly synchronization ##

## Mesh4x and Feedsync ##
Mesh4x is a set of libraries, applications and services for mesh-style data integration and sharing, based on standards. We chose the mesh4x standard as it was the simplest approach to the protocol that we could find that was also defined in an implementation-agnostic way (ie it could be implemented in any language, on any platform). Mesh4x, as a project, goes beyond Feedsync and is not 'tied' to Feedsync In other words, if other standards emerge that support the larger goal of data sharing and live information exchange, mesh4x is also likely to implement that. (
## Pros of FeedSync ##
  * Simple to understand
  * Simple to implement
  * Protected under Creative Commons
  * Standard just describes versioning behavior and doesn't encroach other domains
  * Implementation available on multiple languages
  * Is agnostic to the data format or type of the payload of information (you could synchronize movie files or form records)
  * Preserves versioning in multi-master scenarios
  * Preserves conflicts for multi-master scenarios
  * Is easy to express in Atom or RSS or to decompose into SMS
  * It stays away from defining how conflict resolution should behave
  * It can be extended with other standards/conventions that specify concrete behaviors in concrete cases (e.g. augmenting conflict resolution information)

## Cons of Feedsync ##
  * Little awareness in the market
  * It was orginially designed by folks in Microsoft (listed as a con as it usually brings up suspicious looks around hardcore opensourcers..and this is an opensource project :). But not a con really as the folks who designed it are smart and have proven experience in the sync domain )
  * Because the standard just tackles its main focus area, you are left to figure out other behaviors such as security, HTTP service conventions, etc
  * Versioning history compression is underspecified
  * You need to define service endpoints, discovery, etc (_ej: I see it as a pro because I like domain-specific standards rather than all-encompassing specification, but for lazy folks it is a con. Mesh4x as an implementation is better suited to suggest end to end conventions that tackle all these domains simultaneously_)