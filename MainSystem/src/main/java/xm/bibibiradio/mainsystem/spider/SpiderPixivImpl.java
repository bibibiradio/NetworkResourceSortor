/**
 * Created by saki on 15/12/15.
 * QQ: 369512841
 */

package xm.bibibiradio.mainsystem.spider;

import com.bibibiradio.httpsender.HttpSender;

import net.sf.json.JSONArray;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.mainsystem.dal.ResourceDAO;
import xm.bibibiradio.mainsystem.dal.ResourceData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.Exception;

import com.bibibiradio.httpsender.HttpSenderImplV1;
import com.bibibiradio.httpsender.ResponseData;

import xm.bibibiradio.mainsystem.dal.ViewDAO;
import xm.bibibiradio.mainsystem.dal.ViewData;

public class SpiderPixivImpl implements ISpider {
    final private static Logger loggerStarter = Logger.getLogger("pixivSpiderStarter");
    final private static Logger loggerUpdater = Logger.getLogger("pixivSpiderUpdater");
    
    private long id;
    private long fetchFailNum;
    private int renewSitePageFlag = 0;
    private int maxRenewNum = 8;
    private long limitFailNum = 300;
    private Date lastResultDate = null;
    private static ResourceDAO resourceDAO = null;
    private static ViewDAO viewDAO = null;
    private static HttpSender httpSender = null;
    private static SimpleDateFormat pixivDateFormat;
    private static Properties conf;

    static {
        httpSender = new HttpSenderImplV1();
        httpSender.setCodec(true);
        httpSender.setRetryTime(3);
        httpSender.setSendFreq(1500);
        httpSender.setTimeout(30000);
        httpSender.setSoTimeout(30000);
        httpSender.setHttpProxy("120.198.236.12", 80);

        resourceDAO = (ResourceDAO) MainSystemBeanFactory.getMainSystemBeanFactory().getBean("resourceDAO");
        viewDAO = (ViewDAO) MainSystemBeanFactory.getMainSystemBeanFactory().getBean("viewDAO");
        //httpSender.setHttpProxy("127.0.0.1", 8080);
    }

    private ArrayList<String> logMsg = new ArrayList<String>();

    public SpiderPixivImpl() {
        id = initId();
    }

    private String getUrlParam(String targetParam, String targetUrl) {
        String[] urlParts = targetUrl.split("\\?");
        if (urlParts.length > 1) {
            String paramQuery = urlParts[1];
            for (String param : paramQuery.split("&")) {
                String[] item = param.split("=");
                if (item[0].equals(targetParam)) {
                    return item[1];
                }
            }
        }
        return "";
    }

    private long initId() {
        String illustId = "10000000";

        try {
            ResourceData rData = resourceDAO.selectMaxDataByType("pixiv");
            if (rData == null) {
                return Long.parseLong(illustId);
            }
            String lastUrl = rData.getrUrl();
            //String lastUrl = "http://www.pixiv.net/member_illust.php?mode=medium&illust_id=23452";
            String respId = getUrlParam("illust_id", lastUrl);

            if (!respId.isEmpty()) {
                illustId = respId;
                return Long.parseLong(illustId) + 1;
            }

        } catch (Exception ex) {
            loggerStarter.error("error message", ex);
        }
        //loggerStarter.info("find bsite av num "+avId);
        return Long.parseLong(illustId);
    }


    @Override
    public void start(String configPath) {
        try {
            conf = Resources.getResourceAsProperties(configPath);
        } catch (Exception ex) {
            loggerStarter.error("error message", ex);
            logMsg.add(ex.getMessage());
        }

        while (true) {

            try {

                ResourceData rData = new ResourceData();
                rData.setrSource(0);
                rData.setrType("pixiv");
                logMsg.add(String.valueOf(id));
                if (isEnd(getter(String.valueOf(id), rData), rData)) {
                    logMsg.add("check id is final");
                    return;
                }

            } finally {
                if (logMsg.size() >= 2) {
                    loggerStarter.info(logMsg.toString());
                }
                logMsg.clear();
            }
            id++;
        }
    }

    private boolean isEnd(int errStatus, ResourceData rData) {

        switch (errStatus) {
            case 0:
                fetchFailNum = 0;
                break;
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
                fetchFailNum++;
                break;
            case 105:
                if(renewSitePageFlag <= maxRenewNum){
                    renewSitePageFlag++;
                    id--;
                    return false;
                }else{
                    renewSitePageFlag = 0;
                    return false;
                }
            case 200:
                return true;
            default:
                return false;
        }

        if (fetchFailNum > limitFailNum) {
            if (lastResultDate == null) {
                rData = resourceDAO.selectMaxDataByType("pixiv");
                if (rData == null) {
                    logMsg.add("not find last create time");
                    return false;
                }
                lastResultDate = rData.getrGmtCreate();

                if (new Date().getTime() - lastResultDate.getTime() <= 24 * 60 * 60 * 1000) {
                    logMsg.add("falseAcc too large and create time is last");
                    return true;
                }
            }
        }
        return false;
    }

    private int isError(String resString) {
        if (resString.indexOf("<p class=\"error-message\">作品IDが取得できませんでした。URLに誤りがないかご確認ください。</p>") >= 0) {
            logMsg.add("not invalid 对不起，不存在该作品ID, 请确认URL是否有误");
            return 100;
        }

        if (resString.indexOf("抱歉，您不可阅览这作品是因为这个作品的公开设置是设为好P友而已／私人状态") >= 0) {
            logMsg.add("not invalid 作品的公开设置是设为好P友而已／私人状态");
            return 101;
        }

        if (resString.indexOf("<p class=\"error-message\">抱歉，您当前访问的作品页面已经被删除，或者不存在。</p>") >= 0) {
            logMsg.add("not invalid 无权访或已被删除");
            return 102;
        }
        if (resString.indexOf("这幅作品已经被删除了") >= 0) {
            logMsg.add("not invalid 作品已经被删除");
            return 103;
        }
        if (resString.indexOf("<div class=\"works_display\">") < 0) {
            logMsg.add("not invalid 未找到作品图片, 可能错误页面");
            return 104;
        }
        if (resString.indexOf("https://www.secure.pixiv.net/login.php") >= 0) {
            logMsg.add("not invalid 未登录");
            return 200;
        }


        return 0;
    }

    private Date changeFromPixivDate(String dateString) {
        try {
            SimpleDateFormat pixivDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            return pixivDateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        logMsg.add("date format error " + dateString);
        return null;
    }

    private List<Object> getHtmlText(String url, Map<String, String> header) {
        String resString = "";
        ArrayList<Object> resp = new ArrayList<Object>();
        try {
            System.out.println(url);
            header.put("Cookie", conf.getProperty("pixivCookie"));
            header.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36");
            ResponseData res = httpSender.send(url, 0, header, null);
            if (res == null) {
                logMsg.add("url is sth wrong");
                resp.add(0, 400);
                return resp;
            }

            resString = new String(res.getResponseContent(), "UTF-8");

            int errStatus = isError(resString);
            resp.add(0, errStatus);
            if (errStatus >= 100) {
                resp.add(1, "");
            } else {
                resp.add(1, resString);
            }

        } catch (Exception ex) {
            loggerStarter.error("error message", ex);
            logMsg.add(ex.getMessage());
        }

        return resp;

    }

    private int getter(String id, ResourceData rData) {
        String htmlText = "";
        try {
            Map<String, String> header = new HashMap<String, String>();

            String resCreateTime = null;
            String title = null;
            String author = null;
            String viewCount = null;
            String ratedCount = null;
            String scoreCount = null;
            String showUrl = null;
            String multiplePicUrl = null;
            //ArrayList<String> tags = new ArrayList<String>();
            String tags = "";
            HashMap<String, String> otherDetail = new HashMap<String, String>();
            //Iterator<Element> iter = null;

            String url = "http://www.pixiv.net/member_illust.php?mode=medium&illust_id=" + id;
            rData.setrUrl(url);

            //获取页面Document树
            List<Object> respResult = getHtmlText(url, header);
            int errStatus = Integer.parseInt(respResult.get(0).toString());
            if (errStatus != 0) {
                return errStatus;
            }

            htmlText = respResult.get(1).toString();
            if (htmlText.isEmpty()) {
                logMsg.add("url is sth wrong: the empty HTML");
                return 400;
            }
            Document doc = Jsoup.parse(htmlText);

            //获取作品创建时间
            Elements elements = doc.select(".work-info .meta li");
            Iterator<Element> iter = elements.iterator();
            if (iter.hasNext()) {
                Element item = iter.next();
                resCreateTime = item.text();
                if (resCreateTime.matches(".*年.*月.*日.*")) {
                    rData.setrGmtCreate(changeFromPixivDate(resCreateTime));
                } else {
                    return 105;
                }
            }else{
                return 300;
            }
            /*Elements elements = doc.select("form.bookmark-detail-unit span.meta");
            resCreateTime = elements.get(0).text();
            rData.setrGmtCreate(changeFromPixivDate(resCreateTime));*/

            //设置作品类别为图片
            rData.setrCategory("图片");

            //获取作品名称
            elements = doc.select(".work-info .title");
            title = elements.get(0).text();
            rData.setrTitle(title);

            //获取作者名称
            elements = doc.select(".user-link .user");
            author = elements.get(0).text();
            rData.setAuthor(author);

            //获取浏览量
            elements = doc.select(".score .view-count");
            viewCount = elements.get(0).text();
            rData.setrPv(Long.parseLong(viewCount));

            //获取评分次数
            elements = doc.select(".score .rated-count");
            ratedCount = elements.get(0).text();
            otherDetail.put("ratedCount", ratedCount);

            //获取总分
            elements = doc.select(".score .score-count");
            scoreCount = elements.get(0).text();
            otherDetail.put("scoreCount", scoreCount);

            //获取主要图片以及图片集地址
            elements = doc.select(".works_display ._layout-thumbnail img");
            showUrl = elements.get(0).attr("src");
            rData.setrShowUrl(showUrl);
            if (!doc.select(".works_display .multiple").isEmpty()) {
                elements = doc.select(".works_display a._work.multiple");
                multiplePicUrl = "http://www.pixiv.net/" + elements.get(0).attr("href");
                otherDetail.put("multiplePicUrl", multiplePicUrl);
            } else {
                otherDetail.put("multiplePicUrl", "");
            }

            //获取作品tags
            elements = doc.select(".tags-container .tags li a.text");
            iter = elements.iterator();
            while (iter.hasNext()) {
                Element item = iter.next();
                tags = tags + item.text();
                if (iter.hasNext()) {
                    tags = tags + ",";
                }
            }
            rData.setrTags(tags);

            //设置otherDetail
            rData.setOtherDetail(JSONObject.fromObject(otherDetail).toString());

            //获取评论数据
            elements = doc.select("a.user-link");
            String memberInfoUrl = elements.attr("href");
            String userId = getUrlParam("id", memberInfoUrl);

            List<Object> cmtResult = getComment(id, userId, httpSender, header);
            if (cmtResult == null) {
                logMsg.add("comment read fail");
            }

            //设置评论数量
            rData.setrComment((Long) cmtResult.get(0));

            //处理默认值
            rData.setrCategory("");
            rData.setrGmtInsert(new Date());

            //作品信息存入数据库
            resourceDAO.insertResource(rData);

            //补充评论数据信息: 1.所属站点 2.所属作品id
            List<ViewData> viewDatas = (List<ViewData>) cmtResult.get(1);
            long resourceId = rData.getrId();
            for (ViewData viewData : viewDatas) {
                viewData.setResourceId(resourceId);
                viewData.setViewType("pixiv");
                viewDAO.insertView(viewData);
            }

        } catch (Exception ex) {
            // TODO Auto-generated catch block

            JSONArray jsonObject = JSONArray.fromObject(rData);
            System.out.println(jsonObject);
            loggerStarter.error("error message", ex);
            logMsg.add(ex.getMessage());
            logMsg.add(htmlText);

            return 300;
        }
        return 0;
    }

    private List<Object> getComment(String illustId, String userId, HttpSender httpSender, Map<String, String> header) {
        ArrayList<Object> result = new ArrayList<Object>();
        ArrayList<ViewData> commentList = new ArrayList<ViewData>();
        try {
            long page = 1;
            long cmtCount = 0;
            boolean hasNextPage = true;

            String name = null;
            String createTime = null;
            String content = null;

            String url = "http://www.pixiv.net/rpc_comment_history.php";
            String postParam = "i_id=" + illustId + "&u_id=" + userId;
            header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            header.put("Referer", "http://www.pixiv.net/member_illust.php?mode=medium&illust_id=" + illustId);

            Elements elements = new Elements();

            /*
            * 异步请求所有评论数据
            * */
            while (hasNextPage) {
                postParam = postParam + "&p=" + page;
                ResponseData res = httpSender.send(url, 1, header, postParam.getBytes("UTF-8"));
                String resString = new String(res.getResponseContent(), "UTF-8");

                //获取返回的json数据中评论html代码
                JSONObject respObj = JSONObject.fromObject(resString);
                JSONObject bodyObj = respObj.getJSONObject("body");
                String nextPage = bodyObj.getString("more");
                String cmtHtml = bodyObj.getString("html");
                Document cmtDoc = Jsoup.parse(cmtHtml);

                Elements elemsGroup = cmtDoc.select("._comment-item");
                elements.addAll(elemsGroup);

                if (!Boolean.valueOf(nextPage).booleanValue()) {
                    hasNextPage = false;
                } else {
                    page = page + 1;
                }
            }

            /*
            * 评论数据提取相关信息
            * */
            cmtCount = elements.size();
            int floorNum = (int) cmtCount;
            Iterator<Element> iter = elements.iterator();
            while (iter.hasNext()) {

                ViewData viewData = new ViewData();
                Element item = iter.next();

                //获取评论用户名称
                Elements elem = item.select(".meta .user-name");
                name = elem.text();
                viewData.setViewerName(name);

                //获取评论发表时间
                elem = item.select(".meta .date");
                createTime = elem.text();
                if (createTime.matches(".*年.*月.*日.*")) {
                    viewData.setGmtViewTime(changeFromPixivDate(createTime));
                }

                //获取评论内容
                elem = item.select(".comment .body");
                content = elem.text();
                if (elem.isEmpty()) {
                    elem = item.select(".comment .sticker-container img");
                    content = "CMT_PIC:" + elem.attr("data-src");
                }
                viewData.setViewContent(content);

                //设置插入数据库的时间
                viewData.setGmtInsertTime(new Date());

                //获取该评论所在楼层数
                if (floorNum > 0) {
                    viewData.setFloor(floorNum);
                    floorNum = floorNum - 1;
                }

                commentList.add(viewData);
            }

            result.add(0, cmtCount);
            result.add(1, commentList);


        } catch (Exception ex) {
            // TODO Auto-generated catch block
            loggerStarter.error("error message", ex);
            logMsg.add(ex.getMessage());
            return new ArrayList<Object>();
        }
        return result;
    }

    @Override
    public void update(String configPath) {
        // TODO Auto-generated method stub
        
    }


}
