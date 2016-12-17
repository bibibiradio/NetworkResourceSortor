package xm.bibibiradio.mainsystem.webservice.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.mainsystem.webservice.biz.ResourceScoreBiz;
import xm.bibibiradio.mainsystem.webservice.biz.ResourceScoreData;

@RestController
public class ResourceScoreRestfulController {
    final static Logger             LOGGER = Logger.getLogger(ResourceScoreRestfulController.class);
    static private ResourceScoreBiz resourceScoreBiz;

    @RequestMapping("/api/potentialResources")
    public RestfulResult greeting(HttpServletRequest req,
                                  @RequestParam(value = "site", required = false, defaultValue = "1") String site,
                                  @RequestParam(value = "type", required = false, defaultValue = "0") String type,
                                  @RequestParam(value = "category", required = false, defaultValue = "游戏") String category,
                                  @RequestParam(value = "limitDay", required = false, defaultValue = "7") String limitDay,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") String page,
                                  @RequestParam(value = "tag", required = false, defaultValue = "") String tag
                                  ) {
        try {
            List<ResourceScoreData> list = null;
            long allNum = 0;
            
            if (resourceScoreBiz == null)
                resourceScoreBiz = (ResourceScoreBiz) MainSystemBeanFactory
                    .getMainSystemBeanFactory().getBean("resourceScoreBiz");

            list = resourceScoreBiz.getResourceScore(Integer.valueOf(type), Integer.valueOf(site),
                category, Integer.valueOf(page), Integer.valueOf(limitDay),tag);
            if (list == null)
                list = new ArrayList<ResourceScoreData>();

            allNum = resourceScoreBiz.getResourceNum(Integer.valueOf(type), Integer.valueOf(site),
                category, Integer.valueOf(limitDay),tag);

            PageResult<List<ResourceScoreData>> result = new PageResult<List<ResourceScoreData>>(
                allNum, list.size(), Long.valueOf(page), list);
            return RestfulResultUtil.newRestfulResult(result);
        } catch (Exception ex) {
            LOGGER.error("error", ex);
            return RestfulResultUtil.newErrorRestfulResult(ex);
        }

    }
}
