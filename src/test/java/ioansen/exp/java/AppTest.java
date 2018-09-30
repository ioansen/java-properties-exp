package ioansen.exp.java;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.tika.Tika;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private String path = System.getProperty("user.dir") + "\\src\\main\\resources\\EmptyFrame-1.0.jar";

    @Test
    public void mimeTypeShouldBeJar() {
        File file = new File(path);
        if(file.exists() && !file.isDirectory()) {
            Tika tika = new Tika();
            try {
                String mimeType = tika.detect(file);

                assertEquals("application/java-archive", mimeType);
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            fail(path + " -- file doesn't exist or is a directory");
        }
    }


    @Test
    public void runJarShouldReturnZero()
    {
        assertEquals("Error code must be 0",0, App.runJar(path));
       // assertEquals("Error code must be 0",0, App.runJar(path));
    }

    @Test
    public void runJarShouldReturnOne()
    {
        String errPath = path.replace('E', 'm');
        assertEquals("Error code must be 1",1, App.runJar(errPath));
    }

    @Test
    public void deleteJarShouldReturnZero()
    {
        assertEquals("Error code must be 0",0, App.stopJar());
    }
}
