<?php
require_once ('MapDataStore.php');

/**
 * ArrayDataStore - used to model a one-to-many relationship where a single
 * ID returns an array of related IDs when queries.
 *
 * @author paul
 */
class ArrayDataStore extends MapDataStore {
  public function __construct($type_many, $type_one) {
    parent::__construct(strtolower($type_many) . '-array-to-' . strtolower($type_one) . '-id-map');
  }
}

?>
