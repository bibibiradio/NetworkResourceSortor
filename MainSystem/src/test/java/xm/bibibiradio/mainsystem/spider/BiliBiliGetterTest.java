package xm.bibibiradio.mainsystem.spider;

import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.mainsystem.dal.ResourceDAO;
import xm.bibibiradio.mainsystem.dal.ResourceData;
import xm.bibibiradio.mainsystem.starter.Env;

public class BiliBiliGetterTest {
    final static Logger LOGGER = Logger.getLogger(BiliBiliGetterTest.class);
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    //@Test
    public void test1(){
        try{
            System.out.println(new String(Base64.encodeBase64("测试测试".getBytes())));
        }catch(Exception ex){
            
        }
    }
    @Test
    public void test() {
        ResourceDAO testDAO = null;
        if(testDAO == null){
            Env.setEnvFlag(1);
            testDAO = (ResourceDAO) MainSystemBeanFactory.getMainSystemBeanFactory().getBean("resourceDAO");
        }
        BiliBiliGetter getter = new BiliBiliGetter();
        getter.setHttpMode("single");
        getter.init();
        
        try{
            getter.get("5544338");
            
            LOGGER.info(getter.getAuthor());
            LOGGER.info(getter.getCategory());
            LOGGER.info(getter.getClick());
            LOGGER.info(getter.getCoins());
            LOGGER.info(getter.getDanmu());
            LOGGER.info(getter.getDuration());
            LOGGER.info(getter.getDurationLong());
            LOGGER.info(getter.getFavourites());
            LOGGER.info(getter.getImgSrc());
            LOGGER.info(getter.getResCreateDate());
            LOGGER.info(getter.getResCreateTime());
            LOGGER.info(getter.getTags());
            //QTS4WoATVSB3MQwgDx8aIbKrS9VNEJ9fS+VfHztfYmlsaWJpbGlf1OnU6TlVxpFR
            LOGGER.info(getter.getTitle());
            LOGGER.info(getter.getTotalComment());
            LOGGER.info(getter.getTypeId());
            LOGGER.info("asu:"+getter.getAuthorShowUrl());
            LOGGER.info("au:"+getter.getAuthorUrl());
            
            ResourceData rd = new ResourceData();
            rd.setAuthorId(0);
            rd.setOtherDetail("");
            rd.setrCategory(getter.getCategory());
            rd.setrCoin(0);
            rd.setrCollect(0);
            rd.setrComment(1);
            rd.setrDanmu(1);
            rd.setrDuration(2);
            rd.setrPv(1);
            rd.setrShowUrl("");
            rd.setrUrl("");
            rd.setrTags("123");
            rd.setrGmtCreate(new Date());
            rd.setrTitle(getter.getTitle());
            testDAO.insertResource(rd);
            for(BiliBiliCommentData vd : getter.getViewDatas()){
                LOGGER.info(vd.getViewerName()+" "+vd.getMid()+" "+vd.getFloor()+" "+vd.getReplyCount()
                    +" "+vd.getLevel()+" "+vd.getSex()+" "+vd.getShowUrl()+" "+vd.getViewerUrl()+" "+vd.getContent());
            }
            
        }catch(Exception ex){
            LOGGER.error("error",ex);
        }
    }

}
