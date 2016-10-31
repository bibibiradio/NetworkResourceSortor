package xm.bibibiradio.post.dal;

import java.util.Date;
import java.util.List;

import xm.bibibiradio.post.biz.PostConfigData;
import xm.bibibiradio.post.biz.PostResourceData;

public interface PostConfigDAO {
    public List<PostConfigData> selectAllPostConfigData();
    public List<PostResourceData> selectNeedPostResourceData(PostConfigData postConfig);
    public void updatePostConfigLastPost(Date lastPost,long postConfigId);
}
