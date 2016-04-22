package xm.bibibiradio.mainsystem.ml;

public class Item {
    private int type;
    private Object content;
    public Item(int type,Object content){
        this.type = type;
        this.content = content;
    }
    
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Object getContent() {
        return content;
    }
    public void setContent(Object content) {
        this.content = content;
    }
    
    
}
