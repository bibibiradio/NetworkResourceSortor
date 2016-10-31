package xm.bibibiradio.mainsystem.score;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.util.HiveShellClient;

public class ScoreResoucesImportImpl implements IScorer {
    final static private Logger logger = Logger.getLogger(ScoreResoucesImportImpl.class);
    private static String mysqlUrl;
    private static String mysqlUser;
    private static String mysqlPassword;
    private static String defaultLastTime;
    private static Properties conf;
    private static SimpleDateFormat hiveFormat;
    
    final private static String createTableSql = "Create table IF NOT EXISTS resources (r_id bigint,r_type string,r_category string,author_id bigint,r_pv bigint,r_comment bigint,r_gmt_create string,item_gmt_insert string);"+
            "Create table IF NOT EXISTS viewtable (view_id bigint,r_id bigint,viewer_id bigint,view_type string,floor int,gmt_insert_time string,gmt_view_time string);";
    
    final private static String lastTimeSql = "select max(item_gmt_insert) from resources;";
    final private static String sqoopImportResources = "sqoop import --incremental lastmodified --connect %s --username %s --password %s --table %s --hive-table resources --columns \"r_id,r_type,r_category,author_id,r_pv,r_comment,r_gmt_create,item_gmt_insert\" --check-column r_gmt_create --last-value \"%s\" --hive-import --hive-drop-import-delims --bindir /opt/hadoop/sqoop-1.4.6.bin__hadoop-2.0.4-alpha/lib";
    final private static String sqoopImportViewtable = "sqoop import --incremental lastmodified --connect %s --username %s --password %s --table %s --hive-table viewtable --columns \"view_id,r_id,viewer_id,view_type,floor,gmt_insert_time,gmt_view_time\" --check-column gmt_view_time --last-value \"%s\" --hive-import --hive-drop-import-delims --bindir /opt/hadoop/sqoop-1.4.6.bin__hadoop-2.0.4-alpha/lib";
    
    static{
        hiveFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    }
    
    private boolean initConf(String configPath){
        try{
            conf = Resources.getResourceAsProperties(configPath);
            mysqlUrl = conf.getProperty("dbConnectUrl");
            mysqlUser = conf.getProperty("dbUser");
            mysqlPassword = conf.getProperty("dbPassword");
            defaultLastTime = conf.getProperty("defaultLastTime");
            return true;
        }catch(Exception ex){
            logger.error("error message",ex);
            return false;
        }
    }
    
    private void sqoopImport(String formatCmd,String mysqlUrl,String mysqlUser,String mysqlPassword,String tableName,String time){
        String cmd = String.format(formatCmd, mysqlUrl,mysqlUser,mysqlPassword,tableName,time);
        logger.info(cmd);
        logger.info(HiveShellClient.retStringShellExec(cmd));
    }
    
    @Override
    public void start(String configPath) {
        // TODO Auto-generated method stub
        try{
            if(!initConf(configPath)){
                logger.error("get config error!");
                return;
            }
            
            
            realStart();
            //fileExport();
            logger.info("END SCORE RESOURCES IMPORT IMPL");
                
        }catch(Exception ex){
            logger.error("error message",ex);
        }
    }
    
    private void realStart() throws Exception{
        HiveShellClient.retStringHiveSql(createTableSql);
        
        String lastTime = null;
        lastTime = HiveShellClient.retStringHiveSql(lastTimeSql);
        if(lastTime.length() <= 6 || lastTime == null){
            lastTime = defaultLastTime;
        }
        Date d = hiveFormat.parse(lastTime);
        lastTime = hiveFormat.format(new Date(d.getTime() + 1000));
        logger.info("lastTime:"+lastTime);
        
        sqoopImport(sqoopImportResources,mysqlUrl,mysqlUser,mysqlPassword,"resources",lastTime);
        sqoopImport(sqoopImportViewtable,mysqlUrl,mysqlUser,mysqlPassword,"viewtable",lastTime);
    }
    
    public static void main(String[] args) {
        ScoreResoucesImportImpl scoreI = new ScoreResoucesImportImpl();
        scoreI.start("mainSystemConf.properties");
    }
}
