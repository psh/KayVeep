package com.caffeinatedbliss.kayveep;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * @author Paul S. Hawke (paul.hawke@gmail.com)
 * On: 2/22/12 at 10:02 PM
 */
public class FileSys {
    public void mkdir(String dir) {
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dirFile.mkdirs();
        }
    }

    public String load(String dataDir, String file) {
        File theFile = getFile(dataDir, file);
        try {
            return FileUtils.readFileToString(theFile, Charset.defaultCharset());
        } catch (IOException ignore) {
        }
        return null;
    }

    public byte[] loadBytes(String dataDir, String file) {
        File theFile = getFile(dataDir, file);
        try {
            return FileUtils.readFileToByteArray(theFile);
        } catch (IOException ignore) {
        }
        return null;
    }

    public void save(String dataDir, String file, String data) {
        File theFile = getFile(dataDir, file);
        try (PrintWriter pw = new PrintWriter(new FileWriter(theFile))) {
            pw.print(data);
        } catch (IOException ignore) {
        }
    }

    public void save(String dataDir, String file, byte[] data) {
        File theFile = getFile(dataDir, file);
        FileOutputStream fw = null;

        try {
            fw = new FileOutputStream(theFile);
            fw.write(data);
        } catch (IOException ignore) {
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

    public void delete(String dataDir, String file) {
        File theFile = getFile(dataDir, file);
        //noinspection ResultOfMethodCallIgnored
        theFile.delete();
    }

    public void downloadTo(String urlString, String dataDir, String file) {
        InputStream is = null;
        BufferedOutputStream outStream = null;
        try {
            File tmpFile = getFile(dataDir, file + ".tmp");
            outStream = new BufferedOutputStream(new FileOutputStream(tmpFile));
            is = getInputStream(urlString);
            crossTheStreams(is, outStream);
            renameFile(dataDir, tmpFile, file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    protected void crossTheStreams(InputStream is, BufferedOutputStream outStream) throws IOException {
        byte[] buf = new byte[4096];
        int read;
        while ((read = is.read(buf)) != -1) {
            outStream.write(buf, 0, read);
        }
        outStream.close();
    }

    public void renameFile(String dataDir, File oldName, String newName) {
        File realFile = getFile(dataDir, newName);
        //noinspection ResultOfMethodCallIgnored
        oldName.renameTo(realFile);
    }

    protected InputStream getInputStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection uCon = url.openConnection();
        return uCon.getInputStream();
    }

    public File getFile(String dataDir, String file) {
        return new File(dataDir, file);
    }
}
