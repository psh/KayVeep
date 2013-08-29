<?php
require_once ('MapDataStore.php');

/**
 * Description of TimestampDataStore
 *
 * @author paul
 */
class TimestampDataStore extends MapDataStore {
  public function __construct($type) {
    parent::__construct(strtolower($type) . '-time-to-id-map');
  }
}

?>
