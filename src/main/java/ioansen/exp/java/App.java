package ioansen.exp.java;


import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.prefs.Preferences;

public class App
{
    private static String genCustomId(){
        Instant now = Instant.now();
        return "" + now.getEpochSecond() + now.getNano();
    }


    private static List<String> createCmd(String id, String path, String... args ){
        List<String> cmd = new ArrayList<>();
        cmd.add("java");
        cmd.add("-jar");
        cmd.add(path);
        cmd.addAll(Arrays.asList(args));
        cmd.add(id);
        return cmd;
    }


    public static int runJar(String path, String... args ){

        String custtomId = genCustomId();
        boolean succsefullStart = false;
        int pid = -1;

        try {

            System.out.println("Attempting to start jar");
            Process startJar = new ProcessBuilder(createCmd(custtomId, path, args))
                    .redirectErrorStream(true)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .start();


            System.out.println("Searching if jar is running");
            Process search = new ProcessBuilder("wmic", "process",
                    "where", "\"CommandLine like '%" + custtomId +"%'\"",
                    "get", "Name,", "ProcessId,", "CommandLine",
                    "/format:csv")
                    .start();

            try(BufferedReader searchReader = new BufferedReader(new InputStreamReader(search.getInputStream()))) {
                String line;
                while ((line = searchReader.readLine()) != null){
                    if (line.contains("cmd.exe")) continue;
                    if (line.contains(path)){
                        System.out.println(line);
                        succsefullStart = true;
                        pid = Integer.valueOf(line.substring(line.lastIndexOf(',')+1));
                    }
                }
            }

            System.out.println("Search finished");
            if ( succsefullStart){
                    System.out.println("Jar running under pid: " + pid);
                    System.out.println();
                    storeId(String.valueOf(pid));
                    return 0;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Jar not running");
        System.out.println();
        return 1;
    }


    private static void storeId(String pid){
        try {
            Properties props = new Properties();
            props.setProperty("jarPid", pid);

            try(OutputStream out = new FileOutputStream( "C://temp//jarPid.txt")){
                props.store(out, "The pid of the last jar started by properties app ");
            }


        }
        catch (IOException e ) {
            e.printStackTrace();
        }

    }

    private static String getStoredId(){
        Properties props = new Properties();

        try (InputStream is = new FileInputStream("C://temp//jarPid.txt")){
            props.load(is);
        } catch ( IOException e ) { e.printStackTrace();}

        return  props.getProperty("jarPid", "-1");

    }


    public static int stopJar(){
        try {
            String pid = getStoredId();
            System.out.println("Attempting to delete process: " + getStoredId());
            Process search = new ProcessBuilder
                    ("wmic", "process", getStoredId(), "delete")
                    .redirectErrorStream(true)
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .start();
            System.out.println("Process " + pid + " deleted succesfully");
            System.out.println();
            return 0;
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println();
        return 1;
    }


    public static void main( String[] args )
    {

    }
}
