package xm.bibibiradio.post.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MailSenderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {

        MailSender mailSender = new MailSender();
        mailSender.init("smtp.163.com", "25", false, true, "qbjxiaolei@163.com", "123456");

        try {
            mailSender.sendMail("qbjxiaolei@163.com", "qbjxiaolei@163.com", null, "test_xiaolei", testTemplate());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    static private String testTemplate(){
        String head = "<table>"+
        "<tr>"+
            "<th>缩略图</th>"+
            "<th>标题</th>"+
            "<th>作者</th>"+
            "<th>点击量</th>"+
            "<th>评论量</th>"+
            "<th>分数</th>"+
            "<th>创建时间</th>"+
        "</tr>";
        String loopDodyTemplate = "<tr><td><img src=\"%s\" /></td>"+
            "<td><a href=\"%s\">%s</a></td>"+
            "<td><a href=\"%s\">%s</a></td>"+
            "<td>%s</td>"+
            "<td>%s</td>"+
            "<td>%s</td>"+
            "<td>%s</td></tr>";
        String end = "</table>";
        
        String body = String.format(loopDodyTemplate,"http://i0.hdslb.com/bfs/archive/5a33215f919eb169e340b60e51f05fa473fc79d1.jpg_160x100.jpg"
            ,"http://www.bilibili.com/video/av6785313/"
            ,"[新人处女作]TDA式小鸟的极乐净土_MMD·3D_动画_bilibili_哔哩哔哩弹幕视频网"
            ,"http://space.bilibili.com/35544653"
            ,"扛起沪娘就跑"
            ,"258"
            ,"6"
            ,"143561"
            ,"2016-10-22 23:01");
        return head+body+end;
    }

}
