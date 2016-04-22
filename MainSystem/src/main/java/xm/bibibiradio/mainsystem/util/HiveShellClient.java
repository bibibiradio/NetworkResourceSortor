package xm.bibibiradio.mainsystem.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class HiveShellClient {
    static final private Logger logger = Logger.getLogger(HiveShellClient.class);
    
    public static Process rawShellExec(String shell){
        Process proc = null;
        String[] cmdA = { "/bin/sh", "-c", shell };
        try{
            proc = Runtime.getRuntime().exec(cmdA);
        }catch(Exception ex){
            logger.error("error message",ex);
        }
        return proc;
    }
    
    public static InputStream retInputStreamShellExec(String shell){
        try{
            Process proc = rawShellExec(shell);
            if(proc == null){
                return null;
            }
            //proc.waitFor();
            return proc.getInputStream();
            //return proc.getErrorStream();
        }catch(Exception ex){
            logger.error("error message",ex);
            return null;
        }
    }
    
    public static String retStringShellExec(String shell){
        InputStream input = retInputStreamShellExec(shell);
        if(input == null){
            return null;
        }
        try{
            return new String(readAllFromInputStream(input));
        }catch(Exception ex){
            logger.error("error message",ex);
            return null;
        }
    }
    
    public static List<String> retLineStringShellExec(String shell){
        InputStream input = retInputStreamShellExec(shell);
        if(input == null){
            return null;
        }
        
        List<String> ret = new ArrayList<String>();
        
        try{
            BufferedReader lines = new BufferedReader(new InputStreamReader(input));
            String line = null;
            while((line = lines.readLine()) != null){
                ret.add(line);
            }
            return ret;
        }catch(Exception ex){
            logger.error("error message",ex);
            return null;
        }
    }
    
    public static List<String> retLineStringHiveSql(String sql){
        String cmd = "hive -e \""+sql+"\"";
        logger.info(cmd);
        return retLineStringShellExec(cmd);
    }
    
    public static String retStringHiveSql(String sql){
        
        String cmd = "hive -e \""+sql+"\"";
        logger.info(cmd);
        return retStringShellExec(cmd);
        
    }
    
    private static byte[] readAllFromInputStream(InputStream inputStream){
        StringBuilder sb1 = new StringBuilder();      
        byte[] bytes = new byte[4096];    
        int size = 0;    
          
        try {      
            while ((size = inputStream.read(bytes)) > 0) {    
                String str = new String(bytes, 0, size);    
                sb1.append(str);    
            }    
        } catch (IOException e) {
            e.printStackTrace();      
        } finally {
            try {      
                inputStream.close();      
            } catch (IOException e) {      
                logger.error("error message",e);      
            } 
        }  
        return sb1.toString().getBytes();
    }
}
