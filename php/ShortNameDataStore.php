<?php
require_once('DataStore.php');
require_once('IdToNameMapper.php');


/**
 * Description of ShortNameDataStore
 *
 * @author paul
 */
class ShortNameDataStore {
  private $map_name;
  private $meta;
  private $map;

  public function __construct($data_dir) {
    $this->map_name = 'short-name-to-id-map';

    $this->meta = new DataStore();
    $this->meta->set_data_dir($data_dir . '/meta');
    $this->map = $this->load_mapping($this->map_name);
  }

  public function create_short_name($id) {
    $name = $this->generate_name($id);
    $this->map->put_raw($id, $name);
    $this->meta->store($this->map_name, $this->map->to_json());
    return $name;
  }

  public function get_id($name) {
    return $this->map->get_id($name);
  }

  public function delete_short_name($name) {
    $id = $this->map->get_id($name);
    $this->map->remove_raw($id, $name);
    $this->meta->store($this->map_name, $this->map->to_json());
  }

  private function load_mapping($name) {
    $map = new IdToNameMapper();
    $json = $this->meta->load($name);

    if ($json) {
      $map->from_json($json);
    }

    return $map;
  }

  private function store_mapping($name, $map) {
    $this->meta->store($name, $map->to_json());
  }

  private function generate_name($id) {
    $arr = sscanf($id, '%04x%04x-%04x-%04x-%04x-%04x%04x%04x');
    $short = 0;
    for ($i = 0; $i < count($arr); $i++) {
      $short ^= $arr[$i];
    }

    $name = sprintf('%04x', $short);
    $id = $this->map->get_id($name);
    while (isset($id)) {
      $short += 13;
      $name = sprintf('%04x', $short);
      $id = $this->map->get_id($name);
    }

    return $name;
  }
}

?>
