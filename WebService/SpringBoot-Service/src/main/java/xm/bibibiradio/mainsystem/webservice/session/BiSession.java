package xm.bibibiradio.mainsystem.webservice.session;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class BiSession implements HttpSession {
    private HashMap<String,Object> attrs = new HashMap<String,Object>();
    private String id;
    private Date lastAccessTime;
    private Date createTime;
    private int invalidTime;
    private boolean isNew;
    
    public BiSession(){
        lastAccessTime = new Date();
        createTime = new Date();
        //invalidTime = SessionManager.SESSION_INVALID_TIME;
    }
    
    public HashMap<String, Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(HashMap<String, Object> attrs) {
        this.attrs = attrs;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public int getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(int invalidTime) {
        this.invalidTime = invalidTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Override
    public Object getAttribute(String arg0) {
        // TODO Auto-generated method stub
        return attrs.get(arg0);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getCreationTime() {
        // TODO Auto-generated method stub
        return createTime.getTime();
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        // TODO Auto-generated method stub
        return lastAccessTime.getTime();
    }

    @Override
    public int getMaxInactiveInterval() {
        // TODO Auto-generated method stub
        return invalidTime;
    }

    @Override
    public ServletContext getServletContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getValue(String arg0) {
        // TODO Auto-generated method stub
        return getAttribute(arg0);
    }

    @Override
    public String[] getValueNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void invalidate() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isNew() {
        // TODO Auto-generated method stub
        return isNew;
    }

    @Override
    public void putValue(String arg0, Object arg1) {
        // TODO Auto-generated method stub
        setAttribute(arg0,arg1);
    }

    @Override
    public void removeAttribute(String arg0) {
        // TODO Auto-generated method stub
        attrs.remove(arg0);
    }

    @Override
    public void removeValue(String arg0) {
        // TODO Auto-generated method stub
        removeAttribute(arg0);
    }

    @Override
    public void setAttribute(String arg0, Object arg1) {
        // TODO Auto-generated method stub
        attrs.put(arg0, arg1);
    }

    @Override
    public void setMaxInactiveInterval(int arg0) {
        // TODO Auto-generated method stub
        invalidTime = arg0;
    }

}
