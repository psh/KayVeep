package com.caffeinatedbliss.kayveep

abstract class KayVeepTestCase extends GroovyTestCase {
    protected String tempDir
    protected File actualDataDir
    protected File data

    @Override
    protected void setUp() {
        tempDir = System.getProperty("java.io.tmpdir")
        actualDataDir = new File(tempDir, "meta")
        data = new File(actualDataDir, getDataStoreFileName() + ".json")
    }

    protected String getDataStoreFileName() {
        return "test"
    }

    @Override
    protected void tearDown() {
        if (data != null && data.exists()) {
            data.delete()
        }
        if (actualDataDir != null && actualDataDir.exists()) {
            actualDataDir.deleteDir()
        }
    }

}
