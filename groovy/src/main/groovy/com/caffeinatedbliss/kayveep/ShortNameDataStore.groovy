package com.caffeinatedbliss.kayveep

import java.text.DecimalFormat

class ShortNameDataStore {
    private mapName;
    DataStore meta;
    IdToNameMapper map;

    ShortNameDataStore(dataDir) {
        mapName = 'short-name-to-id-map';

        meta = new DataStore();
        meta.setDataDir(dataDir.'/meta');
        map = loadMapping(this.mapName);
    }

    def createShortName(id) {
        def name = generateName(id);
        map.put_raw(id, name);
        meta.store(this.mapName, map.toJson());
        return name;
    }

    def delete_short_name(name) {
        def id = map.getId(name);
        map.remove_raw(id, name);
        meta.store(mapName, map.toJson());
    }

    private loadMapping(name) {
        map = new IdToNameMapper();
        def json = meta.load(name);

        if (json) {
            map.fromJson(json);
        }

        return map;
    }

    private storeMapping(name, map) {
        meta.store(name, map.toJson());
    }

    private generateName(String id) {
        def arr = [
            Integer.parseInt(id.substring(0, 4),16),
            Integer.parseInt(id.substring(4, 8),16),
            /* - */
            Integer.parseInt(id.substring(9, 13),16),
            /* - */
            Integer.parseInt(id.substring(14, 18),16),
            /* - */
            Integer.parseInt(id.substring(19, 23),16),
            /* - */
            Integer.parseInt(id.substring(24, 28),16),
            Integer.parseInt(id.substring(28, 32),16),
            Integer.parseInt(id.substring(32, id.length()),16)
        ]

        Integer total = 0;
        arr.each { a ->
            total ^= a
        }

        def name = convertIntegerToHex(total)
        id = this.map.getId(name);
        while (id) {
            total++;
            name = convertIntegerToHex(total)
            id = this.map.getId(name);
        }

        return name;
    }

    def convertIntegerToHex(Integer total) {
        def name = Integer.toHexString(total)
        if (name.length() < 4) {
            name = "0000".substring(0, 4-name.length())+name
        }
        name
    }
}
