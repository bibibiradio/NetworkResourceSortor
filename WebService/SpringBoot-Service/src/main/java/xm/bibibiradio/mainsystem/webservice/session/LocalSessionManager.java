package xm.bibibiradio.mainsystem.webservice.session;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.webservice.util.StaticUtils;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class LocalSessionManager implements SessionManager {
    private LoadingCache<String,Optional<HttpSession>> sessions;
    private int threadNum = 4;
    private long writeExpire = 1000*24*60*60;
    private long accessExpire = 1000*30*60;
    private Random random = new Random();
    private String checkKey = "123456";
    static final Logger LOGGER = Logger.getLogger(LocalSessionManager.class);
    
    public LocalSessionManager(int threadNum,int writeExpire,int accessExpire,String checkKey){
        this.threadNum = threadNum;
        this.writeExpire = writeExpire;
        this.accessExpire = accessExpire;
        this.checkKey = checkKey;
        sessions = CacheBuilder.newBuilder()
                .concurrencyLevel(threadNum)
                .expireAfterWrite(writeExpire, TimeUnit.MILLISECONDS)
                .expireAfterAccess(accessExpire, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String,Optional<HttpSession>>(){

                    @Override
                    public Optional<HttpSession> load(String key) throws Exception {
                        // TODO Auto-generated method stub
                        Optional<HttpSession> option = Optional.absent();
                        return option;
                    }
                    
                });
    }
    
    public LocalSessionManager(){
        sessions = CacheBuilder.newBuilder()
                .concurrencyLevel(threadNum)
                .expireAfterWrite(writeExpire, TimeUnit.MILLISECONDS)
                .expireAfterAccess(accessExpire, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String,Optional<HttpSession>>(){

                    @Override
                    public Optional<HttpSession> load(String key) throws Exception {
                        // TODO Auto-generated method stub
                        Optional<HttpSession> option = Optional.absent();
                        return option;
                    }
                    
                });
    }
    
    @Override
    public HttpSession getSession(String id) {
        // TODO Auto-generated method stub
        try {
            if(!checkCode(id)){
                return null;
            }
            
            Optional<HttpSession> opt = sessions.get(id);
            if(!opt.isPresent()){
                return null;
            }else{
                return opt.get();
            }
        } catch (ExecutionException ex) {
            // TODO Auto-generated catch block
            LOGGER.error("error message",ex);
            return null;
        }
    }

    @Override
    public void setSession(String id, HttpSession sess) {
        // TODO Auto-generated method stub
        if(checkCode(id)){
            if(sess == null){
                return;
            }
            sessions.put(id, Optional.of(sess));
        }
    }

    @Override
    public void removeSession(String id) {
        // TODO Auto-generated method stub
        sessions.invalidate(id);
    }

    @Override
    public String genSessionId() {
        // TODO Auto-generated method stub
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] needBytes = new byte[16];
            random.nextBytes(needBytes);
            md.update(needBytes);
            byte[] hash = md.digest();
            String rawKey = StaticUtils.bytesToHexString(hash);
            return addCheckCode(rawKey);
        }catch(Exception ex){
            LOGGER.error("error message",ex);
            return null;
        }
    }

    @Override
    public String reGenSessionId(HttpSession sess) {
        // TODO Auto-generated method stub
        String id = sess.getId();
        removeSession(id);
        String newId = genSessionId();
        setSession(newId,sess);
        return newId;
    }
    
    private String addCheckCode(String rawKey) throws Exception{
        return rawKey+":"+genCheckCode(rawKey);
    }
    
    private boolean checkCode(String key){
        try{
            String[] parts = key.split(":");
            if(parts.length != 2){
                return false;
            }
            
            if(parts[1].equals(genCheckCode(parts[0]))){
                return true;
            }else{
                return false;
            }
        }catch(Exception ex){
            LOGGER.error("error message",ex);
            return false;
        }
    }
    
    private String genCheckCode(String key) throws Exception{
        String allCheckCode = genHmac(key);
        return allCheckCode.substring(8, 16);
    }
    
    private String genHmac(String key) throws Exception{
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");  
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] hmac = mac.doFinal();
        return StaticUtils.bytesToHexString(hmac);
    }

    @Override
    public long getAllSessionNum() {
        // TODO Auto-generated method stub
        return sessions.size();
    }

}
