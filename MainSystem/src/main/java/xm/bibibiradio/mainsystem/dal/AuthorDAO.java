package xm.bibibiradio.mainsystem.dal;

public interface AuthorDAO {
    public AuthorData select(String authorName,String authorType);
    public long insert(AuthorData authorData);
}
