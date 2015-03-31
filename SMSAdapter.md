# Introduction #

One of the common questions we get is how can I share/sync information with some remote machine 2 ways when I don't have an internet connection?

One answer is: Sync to a file, put the file on a USB drive, get it over to the other side, and get someone else to sync to their other source from the file.

But an alternative if you have cell signal is to synchronize by receiving and sending a bunch of SMS messges, by plugging in a phone to your computer, or directly from a phone with the data.

We are currently working on implementing the sync protocol over SMS messages.


The main idea is [Sync data without an Internet connection](http://edjez.instedd.org/2008/06/mesh4x-sms-adapter-sync-data-without.html).

## How it works ##

We just started the effort mid-June 2008, but the broad idea is to have a protocol that:
1. Checks if a sync is needed and handshakes the protocol behavior with one message each way
2. Finds out what actually needs to be synced
3. Syncs only the required entities minimizing the messages needed.

The results is a sync that is 2-ways between both endpoints. That is, if record A was changed on the mobile device / PC and record B had changed in the server, after synchronizing both machines have the latest versions of A and B.

Because of the mesh4x adapter architecture, you can use any other existing adapter to decide how/where the synchronized data will get stored. For example, you could sync two MySQL databases with each other (using the Hibernate Adapter), or a set of J2ME RMS records with a google spreadsheet (using the corresponding adapters which we dont have yet but invite you to build)




## Peer-Peer and Client-Server scenarios ##

Our hope is to have client and server implementations:

**Server scenario**: so if you wanted to sync to a server, you could send the SMS to a centralized service or to a gateway phone plugged in to a PC and your server would just get standard http sync requests.


**Client Scenario**: You have a PC with a phone plugged in (or a mobile device with the data) and a set of SMS messages get exchanged with another PC/phone that then applies the changes locally.

### Desktop application ###
The Sms Adapter for desktop applications uses the [smslib](http://www.smslib.org/) java library to send and receive messages.
The idea is to share KML files between two PCs with a phone plugged in, the application uses the phone as a modem.

![http://mesh4x.googlecode.com/svn/wiki/files/desktopClient1.png](http://mesh4x.googlecode.com/svn/wiki/files/desktopClient1.png)

You can read about the demo in [Mesh4x SMS Adapter Desktop PC](http://jtondato.clariusconsulting.net/2008/10/mesh4x-sms-adapter-desktop-pc.html).

The demo requires the "Java Communications Library" installed in your PC, please check the following [installation instructions](http://www.smslib.org/doc/installation/) (only Java Communications Library section).

You could check if your mobile phone is in the [Compatible GSM Modems/Phones](http://www.smslib.org/doc/compatibility/) list.

[Download desktop application](http://mesh4x.googlecode.com/files/mesh4j-SMS-DemoApp-0.1.9.zip).

![http://mesh4x.googlecode.com/svn/wiki/files/desktopClient2.png](http://mesh4x.googlecode.com/svn/wiki/files/desktopClient2.png)


### Mobile application ###
In collaboration with [JavaRosa](http://www.openrosa.org/index.php/javarosa), an [OpenRosa](http://www.openrosa.org/) java implementation which supports XForms for J2ME, we have migrated Mesh4J to J2ME.

The main idea is to provide mesh capabilities allowing mobile phones to share XForms definitions and data. You could read about it in [Mesh4x goes mobile with JavaROSA, allows you to sync data on your handset with no Internet](http://edjez.instedd.org/2008/10/mesh4x-goes-mobile-with-javarosa-allows.html).


![http://mesh4x.googlecode.com/svn/wiki/files/javarosa1.png](http://mesh4x.googlecode.com/svn/wiki/files/javarosa1.png)

[Download Mesh4x+JavaRosa MIDP demo](http://mesh4x.googlecode.com/files/Mesh4XForm_1.1.zip).

You can read about the demo in [Mesh4x+JavaRosa MIDP Demo - step by step](http://jtondato.clariusconsulting.net/2008/10/javarosa-midp-demo.html). This demo requires a mobile phone MIDP 2.0 and CLDC 1.1 compatible.

The entire application was written in J2ME using [J2ME Polish](http://www.j2mepolish.org/cms/leftsection/introduction.html), which then makes the app look great on specific handset models and adapts the UI to the capabilities of each phone.


![http://mesh4x.googlecode.com/svn/wiki/files/javarosa2.png](http://mesh4x.googlecode.com/svn/wiki/files/javarosa2.png)


## More Information ##
[Epi Info a real use case](http://taha.instedd.org/search/label/Mesh)

[Mesh4x SMS Adapter: Sync data without an Internet connection](http://edjez.instedd.org/2008/06/mesh4x-sms-adapter-sync-data-without.html)

[Mesh4x goes mobile with JavaROSA, allows you to sync data on your handset with no Internet](http://edjez.instedd.org/2008/10/mesh4x-goes-mobile-with-javarosa-allows.html)

[Mesh4x J2ME version with JavaRosa XForms](http://jtondato.clariusconsulting.net/2008/10/mesh4x-j2me-version-with-javarosa.html)

[Mesh4x+JavaRosa MIDP Demo - step by step](http://jtondato.clariusconsulting.net/2008/10/javarosa-midp-demo.html)

[Mesh4x SMS Adapter Desktop PC](http://jtondato.clariusconsulting.net/2008/10/mesh4x-sms-adapter-desktop-pc.html)