<?php

//
// Base class for all object types stored in the database
//
require_once('StorableObject.php');

//
// Top level data management class for object access.
// This include pulls in:
//   - DataStore
//   - RawDataStore
//   - ObjectDataStore
//       - IdToNameMapper
//   - TimestampDataStore
//       - MapDataStore
//           - DataStore
//
require_once('DomainDataStore.php');

//
// Management interface dealing primarily with foreign keys.
// This include pulls in:
//   - MapDataStore
//       - DataStore
//
require_once('ArrayDataStore.php');

//
// Management interface for mapping ID to short (generated) named
//  This include pulls in:
//    - DataStore
//    - IdToNameMapper
//
require_once('ShortNameDataStore.php');

?>
