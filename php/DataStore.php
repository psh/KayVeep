<?php
require_once('DataStoreKeys.php');

/**
 * A simple file based Key-Value DataStore for JSON data.
 *
 * @author paul
 */
class DataStore {
  private $data_dir;
  private $file_extn;

  function __construct($extn = 'json') {
    $this->file_extn = $extn;
  }

  public function set_data_dir($dir) {
    $this->data_dir = $dir;
    if (!file_exists($dir)) {
      if (!mkdir($dir, 0777, true)) {
        die('Making data dir FAILED!<br>');
      }
      chmod($dir, 0777);
    }
  }

  public function get_key() {
    return DataStoreKeys::get_key();
  }

  public function load($key) {
    $file = $this->construct_filename($key);
    if (file_exists($file)) {
      $data = file_get_contents($file);
      return $data;
    }
    return false;
  }

  public function store($key, $data) {
    $file = $this->construct_filename($key);
    file_put_contents($file, $data);
    chmod($file, 0666);
  }

  public function delete($key) {
    $file = $this->construct_filename($key);
    if (file_exists($file)) {
      unlink($file);
    }
  }

  private function construct_filename($name) {
    return $this->data_dir . '/' . $name . '.' . $this->file_extn;
  }
}

?>
