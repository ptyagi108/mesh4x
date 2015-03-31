# How to use Mesh4x to simplify mobile data collection with JavaROSA #

At a glance:

To quickly get up and running with mobile data collection you will need:
  * A mobile phone that support JavaROSA (you can see the list here http://code.javarosa.org/wiki/Devices)
  * A SIM card with connectivity plan (we have plans to just require SMS in a future but for the moment this is needed)
  * A choice of database to collect the information into MS Access, MS Excel, MySQL or Google Spreasheets for example

The steps are:

## 1. Define your survey form by designing your data source table (eg an Excel spreadsheet or Access Database). E.g. with Access : ##
> I. Define a table, primary key is required.

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_1.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_1.png)

> II. Add data and save the file

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_2.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_2.png)


## 2. Synchronize your database with the cloud server ##

> I. Download and run the Ektoo clien,  see the package here Mesh4xDownloads

> II. Point to your source on the left side and to the ?cloud? on the right hand side

> ![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_3.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_3.png)

> III. Press synchronize

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_4.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_4.png)

> IV. Check the cloud
> http://sync.staging.instedd.org:8080/mesh4x/feeds/MyExample/MyTable

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_5.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_5.png)

> View source example:

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_6.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_6.png)

JavaRosa Mesh4x client read an atom10 feed with content as xform.

> http://sync.staging.instedd.org:8080/mesh4x/feeds/MyExample/MyTable?format=atom10&content=xform

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_7.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_7.png)

> View source example:

> ![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_8.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_8.png)

## 3. Deploy and configure JavaROSA on your phone ##

> I. Download JavaROSA with Mesh4x client, see the package here Mesh4xDownloads

> II. Copy it to your phone application directory

> III. Open client

> ![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_9.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_9.png)

> IV. Configure the Mesh4x server, Mesh name as follows

> ![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_10.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_10.png)

## 4. Download form definition ##

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_11.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_11.png)

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_12.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_12.png)

## 5. Sync data with the cloud ##

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_13.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_13.png)

## 6. View imported data ##

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_14.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_14.png)

## 7. Update data on the form ##

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_15.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_15.png)

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_16.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_16.png)

> Select "save and send later"

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_17.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_17.png)

## 8. Add some new data on the form ##

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_16_1.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_16_1.png)

> Select "save and send later"

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_17.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_17.png)

## 9. Synchronize the from the phone ##

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_18.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_18.png)

## 10. All the information that was recently collected on the phone should now be on the mesh. ##

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_19.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_19.png)

## 11. Go back to the desktop client and synchronize again ##

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_20.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_20.png)

## 12. All the information on the server will be in your database ##

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_21.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_21.png)

![http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_22.png](http://mesh4x.googlecode.com/svn/wiki/files/mesh4x_javarosa_22.png)