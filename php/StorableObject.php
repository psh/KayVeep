<?php

/**
 * A base class for storable objects
 *
 * @author paul
 */
abstract class StorableObject {
  private $_additional_vars = array();

  abstract function get_id();

  abstract function set_id($id);

  abstract function get_name();

  abstract function to_json();

  abstract function from_json($json);

  public function __get($name) {
    return $this->_additional_vars[$name];
  }

  public function __set($name, $value) {
    $this->_additional_vars[$name] = $value;
  }

  public function to_array() {
    $arr = get_object_vars($this);
    foreach ($this->_additional_vars as $name => $value) {
      $arr[$name] = $value;
    }
    unset($arr['_additional_vars']);
    return $arr;
  }
}

?>
