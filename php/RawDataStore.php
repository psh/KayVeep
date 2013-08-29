<?php

/**
 * Description of RawDataStore
 *
 * @author paul
 */
class RawDataStore extends DataStore {
  function __construct() {
    parent::__construct('raw');
  }

  public function set_data_dir($data_dir) {
    parent::set_data_dir($data_dir . '/raw');
  }
}

?>
