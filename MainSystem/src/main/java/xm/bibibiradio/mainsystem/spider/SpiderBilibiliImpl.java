package xm.bibibiradio.mainsystem.spider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.mainsystem.dal.AuthorDAO;
import xm.bibibiradio.mainsystem.dal.AuthorData;
import xm.bibibiradio.mainsystem.dal.ResourceDAO;
import xm.bibibiradio.mainsystem.dal.ResourceData;
import xm.bibibiradio.mainsystem.dal.ViewDAO;
import xm.bibibiradio.mainsystem.dal.ViewData;
import xm.bibibiradio.mainsystem.dal.ViewerDAO;
import xm.bibibiradio.mainsystem.dal.ViewerData;

import com.bibibiradio.httpsender.HttpSender;
import com.bibibiradio.httpsender.HttpSenderImplPool;
import com.bibibiradio.httpsender.HttpSenderImplV1;
import com.bibibiradio.httpsender.HttpSenderPool;
import com.bibibiradio.httpsender.ProxysParse;
import com.bibibiradio.httpsender.ResponseData;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class SpiderBilibiliImpl implements ISpider {
	final private static Logger loggerStarter = Logger.getLogger("bilibiliSpiderStarter");
	final private static Logger loggerUpdater = Logger.getLogger("bilibiliSpiderUpdater");
	final private static int MAXFALSE = 1000;
	
	private HttpSender httpSender = null;
	private HttpSender httpSenderUpdate = null;
	private ResourceDAO resourceDAO = null;
	private ViewDAO viewDAO = null;
	private ViewerDAO viewerDAO = null;
	private AuthorDAO authorDAO = null;
	private ResourceDAO resourceUpdateDAO = null;
    private ViewDAO viewUpdateDAO = null;
    private ViewerDAO viewerUpdateDAO = null;
    private AuthorDAO authorUpdateDAO = null;
    private Properties startConf = null;
    private Properties updateConf = null;
    
    private String startHttpMode;
    private String updateHttpMode;
    private String startProxys;
    private String updateProxys;
    private long startInvalidTime;
    private long updateInvalidTime;
    private int startErrorCount;
    private int updateErrorCount;
    
    static private LoadingCache<String,Optional<Long>> authorCache;
    static private LoadingCache<String,Optional<Long>> viewerCache;
    
    
	
	private static int falseLine = 200;
	private int falseAcc;
	private long id;
	
	private static SimpleDateFormat biliDateFormat;
	
	private Date lastCreate = null;
	private HashMap<String,String> header;
	
	private ArrayList<String> logMsg = new ArrayList<String>();
	static{
		biliDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		authorCache = CacheBuilder.newBuilder()
		        .concurrencyLevel(4)
		        .maximumSize(1000000)
		        .build(new CacheLoader<String,Optional<Long>>(){

                    @Override
                    public Optional<Long> load(String key) throws Exception {
                        // TODO Auto-generated method stub
                        return Optional.absent();
                    }
		            
		        });
		
		viewerCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .maximumSize(1000000)
                .build(new CacheLoader<String,Optional<Long>>(){

                    @Override
                    public Optional<Long> load(String key) throws Exception {
                        // TODO Auto-generated method stub
                        return Optional.absent();
                    }
                    
                });
	}
	
	public SpiderBilibiliImpl(){
		//id = initId();
		header = new HashMap<String,String>();
		
		header.put("Cookie", "buvid=5053AE5C-56C4-46CF-8066-8EA981DA1B55; buvid2=1844E5AF-0F26-4045-9045-3857397629072603infoc; pgv_pvi=9991254016; sid=kamclvpt; tma=136533283.3437607.1419622545652.1420731530151.1422036476340.3; __utma=148949161.1334575712.1419692412.1419859981.1427115773.10; __utmz=148949161.1419692412.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); fts=1438357604; dssid=54an958f779de60fb; dsess=BAh7C0kiD3Nlc3Npb25faWQGOgZFVEkiFTk1OGY3NzlkZTYwZmIyNTAGOwBG%0ASSIKY3RpbWUGOwBGbCsH8egnVUkiCGNpcAY7AEYiFDExNS4xOTMuMTg2LjEz%0AN0kiB29zBjsARkkiHE1pY3Jvc29mdCBXaW5kb3dzIDcgNi4xBjsAVEkiCWNz%0AcmYGOwBGSSIlN2U1MDY0MjdlMGQxZjViMDAxYzE1M2U5MWJmMjg5YmUGOwBG%0ASSINdHJhY2tpbmcGOwBGewdJIhRIVFRQX1VTRVJfQUdFTlQGOwBUSSItMjc4%0ANzVhMzNjNmJjNjNkNDJmMDQxYzQ4YzRlOTA2YTUwNDljYzA3ZQY7AEZJIhlI%0AVFRQX0FDQ0VQVF9MQU5HVUFHRQY7AFRJIi1jYTRhZWUwZTgxMjE0YWRkYzVm%0AYjEyODc3Y2Y5ZTVjOGI4YmViN2Q2BjsARg%3D%3D%0A--07f9c0da14d8f59ab01f392870d565107f933260; PLHistory=c8JV%7Co4mQc; pgv_si=s8655816704; DedeID=2921345; DedeUserID=109047; DedeUserID__ckMd5=06d4ed0bc71a45b9; SESSDATA=d70ea484%2C1443202589%2C8d8b877f; uTZ=-480; _dfcaptcha=952a8682712512d64bfc61a4bb2eb66a; _cnt_dyn=null; _cnt_pm=0; _cnt_notify=30");
        //header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36");
        header.put("User-Agent", "Googlebot/2.1 (+http://www.googlebot.com/bot.html)");
	}
	
	private long initId(){
		String avId = "1";
		try{
			ResourceData rData = resourceDAO.selectMaxDataByType("bilibili");
			if(rData == null){
				return 1;
			}
			
			String lastUrl = rData.getrUrl();
			int first = lastUrl.indexOf("av");
			String lastUrl2 = lastUrl.substring(first+2);
			int end = lastUrl2.indexOf("/");
			avId = lastUrl2.substring(0, end);
		}catch(Exception ex){
			loggerStarter.error("error message",ex);
			
		}
		//loggerStarter.info("find bsite av num "+avId);
		return Long.parseLong(avId)+1;
	}
	
	private Date changeFromBiliDate(String dateString){
		try {
			return biliDateFormat.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			loggerStarter.error("error message",e);
		}
		
		logMsg.add("date format error "+dateString);
		return null;
	}
	
	private boolean initStartOnce(String configPath){
	    try{
	        if(startConf == null){
	            startConf = Resources.getResourceAsProperties(configPath);
	            startHttpMode = startConf.getProperty("bilibiliSpiderHttpMode");
	            startProxys = startConf.getProperty("proxys");
	            startInvalidTime = Long.valueOf(startConf.getProperty("invalidTime"));
	            startErrorCount = Integer.valueOf(startConf.getProperty("errorCount"));
	        }
	        
    	    if(httpSender == null){
    	        if(startHttpMode.equals("single")){
    	            httpSender = new HttpSenderImplV1();
    	        }else if(startHttpMode.equals("proxyPool")){
    	            httpSender = new HttpSenderImplPool();
    	            HttpSenderPool httpSenderPool = (HttpSenderPool)httpSender;
    	            httpSenderPool.setInvalidTime(startInvalidTime);
    	            httpSenderPool.setLimitErrorCount(startErrorCount);
    	            httpSenderPool.setProxys(ProxysParse.parse(startProxys));
    	        }
                httpSender.setCodec(true);
                httpSender.setRetryTime(3);
                httpSender.setSendFreq(600);
                httpSender.setTimeout(30000);
                httpSender.setSoTimeout(30000);
                
                httpSender.start();
    	    }
    	    
    	    if(resourceDAO == null || viewDAO == null || viewerDAO == null || authorDAO == null){
        	    resourceDAO = (ResourceDAO) MainSystemBeanFactory.getMainSystemBeanFactory().getBean("resourceDAO");
                viewDAO = (ViewDAO)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("viewDAO");
                viewerDAO = (ViewerDAO)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("viewerDAO");
                authorDAO = (AuthorDAO)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("authorDAO");
    	    }
    	    
    	    return true;
	    }catch(Exception ex){
	        loggerStarter.error("error message",ex);
	        return false;
	    }
	}
	
	private boolean initUpdateOnce(String configPath){
	    try{
            if(updateConf == null){
                updateConf = Resources.getResourceAsProperties(configPath);
                updateHttpMode = updateConf.getProperty("bilibiliSpiderHttpMode");
                updateProxys = startConf.getProperty("proxys");
                updateInvalidTime = Long.valueOf(startConf.getProperty("invalidTime"));
                updateErrorCount = Integer.valueOf(startConf.getProperty("errorCount"));
            }
            
            if(httpSenderUpdate == null){
                if(updateHttpMode.equals("single")){
                    httpSenderUpdate = new HttpSenderImplV1();
                }else if(updateHttpMode.equals("proxyPool")){
                    httpSenderUpdate = new HttpSenderImplPool();
                    HttpSenderPool httpSenderPool = (HttpSenderPool)httpSenderUpdate;
                    httpSenderPool.setInvalidTime(updateInvalidTime);
                    httpSenderPool.setLimitErrorCount(updateErrorCount);
                    httpSenderPool.setProxys(ProxysParse.parse(updateProxys));
                }
                httpSenderUpdate.setCodec(true);
                httpSenderUpdate.setRetryTime(3);
                httpSenderUpdate.setSendFreq(600);
                httpSenderUpdate.setTimeout(30000);
                httpSenderUpdate.setSoTimeout(30000);
                
                httpSenderUpdate.start();
            }
            
            if(resourceUpdateDAO == null || viewUpdateDAO == null){
                resourceUpdateDAO = (ResourceDAO) MainSystemBeanFactory.getMainSystemBeanFactory().getBean("resourceDAO");
                viewUpdateDAO = (ViewDAO)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("viewDAO");
                viewerUpdateDAO = (ViewerDAO)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("viewerDAO");
                authorUpdateDAO = (AuthorDAO)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("authorDAO");
            }
            
            return true;
        }catch(Exception ex){
            loggerUpdater.error("error message",ex);
            return false;
        }
	}
	
	@Override
	public void start(String configPath) {
		// TODO Auto-generated method stub
	    if(!initStartOnce(configPath)){
	        return;
	    }
	    
	    id = initId();
	    
		while(true){
			try{
				ResourceData rData = new ResourceData();
				rData.setrSource(0);
				rData.setrType("bilibili");
				logMsg.add(String.valueOf(id));
			    if(isEnd(!getter(String.valueOf(id),rData,false),rData)){
			    	logMsg.add("check id is final");
			    	return;
			    }
			}finally{
				if(logMsg.size()>=2){
					loggerStarter.info(logMsg.toString());
				}
				logMsg.clear();
			}
		    id++;
		}
	}
	
	@Override
    public void update(String configPath) {
        // TODO Auto-generated method stub
        if(!initUpdateOnce(configPath)){
            return;
        }
        
        long endTimeStamp = System.currentTimeMillis() - 60*60*1000;
        long startTimeStamp = endTimeStamp - 30*24*60*60*1000;
        
        if(startTimeStamp <= 0 || endTimeStamp <=0){
            loggerUpdater.error("FATAL ERROR SYSTEM TIME IS ERROR");
            return;
        }
        
        long startResourceId;
        long endResourceId;
        ResourceData resourceData;
        resourceData = resourceUpdateDAO.selectDateTypeFirst(new Date(startTimeStamp), new Date(endTimeStamp), "bilibili");
        if(resourceData == null){
            loggerUpdater.error("NOT BILIBILI RESOURCE ,CAN'T UPDATE");
            return;
        }
        startResourceId = resourceData.getrId();
        
        resourceData = resourceUpdateDAO.selectDateTypeEnd(new Date(startTimeStamp), new Date(endTimeStamp), "bilibili");
        if(resourceData == null){
            loggerUpdater.error("NOT BILIBILI RESOURCE ,CAN'T UPDATE");
            return;
        }
        endResourceId = resourceData.getrId();
        
        if(startResourceId > endResourceId){
            loggerUpdater.error("UNKNOWN ERROR");
            return;
        }
        
        long updateId = startResourceId;
        
        for(;updateId <= endResourceId;updateId++){
            try{
                ResourceData rData = new ResourceData();
                rData.setrSource(0);
                rData.setrType("bilibili");
                logMsg.add(String.valueOf(updateId));
                getter(String.valueOf(updateId),rData,true);
            }finally{
                if(logMsg.size()>=2){
                    loggerUpdater.info(logMsg.toString());
                }
                logMsg.clear();
            }
        }
    }
	
	private boolean isError(String resString){
		if(resString.indexOf("对不起，你输入的参数有误") >= 0){
			logMsg.add("not invalid 对不起，你输入的参数有误");
            return true;
        }
		
		if(resString.indexOf("此视频不存在或被删除") >= 0){
			logMsg.add("not invalid 此视频不存在或被删除");
            return true;
		}
		
		if(resString.indexOf("本视频已撞车") >= 0){
		    logMsg.add("not invalid 本视频已撞车");
		    return true;
		}
		
		if(resString.indexOf("<a href=\"https://account.bilibili.com/login\">登录</a> 或 <a href=\"https://account.bilibili.com/register/phone\">注册帐号</a>") >=0){
			logMsg.add("not invalid 未登录");
            return true;
		}
		
		if(resString.indexOf("您<font color=\"red\">无权访问</font>本页面　2.本页面已被<font color=\"red\">删除") >= 0){
			logMsg.add("not invalid 无权访或已被删除");
			return true;
		}
		return false;
	}
	
	private boolean isEnd(boolean isFalse,ResourceData rData){
		if(isFalse){
			falseAcc++;
			logMsg.add("falseAcc add "+falseAcc);
		}else{
			falseAcc = 0;
		}
		
		
		if(falseLine <= falseAcc){
			if(lastCreate == null){
				rData = resourceDAO.selectMaxDataByType("bilibili");
				if(rData == null){
					logMsg.add("not find last create time");
					return false;
				}
				lastCreate = rData.getrGmtCreate();
			}
			
			if(new Date().getTime() - lastCreate.getTime() <= 24*60*60*1000 || falseAcc >= MAXFALSE){
				logMsg.add("falseAcc too large and create time is last");
				return true;
			}
		}
		
		return false;
	}
	
	private long changeFromBiliDuration(String dur){
		String[] factors = dur.split(":");
		int size = factors.length;
		long result = 0;
		if(size == 0){
			return -1;
		}
		
		for(int i=0;i<size;i++){
			result = result*60+Integer.parseInt(factors[i]);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private boolean getter(String id,ResourceData rData,boolean isUpdate){
	    try {
	        HttpSender httpSenderGetter;
	        long maxFloor = 0;
	        if(isUpdate){
	            ViewData viewData = viewUpdateDAO.selectMaxFloor(Long.valueOf(id));
	            if(viewData == null){
	                maxFloor = 0;
	            }else{
	                maxFloor = viewData.getFloor();
	            }
	            
	            httpSenderGetter = httpSenderUpdate;
	        }else{
	            httpSenderGetter = httpSender;
	        }
	        
	        String url = "http://www.bilibili.com/video/av"+id+"/";
	        rData.setrUrl(url);
	        
            ResponseData res = httpSenderGetter.send(url, 0, header, null);
            if(res == null){
            	logMsg.add("url is sth wrong");
                return false;
            }
            String resString = null;
            String imgSrc = null;
            String title = null;
            String author = null;
            String click = null;
            String favourites = null;
            String coins = null;
            String danmu = null;
            String typeId = null;
            String duration = null;
            String resCreateTime = null;
            String tags = null;
            String category = null;

            resString = new String(res.getResponseContent(),"UTF-8");
            
            if(isError(resString)){
            	return false;
            }
            
            Document doc = Jsoup.parse(resString);
            Elements elements = doc.select("img.cover_image");
            Iterator<Element> iter = elements.iterator();
            if(iter.hasNext()){
                Element ele = iter.next();
                imgSrc = ele.attr("src");
            }
            rData.setrShowUrl(imgSrc);
            
            elements = doc.select("a[rel=v:url]");
            {
            	Element ele = elements.get(1);
            	category = ele.text();
            }
            rData.setrCategory(category);
            
            
            elements = doc.getElementsByTag("title");
            iter = elements.iterator();
			title = elements.get(0).text();
            if(iter.hasNext()){
                Element ele = iter.next();
                title = ele.text();
            }
            rData.setrTitle(title);
            
            elements = doc.select("meta[name=keywords]");
            iter = elements.iterator();
            if(iter.hasNext()){
                Element ele = iter.next();
                tags = ele.attr("content");
            }
            rData.setrTags(tags);
            
            elements = doc.select("meta[name=author]");
            iter = elements.iterator();
            if(iter.hasNext()){
                Element ele = iter.next();
                author = ele.attr("content");
            }
            rData.setAuthor(author);
            
            elements = doc.getElementsByTag("time");
            iter = elements.iterator();
            if(iter.hasNext()){
                Element ele = iter.next();
                resCreateTime = ele.text();
            }
            rData.setrGmtCreate(changeFromBiliDate(resCreateTime));
            if(rData.getrGmtCreate() == null){
            	return false;
            }
            lastCreate = rData.getrGmtCreate();
            
            url = "http://interface.bilibili.com/player?id=cid:4522719&aid="+id;
            res = httpSenderGetter.send(url, 0, header, null);
            if(res == null){
            	logMsg.add("player url error");
                return false;
            }
            resString = new String(res.getResponseContent(),"UTF-8");
            doc = Jsoup.parse(resString);
            elements = doc.select("click");
            iter = elements.iterator();
            if(iter.hasNext()){
                Element ele = iter.next();
                click = ele.text();
            }
            rData.setrPv(Long.parseLong(click));
            
            elements = doc.select("favourites");
            iter = elements.iterator();
            if(iter.hasNext()){
                Element ele = iter.next();
                favourites = ele.text();
            }
            rData.setrCollect(Long.parseLong(favourites));
            
            elements = doc.select("coins");
            iter = elements.iterator();
            if(iter.hasNext()){
                Element ele = iter.next();
                coins = ele.text();
            }
            rData.setrCoin(Long.parseLong(coins));
            
            elements = doc.select("danmu");
            iter = elements.iterator();
            if(iter.hasNext()){
                Element ele = iter.next();
                danmu = ele.text();
            }
            rData.setrDanmu(Long.parseLong(danmu));
            
            elements = doc.select("typeid");
            iter = elements.iterator();
            if(iter.hasNext()){
                Element ele = iter.next();
                typeId = ele.text();
            }
            rData.setOtherDetail(typeId);
            
            elements = doc.select("duration");
            iter = elements.iterator();
            if(iter.hasNext()){
                Element ele = iter.next();
                duration = ele.text();
            }
            rData.setrDuration(changeFromBiliDuration(duration));
            
            List<Object> result = getComment(id,httpSender,header,isUpdate);
            
            if(result == null){
            	logMsg.add("comment read fail");
            }
            
            rData.setrComment((Integer)result.get(0));
            List<ViewData> viewDatas = (List<ViewData>) result.get(1);
            if(isUpdate){
                resourceUpdateDAO.updateResource(rData);
            }else{
                rData.setAuthorId(selectOrInsertAuthorTable(authorDAO,rData.getAuthor(),rData.getrType()));
                resourceDAO.insertResource(rData);
            }
            long resourceId = rData.getrId();
            
            for(ViewData viewData : viewDatas){
                if(isUpdate){
                	if(viewData.getFloor() <= maxFloor){
                	    continue;
                	}else{
                	    viewData.setViewerId(selectOrInsertViewerTable(viewerUpdateDAO,viewData.getViewerName(),viewData.getViewType()));
                	    viewUpdateDAO.insertView(viewData);
                	}
                }else{
                    viewData.setResourceId(resourceId);
                    viewData.setViewType("bilibili");
                    viewData.setViewerId(selectOrInsertViewerTable(viewerDAO,viewData.getViewerName(),viewData.getViewType()));
                    viewDAO.insertView(viewData);
                }
            }
        } catch (Exception ex) {
            // TODO Auto-generated catch block
        	loggerStarter.error("error message",ex);
        	logMsg.add(ex.getMessage());
            return false;
        }
	    
	   
	    return true;
	}
	
	public long selectOrInsertAuthorTable(AuthorDAO authorInDAO,String authorName,String authorType) throws Exception{
	    Optional<Long> optAuthorId = authorCache.get(authorName+":!:!:"+authorType);
	    if(optAuthorId.isPresent()){
	        return optAuthorId.get();
	    }
	    
	    AuthorData authorData = authorInDAO.select(authorName, authorType);
        if(authorData != null){
            return authorData.getAuthorId();
        }else{
            authorData = new AuthorData();
            authorData.setAuthorName(authorName);
            authorData.setAuthorType(authorType);
            long authorId = authorInDAO.insert(authorData);
            
            authorCache.put(authorName+":!:!:"+authorType, Optional.of(authorId));
            
            return authorId;
        }
	}
	
	public long selectOrInsertViewerTable(ViewerDAO viewerInDAO,String viewerName,String viewerType) throws Exception{
	    Optional<Long> optViewerId = viewerCache.get(viewerName+":!:!:"+viewerType);
	    if(optViewerId.isPresent()){
	        return optViewerId.get();
	    }
	    
	    ViewerData viewerData = viewerInDAO.select(viewerName, viewerType);
        if(viewerData != null){
            return viewerData.getViewerId();
        }else{
            viewerData = new ViewerData();
            viewerData.setViewerName(viewerName);
            viewerData.setViewerType(viewerType);
            long viewerId = viewerInDAO.insert(viewerData);
            
            viewerCache.put(viewerName+":!:!:"+viewerType, Optional.of(viewerId));
            
            return viewerId;
        }
	}
	
	public List<Object> getComment(String id,HttpSender sender,Map<String,String> header,boolean isUpdate){
		int totalPage = -1;
		int totalItem = -1;
		int count = 1;
		int currentPage = 1;
		ArrayList<ViewData> commentList = new ArrayList<ViewData>();
		ArrayList<Object> result = new ArrayList<Object>();
		
		while(true){
			String url = "http://api.bilibili.com/feedback?type=jsonp&ver=3&callback=jQuery17205967126828618348_1442728322660&mode=arc&aid="+id+"&pagesize=20&page="+currentPage+"&_=1442728340251";
			ResponseData resData = sender.send(url,0,header,null);
			
			if(resData == null){
				return null;
			}
			
			try{
				String commentRaw = new String(resData.getResponseContent(),"UTF-8");
				int idx1 = commentRaw.indexOf("(")+1;
				int idx2 = commentRaw.length()-2;
				String commentJson = commentRaw.substring(idx1, idx2);
				
				JSONObject json = JSONObject.fromObject(commentJson);
				if(totalPage == -1){
					totalPage = Integer.valueOf(json.get("pages").toString());
					currentPage = totalPage;
					continue;
				}
				
				if(totalItem == -1){
					totalItem = Integer.valueOf(json.get("results").toString());
					result.add(0,Integer.valueOf(totalItem));
				}
				
				JSONArray pageCommentJson = json.getJSONArray("list");
				int pageItemNum = pageCommentJson.size();
				count += pageItemNum;
				Iterator<JSONObject> iter = pageCommentJson.iterator();
				while(iter.hasNext()){
					JSONObject comment = iter.next();
					count--;
					
					ViewData viewData = new ViewData();
					viewData.setViewContent(comment.get("msg").toString());
					if(comment.get("nick") == null){
						continue;
					}
					viewData.setViewerName(comment.get("nick").toString());
					viewData.setGmtInsertTime(new Date());
					viewData.setGmtViewTime(changeFromBiliDate(comment.get("create_at").toString()));
					viewData.setFloor(count);
					viewData.setReplyCount(Integer.valueOf(comment.get("reply_count").toString()));
					
					commentList.add(viewData);
				}
				count += pageItemNum;
				
				if(totalItem == -1 && totalPage == -1){
					logMsg.add("totalItem and totalPage is always -1");
					break;
				}
				
				if(currentPage <= 1 || count > 200){
					break;
				}
				
				currentPage--;
			}catch(Exception ex){
				
				loggerStarter.error("error message",ex);
				logMsg.add(ex.getMessage()+" page:"+currentPage);
				if(totalPage == -1 || totalItem == -1){
					return null;
				}
				
				currentPage--;
			}
		}
		
		result.add(1,commentList);
		return result;
	}
	
}
