package xm.bibibiradio.mainsystem.score;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.util.HiveShellClient;

public class ScoreViewerHiveShellImpl implements IScorer {
    final static private Logger logger = Logger.getLogger(ScoreViewerHiveShellImpl.class);
    private static String mysqlUrl;
    private static String mysqlUser;
    private static String mysqlPassword;
    
    private static Properties conf;
    
    final private static String createTableSql = "create table IF NOT EXISTS temp_viewer_category (viewer_id bigint,category string);create table IF NOT EXISTS temp_viewer_score (viewer_id bigint,score bigint,r_num bigint);"+
                                            "create table IF NOT EXISTS viewer_score (viewer_id bigint,score bigint,type string,category string,cnt bigint,r_num bigint,gmt_create_time string,gmt_update_time string);";
    
    final private static String typeSql = "select r_type from resources group by r_type";
    
    final private static String categorySql = "create temporary function scorerank as 'xm.bibibiradio.mainsystem.udf.ScoreRank';insert OVERWRITE TABLE temp_viewer_category select viewer_id,r_category from (select viewer_id,r_category,cnt,scorerank(viewer_id) as rank from (select viewer_id,r_category,cnt from (select t11.viewer_id as viewer_id,t22.r_category as r_category,count(1) as cnt from (select r_id,viewer_id from viewtable where view_type = '%s') t11 left join (select r_id,r_category from resources where r_type = '%s') t22 on t11.r_id=t22.r_id group by viewer_id,r_category) t1 distribute by viewer_id sort by viewer_id,cnt desc) t2) t3 where rank = 1;";
    final private static String scoreSql = "insert OVERWRITE TABLE temp_viewer_score select viewer_id,avg(r_pv*(1-(ifloor/r_comment))) as score,count(viewer_id) as r_num from (select min(floor) as ifloor,viewer_id,r_id from viewtable where view_type='%s' group by r_id,viewer_id) as b left join resources on b.r_id=resources.r_id group by viewer_id;";
    final private static String mergeSql = "insert OVERWRITE TABLE viewer_score select viewer_id,score,'%s',category,r_num,r_num,from_unixtime(unix_timestamp()),from_unixtime(unix_timestamp()) from (select temp_viewer_category.viewer_id as viewer_id,temp_viewer_category.category as category,temp_viewer_score.r_num as r_num,temp_viewer_score.score as score from temp_viewer_category left join temp_viewer_score on temp_viewer_category.viewer_id = temp_viewer_score.viewer_id) t1 where score is not null;";
    final private static String notFirstMergeSql = "insert into TABLE viewer_score select viewer_id,score,'%s',category,r_num,r_num,from_unixtime(unix_timestamp()),from_unixtime(unix_timestamp()) from (select temp_viewer_category.viewer_id as viewer_id,temp_viewer_category.category as category,temp_viewer_score.r_num as r_num,temp_viewer_score.score as score from temp_viewer_category left join temp_viewer_score on temp_viewer_category.viewer_id = temp_viewer_score.viewer_id) t1 where score is not null;";
    
    final private static String sqoopExport = "sqoop export --connect %s --username %s --password %s --table %s --export-dir '/hive/warehouse/viewer_score' --update-key viewer_id --update-mode allowinsert --input-fields-terminated-by '\\001' --input-lines-terminated-by '\\n' --mysql-delimiters --bindir /opt/hadoop/sqoop-1.4.6.bin__hadoop-2.0.4-alpha/lib";
    
    
    
    
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
            logger.info("END SCORE VIEWER IMPL");
                
        }catch(Exception ex){
            logger.error("error message",ex);
        }
    }
    
    private void realStart() throws Exception{
        HiveShellClient.retStringHiveSql(createTableSql);
        
        List<String> types = HiveShellClient.retLineStringHiveSql(typeSql);
        logger.info("types:"+types);
        
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
        HiveShellClient.retStringHiveSql(String.format(categorySql,type,type));
        HiveShellClient.retStringHiveSql(String.format(scoreSql,type));
        if(isFirst){
            HiveShellClient.retStringHiveSql(String.format(mergeSql,type));
        }else{
            HiveShellClient.retStringHiveSql(String.format(notFirstMergeSql,type));
        }
        
        sqoopExport(sqoopExport,mysqlUrl,mysqlUser,mysqlPassword,"viewer_score");
    }
    
    private boolean initConf(String configPath){
        try{
            conf = Resources.getResourceAsProperties(configPath);
            mysqlUrl = conf.getProperty("dbConnectUrl");
            mysqlUser = conf.getProperty("dbUser");
            mysqlPassword = conf.getProperty("dbPassword");
            return true;
        }catch(Exception ex){
            logger.error("error message",ex);
            return false;
        }
    }
    
    private void sqoopExport(String formatCmd,String mysqlUrl,String mysqlUser,String mysqlPassword,String tableName){
        String cmd = String.format(formatCmd, mysqlUrl,mysqlUser,mysqlPassword,tableName);
        logger.info(cmd);
        logger.info(HiveShellClient.retStringShellExec(cmd));
    }
    
    
    public static void main(String[] args) {
        ScoreResoucesImportImpl scoreI = new ScoreResoucesImportImpl();
        ScoreViewerHiveShellImpl scoreV = new ScoreViewerHiveShellImpl();
        scoreI.start("mainSystemConf.properties");
        scoreV.start("mainSystemConf.properties");
    }
}
