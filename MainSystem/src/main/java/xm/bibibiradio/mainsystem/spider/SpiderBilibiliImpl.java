package xm.bibibiradio.mainsystem.spider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.mainsystem.dal.AuthorDAO;
import xm.bibibiradio.mainsystem.dal.AuthorData;
import xm.bibibiradio.mainsystem.dal.ResourceDAO;
import xm.bibibiradio.mainsystem.dal.ResourceData;
import xm.bibibiradio.mainsystem.dal.ViewDAO;
import xm.bibibiradio.mainsystem.dal.ViewData;
import xm.bibibiradio.mainsystem.dal.ViewerDAO;
import xm.bibibiradio.mainsystem.dal.ViewerData;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class SpiderBilibiliImpl implements ISpider {
    final private static Logger                  LOGGER       = Logger
                                                                  .getLogger(SpiderBilibiliImpl.class);
    final private static int                     MAXFALSE     = 1000;
    private BiliBiliGetter                       forwardGetter;
    private BiliBiliGetter                       afterGetter;
    private BiliBiliGetter                       updateGetter;

    private ResourceDAO                          resourceDAO  = null;
    private ViewDAO                              viewDAO      = null;
    private ViewerDAO                            viewerDAO    = null;
    private AuthorDAO                            authorDAO    = null;
    private Properties                           conf         = null;

    private String                               httpMode;

    private String                               proxys;
    private long                                 invalidTime;
    private int                                  errorCount;

    private LoadingCache<String, Optional<Long>> authorCache;
    private LoadingCache<String, Optional<Long>> viewerCache;

    private static int                           falseLine    = 200;
    private long                                  forwardRound = 0;
    private int                                  falseAcc;

    public void init(String configPath) throws Exception {

        authorCache = CacheBuilder.newBuilder().concurrencyLevel(4).maximumSize(1000000)
            .build(new CacheLoader<String, Optional<Long>>() {

                @Override
                public Optional<Long> load(String key) throws Exception {
                    // TODO Auto-generated method stub
                    return Optional.absent();
                }

            });

        viewerCache = CacheBuilder.newBuilder().concurrencyLevel(4).maximumSize(1000000)
            .build(new CacheLoader<String, Optional<Long>>() {

                @Override
                public Optional<Long> load(String key) throws Exception {
                    // TODO Auto-generated method stub
                    return Optional.absent();
                }

            });
        falseAcc = 0;
        if (conf == null) {
            conf = Resources.getResourceAsProperties(configPath);
            httpMode = conf.getProperty("bilibiliSpiderHttpMode");
            proxys = conf.getProperty("proxys");
            invalidTime = Long.valueOf(conf.getProperty("invalidTime"));
            errorCount = Integer.valueOf(conf.getProperty("errorCount"));
        }

        forwardGetter = new BiliBiliGetter();
        afterGetter = new BiliBiliGetter();
        updateGetter = new BiliBiliGetter();

        forwardGetter.setHttpMode(httpMode);
        forwardGetter.setProxys(proxys);
        forwardGetter.setInvalidTime(invalidTime);
        forwardGetter.setErrorCount(errorCount);
        afterGetter.setHttpMode(httpMode);
        afterGetter.setProxys(proxys);
        afterGetter.setInvalidTime(invalidTime);
        afterGetter.setErrorCount(errorCount);
        updateGetter.setHttpMode(httpMode);
        updateGetter.setProxys(proxys);
        updateGetter.setInvalidTime(invalidTime);
        updateGetter.setErrorCount(errorCount);

        forwardGetter.init();
        afterGetter.init();
        updateGetter.init();

        if (resourceDAO == null || viewDAO == null || viewerDAO == null || authorDAO == null) {
            resourceDAO = (ResourceDAO) MainSystemBeanFactory.getMainSystemBeanFactory().getBean(
                "resourceDAO");
            viewDAO = (ViewDAO) MainSystemBeanFactory.getMainSystemBeanFactory().getBean("viewDAO");
            viewerDAO = (ViewerDAO) MainSystemBeanFactory.getMainSystemBeanFactory().getBean(
                "viewerDAO");
            authorDAO = (AuthorDAO) MainSystemBeanFactory.getMainSystemBeanFactory().getBean(
                "authorDAO");
        }

    }

    private long initForwardId() {
        String avId = "5544331";
        try {
            Long innerId = resourceDAO.selectMaxDataBySite(1);
            if (innerId == null) {
                LOGGER.info("initForwardId get" + avId);
                return Long.parseLong(avId);
            }
            forwardRound++;
            LOGGER.info("initForwardId get" + innerId);
            return innerId;
        } catch (Exception ex) {
            LOGGER.error("error message", ex);
        }
        //loggerStarter.info("find bsite av num "+avId);
        LOGGER.info("initForwardId get" + avId+" and error forward round "+forwardRound);
        return Long.parseLong(avId);
    }

    private long initAfterId() {
        String avId = "5544330";
        try {
            Long innerId = resourceDAO.selectMinDataBySite(1);
            if (innerId == null) {
                LOGGER.info("initAfterId get" + avId);
                return Long.parseLong(avId);
            }

            LOGGER.info("initAfterId get" + innerId);
            return innerId;
        } catch (Exception ex) {
            LOGGER.error("error message", ex);

        }
        //loggerStarter.info("find bsite av num "+avId);
        LOGGER.info("initAfterId get" + avId);
        return Long.parseLong(avId);
    }

    @Override
    public void startForward(String configPath) {
        // TODO Auto-generated method stub
        long id = initForwardId();
        ArrayList<String> logMsg = new ArrayList<String>();
        while (true) {
            try {
                ResourceData rData = new ResourceData();
                AuthorData aData = new AuthorData();
                rData.setrSite(1);
                rData.setrType(0);
                logMsg.add(String.valueOf(id));
                try {
                    forwardGetter.get(String.valueOf(id));
                    aData.setAuthorName(forwardGetter.getAuthor());
                    aData.setAuthorSite(1);
                    aData.setAuthorShowUrl(forwardGetter.getAuthorShowUrl());
                    aData.setAuthorUrl(forwardGetter.getAuthorUrl());
                    aData.setAuthorCategory("");
                    rData.setAuthorId(selectOrInsertAuthorTable(authorDAO, aData));
                    rData.setOtherDetail("fg");
                    rData.setrCategory(forwardGetter.getCategory());
                    rData.setrCoin(Long.parseLong(forwardGetter.getCoins()));
                    rData.setrCollect(0);
                    rData.setrComment(forwardGetter.getTotalComment());
                    rData.setrDanmu(Long.parseLong(forwardGetter.getDanmu()));
                    rData.setrDuration(forwardGetter.getDurationLong());
                    rData.setrPv(Long.parseLong(forwardGetter.getClick()));
                    rData.setrShowUrl(forwardGetter.getImgSrc());
                    rData.setrUrl("http://www.bilibili.com/video/av" + id + "/");
                    rData.setrTags(forwardGetter.getTags());
                    rData.setrGmtCreate(forwardGetter.getResCreateDate());
                    rData.setrTitle(forwardGetter.getTitle());
                    rData.setrInnerId(id);
                    forwardRound = 0;
                    falseAcc = 0;
                    
                    resourceDAO.insertResource(rData);
                    long resourceId = rData.getrId();
                    for (BiliBiliCommentData cData : forwardGetter.getViewDatas()) {
                        ViewData viewData = new ViewData();
                        viewData.setFloor(cData.getFloor());
                        viewData.setGmtViewTime(cData.getViewDate());
                        viewData.setReplyCount(cData.getReplyCount());
                        viewData.setrId(resourceId);
                        viewData.setViewContent(cData.getContent());

                        ViewerData viewerData = new ViewerData();
                        viewerData.setViewerLevel(cData.getLevel());
                        viewerData.setViewerName(cData.getViewerName());
                        if (cData.getSex().equals("男"))
                            viewerData.setViewerSex(0);
                        else if (cData.getSex().equals("女"))
                            viewerData.setViewerSex(1);
                        else
                            viewerData.setViewerSex(2);
                        viewerData.setViewerShowUrl(cData.getShowUrl());
                        viewerData.setViewerSite(1);
                        viewerData.setViewerUrl(cData.getViewerUrl());
                        viewerData.setViewerCategory("");

                        viewData.setViewerId(selectOrInsertViewerTable(viewerDAO, viewerData));

                        viewDAO.insertView(viewData);
                    }

                } catch (Exception ex) {
                    LOGGER.error(logMsg.toString() + " error", ex);
                    if (isForwardEnd(id)) {
                        return;
                    }
                }
            } finally {
                if (isForwardEnd(id)) {
                    return;
                }
                if (logMsg.size() >= 2) {
                    LOGGER.info(logMsg.toString());
                }
                logMsg.clear();
                id++;
            }
        }
    }

    private boolean isForwardEnd(long id) {
        falseAcc++;
        if (falseAcc >= MAXFALSE) {
            return true;
        }
        return false;
    }

    @Override
    public void startAfter(String configPath) throws Exception {
        // TODO Auto-generated method stub
        long id = initAfterId();
        ArrayList<String> logMsg = new ArrayList<String>();
        while (true) {
            try {
                ResourceData rData = new ResourceData();
                AuthorData aData = new AuthorData();
                rData.setrSite(1);
                rData.setrType(0);
                logMsg.add(String.valueOf(id));
                try {
                    afterGetter.get(String.valueOf(id));
                    aData.setAuthorName(afterGetter.getAuthor());
                    aData.setAuthorSite(1);
                    aData.setAuthorShowUrl(afterGetter.getAuthorShowUrl());
                    aData.setAuthorUrl(afterGetter.getAuthorUrl());
                    aData.setAuthorCategory("");
                    rData.setAuthorId(selectOrInsertAuthorTable(authorDAO, aData));
                    rData.setOtherDetail("ag");
                    rData.setrCategory(afterGetter.getCategory());
                    rData.setrCoin(Long.parseLong(afterGetter.getCoins()));
                    rData.setrCollect(0);
                    rData.setrComment(afterGetter.getTotalComment());
                    rData.setrDanmu(Long.parseLong(afterGetter.getDanmu()));
                    rData.setrDuration(afterGetter.getDurationLong());
                    rData.setrPv(Long.parseLong(afterGetter.getClick()));
                    rData.setrShowUrl(afterGetter.getImgSrc());
                    rData.setrUrl("http://www.bilibili.com/video/av" + id + "/");
                    rData.setrTags(afterGetter.getTags());
                    rData.setrGmtCreate(afterGetter.getResCreateDate());
                    rData.setrTitle(afterGetter.getTitle());
                    rData.setrInnerId(id);

                    resourceDAO.insertResource(rData);
                    long resourceId = rData.getrId();

                    for (BiliBiliCommentData cData : afterGetter.getViewDatas()) {
                        ViewData viewData = new ViewData();
                        viewData.setFloor(cData.getFloor());
                        viewData.setGmtViewTime(cData.getViewDate());
                        viewData.setReplyCount(cData.getReplyCount());
                        viewData.setrId(resourceId);
                        viewData.setViewContent(cData.getContent());

                        ViewerData viewerData = new ViewerData();
                        viewerData.setViewerLevel(cData.getLevel());
                        viewerData.setViewerName(cData.getViewerName());
                        if (cData.getSex().equals("男"))
                            viewerData.setViewerSex(0);
                        else if (cData.getSex().equals("女"))
                            viewerData.setViewerSex(1);
                        else
                            viewerData.setViewerSex(2);
                        viewerData.setViewerShowUrl(cData.getShowUrl());
                        viewerData.setViewerSite(1);
                        viewerData.setViewerUrl(cData.getViewerUrl());
                        viewerData.setViewerCategory("");

                        viewData.setViewerId(selectOrInsertViewerTable(viewerDAO, viewerData));

                        viewDAO.insertView(viewData);
                    }
                } catch (Exception ex) {
                    LOGGER.error(logMsg.toString() + " error", ex);
                    if (isAfterEnd(id)) {
                        return;
                    }
                }
            } finally {
                if (logMsg.size() >= 2) {
                    LOGGER.info(logMsg.toString());
                }
                logMsg.clear();
            }
            id--;
        }
    }

    private boolean isAfterEnd(long id) {
        if (id <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public void updateNow(String configPath) {

        long endTimeStamp = System.currentTimeMillis() - 10 * 60 * 1000L;
        long startTimeStamp = endTimeStamp - 21 * 24 * 60 * 60 * 1000L;
        ArrayList<String> logMsg = new ArrayList<String>();
        List<Long> rIds = null;
        
        try{
            if (startTimeStamp <= 0 || endTimeStamp <= 0) {
                LOGGER.error("FATAL ERROR SYSTEM TIME IS ERROR");
                return;
            }
    
            rIds = resourceDAO.selectDateSiteList(new Date(startTimeStamp), new Date(endTimeStamp), 1);
        }catch(Exception ex){
            LOGGER.error("error",ex);
            return;
        }

        

        for (long rId : rIds) {
            try {
                ResourceData rData = new ResourceData();
                rData.setrSite(1);
                rData.setrType(0);
                try {
                    
                    HashMap<String,Object> hm = resourceDAO.selectCommentByRid(rId);
                    long oldComment = (Long)hm.get("r_comment");
                    long avId = (Long)hm.get("r_inner_id");
                    
                    logMsg.add(String.valueOf(avId));
                    
                    updateGetter.get(String.valueOf(avId));
                    
                    AuthorData aData = new AuthorData();
                    aData.setAuthorName(updateGetter.getAuthor());
                    aData.setAuthorSite(1);
                    aData.setAuthorShowUrl(updateGetter.getAuthorShowUrl());
                    aData.setAuthorUrl(updateGetter.getAuthorUrl());
                    aData.setAuthorCategory("");
                    rData.setAuthorId(selectOrInsertAuthorTable(authorDAO, aData));
                    rData.setOtherDetail(updateGetter.getTypeId());
                    rData.setrCategory(updateGetter.getCategory());
                    rData.setrCoin(Long.parseLong(updateGetter.getCoins()));
                    rData.setrCollect(0);
                    rData.setrComment(updateGetter.getTotalComment());
                    rData.setrDanmu(Long.parseLong(updateGetter.getDanmu()));
                    rData.setrDuration(updateGetter.getDurationLong());
                    rData.setrPv(Long.parseLong(updateGetter.getClick()));
                    rData.setrShowUrl(updateGetter.getImgSrc());
                    rData.setrUrl("http://www.bilibili.com/video/av" + avId + "/");
                    rData.setrTags(updateGetter.getTags());
                    rData.setrGmtCreate(updateGetter.getResCreateDate());
                    rData.setrTitle(updateGetter.getTitle());
                    rData.setrId(avId);

                    resourceDAO.updateResource(rData);
                    long resourceId = rData.getrId();

                    for (BiliBiliCommentData cData : updateGetter.getViewDatas()) {
                        if (cData.getFloor() > oldComment) {
                            ViewData viewData = new ViewData();
                            viewData.setFloor(cData.getFloor());
                            viewData.setGmtViewTime(cData.getViewDate());
                            viewData.setReplyCount(cData.getReplyCount());
                            viewData.setrId(resourceId);
                            viewData.setViewContent(cData.getContent());

                            ViewerData viewerData = new ViewerData();
                            viewerData.setViewerLevel(cData.getLevel());
                            viewerData.setViewerName(cData.getViewerName());
                            if (cData.getSex().equals("男"))
                                viewerData.setViewerSex(0);
                            else if (cData.getSex().equals("女"))
                                viewerData.setViewerSex(1);
                            else
                                viewerData.setViewerSex(2);
                            viewerData.setViewerShowUrl(cData.getShowUrl());
                            viewerData.setViewerSite(1);
                            viewerData.setViewerUrl(cData.getViewerUrl());
                            viewerData.setViewerCategory("");

                            viewData.setViewerId(selectOrInsertViewerTable(viewerDAO, viewerData));

                            viewDAO.insertView(viewData);
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.error(logMsg.toString() + " error", ex);
                }
            } finally {
                if (logMsg.size() >= 2) {
                    LOGGER.info(logMsg.toString());
                }
                logMsg.clear();
            }
        }
    }

    public long selectOrInsertAuthorTable(AuthorDAO authorInDAO, AuthorData authorData)
                                                                                       throws Exception {
        Optional<Long> optAuthorId = authorCache.get(authorData.getAuthorName() + ":!:!:"
                                                     + authorData.getAuthorSite());
        if (optAuthorId.isPresent()) {
            return optAuthorId.get();
        }

        Long authorId = authorInDAO.select(authorData.getAuthorName(), authorData.getAuthorSite());
        if (authorId != null) {
            authorCache.put(authorData.getAuthorName() + ":!:!:" + authorData.getAuthorSite(),
                Optional.of(authorId));
            return authorId;
        } else {

            long aaid = authorInDAO.insert(authorData);

            authorCache.put(authorData.getAuthorName() + ":!:!:" + authorData.getAuthorSite(),
                Optional.of(aaid));

            return aaid;
        }
    }

    public long selectOrInsertViewerTable(ViewerDAO viewerInDAO, ViewerData viewerData)
                                                                                       throws Exception {
        Optional<Long> optViewerId = viewerCache.get(viewerData.getViewerName() + ":!:!:"
                                                     + viewerData.getViewerSite());
        if (optViewerId.isPresent()) {
            return optViewerId.get();
        }

        Long viewerId = viewerInDAO.select(viewerData.getViewerName(), viewerData.getViewerSite());
        if (viewerId != null) {
            viewerCache.put(viewerData.getViewerName() + ":!:!:" + viewerData.getViewerSite(),
                Optional.of(viewerId));
            return viewerId;
        } else {

            long vId = viewerInDAO.insert(viewerData);

            viewerCache.put(viewerData.getViewerName() + ":!:!:" + viewerData.getViewerSite(),
                Optional.of(vId));

            return vId;
        }
    }

}
