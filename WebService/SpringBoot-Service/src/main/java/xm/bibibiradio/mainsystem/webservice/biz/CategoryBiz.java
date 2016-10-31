package xm.bibibiradio.mainsystem.webservice.biz;

import java.util.ArrayList;
import java.util.List;

public class CategoryBiz {
    public List<String> getCategories(){
        List<String> categories = new ArrayList<String>();
        
        categories.add("游戏");
        categories.add("动画");
        categories.add("娱乐");
        categories.add("广告");
        categories.add("时尚");
        categories.add("生活");
        categories.add("电影");
        categories.add("电视剧");
        categories.add("番剧");
        categories.add("科技");
        categories.add("舞蹈");
        categories.add("音乐");
        categories.add("鬼畜");
        categories.add("公告");
        
        return categories;
    }
}
