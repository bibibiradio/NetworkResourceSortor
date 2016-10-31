package xm.bibibiradio.mainsystem.webservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.mainsystem.webservice.biz.AuthorScoreBiz;
import xm.bibibiradio.mainsystem.webservice.biz.AuthorScoreData;
import xm.bibibiradio.mainsystem.webservice.biz.CategoryBiz;

@Controller
public class AuthorScoreController {
    final static Logger           LOGGER = Logger.getLogger(AuthorScoreController.class);
    static private AuthorScoreBiz authorScoreBiz;
    static private CategoryBiz      categoryBiz;

    @RequestMapping("/authorScore")
    public String greeting(@RequestParam(value = "site", required = false, defaultValue = "1") String site,
                           @RequestParam(value = "category", required = false, defaultValue = "游戏") String category,
                           @RequestParam(value = "page", required = false, defaultValue = "1") String page,
                           Model model) {
        List<AuthorScoreData> list = null;
        try {
            if (authorScoreBiz == null)
                authorScoreBiz = (AuthorScoreBiz) MainSystemBeanFactory.getMainSystemBeanFactory()
                    .getBean("authorScoreBiz");
            if (categoryBiz == null)
                categoryBiz = (CategoryBiz) MainSystemBeanFactory.getMainSystemBeanFactory()
                    .getBean("categoryBiz");
            list = authorScoreBiz.getAuthorsScore(Integer.valueOf(site), category,
                Integer.valueOf(page));
            if (list == null)
                list = new ArrayList<AuthorScoreData>();
        } catch (Exception ex) {
            LOGGER.error("error", ex);
            list = new ArrayList<AuthorScoreData>();
        }
        
        String nextPageUrl = new StringBuilder().append("/authorScore?site=").append(site)
                .append("&category=").append(category).append("&page=").append(Integer.valueOf(page)+1).toString();
        
        String prePageUrl = "";
        if(Integer.valueOf(page) > 1){
            prePageUrl = new StringBuilder().append("/authorScore?site=").append(site)
                    .append("&category=").append(category).append("&page=").append(Integer.valueOf(page)-1).toString();
        }else{
            prePageUrl = new StringBuilder().append("/authorScore?site=").append(site)
                    .append("&category=").append(category).append("&page=").append(1).toString();
        }
        
        String firstPageUrl = new StringBuilder().append("/authorScore?site=").append(site)
                .append("&category=").append(category).append("&page=").append(1).toString();
        
        model.addAttribute("authors", list);
        model.addAttribute("prePageUrl", prePageUrl);
        model.addAttribute("nextPageUrl", nextPageUrl);
        model.addAttribute("firstPageUrl", firstPageUrl);
        model.addAttribute("categorys", categoryBiz.getCategories());
        return "authorScore";
    }
}
