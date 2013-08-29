<?php

class IdToNameMapper {
  var $id_to_name;
  var $name_to_id;

  public function  __construct() {
    $this->id_to_name = array();
    $this->name_to_id = array();
  }

  public function to_json() {
    return json_encode($this);
  }

  public function from_json($json) {
    $json_data = json_decode($json, true);

    $this->id_to_name = $json_data['id_to_name'];
    $this->name_to_id = $json_data['name_to_id'];
  }

  public function put($item) {
    $this->put_raw($item->get_id(), $item->get_name());
  }

  public function put_raw($id, $name) {
    $this->id_to_name[$id] = $name;
    $this->name_to_id[$name] = $id;
  }

  public function remove($item) {
    $this->remove_raw($item->get_id(), $item->get_name());
  }

  public function remove_raw($id, $name) {
    unset($this->id_to_name[$id]);
    unset($this->name_to_id[$name]);
  }

  public function get_name($id) {
    return $this->id_to_name[$id];
  }

  public function get_id($name) {
    return $this->name_to_id[$name];
  }

  public function all_ids() {
    return array_keys($this->id_to_name);
  }
}

?>
