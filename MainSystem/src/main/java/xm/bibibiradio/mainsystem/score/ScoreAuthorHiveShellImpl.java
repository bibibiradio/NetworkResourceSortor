package xm.bibibiradio.mainsystem.score;


import java.util.List;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.util.HiveShellClient;

public class ScoreAuthorHiveShellImpl implements IScorer {
    final static private Logger logger = Logger.getLogger(ScoreAuthorHiveShellImpl.class);
    
    private static String mysqlUrl;
    private static String mysqlUser;
    private static String mysqlPassword;
    
    private static Properties conf;
    
    
    
    final private static String createTable = "create table IF NOT EXISTS temp_author_category (author_id bigint,category string);create table IF NOT EXISTS temp_author_score (author_id bigint,score bigint,r_num bigint);create table IF NOT EXISTS author_score (author_id bigint,type string,category string,r_num bigint,score bigint,gmt_create_time string,gmt_update_time string);";
    final private static String typeSql = "select r_type from resources group by r_type";
    final private static String categorySql = "create temporary function scorerank as 'xm.bibibiradio.mainsystem.udf.ScoreRank';insert OVERWRITE TABLE temp_author_category select author_id,r_category from (select author_id,r_category,cnt,scorerank(author_id) as rank from (select author_id,r_category,count(1) as cnt from resources where r_type='%s' group by author_id,r_category) t1 distribute by author_id sort by author_id,cnt desc) t2 where rank = 1;";
    final private static String scoreSql = "create temporary function scoreauthor as 'xm.bibibiradio.mainsystem.udf.ScoreAuthorResolver';insert OVERWRITE TABLE temp_author_score select author_id,scoreauthor(r_pv) as score,count(1) as r_num from resources where r_type='%s' group by author_id;";
    final private static String mergeSql = "insert OVERWRITE TABLE author_score select author_id,'%s',category,r_num,score,from_unixtime(unix_timestamp()),from_unixtime(unix_timestamp()) from (select temp_author_category.author_id as author_id,temp_author_category.category as category,temp_author_score.score as score,temp_author_score.r_num as r_num from temp_author_category left join temp_author_score on temp_author_category.author_id = temp_author_score.author_id) t1 where score is not null;";
    final private static String notFirstMergeSql = "insert into TABLE author_score select author_id,'%s',category,r_num,score,from_unixtime(unix_timestamp()),from_unixtime(unix_timestamp()) from (select temp_author_category.author_id as author_id,temp_author_category.category as category,temp_author_score.score as score,temp_author_score.r_num as r_num from temp_author_category left join temp_author_score on temp_author_category.author_id = temp_author_score.author_id) t1 where score is not null;";
    
    final private static String sqoopExport = "sqoop export --connect %s --username %s --password %s --table %s --export-dir \"/hive/warehouse/author_score/\" --update-key author_id --update-mode allowinsert --input-fields-terminated-by '\\001' --input-lines-terminated-by '\\n' --mysql-delimiters --bindir /opt/hadoop/sqoop-1.4.6.bin__hadoop-2.0.4-alpha/lib";
    
    
    
    private boolean initConf(String configPath){
        try{
            conf = Resources.getResourceAsProperties(configPath);
            mysqlUrl = conf.getProperty("dbConnectUrl");
            mysqlUser = conf.getProperty("dbUser");
            mysqlPassword = conf.getProperty("dbPassword");
            //cyctime = Long.valueOf(conf.getProperty("ScoreViewerCycleTime"));
            
            return true;
        }catch(Exception ex){
            logger.error("error message",ex);
            return false;
        }
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
            logger.info("END SCORE AUTHOR IMPL");
            
            
            
            
        }catch(Exception ex){
            logger.error("error message",ex);
        }
    }
    
    private void realStart() throws Exception{
        HiveShellClient.retStringHiveSql(createTable);
        
        List<String> types = HiveShellClient.retLineStringHiveSql(typeSql);
        
        logger.info("types:"+types);
        //if(deleteOldData()){
        int i = 0;
        for (String type : types) {
            if(i == 0){
                typeStart(type,true);
            }else{
                typeStart(type,false);
            }
            i++;
        }
        
    }
    
    private void typeStart(String type,boolean isFirst){
        logger.info("DEAL TYPE:"+type+" ISFIRST:"+isFirst);
        HiveShellClient.retStringHiveSql(String.format(categorySql,type));
        HiveShellClient.retStringHiveSql(String.format(scoreSql,type));
        if(isFirst){
            HiveShellClient.retStringHiveSql(String.format(mergeSql,type));
        }else{
            HiveShellClient.retStringHiveSql(String.format(notFirstMergeSql,type));
        }
        
        sqoopExport(sqoopExport,mysqlUrl,mysqlUser,mysqlPassword,"author_score");
        //fileExport();
    }
    
    private void sqoopExport(String formatCmd,String mysqlUrl,String mysqlUser,String mysqlPassword,String tableName){
        String cmd = String.format(formatCmd, mysqlUrl,mysqlUser,mysqlPassword,tableName);
        logger.info(cmd);
        logger.info(HiveShellClient.retStringShellExec(cmd));
    }
    
    public static void main(String[] args) {
        ScoreResoucesImportImpl scoreI = new ScoreResoucesImportImpl();
        ScoreAuthorHiveShellImpl scoreA = new ScoreAuthorHiveShellImpl();
        scoreI.start("mainSystemConf.properties");
        scoreA.start("mainSystemConf.properties");
    }
}
