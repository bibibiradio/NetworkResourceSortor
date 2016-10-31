package xm.bibibiradio.mainsystem.spider;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xm.bibibiradio.mainsystem.dal.ViewData;

import com.bibibiradio.httpsender.HttpSender;
import com.bibibiradio.httpsender.HttpSenderImplV1;
import com.bibibiradio.httpsender.ResponseData;

public class SpiderBilibiliImplTest {
    private static SpiderBilibiliImpl spider;

    @Before
    public void setUp() throws Exception {
        if (spider == null) {
            spider = new SpiderBilibiliImpl();
        }
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void dealError() {
        try {
            HttpSender httpSender = new HttpSenderImplV1();
            httpSender.setCodec(true);
            String url = "http://interface.bilibili.com/player?id=cid:4522719&aid=4159680";
            Map<String, String> header = new HashMap<String, String>();
            header
                .put(
                    "Cookie",
                    "buvid=5053AE5C-56C4-46CF-8066-8EA981DA1B55; buvid2=1844E5AF-0F26-4045-9045-3857397629072603infoc; pgv_pvi=9991254016; sid=kamclvpt; tma=136533283.3437607.1419622545652.1420731530151.1422036476340.3; __utma=148949161.1334575712.1419692412.1419859981.1427115773.10; __utmz=148949161.1419692412.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); fts=1438357604; dssid=54an958f779de60fb; dsess=BAh7C0kiD3Nlc3Npb25faWQGOgZFVEkiFTk1OGY3NzlkZTYwZmIyNTAGOwBG%0ASSIKY3RpbWUGOwBGbCsH8egnVUkiCGNpcAY7AEYiFDExNS4xOTMuMTg2LjEz%0AN0kiB29zBjsARkkiHE1pY3Jvc29mdCBXaW5kb3dzIDcgNi4xBjsAVEkiCWNz%0AcmYGOwBGSSIlN2U1MDY0MjdlMGQxZjViMDAxYzE1M2U5MWJmMjg5YmUGOwBG%0ASSINdHJhY2tpbmcGOwBGewdJIhRIVFRQX1VTRVJfQUdFTlQGOwBUSSItMjc4%0ANzVhMzNjNmJjNjNkNDJmMDQxYzQ4YzRlOTA2YTUwNDljYzA3ZQY7AEZJIhlI%0AVFRQX0FDQ0VQVF9MQU5HVUFHRQY7AFRJIi1jYTRhZWUwZTgxMjE0YWRkYzVm%0AYjEyODc3Y2Y5ZTVjOGI4YmViN2Q2BjsARg%3D%3D%0A--07f9c0da14d8f59ab01f392870d565107f933260; PLHistory=c8JV%7Co4mQc; pgv_si=s8655816704; DedeID=2921345; DedeUserID=109047; DedeUserID__ckMd5=06d4ed0bc71a45b9; SESSDATA=d70ea484%2C1443202589%2C8d8b877f; uTZ=-480; _dfcaptcha=952a8682712512d64bfc61a4bb2eb66a; _cnt_dyn=null; _cnt_pm=0; _cnt_notify=30");
            header
                .put(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36");
            ResponseData res = httpSender.send(url, 0, header, null);
            String resString = new String(res.getResponseContent(), "UTF-8");
            Document doc = Jsoup.parse(resString);
            Elements elements = doc.select("click");
            Iterator<Element> iter = elements.iterator();
            if(iter.hasNext()){
                Element ele = iter.next();
                String click = ele.text();
                System.out.println(Long.parseLong(click));
            }
            
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    //@Test
    public void testHtml() {
        try {
            HttpSender httpSender = new HttpSenderImplV1();
            httpSender.setCodec(true);
            String url = "http://www.bilibili.com/video/av" + 31 + "/";

            System.out.println(url);
            Map<String, String> header = new HashMap<String, String>();
            header
                .put(
                    "Cookie",
                    "buvid=5053AE5C-56C4-46CF-8066-8EA981DA1B55; buvid2=1844E5AF-0F26-4045-9045-3857397629072603infoc; pgv_pvi=9991254016; sid=kamclvpt; tma=136533283.3437607.1419622545652.1420731530151.1422036476340.3; __utma=148949161.1334575712.1419692412.1419859981.1427115773.10; __utmz=148949161.1419692412.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); fts=1438357604; dssid=54an958f779de60fb; dsess=BAh7C0kiD3Nlc3Npb25faWQGOgZFVEkiFTk1OGY3NzlkZTYwZmIyNTAGOwBG%0ASSIKY3RpbWUGOwBGbCsH8egnVUkiCGNpcAY7AEYiFDExNS4xOTMuMTg2LjEz%0AN0kiB29zBjsARkkiHE1pY3Jvc29mdCBXaW5kb3dzIDcgNi4xBjsAVEkiCWNz%0AcmYGOwBGSSIlN2U1MDY0MjdlMGQxZjViMDAxYzE1M2U5MWJmMjg5YmUGOwBG%0ASSINdHJhY2tpbmcGOwBGewdJIhRIVFRQX1VTRVJfQUdFTlQGOwBUSSItMjc4%0ANzVhMzNjNmJjNjNkNDJmMDQxYzQ4YzRlOTA2YTUwNDljYzA3ZQY7AEZJIhlI%0AVFRQX0FDQ0VQVF9MQU5HVUFHRQY7AFRJIi1jYTRhZWUwZTgxMjE0YWRkYzVm%0AYjEyODc3Y2Y5ZTVjOGI4YmViN2Q2BjsARg%3D%3D%0A--07f9c0da14d8f59ab01f392870d565107f933260; PLHistory=c8JV%7Co4mQc; pgv_si=s8655816704; DedeID=2921345; DedeUserID=109047; DedeUserID__ckMd5=06d4ed0bc71a45b9; SESSDATA=d70ea484%2C1443202589%2C8d8b877f; uTZ=-480; _dfcaptcha=952a8682712512d64bfc61a4bb2eb66a; _cnt_dyn=null; _cnt_pm=0; _cnt_notify=30");
            header
                .put(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36");
            ResponseData res = httpSender.send(url, 0, header, null);
            String resString = new String(res.getResponseContent(), "UTF-8");
            Document doc = Jsoup.parse(resString);
            Elements elements = doc.select("meta[name=author]");
            Iterator<Element> iter = elements.iterator();
            if (iter.hasNext()) {
                Element ele = iter.next();
                String author = ele.attr("content");
                System.out.println(author);
            }

        } catch (Exception ex) {

        }
    }

    //@Test
    public void testJson() {
        HttpSender httpSender = new HttpSenderImplV1();
        httpSender.setCodec(true);

        ResponseData resData = httpSender
            .send(
                "http://api.bilibili.com/feedback?type=jsonp&ver=3&callback=jQuery17205967126828618348_1442728322660&mode=arc&aid=2918726&pagesize=20&page=1&_=1442728340251",
                0, null, null);
        try {
            String testJsonS1 = new String(resData.getResponseContent(), "UTF-8");
            int idx1 = testJsonS1.indexOf("(");
            assertTrue(idx1 >= 0);
            idx1++;
            int idx2 = testJsonS1.length() - 2;
            String testJsonS2 = testJsonS1.substring(idx1, idx2);
            System.out.println(testJsonS2);

            JSONObject jsonObject = JSONObject.fromObject(testJsonS2);
            System.out.println(jsonObject.get("results").toString());
            JSONArray jArray = jsonObject.getJSONArray("list");
            System.out.println(jArray.size());
            assertTrue(jArray.size() > 0);
            Iterator<JSONObject> iter = jArray.iterator();
            while (iter.hasNext()) {
                //System.out.println(iter.next().getClass().getName());
                JSONObject commentObj = iter.next();
                System.out.println(commentObj.get("msg"));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    

    //@Test
    public void testStart() {
        //spider.start("");
        assertTrue(true);
    }

}
