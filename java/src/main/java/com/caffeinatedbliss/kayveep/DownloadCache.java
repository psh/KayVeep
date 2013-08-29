package com.caffeinatedbliss.kayveep;

import java.io.File;

/**
 * DownloadCache
 *
 * @author Paul Hawke (phawke@rgare.com)
 */
public class DownloadCache {
	private String fileExtn;
	private FileSys fileSystem;
	private DataStore meta;
	private String dataDir;
	private KayVeepUtilities kayVeepUtilities;

	public DownloadCache() {
		this("bin");
	}

	public DownloadCache(String extn) {
		this.fileExtn = extn;
		this.fileSystem = new FileSys();
		this.meta = new DataStore();
		this.kayVeepUtilities = new KayVeepUtilities();
	}

	public void setDataDir(String dir) {
		this.dataDir = dir;
		this.fileSystem.mkdir(dir);
		this.meta.setDataDir(dir);
	}

	public void store(String urlString) {
		String key = kayVeepUtilities.getKey();
		String file = this.constructFilename(key);
		fileSystem.downloadTo(urlString, this.dataDir, file);
		updateMapping(key, urlString);
	}

	public File load(String urlString) {
		IdToNameMapper mapper = kayVeepUtilities.loadMapping("cache-index", meta);
		String key = mapper.getId(urlString);
		if (key == null) {
			return null;
		}
		String file = this.constructFilename(key);
		return fileSystem.getFile(this.dataDir, file);
	}

	private void updateMapping(String key, String urlString) {
		IdToNameMapper mapper = kayVeepUtilities.loadMapping("cache-index", meta);
		mapper.putRaw(key, urlString);
		kayVeepUtilities.storeMapping("cache-index", mapper, meta);
	}

	private String constructFilename(String name) {
		return name + '.' + this.fileExtn;
	}
}
