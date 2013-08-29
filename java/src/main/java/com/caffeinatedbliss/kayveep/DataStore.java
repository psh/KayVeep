package com.caffeinatedbliss.kayveep;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 *         On: 2/22/12 at 9:59 PM
 */
public class DataStore {
	private String fileExtn;
	private FileSys fileSystem;
	private String dataDir;

	public DataStore() {
		this("json");
	}

	public DataStore(String extn) {
		this.fileExtn = extn;
		this.fileSystem = new FileSys();
	}

	public void setDataDir(String dir) {
		this.dataDir = dir;
		this.fileSystem.mkdir(dir);
	}

	public String load(String key) {
		String file = this.constructFilename(key);
		return this.fileSystem.load(this.dataDir, file);
	}

	public byte[] loadBytes(String key) {
		String file = this.constructFilename(key);
		return this.fileSystem.loadBytes(this.dataDir, file);
	}

	public void store(String key, String data) {
		String file = this.constructFilename(key);
		this.fileSystem.save(this.dataDir, file, data);
	}

	public void store(String key, byte[] data) {
		String file = this.constructFilename(key);
		this.fileSystem.save(this.dataDir, file, data);
	}

	public void delete(String key) {
		String file = this.constructFilename(key);
		this.fileSystem.delete(this.dataDir, file);
	}

	private String constructFilename(String name) {
		return name + '.' + this.fileExtn;
	}
}
