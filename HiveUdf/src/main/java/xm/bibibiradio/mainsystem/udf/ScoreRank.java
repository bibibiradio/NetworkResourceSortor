package xm.bibibiradio.mainsystem.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class ScoreRank extends UDF {
    private long rowRank;
    private String storedName;
    public long evaluate(String name){
        if(rowRank == 0 || storedName == null){
            rowRank = 1;
            storedName = name;
            return rowRank;
        }
        
        if(storedName.equals(name)){
            rowRank++;
            return rowRank;
        }else{
            rowRank = 1;
            storedName = name;
            return rowRank;
        }
    }
    
    public long evaluate(){
        rowRank++;
        return rowRank;
    }
}
