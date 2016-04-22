package xm.bibibiradio.mainsystem.webservice.common.util;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public abstract class CachedAbstract {
    private final static Logger LOGGER = Logger.getLogger(CachedAbstract.class);
    protected LoadingCache<Object,Optional<Object>> cache;
    private long accessExpire = 1000*24*60*60;
    private long maxNum = 100000;
    private int threadNum = 4;
    
    public CachedAbstract(String accessExpire,String maxNum,String threadNum){
        this.accessExpire =Long.parseLong(accessExpire);
        this.maxNum = Long.parseLong(maxNum);
        this.threadNum = Integer.parseInt(threadNum);
        
        cache = CacheBuilder.newBuilder()
        .expireAfterAccess(this.accessExpire, TimeUnit.MILLISECONDS)
        .maximumSize(this.maxNum)
        .concurrencyLevel(this.threadNum)
        .build(new CacheLoader<Object,Optional<Object>>(){

            @Override
            public Optional<Object> load(Object key) throws Exception {
                // TODO Auto-generated method stub
                return Optional.absent();
            }
            
        });
    }
    
    protected Object get(Object key){
        try{
            Optional<Object> optObj = cache.get(key);
            if(optObj.isPresent()){
                return optObj.get();
            }else{
                return null;
            }
        }catch(Exception ex){
            LOGGER.error("error message",ex);
            return null;
        }
    }
    
    protected void put(Object key,Object value){
        cache.put(key, Optional.of(value));
    }
}
