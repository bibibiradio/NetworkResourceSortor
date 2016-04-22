package xm.bibibiradio.mainsystem.score;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.util.SimpleTimer;

public class ScoreViewerImpl implements IScorer {
    private static Logger logger = Logger.getLogger(ScoreViewerImpl.class);
    private static String url;
    private static String user;
    private static String password;
    private static long cyctime;
    private static String typeSql = "select view_type from viewtable group by view_type";
    private static String categorySql = "select r_category,cnt from (select r_category,count(r_category) as cnt from (select viewer_name,r_category from (select viewer_name,r_id from viewtable where view_type = ? and viewer_name = ? group by r_id) b left join resources on b.r_id=resources.r_id) c group by r_category) d order by cnt desc limit 1";
    private static String computeSql = "select viewer_name,score,resource_num from (select viewer_name,avg(r_pv*(1-(ifloor/r_comment))) as score,count(viewer_name) as resource_num from (select min(floor) as ifloor,viewer_name,r_id from viewtable where view_type = ? group by r_id,viewer_name) b left join resources on b.r_id=resources.r_id group by viewer_name) c where resource_num > 10";
    private static String insertSql = "insert into viewer_score (viewer_name,score,viewer_type,viewer_category,view_cnt,view_resource_num,gmt_insert_time,gmt_update_time) values (?,?,?,?,?,?,now(),now())";
    private static String selectSql = "select viewer_id from viewer_score where viewer_type= ? and viewer_name = ?";
    private static String updateSql = "update viewer_score set score=?,view_cnt=?,view_resource_num=?,viewer_category=?,gmt_update_time=now() where viewer_type=? and viewer_name=?";
    private static Properties conf;
    
    private class ScoreDealData{
        public String viewerName;
        public String viewType;
        public String category;
        public long score;
        public long viewCnt;
        public long resourceCnt;
    }
    
    private boolean initConf(String configPath){
        try{
            conf = Resources.getResourceAsProperties(configPath);
            url = conf.getProperty("dbConnectUrl");
            user = conf.getProperty("dbUser");
            password = conf.getProperty("dbPassword");
            cyctime = Long.valueOf(conf.getProperty("ScoreViewerCycleTime"));
            return true;
        }catch(Exception ex){
            logger.error("error message",ex);
            return false;
        }
    }
    
    @Override
    public void start(String configPath) {
        // TODO Auto-generated method stub
        if(!initConf(configPath)){
            logger.error("get config error!");
            return;
        }
        SimpleTimer timer = null;
        while(true){
            if(timer != null){
                long d = timer.isAlert();
                try {
                    Thread.sleep(d);
                } catch (InterruptedException ex) {
                    // TODO Auto-generated catch block
                    logger.error("error message",ex);
                }
            }
            
            logger.info("start to compute Viewer model");
            Map<String,Object> sqlTask= new HashMap<String,Object>();
            try{
                //Init jdbc
                if(!initJdbc()){
                    return;
                }
                
                sqlTask.put("url", url);
                sqlTask.put("user", user);
                sqlTask.put("password",password);
                //sqlTask.put("sql", sql);
                
                //Get DB Connection
                if(!getConn(sqlTask)){
                    return;
                }
                
                //Get View Types
                if(!execute(sqlTask,typeSql)){
                    return;
                }
                
                ResultSet rs = (ResultSet)sqlTask.get("rs");
                if(rs == null){
                    return;
                }
                
                ArrayList<String> types = new ArrayList<String>();
                while(rs.next()){    
                    types.add(rs.getString("view_type"));
                }
                rs.close();
                ((Statement)(sqlTask.get("stmt"))).close();
                
                Connection conn = (Connection) sqlTask.get("conn");
                PreparedStatement psCompute = conn.prepareStatement(computeSql);
                PreparedStatement psCategory = conn.prepareStatement(categorySql);
                PreparedStatement psSelect = conn.prepareStatement(selectSql);
                PreparedStatement psUpdate = conn.prepareStatement(updateSql);
                PreparedStatement psInsert = conn.prepareStatement(insertSql);
                ArrayList<ScoreDealData> viewers = new ArrayList<ScoreDealData>();
                //Loop of types
                for(String type : types){
                    //Compute viewers' score
                    psCompute.setString(1, type);
                    
                    rs = psCompute.executeQuery();
                    viewers.clear();
                    
                    while(rs.next()){
                        ScoreDealData data = new ScoreDealData();
                        data.viewerName = rs.getString("viewer_name");
                        data.viewType = type;
                        data.score = rs.getInt("score");
                        data.viewCnt = rs.getInt("resource_num");
                        data.resourceCnt = rs.getInt("resource_num");
                        viewers.add(data);
                    }
                    rs.close();
                    
                    for(ScoreDealData viewer : viewers){
                        psCategory.setString(1, viewer.viewType);
                        psCategory.setString(2, viewer.viewerName);
                        rs = psCategory.executeQuery();
                        if(rs.next()){
                            viewer.category = rs.getString("r_category");
                        }
                        rs.close();
                    }
                    
                    //Update viewer_score table a new score
                    for(ScoreDealData viewer : viewers){
                       psSelect.setString(1, viewer.viewType);
                       psSelect.setString(2, viewer.viewerName);
                       rs = psSelect.executeQuery();
                       if(rs.next() == false){
                           psInsert.setString(1, viewer.viewerName);
                           psInsert.setLong(2, viewer.score);
                           psInsert.setString(3, viewer.viewType);
                           psInsert.setString(4, viewer.category);
                           psInsert.setLong(5, viewer.viewCnt);
                           psInsert.setLong(6, viewer.resourceCnt);
                           
                           psInsert.executeUpdate();
                       }else{
                           psUpdate.setLong(1, viewer.score);
                           psUpdate.setLong(2, viewer.viewCnt);
                           psUpdate.setLong(3, viewer.resourceCnt);
                           psUpdate.setString(4, viewer.category);
                           psUpdate.setString(5, viewer.viewType);
                           psUpdate.setString(6, viewer.viewerName);
                           
                           psUpdate.executeUpdate();
                       }
                       
                       rs.close();
                    }
                }
                psCompute.close();
                psSelect.close();
                psInsert.close();
                psUpdate.close();
                conn.close();
            }catch(Exception ex){
                logger.error("error message",ex);
            }finally{
                disposeAll(sqlTask);
            }
            logger.info("end to compute Viewer model");
            
            timer = new SimpleTimer();
            timer.setAlertTime(System.currentTimeMillis() + cyctime);
        }
        
    }
    
    private boolean initJdbc(){
        try{
            Class.forName("com.mysql.jdbc.Driver");     
        }catch(Exception ex){
            logger.error("error message",ex);
            return false;
        }
        
        return true;
    }
    
    private boolean getConn(Map<String,Object> sqlTask){
        try{
            Connection conn = DriverManager.getConnection((String)(sqlTask.get("url")), (String)(sqlTask.get("user")), (String)(sqlTask.get("password")));
            if(conn == null){
                return false;
            }
            sqlTask.put("conn", conn);
            return true;
        }catch(Exception ex){
            logger.error("error message",ex);
            return false;
        }
    }
    
    
    private boolean execute(Map<String,Object> sqlTask,String sql){
        try{
            if(sqlTask.get("conn") == null){
                return false;
            }
            Statement stmt = ((Connection)(sqlTask.get("conn"))).createStatement();
            sqlTask.put("stmt", stmt);
            ResultSet rs = stmt.executeQuery(sql);
            if(rs == null){
                return false;
            }
            sqlTask.put("rs",rs);
            return true;
        }catch(Exception ex){
            logger.error("error message",ex);
            return false;
        }
    }
    
    private void disposeAll(Map<String,Object> sqlTask){
        try{
            if(sqlTask.get("rs") != null){
                ((ResultSet)(sqlTask.get("rs"))).close();
            }
            if(sqlTask.get("stmt") != null){
                ((Statement)(sqlTask.get("stmt"))).close();
            }
            if(sqlTask.get("conn") != null){
                ((Connection)(sqlTask.get("conn"))).close();
            }
            
        }catch(Exception ex){
            logger.error("error message",ex);
        }
    }
}
