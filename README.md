##  ABOUT the JavaDB project

A small database meant to mimic the functionality of the WebStorage API. Data is stored via key-value pairs and retreived via key.

# How to use

The Database is initialized via a call to getDatabaseInstance which in turn calls a private constructer in the singleton pattern. This ensures a single instance of localStorage exising at a time.

Items are categorized by type and must be initialized with a String value representing the type via initObjectStores(Type). Objects may be associated with type and all objects associated with type may be returned via findAll(Type).

Several data access operations are available via save, findById, getInitializedObjectStores, and delete.
