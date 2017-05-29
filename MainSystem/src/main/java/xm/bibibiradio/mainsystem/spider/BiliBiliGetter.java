package xm.bibibiradio.mainsystem.spider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xm.bibibiradio.mainsystem.dal.ViewData;

import com.bibibiradio.httpsender.HttpSender;
import com.bibibiradio.httpsender.HttpSenderImplPool;
import com.bibibiradio.httpsender.HttpSenderImplV1;
import com.bibibiradio.httpsender.HttpSenderPool;
import com.bibibiradio.httpsender.ProxysParse;
import com.bibibiradio.httpsender.ResponseData;

public class BiliBiliGetter {
    final static private Logger     LOGGER        = Logger.getLogger(BiliBiliGetter.class);
    private String                  imgSrc        = null;
    private String                  title         = null;
    private String                  author        = null;
    private String                  click         = null;
    private String                  favourites    = null;
    private String                  coins         = null;
    private String                  danmu         = null;
    private String                  typeId        = null;
    private String                  duration      = null;
    private long                    durationLong  = 0;
    private String                  resCreateTime = null;
    private Date                    resCreateDate = null;
    private String                  tags          = null;
    private String                  category      = null;
    private int                     totalComment  = 0;
    private String                  authorUrl     = null;
    private String                  authorShowUrl = null;
    List<BiliBiliCommentData>       viewDatas     = null;

    private String                  httpMode;
    private HttpSender              httpSender    = null;

    private String                  proxys;
    private long                    invalidTime;
    private int                     errorCount;

    private SimpleDateFormat        biliDateFormat;
    private HashMap<String, String> header;
    private ArrayList<String>       logMsg        = new ArrayList<String>();

    private long changeFromBiliDuration(String dur) {
        if(dur == null || dur.equals(""))
            return 0;
        String[] factors = dur.split(":");
        int size = factors.length;
        long result = 0;
        if (size == 0) {
            return -1;
        }

        for (int i = 0; i < size; i++) {
            result = result * 60 + Integer.parseInt(factors[i]);
        }

        return result;
    }

    public void init() {
        biliDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (httpSender == null) {
            if (httpMode.equals("single")) {
                httpSender = new HttpSenderImplV1();
            } else if (httpMode.equals("proxyPool")) {
                httpSender = new HttpSenderImplPool();
                HttpSenderPool httpSenderPool = (HttpSenderPool) httpSender;
                httpSenderPool.setInvalidTime(invalidTime);
                httpSenderPool.setLimitErrorCount(errorCount);
                httpSenderPool.setProxys(ProxysParse.parse(proxys));
            }
            httpSender.setCodec(true);
            httpSender.setRetryTime(3);
            httpSender.setSendFreq(1000);
            httpSender.setTimeout(30000);
            httpSender.setSoTimeout(30000);

            httpSender.start();
        }

        header = new HashMap<String, String>();
        header
            .put(
                "Cookie",
                "buvid=5053AE5C-56C4-46CF-8066-8EA981DA1B55; buvid2=1844E5AF-0F26-4045-9045-3857397629072603infoc; pgv_pvi=9991254016; sid=kamclvpt; tma=136533283.3437607.1419622545652.1420731530151.1422036476340.3; __utma=148949161.1334575712.1419692412.1419859981.1427115773.10; __utmz=148949161.1419692412.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); fts=1438357604; dssid=54an958f779de60fb; dsess=BAh7C0kiD3Nlc3Npb25faWQGOgZFVEkiFTk1OGY3NzlkZTYwZmIyNTAGOwBG%0ASSIKY3RpbWUGOwBGbCsH8egnVUkiCGNpcAY7AEYiFDExNS4xOTMuMTg2LjEz%0AN0kiB29zBjsARkkiHE1pY3Jvc29mdCBXaW5kb3dzIDcgNi4xBjsAVEkiCWNz%0AcmYGOwBGSSIlN2U1MDY0MjdlMGQxZjViMDAxYzE1M2U5MWJmMjg5YmUGOwBG%0ASSINdHJhY2tpbmcGOwBGewdJIhRIVFRQX1VTRVJfQUdFTlQGOwBUSSItMjc4%0ANzVhMzNjNmJjNjNkNDJmMDQxYzQ4YzRlOTA2YTUwNDljYzA3ZQY7AEZJIhlI%0AVFRQX0FDQ0VQVF9MQU5HVUFHRQY7AFRJIi1jYTRhZWUwZTgxMjE0YWRkYzVm%0AYjEyODc3Y2Y5ZTVjOGI4YmViN2Q2BjsARg%3D%3D%0A--07f9c0da14d8f59ab01f392870d565107f933260; PLHistory=c8JV%7Co4mQc; pgv_si=s8655816704; DedeID=2921345; DedeUserID=109047; DedeUserID__ckMd5=06d4ed0bc71a45b9; SESSDATA=d70ea484%2C1443202589%2C8d8b877f; uTZ=-480; _dfcaptcha=952a8682712512d64bfc61a4bb2eb66a; _cnt_dyn=null; _cnt_pm=0; _cnt_notify=30");
        //header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36");
        header.put("User-Agent", "Googlebot/2.1 (+http://www.googlebot.com/bot.html)");
    }

    public void get(String id) throws Exception {
        String url = "http://www.bilibili.com/video/av" + id + "/";
        logMsg.clear();
        logMsg.add("inner_id:"+id);
        
        ResponseData res = httpSender.send(url, 0, header, null);
        if (res == null) {
            logMsg.add("url is sth wrong");
            throw new Exception("httpsender error "+logMsg.toString());
        }

        String resString = new String(res.getResponseContent(), "UTF-8");

        if (isError(resString)) {
            throw new SpiderInnerException("is error "+logMsg.toString());
        }
        
        Document doc = Jsoup.parse(resString);
        Elements elements = doc.select("img.cover_image");
        Iterator<Element> iter = elements.iterator();
        if (iter.hasNext()) {
            Element ele = iter.next();
            imgSrc = ele.attr("src");
        }

        elements = doc.select("a[rel=v:url]");
        {
            Element ele = elements.get(1);
            category = ele.text();
        }

        elements = doc.getElementsByTag("title");
        iter = elements.iterator();
        title = elements.get(0).text();
        if (iter.hasNext()) {
            Element ele = iter.next();
            title = ele.text();
        }

        elements = doc.select("meta[name=keywords]");
        iter = elements.iterator();
        if (iter.hasNext()) {
            Element ele = iter.next();
            tags = ele.attr("content");
        }

        elements = doc.select("meta[name=author]");
        iter = elements.iterator();
        if (iter.hasNext()) {
            Element ele = iter.next();
            author = ele.attr("content");
        }
        
        elements = doc.select("img[data-fn-size=68]");
        iter = elements.iterator();
        if (iter.hasNext()) {
            Element ele = iter.next();
            authorShowUrl = ele.attr("data-fn-src");
        }
        
        elements = doc.select("a.name");
        iter = elements.iterator();
        if (iter.hasNext()) {
            Element ele = iter.next();
            authorUrl = ele.attr("href");
        }

        elements = doc.getElementsByTag("time");
        iter = elements.iterator();
        if (iter.hasNext()) {
            Element ele = iter.next();
            resCreateTime = ele.text();
        }
        resCreateDate = changeFromBiliDate(resCreateTime);

        url = "http://interface.bilibili.com/player?id=cid:4522719&aid=" + id;
        res = httpSender.send(url, 0, header, null);
        if (res == null) {
            logMsg.add("player url error");
            throw new Exception("player url error "+logMsg.toString());
        }
        resString = new String(res.getResponseContent(), "UTF-8");

        doc = Jsoup.parse(resString);
        elements = doc.select("click");
        iter = elements.iterator();
        if (iter.hasNext()) {
            Element ele = iter.next();
            click = ele.text();
        }

        elements = doc.select("favourites");
        iter = elements.iterator();
        if (iter.hasNext()) {
            Element ele = iter.next();
            favourites = ele.text();
        }

        elements = doc.select("coins");
        iter = elements.iterator();
        if (iter.hasNext()) {
            Element ele = iter.next();
            coins = ele.text();
        }

        elements = doc.select("danmu");
        iter = elements.iterator();
        if (iter.hasNext()) {
            Element ele = iter.next();
            danmu = ele.text();
        }

        elements = doc.select("typeid");
        iter = elements.iterator();
        if (iter.hasNext()) {
            Element ele = iter.next();
            typeId = ele.text();
        }

        elements = doc.select("duration");
        iter = elements.iterator();
        if (iter.hasNext()) {
            Element ele = iter.next();
            duration = ele.text();
        }
        durationLong = changeFromBiliDuration(duration);

        List<Object> result = getComment(id, httpSender, header);

        if (result == null) {
            logMsg.add("comment read fail");
        }
        totalComment = (Integer) result.get(0);
        viewDatas = (List<BiliBiliCommentData>) result.get(1);
    }

    private boolean isError(String resString) {
        if (resString.indexOf("对不起，你输入的参数有误") >= 0) {
            logMsg.add("not invalid 对不起，你输入的参数有误");
            return true;
        }

        if (resString.indexOf("此视频不存在或被删除") >= 0) {
            logMsg.add("not invalid 此视频不存在或被删除");
            return true;
        }
        
        if(resString.indexOf("http://static.hdslb.com/mstation/images/video/notfound.png") >=0){
            logMsg.add("not invalid 此视频不存在或被删除");
            return true;
        }

        if (resString.indexOf("本视频已撞车") >= 0) {
            logMsg.add("not invalid 本视频已撞车");
            return true;
        }

        if (resString
            .indexOf("<a href=\"https://account.bilibili.com/login\">登录</a> 或 <a href=\"https://account.bilibili.com/register/phone\">注册帐号</a>") >= 0) {
            logMsg.add("not invalid 未登录");
            return true;
        }

        if (resString.indexOf("您<font color=\"red\">无权访问</font>本页面　2.本页面已被<font color=\"red\">删除") >= 0) {
            logMsg.add("not invalid 无权访或已被删除");
            return true;
        }
        return false;
    }

    private Date changeFromBiliDate(String dateString) {
        try {
            return biliDateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            LOGGER.error("error message", e);
        }

        logMsg.add("date format error " + dateString);
        return null;
    }

    public List<Object> getComment(String id, HttpSender sender, Map<String, String> header) {
        int totalPage = -1;
        int totalItem = -1;
        int count = 1;
        int currentPage = 1;
        ArrayList<BiliBiliCommentData> commentList = new ArrayList<BiliBiliCommentData>();
        ArrayList<Object> result = new ArrayList<Object>();

        while (true) {
            String url = "http://api.bilibili.com/feedback?type=jsonp&ver=3&callback=jQuery17205967126828618348_1442728322660&mode=arc&aid="
                         + id + "&pagesize=20&page=" + currentPage + "&_=1442728340251";
            ResponseData resData = sender.send(url, 0, header, null);

            if (resData == null) {
                return null;
            }

            try {
                String commentRaw = new String(resData.getResponseContent(), "UTF-8");
                int idx1 = commentRaw.indexOf("(") + 1;
                int idx2 = commentRaw.length() - 2;
                String commentJson = commentRaw.substring(idx1, idx2);

                JSONObject json = JSONObject.fromObject(commentJson);
                if (totalPage == -1) {
                    totalPage = Integer.valueOf(json.get("pages").toString());
                    currentPage = totalPage;
                    continue;
                }

                if (totalItem == -1) {
                    totalItem = Integer.valueOf(json.get("results").toString());
                    result.add(0, Integer.valueOf(totalItem));
                }

                JSONArray pageCommentJson = json.getJSONArray("list");
                int pageItemNum = pageCommentJson.size();
                count += pageItemNum;
                Iterator<JSONObject> iter = pageCommentJson.iterator();
                while (iter.hasNext()) {
                    JSONObject comment = iter.next();
                    count--;
                    BiliBiliCommentData cData = new BiliBiliCommentData();

                    cData.setContent(comment.get("msg").toString());

                    if (comment.get("nick") == null) {
                        continue;
                    }
                    cData.setViewerName(comment.get("nick").toString());

                    cData.setViewDate(changeFromBiliDate(comment.get("create_at").toString()));

                    cData.setFloor(count);

                    cData.setReplyCount(Integer.valueOf(comment.get("reply_count").toString()));
                    cData.setMid(comment.get("mid").toString());
                    cData.setViewerUrl("http://space.bilibili.com/" + cData.getMid());
                    cData.setSex(comment.get("sex").toString());
                    cData.setShowUrl(comment.get("face").toString());
                    cData.setLevel(Integer.valueOf(comment.get("lv").toString()));

                    commentList.add(cData);
                }
                count += pageItemNum;

                if (totalItem == -1 && totalPage == -1) {
                    logMsg.add("totalItem and totalPage is always -1");
                    break;
                }

                if (currentPage <= 1 || count > 200) {
                    break;
                }

                currentPage--;
            } catch (Exception ex) {

                LOGGER.error("error message", ex);
                logMsg.add(ex.getMessage() + " page:" + currentPage);
                if (totalPage == -1 || totalItem == -1) {
                    return null;
                }

                currentPage--;
            }
        }

        result.add(1, commentList);
        return result;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getFavourites() {
        return favourites;
    }

    public void setFavourites(String favourites) {
        this.favourites = favourites;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getDanmu() {
        return danmu;
    }

    public void setDanmu(String danmu) {
        this.danmu = danmu;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getDurationLong() {
        return durationLong;
    }

    public void setDurationLong(long durationLong) {
        this.durationLong = durationLong;
    }

    public String getResCreateTime() {
        return resCreateTime;
    }

    public void setResCreateTime(String resCreateTime) {
        this.resCreateTime = resCreateTime;
    }

    public Date getResCreateDate() {
        return resCreateDate;
    }

    public void setResCreateDate(Date resCreateDate) {
        this.resCreateDate = resCreateDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }

    public List<BiliBiliCommentData> getViewDatas() {
        return viewDatas;
    }

    public void setViewDatas(List<BiliBiliCommentData> viewDatas) {
        this.viewDatas = viewDatas;
    }

    public String getHttpMode() {
        return httpMode;
    }

    public void setHttpMode(String httpMode) {
        this.httpMode = httpMode;
    }

    public HttpSender getHttpSender() {
        return httpSender;
    }

    public void setHttpSender(HttpSender httpSender) {
        this.httpSender = httpSender;
    }

    public String getProxys() {
        return proxys;
    }

    public void setProxys(String proxys) {
        this.proxys = proxys;
    }

    public long getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(long invalidTime) {
        this.invalidTime = invalidTime;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public SimpleDateFormat getBiliDateFormat() {
        return biliDateFormat;
    }

    public void setBiliDateFormat(SimpleDateFormat biliDateFormat) {
        this.biliDateFormat = biliDateFormat;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    public ArrayList<String> getLogMsg() {
        return logMsg;
    }

    public void setLogMsg(ArrayList<String> logMsg) {
        this.logMsg = logMsg;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getAuthorShowUrl() {
        return authorShowUrl;
    }

    public void setAuthorShowUrl(String authorShowUrl) {
        this.authorShowUrl = authorShowUrl;
    }
    
    
}
