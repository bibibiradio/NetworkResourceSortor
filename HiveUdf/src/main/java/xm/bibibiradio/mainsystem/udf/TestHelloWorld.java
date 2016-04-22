package xm.bibibiradio.mainsystem.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

public class TestHelloWorld extends UDF {
    public String evaluate(String name){
        return "Hello World";
    }
}
