<?php

/**
 * Description of DataStoreKeys
 *
 * @author paul
 */
class DataStoreKeys {
  public static function get_key() {
    // Return a v4 compliant UUID
    return sprintf('%04x%04x-%04x-%04x-%04x-%04x%04x%04x',
      // 32 bits for "time_low"
                   mt_rand(0, 0xffff), mt_rand(0, 0xffff),

      // 16 bits for "time_mid"
                   mt_rand(0, 0xffff),

      // 16 bits for "time_hi_and_version",
      // four most significant bits holds version number 4
                   mt_rand(0, 0x0fff) | 0x4000,

      // 16 bits, 8 bits for "clk_seq_hi_res",
      // 8 bits for "clk_seq_low",
      // two most significant bits holds zero and one for variant DCE1.1
                   mt_rand(0, 0x3fff) | 0x8000,

      // 48 bits for "node"
                   mt_rand(0, 0xffff), mt_rand(0, 0xffff), mt_rand(0, 0xffff)
    );
  }

  public static function create_one_to_many_fk($data_dir, $one, $many) {
    $fk = new ArrayDataStore($many, $one);
    $fk->set_data_dir($data_dir);
    return $fk;
  }

  public static function update_one_to_many_fk($array_store, $one, $many) {
    $fk_ids = $array_store->load($one);
    if (!is_array($fk_ids)) {
      $fk_ids = array();
    }
    $fk_ids[$many] = 1;
    $array_store->store($one, $fk_ids);
  }
}

?>
