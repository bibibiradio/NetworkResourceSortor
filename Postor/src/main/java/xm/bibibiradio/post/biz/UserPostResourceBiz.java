package xm.bibibiradio.post.biz;

import java.util.Date;
import java.util.List;

import xm.bibibiradio.post.dal.PostConfigDAO;

public class UserPostResourceBiz {
    private PostConfigDAO postConfigDAO;

    public List<PostConfigData> getAllPostTasks() throws Exception {
        return postConfigDAO.selectAllPostConfigData();
    }

    public List<PostResourceData> getNeedPostResource(PostConfigData postConfigData)
                                                                                    throws Exception {
        List<PostResourceData> result = null;

        if (postConfigData.getLastPost().getTime() + postConfigData.getPostFreq() > System
            .currentTimeMillis())
            return null;

        result = postConfigDAO.selectNeedPostResourceData(postConfigData);

        for (PostResourceData data : result) {
            if(data.getSite() == 1)
                data.setResourceShowUrl(data.getResourceShowUrl() + "_160x100.jpg");
        }

        return result;
    }
    
    public void updatePostTime(long postConfigId){
        postConfigDAO.updatePostConfigLastPost(new Date(), postConfigId);
    }

    public PostConfigDAO getPostConfigDAO() {
        return postConfigDAO;
    }

    public void setPostConfigDAO(PostConfigDAO postConfigDAO) {
        this.postConfigDAO = postConfigDAO;
    }

}
