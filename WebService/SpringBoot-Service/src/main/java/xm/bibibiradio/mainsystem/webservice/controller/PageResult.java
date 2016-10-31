package xm.bibibiradio.mainsystem.webservice.controller;

public class PageResult<E> {
    private long allNum;
    private long nowNum;
    private long currentPage;
    private E ele;
    
    public PageResult(){
        
    }
    
    public PageResult(long allNum,long nowNum,long currentPage,E ele){
        this.allNum = allNum;
        this.currentPage = currentPage;
        this.ele = ele;
        this.nowNum = nowNum;
    }
    
    public long getAllNum() {
        return allNum;
    }
    public void setAllNum(long allNum) {
        this.allNum = allNum;
    }
    public long getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }
    public E getEle() {
        return ele;
    }
    public void setEle(E ele) {
        this.ele = ele;
    }

    public long getNowNum() {
        return nowNum;
    }

    public void setNowNum(long nowNum) {
        this.nowNum = nowNum;
    }
    
    
}
