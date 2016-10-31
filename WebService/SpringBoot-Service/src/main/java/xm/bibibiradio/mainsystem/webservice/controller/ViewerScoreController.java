package xm.bibibiradio.mainsystem.webservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.mainsystem.webservice.biz.CategoryBiz;
import xm.bibibiradio.mainsystem.webservice.biz.ViewerScoreBiz;
import xm.bibibiradio.mainsystem.webservice.biz.ViewerScoreData;

@Controller
public class ViewerScoreController {
    final static Logger             LOGGER = Logger.getLogger(ResourceScoreController.class);
    static private ViewerScoreBiz viewerScoreBiz;
    static private CategoryBiz      categoryBiz;

    @RequestMapping("/viewerScore")
    public String greeting(@RequestParam(value = "site", required = false, defaultValue = "1") String site,
                           @RequestParam(value = "category", required = false, defaultValue = "游戏") String category,
                           @RequestParam(value = "page", required = false, defaultValue = "1") String page,
                           Model model) {
        List<ViewerScoreData> list = null;
        try {
            if (viewerScoreBiz == null)
                viewerScoreBiz = (ViewerScoreBiz) MainSystemBeanFactory
                    .getMainSystemBeanFactory().getBean("viewerScoreBiz");
            if (categoryBiz == null)
                categoryBiz = (CategoryBiz) MainSystemBeanFactory.getMainSystemBeanFactory()
                    .getBean("categoryBiz");

            list = viewerScoreBiz.getViewerScore(Integer.valueOf(site), category,
                Integer.valueOf(page));
            if (list == null)
                list = new ArrayList<ViewerScoreData>();
        } catch (Exception ex) {
            LOGGER.error("error", ex);
            list = new ArrayList<ViewerScoreData>();
        }
        
        String nextPageUrl = new StringBuilder().append("/viewerScore/site=").append(site)
                .append("&category=").append(category).append("&page=").append(Integer.valueOf(page)+1).toString();
        
        String prePageUrl = "";
        if(Integer.valueOf(page) > 1){
            prePageUrl = new StringBuilder().append("/viewerScore/site=").append(site)
                    .append("&category=").append(category).append("&page=").append(Integer.valueOf(page)-1).toString();
        }else{
            prePageUrl = new StringBuilder().append("/authorScore/site=").append(site)
                    .append("&category=").append(category).append("&page=").append(1).toString();
        }
        
        String firstPageUrl = new StringBuilder().append("/authorScore/site=").append(site)
                .append("&category=").append(category).append("&page=").append(1).toString();
        
        model.addAttribute("viewers", list);
        model.addAttribute("prePageUrl", prePageUrl);
        model.addAttribute("nextPageUrl", nextPageUrl);
        model.addAttribute("firstPageUrl", firstPageUrl);
        model.addAttribute("categorys", categoryBiz.getCategories());
        return "viewerScore";
    }
    
    
}
