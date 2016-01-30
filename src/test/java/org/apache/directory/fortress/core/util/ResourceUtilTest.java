package org.apache.directory.fortress.core.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class ResourceUtilTest {


    @Test
    public void test_File_Does_Not_Exist_returns_null() {
        InputStream inputStream = ResourceUtil.getInputStream("mumble jumble");
        assertNull("Should be null", inputStream);
    }

    /**
     * An empty string is translated into the base url of resources.
     * This is a "special" variant of the test_resource_dir_returns_null().
     */
    @Test
    public void test_empty_string_returns_null() {
        InputStream inputStream = ResourceUtil.getInputStream("");
        assertNull("Should be null", inputStream);
    }

    @Test
    public void test_resource_dir_returns_null() {
        InputStream inputStream = ResourceUtil.getInputStream("org");
        assertNull("Should be null", inputStream);
    }

    /**
     * Translates into a directory and the and the base root of resources
     */
    @Test
    public void test_dot_string_returns_null() {
        InputStream inputStream = ResourceUtil.getInputStream(".");
        assertNull("Should be null", inputStream);
    }

    /**
     * Translates into a directory and the and the base root of resources
     */
    @Test
    public void test_slash_string_returns_null() {
        InputStream inputStream = ResourceUtil.getInputStream("/");
        assertNull("Should be null", inputStream);
    }

    @Test
    public void test_null_string_returns_null() {
        InputStream inputStream = ResourceUtil.getInputStream(null);
        assertNull("Should be null", inputStream);
    }

    @Test
    public void test_file_found_in_relative_path_returns_null() {
        File file = new File("config/ehcache.xml");
        assertTrue("File in relative path should exist, just for test, any file will do", file.exists() && !file.isAbsolute() && !file.isDirectory());
        InputStream inputStream = ResourceUtil.getInputStream("config/ehcache.xml");
        assertNull("Should be null", inputStream);
    }

    @Test
    public void test_dir_returns_null() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        assertNotNull("Didn't find a temp dir", tmpDir);
        assertFalse("Didn't find a temp dir", tmpDir.isEmpty());
        System.out.println("Temp dir: " + tmpDir);
        InputStream inputStream = ResourceUtil.getInputStream(tmpDir);
        assertNull("Should be null", inputStream);
    }

    @Test
    public void test_web_addr_returns_null() {
        InputStream inputStream = ResourceUtil.getInputStream("http://google.com");
        assertNull("Should be null", inputStream);
    }

    @Test
    public void test_resource_file_returns_input_stream() throws IOException {
        String reference = "fortress.properties";
        File file = new File(reference);
        assertTrue("File should not exist in relative path.", !file.exists());
        InputStream inputStream = ResourceUtil.getInputStream(reference);
        assertNotNull("This should be a file in resources only. Just for test, any file will do", inputStream);
    }

    @Test
    public void test_resource_file_in_folder_returns_input_stream() throws IOException {
        String reference = "bootstrap/ehcache.xml";
        File file = new File(reference);
        assertTrue("File should not exist in relative path.", !file.exists());
        InputStream inputStream = ResourceUtil.getInputStream(reference);
        assertNotNull("This should be a file in resources only. Just for test, any file will do", inputStream);
    }
}