<?php
require_once ('DataStore.php');

/**
 * Description of MapDataStore
 *
 * @author paul
 */
abstract class MapDataStore {

  private $meta;
  private $map_name;

  public function __construct($name) {
    $this->meta = new DataStore();
    $this->map_name = $name;
  }

  public function set_data_dir($data_dir) {
    $this->meta->set_data_dir($data_dir . '/meta');
  }

  public function load($id) {
    $map = $this->load_mapping($this->map_name);
    return $map[$id];
  }

  public function load_all() {
    return $this->load_mapping($this->map_name);
  }

  public function store($id, $data) {
    $map = $this->load_mapping($this->map_name);
    $map[$id] = $data;
    $this->meta->store($this->map_name, json_encode($map));
  }

  public function delete($id) {
    $map = $this->load_mapping($this->map_name);
    unset($map[$id]);
    $this->meta->store($this->map_name, json_encode($map));
  }

  private function load_mapping($name) {
    $map = array();

    $json = $this->meta->load($name);
    if ($json) {
      $map = json_decode($json, true);
    }

    return $map;
  }
}

?>
