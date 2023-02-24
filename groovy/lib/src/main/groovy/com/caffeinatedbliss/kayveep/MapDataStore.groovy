package com.caffeinatedbliss.kayveep

/**
 * MapDataStore
 *
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 */
class MapDataStore extends DataStore {
	def String mapName
	def DataStore meta

	MapDataStore(String mapName, fileExtn = "json") {
		super(fileExtn)
		this.mapName = mapName
		this.meta = new DataStore()
	}

	@Override
	def setDataDir(File dir) {
		meta.dataDir = new File(dir, "meta")
	}

	@Override
	def load(String key) {
		load_mapping(mapName).get(key)
	}

	def loadAll() {
		load_mapping(mapName)
	}

	@Override
	def store(String key, Object value) {
        def map = load_mapping(mapName)
        map[(key)] = value
        meta.store(mapName, encodeJson(map))
	}

	@Override
	def delete(String key) {
		def map = load_mapping(mapName)
		map.remove(key)
		meta.store(mapName, encodeJson(map))
	}

	private load_mapping(String name) {
		def map = [:]
		def json = meta.load(name)
		if (json) {
			def data = decodeJson(json)
			map.putAll(data)
		}
		return map
	}
}
