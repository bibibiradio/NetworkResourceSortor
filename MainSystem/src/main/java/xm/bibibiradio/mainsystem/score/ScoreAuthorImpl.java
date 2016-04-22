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

public class ScoreAuthorImpl implements IScorer{
    private static Logger logger = Logger.getLogger(ScoreViewerImpl.class);
    private static String url;
    private static String user;
    private static String password;
    private static int cutLimit;
    private static int cutRange;
    private static int lastOut;
    private static long cyctime;
    private static String typeSql = "select r_type from resources group by r_type";
    private static String authorSql = "select author from resources where r_type = ? and author is not null group by author";
    private static String authorUpNumSql = "select count(r_id) as cnt from resources where author = ?";
    private static String categorySql = "select r_category from (select r_category,count(r_id) as cnt from resources where r_type = ? and author = ? group by r_category) b order by b.cnt desc limit 1";
    //private static String computeSqlNotLimit = "select arg(r_pv) as score from resources where author = ?";
    private static String computeSql = "select avg(b.r_pv) as score from (select r_pv from resources where author = ? order by r_id asc limit ?,?) b";
    private static String insertSql = "insert into author_score (author_name,author_type,author_category,resource_num,score,gmt_insert_time,gmt_update_time) values (?,?,?,?,?,now(),now())";
    private static String selectSql = "select author_id from author_score where author_type= ? and author_name = ?";
    private static String updateSql = "update author_score set score=?,resource_num=?,author_category=?,gmt_update_time=now() where author_type=? and author_name=?";
    private static Properties conf;
    
    private class AuthorData{
        public String authorName;
        public String authorType;
        public String category;
        public long resourceNum;
        public long score; 
    }
    
    private boolean initConf(String configPath){
        try{
            conf = Resources.getResourceAsProperties(configPath);
            url = conf.getProperty("dbConnectUrl");
            user = conf.getProperty("dbUser");
            password = conf.getProperty("dbPassword");
            cutLimit = Integer.valueOf(conf.getProperty("cutLimit"));
            cutRange = Integer.valueOf(conf.getProperty("cutRange"));
            lastOut = Integer.valueOf(conf.getProperty("lastOut"));
            cyctime = Long.valueOf(conf.getProperty("ScoreAuthorCycleTime"));
            return true;
        }catch(Exception ex){
            logger.error("error message",ex);
            return false;
        }
    }
    
    public void start(String configPath){
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
            logger.info("start to compute Author model");
            Map<String,Object> sqlTask= new HashMap<String,Object>();
            try{
                if(!initJdbc()){
                    return;
                }
                
                sqlTask.put("url", url);
                sqlTask.put("user", user);
                sqlTask.put("password",password);
                
                if(!getConn(sqlTask)){
                    return;
                }
                
                Connection conn = (Connection) sqlTask.get("conn");
                Statement typeStmt = ((Connection)(sqlTask.get("conn"))).createStatement();
                ResultSet rs = typeStmt.executeQuery(typeSql);
                
                if(rs == null){
                    return;
                }
                
                ArrayList<String> types = new ArrayList<String>();
                while(rs.next()){    
                    types.add(rs.getString("r_type"));
                }
                rs.close();
                typeStmt.close();
                
                ArrayList<AuthorData> authors = new ArrayList<AuthorData>();
                PreparedStatement psAuthor = conn.prepareStatement(authorSql);
                PreparedStatement psUpNum = conn.prepareStatement(authorUpNumSql);
                PreparedStatement psCompute = conn.prepareStatement(computeSql);
                PreparedStatement psCategory = conn.prepareStatement(categorySql);
                PreparedStatement psSelect = conn.prepareStatement(selectSql);
                PreparedStatement psUpdate = conn.prepareStatement(updateSql);
                PreparedStatement psInsert = conn.prepareStatement(insertSql);
                for(String type : types){
                    authors.clear();
                    psAuthor.setString(1, type);
                    
                    rs = psAuthor.executeQuery();
                    while(rs.next()){
                        AuthorData author = new AuthorData();
                        author.authorName = rs.getString("author");
                        author.authorType = type;
                        
                        authors.add(author);
                    }
                    rs.close();
                    
                    for(AuthorData author : authors){
                        psUpNum.setString(1, author.authorName);
                        
                        rs = psUpNum.executeQuery();
                        if(rs.next()){
                            author.resourceNum = rs.getLong("cnt");                        
                        }
                        rs.close();
                        
                        long start = 0;
                        long end = 0;
                        
                        if(author.resourceNum < cutLimit){
                            start = 1;
                            end = author.resourceNum;
                        }else{
                            start = (cutRange - 1)*author.resourceNum/(cutRange);
                            end = author.resourceNum - lastOut;
                        }
                        
                        psCompute.setString(1, author.authorName);
                        psCompute.setLong(2, start);
                        psCompute.setLong(3, end);
                        
                        rs = psCompute.executeQuery();
                        if(rs.next()){
                            author.score = rs.getLong("score");
                        }
                        rs.close();
                        
                        psCategory.setString(1, author.authorType);
                        psCategory.setString(2, author.authorName);
                        
                        rs = psCategory.executeQuery();
                        if(rs.next()){
                            author.category = rs.getString("r_category");
                        }
                        rs.close();
                        
                        psSelect.setString(1, author.authorType);
                        psSelect.setString(2, author.authorName);
                        rs = psSelect.executeQuery();
                        if(rs.next()){
                            psUpdate.setLong(1, author.score);
                            psUpdate.setLong(2, author.resourceNum);
                            psUpdate.setString(3, author.category);
                            psUpdate.setString(4, author.authorType);
                            psUpdate.setString(5, author.authorName);
                            
                            psUpdate.executeUpdate();
                        }else{
                            psInsert.setString(1, author.authorName);
                            psInsert.setString(2, author.authorType);
                            psInsert.setString(3, author.category);
                            psInsert.setLong(4, author.resourceNum);
                            psInsert.setLong(5, author.score);
                            
                            psInsert.executeUpdate();
                        }
                        rs.close();
                    }
                }
                psAuthor.close();
                psUpNum.close();
                psCompute.close();
                psCategory.close();
                psSelect.close();
                psUpdate.close();
                psInsert.close();
                
                conn.close();
            }catch(Exception ex){
                logger.error("error message",ex);
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
}
