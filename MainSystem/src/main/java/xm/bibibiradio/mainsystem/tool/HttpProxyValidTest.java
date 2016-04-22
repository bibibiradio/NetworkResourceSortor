package xm.bibibiradio.mainsystem.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bibibiradio.httpsender.HttpSender;
import com.bibibiradio.httpsender.HttpSenderImplV1;
import com.bibibiradio.httpsender.ProxyContent;
import com.bibibiradio.httpsender.ProxysParse;
import com.bibibiradio.httpsender.ResponseData;

public class HttpProxyValidTest {
    final private static Logger logger = Logger.getLogger(HttpProxyValidTest.class);
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String proxyString = "120.195.205.232:80,117.136.234.7:81,120.198.236.12:80";
        String testUrl = "http://www.bilibili.com/video/av2893535/";
        List<String> result = new ArrayList<String>();
        ProxyContent[] proxys = ProxysParse.parse(proxyString);
        
        
        
        for(ProxyContent proxy:proxys){
            logger.info("test "+proxy.getIp()+":"+proxy.getPort());
            
            HttpSender httpSender = new HttpSenderImplV1();
            httpSender.setCodec(true);
            httpSender.setRetryTime(1);
            httpSender.setSendFreq(600);
            httpSender.setTimeout(5000);
            httpSender.setSoTimeout(5000);
            httpSender.setHttpProxy(proxy.getIp(), proxy.getPort());
            
            httpSender.start();
            
            Map<String,String> header = new HashMap<String,String>();
            
            int succCnt = 0;
            int falseCnt = 0;
            int sum = 3;
            
            for(int i=0;i<sum;i++){
                ResponseData res = httpSender.send(testUrl,0, header, null);
                if(res == null){
                    falseCnt++;
                }else{
                    succCnt++;
                }
            }
            
            if(succCnt > falseCnt){
                result.add(proxy.getIp()+":"+proxy.getPort()+" valid succCnt "+succCnt);
            }else{
                result.add(proxy.getIp()+":"+proxy.getPort()+" not valid succCnt "+succCnt);
            }
        }
        
        for(String r:result){
            logger.info(r);
        }
    }

}
