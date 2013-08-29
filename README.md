What is KayVeep?
----------------

In short, it is a drop-dead-simple no-SQL data store.  What *hsqldb* is to *Java*, *Kayveep* aimed to be initially for PHP.  Then,
someone asked if KayVeep could be made to work in other languages, notably Groovy and Java.  The name is a mashup of "K-V-P", that is,
"Key-Value-Pair", the essential datatype that the store is built upon.  Data is stored in JSON format as simple text files in the 
filesystem.

Hsqldb, what?
-------------

In Java, there are plenty of SQL databases that run as separate processes (MySQL, Oracle, PostgreSQL) but for creating a small
applicaton there is only one *hsqldb* that runs in-process, inside your own application.  Similarly, there are plenty of noSQL data
storage projects that will run as separate processes but I could see none that run in-process in your own application.

The API.
--------

Everything builds from *DataStore* and its three methods ```store()```, ```load()``` and ```delete()``` that allow you to store a
key/value pair, load the value from storage by key and delete it by its key respectively.  Note, the use of "key" and "ID" are used
synonymously throughout this document.  Everything else in KayVeep boils down to using a DataStore.

The *DataStore* stores a single item and returns it and really doesnt care what that single item is.  The *ArrayDataStore* builds
on the simple *DataStore* to allow a key to return an array of values.  This data structure is valuable for modelling one-to-many
relationships, for example.

Higher level serialization is handled by the *ObjectDataStore* which serializes the object itself and transparently stores meta-data
about the object as well.  A mapping between object type and IDs of all objects of that type is maintained, creating an index of sorts
to avoid trawling every file in the store when performing queries; using the type to ID mapping, its possible to query just the
"Employee" data stored in the store for instance.

Serialization is taken further with the *DomainDataStore* which adds meta-data storage of created and updated timestamps for data
items, and the ability to mark certain fields in an object as "raw".  Raw fields are not stored in the basic JSON representation but
instead are dropped into a file of their own.  This keeps the JSON datastore lightweight (for queries) yet still allows large "BLOB"
style data to be stored in KeyVeep.  Flagged raw fields are automatically split out from the object on ```store()``` and reconstituted
on ```load()```.  The *DomainDataStore* introduces the ability to query the store.

The Versions
------------

Everything started with PHP, and then moved on to Groovy as another dynamic, loosely typed language.  Since Groovy is close to Java,
that was the next port with considerations added for making the API type-safe.

The data in the data-store is the vital criteria.  Each language version must be 100% fully compatible with the others to allow data
portability.  Meta data, type data and JSON files must live in the same places.

KayVeep will be moving in two directions from here.  Firstly, gaining other language implementations.  Secondly, the data store needs
to be used by applications.
