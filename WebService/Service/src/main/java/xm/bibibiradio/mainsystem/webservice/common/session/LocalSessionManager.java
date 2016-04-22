package xm.bibibiradio.mainsystem.webservice.common.session;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.webservice.util.StaticUtils;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class LocalSessionManager implements SessionManager {
    private LoadingCache<String,Optional<Map<Object,Object>>> sessions;
    private int threadNum = 4;
    private long writeExpire = 1000*24*60*60;
    private long accessExpire = 1000*30*60;
    private Random random = new Random();
    private String checkKey = "123456";
    static final Logger logger = Logger.getLogger(LocalSessionManager.class);
    public LocalSessionManager(int threadNum,int writeExpire,int accessExpire,String checkKey){
        this.threadNum = threadNum;
        this.writeExpire = writeExpire;
        this.accessExpire = accessExpire;
        this.checkKey = checkKey;
        sessions = CacheBuilder.newBuilder()
                .concurrencyLevel(threadNum)
                .expireAfterWrite(writeExpire, TimeUnit.MILLISECONDS)
                .expireAfterAccess(accessExpire, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String,Optional<Map<Object,Object>>>(){

                    @Override
                    public Optional<Map<Object,Object>> load(String key) throws Exception {
                        // TODO Auto-generated method stub
                        Optional<Map<Object,Object>> option = Optional.absent();
                        return option;
                    }
                    
                });
    }
    
    public LocalSessionManager(){
        sessions = CacheBuilder.newBuilder()
                .concurrencyLevel(threadNum)
                .expireAfterWrite(writeExpire, TimeUnit.MILLISECONDS)
                .expireAfterAccess(accessExpire, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String,Optional<Map<Object,Object>>>(){

                    @Override
                    public Optional<Map<Object,Object>> load(String key) throws Exception {
                        // TODO Auto-generated method stub
                        Optional<Map<Object,Object>> option = Optional.absent();
                        return option;
                    }
                    
                });
    }
    @Override
    public Map<Object, Object> getSession(String key) {
        // TODO Auto-generated method stub
        try {
            if(!checkCode(key)){
                return null;
            }
            
            Optional<Map<Object,Object>> opt = sessions.get(key);
            if(!opt.isPresent()){
                return null;
            }else{
                return opt.get();
            }
        } catch (ExecutionException ex) {
            // TODO Auto-generated catch block
            logger.error("error message",ex);
            return null;
        }
    }

    @Override
    public void setSession(String key, Map<Object, Object> value) {
        // TODO Auto-generated method stub
        if(checkCode(key)){
            if(value == null){
                return;
            }
            sessions.put(key, Optional.of(value));
        }
    }
    
    @Override
    public void removeSession(String key) {
        // TODO Auto-generated method stub
        sessions.invalidate(key);
    }

    @Override
    public String genSessionKey() {
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
            logger.error("error message",ex);
            return null;
        }
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
            logger.error("error message",ex);
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
}
