package ioansen.exp.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private String path = System.getProperty("user.dir") + "\\src\\main\\resources\\EmptyFrame-1.0.jar";

    @Test
    public void runJarShouldReturnZero()
    {
        assertEquals("Error code must be 0",0, App.runJar(path));
        assertEquals("Error code must be 0",0, App.runJar(path));
    }

    @Test
    public void runJarShouldReturnOne()
    {
        String errPath = path.replace('E', 'm');
        assertEquals("Error code must be 1",1, App.runJar(errPath));
    }

}
