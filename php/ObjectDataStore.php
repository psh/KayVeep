<?php
require_once ('DataStore.php');
require_once ('IdToNameMapper.php');

/**
 * Built on top of the basic key-value data store, this class adds PHP object
 * semantics and id-to-name mapping capability.
 *
 * @author paul
 */
class ObjectDataStore {

  private $data;
  private $meta;
  private $types;

  public function __construct() {
    $this->data = new DataStore();
    $this->meta = new DataStore();
    $this->types = new DataStore();
  }

  public function set_data_dir($dir) {
    $this->data->set_data_dir($dir);
    $this->meta->set_data_dir($dir . '/meta');
    $this->types->set_data_dir($dir . '/types');
  }

  /**
   * Load an object from the data store.
   *
   * @param (mixed) $id the ID of the object to load
   * @param String $type the type of object being loaded
   * @return (mixed)
   */
  public function load($id, $type) {
    $json = $this->data->load($id);
    if ($json) {
      $object = new $type();
      $object->from_json($json);

      $this->store_key($id, $type);

      return $object;
    }
  }

  /**
   * Performs an "insert or update" type of operation on the StorableObject
   * passed in.  If the object has no ID of its own, one will be generated
   * by the data store.
   *
   * @param (mixed) $object
   */
  public function store($object) {
    if (!$object->get_id()) {
      $key = $this->data->get_key();
      $object->set_id($key);
    }
    $this->data->store($object->get_id(), $object->to_json());
    $this->store_key($object->get_id(), get_class($object));
  }

  public function delete($object) {
    $this->delete_key($object->get_id(), get_class($object));
    $this->data->delete($object->get_id());
  }

  public function load_mapping($name) {
    $map = new IdToNameMapper();
    $json = $this->meta->load($name);

    if ($json) {
      $map->from_json($json);
    }

    return $map;
  }

  public function store_mapping($name, $map) {
    $this->meta->store($name, $map->to_json());
  }

  private function store_key($id, $type) {
    $json = $this->types->load($type);
    if ($json) {
      $keys = json_decode($json, true);
    } else {
      $keys = array();
    }
    $keys[$id] = 1;
    $this->types->store($type, json_encode($keys));
  }

  private function delete_key($id, $type) {
    $json = $this->types->load($type);
    if ($json) {
      $keys = json_decode($json, true);
      unset($keys[$id]);
    }
    $this->types->store($type, json_encode($keys));
  }
}

?>
