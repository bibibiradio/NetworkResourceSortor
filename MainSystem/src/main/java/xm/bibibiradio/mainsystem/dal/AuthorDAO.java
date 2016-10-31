package xm.bibibiradio.mainsystem.dal;

public interface AuthorDAO {
    public Long select(String authorName,int authorSite);
    public long insert(AuthorData authorData);
}
