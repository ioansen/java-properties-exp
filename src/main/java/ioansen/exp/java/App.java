package ioansen.exp.java;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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


            System.out.println("Searching if process is running");
            Process search = new ProcessBuilder("wmic", "process",
                    "where", "\"CommandLine like '%" + custtomId +"%'\"",
                    "get", "Name,", "ProcessId,", "CommandLine",
                    "/format:csv")
                    .redirectErrorStream(true)
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
                    return 0;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Jar not running");
        System.out.println();
        return 1;
    }


    private static void storeSystemPreferenceId(String pid){
        Preferences prefs = Preferences.systemRoot().node(App.class.getName());
        prefs.put("jarId", pid);
    }

    private static String getSystemPreferenceId(){
        Preferences prefs = Preferences.systemRoot().node(App.class.getName());
        return prefs.get("jarId", "-1");
    }


    public static void main( String[] args )
    {

    }
}
