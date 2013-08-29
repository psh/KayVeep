<?php
require_once ('DataStore.php');
require_once ('RawDataStore.php');
require_once ('ObjectDataStore.php');
require_once ('TimestampDataStore.php');

/**
 * Data store handling domain objects - adding CRUD features.
 *
 * @author paul
 */
class DomainDataStore {
  private $data;
  private $raw_data;
  private $map_name;
  private $map_type;
  private $map;
  private $created;
  private $updated;
  private $raw_fields;

  public function __construct($type, $data_dir, $raw = null) {
    $this->map_type = $type;
    $basename = strtolower($type);
    $this->map_name = $basename . '-id-to-name-map';

    if (isset($raw)) {
      if (is_array($raw)) {
        $this->raw_fields = $raw;
      } else {
        $this->raw_fields = array();
        $this->raw_fields[] = $raw;
      }
    } else {
      $this->raw_fields = array();
    }

    $this->raw_data = new RawDataStore();
    $this->data = new ObjectDataStore();
    $this->created = new TimestampDataStore($basename . '-create');
    $this->updated = new TimestampDataStore($basename . '-update');

    $this->data->set_data_dir($data_dir);
    $this->raw_data->set_data_dir($data_dir);
    $this->created->set_data_dir($data_dir);
    $this->updated->set_data_dir($data_dir);

    $this->map = $this->data->load_mapping($this->map_name);
  }

  public function create($obj, $key = NULL) {
    if (!isset($key)) {
      $key = $this->raw_data->get_key();
    }
    $obj->set_id($key);
    if (is_array($this->raw_fields) && count($this->raw_fields) > 0) {
      $reflect = new ReflectionClass($this->map_type);
      foreach ($this->raw_fields as $name) {
        $property = $reflect->getProperty($name);
        $raw_value = $property->getValue($obj);
        if (isset($raw_value) && strlen(trim($raw_value)) > 0) {
          $raw_key = $key . '-' . $name;
          $this->raw_data->store($raw_key, trim($raw_value));
          $property->setValue($obj, null);
        }
      }
    }
    $this->data->store($obj);
    $this->update_mapping($obj);
    $created_time = time();
    $this->created->store($obj->get_id(), $created_time);
    $obj->created_time = $created_time;
  }

  public function load_by_id($id) {
    $obj = $this->data->load($id, $this->map_type);
    if ($obj) {
      $this->load_raw_properties($obj);
      $this->update_mapping($obj);
      $obj->created_time = $this->created->load($obj->get_id());
      $obj->updated_time = $this->updated->load($obj->get_id());
      return $obj;
    }
  }

  public function load_by_name($name) {
    $id = $this->map->get_id($name);
    if ($id) {
      $obj = $this->data->load($id, $this->map_type);
      if ($obj) {
        $this->load_raw_properties($obj);
        $this->update_mapping($obj);
        $obj->created_time = $this->created->load($obj->get_id());
        $obj->updated_time = $this->updated->load($obj->get_id());
        return $obj;
      }
    }
  }

  private function load_raw_properties($obj) {
    if (is_array($this->raw_fields) && count($this->raw_fields) > 0) {
      $reflect = new ReflectionClass($this->map_type);
      foreach ($this->raw_fields as $name) {
        $raw_key = $obj->get_id() . '-' . $name;
        $property = $reflect->getProperty($name);
        $raw_value = $this->raw_data->load($raw_key);
        if (isset($raw_value)) {
          $property->setValue($obj, $raw_value);
        }
      }
    }
  }

  public function store($obj) {
    if (is_array($this->raw_fields) && count($this->raw_fields) > 0) {
      $reflect = new ReflectionClass($this->map_type);
      foreach ($this->raw_fields as $name) {
        $raw_key = $obj->get_id() . '-' . $name;
        $to = $reflect->getProperty($name);
        $raw_value = $to->getValue($obj);
        if ($raw_value != null && strlen(trim($raw_value)) > 0) {
          $this->raw_data->store($raw_key, trim($raw_value));
          $to->setValue($obj, null);
        } else {
          $this->raw_data->delete($raw_key);
        }
      }
    }
    $this->data->store($obj);
    $this->update_mapping($obj);
    $this->updated->store($obj->get_id(), time());
  }

  public function delete($obj) {
    if (is_array($this->raw_fields) && count($this->raw_fields) > 0) {
      $reflect = new ReflectionClass($this->map_type);
      foreach ($this->raw_fields as $name) {
        $raw_key = $obj->get_id() . '-' . $name;
        $this->raw_data->delete($raw_key);
      }
    }
    $this->data->delete($obj);
    $this->map->remove($obj);
    $this->created->delete($obj->get_id());
    $this->updated->delete($obj->get_id());
    $this->data->store_mapping($this->map_name, $this->map);
  }

  public function all_keys() {
    return $this->map->all_ids();
  }

  public function filtered_list($filter) {
    $ret = Array();
    $arr = $this->map->all_ids();
    for ($i = 0; $i < count($arr); $i++) {
      $obj = $this->load_by_id($arr[$i]);
      if ($filter($obj)) {
        $ret[] = $obj;
      }
    }
    return $ret;
  }

  private function update_mapping($obj) {
    $this->map->remove($obj);
    $this->map->put($obj);
    $this->data->store_mapping($this->map_name, $this->map);
  }
}

?>
