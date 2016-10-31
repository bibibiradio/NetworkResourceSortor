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
import xm.bibibiradio.mainsystem.webservice.biz.ResourceScoreBiz;
import xm.bibibiradio.mainsystem.webservice.biz.ResourceScoreData;

@Controller
public class ResourceScoreController {
    final static Logger             LOGGER = Logger.getLogger(ResourceScoreController.class);
    static private ResourceScoreBiz resourceScoreBiz;
    static private CategoryBiz      categoryBiz;

    @RequestMapping("/potentialResources")
    public String greeting(@RequestParam(value = "site", required = false, defaultValue = "1") String site,
                           @RequestParam(value = "type", required = false, defaultValue = "0") String type,
                           @RequestParam(value = "category", required = false, defaultValue = "游戏") String category,
                           @RequestParam(value = "limitDay", required = false, defaultValue = "7") String limitDay,
                           @RequestParam(value = "page", required = false, defaultValue = "1") String page,
                           Model model) {
        List<ResourceScoreData> list = null;
        try {
            if (resourceScoreBiz == null)
                resourceScoreBiz = (ResourceScoreBiz) MainSystemBeanFactory
                    .getMainSystemBeanFactory().getBean("resourceScoreBiz");

            if (categoryBiz == null)
                categoryBiz = (CategoryBiz) MainSystemBeanFactory.getMainSystemBeanFactory()
                    .getBean("categoryBiz");

            list = resourceScoreBiz.getResourceScore(Integer.valueOf(type), Integer.valueOf(site),
                category, Integer.valueOf(page), Integer.valueOf(limitDay));
            if (list == null)
                list = new ArrayList<ResourceScoreData>();
        } catch (Exception ex) {
            LOGGER.error("error", ex);
            list = new ArrayList<ResourceScoreData>();
        }

        String nextPageUrl = new StringBuilder().append("/potentialResources?site=").append(site)
            .append("&type=").append(type).append("&category=").append(category)
            .append("&limitDay=").append(limitDay).append("&page=")
            .append(Integer.valueOf(page) + 1).toString();
        String prePageUrl = "";
        if (Integer.valueOf(page) > 1) {
            prePageUrl = new StringBuilder().append("/potentialResources?site=").append(site)
                .append("&type=").append(type).append("&category=").append(category)
                .append("&limitDay=").append(limitDay).append("&page=")
                .append(Integer.valueOf(page) - 1).toString();
        } else {
            prePageUrl = new StringBuilder().append("/potentialResources?site=").append(site)
                .append("&type=").append(type).append("&category=").append(category)
                .append("&limitDay=").append(limitDay).append("&page=")
                .append(Integer.valueOf(page)).toString();
        }
        
        String firstPageUrl = new StringBuilder().append("/potentialResources?site=").append(site)
                .append("&type=").append(type).append("&category=").append(category)
                .append("&limitDay=").append(limitDay).append("&page=")
                .append(1).toString();

        model.addAttribute("resources", list);
        model.addAttribute("prePageUrl", prePageUrl);
        model.addAttribute("nextPageUrl", nextPageUrl);
        model.addAttribute("firstPageUrl", firstPageUrl);
        model.addAttribute("categorys", categoryBiz.getCategories());
        model.addAttribute("nowCategory",category);
        return "potentialResources";
    }

}
