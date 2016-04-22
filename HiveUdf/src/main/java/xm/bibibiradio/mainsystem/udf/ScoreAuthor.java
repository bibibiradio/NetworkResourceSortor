package xm.bibibiradio.mainsystem.udf;

import java.util.ArrayList;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.io.LongWritable;

public class ScoreAuthor extends GenericUDAFEvaluator {
    private PrimitiveObjectInspector inputOI;
    private LongWritable result;
    private int cutLimit = 15;
    private int cutRange = 3;
    private int lastOut = 3;
    
    @Override
    public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
      assert (parameters.length == 1);
      super.init(m, parameters);
      result = new LongWritable(0);
      inputOI = (PrimitiveObjectInspector) parameters[0];
      return PrimitiveObjectInspectorFactory.writableLongObjectInspector;
    }
    
    /** 存储sum的值的类 */
    static class Score implements AggregationBuffer {
      boolean isEmpty;
      ArrayList<Long> pvs;
      long sum;
      int cnt;
    }
    
    @Override
    public AggregationBuffer getNewAggregationBuffer() throws HiveException {
        // TODO Auto-generated method stub
        Score score = new Score();
        reset(score);
        return score;
    }
    

    @Override
    public void iterate(AggregationBuffer arg, Object[] params) throws HiveException {
        // TODO Auto-generated method stub
        Long pv = PrimitiveObjectInspectorUtils.getLong(params[0], inputOI);
        Score score = (Score) arg;
        score.cnt++;
        score.isEmpty = false;
        score.pvs.add(pv);
    }

    @Override
    public void merge(AggregationBuffer arg0, Object arg1) throws HiveException {
        // TODO Auto-generated method stub
        if(arg1 != null){
            Long pv = PrimitiveObjectInspectorUtils.getLong(arg1, inputOI);
            Score score = (Score) arg0;
            score.cnt++;
            score.isEmpty = false;
            score.pvs.add(pv);
        }
        
    }

    @Override
    public void reset(AggregationBuffer arg0) throws HiveException {
        // TODO Auto-generated method stub
        Score score = (Score) arg0;
        score.isEmpty = true;
        score.pvs = new ArrayList<Long>();
        score.cnt = 1;
        score.sum = 0;
    }

    @Override
    public Object terminate(AggregationBuffer arg0) throws HiveException {
        // TODO Auto-generated method stub
        Score score = (Score) arg0;
        if(score.isEmpty){
            //result.set(score.sum);
            return null;
        }
        int cnt = score.cnt;
        if(cnt < cutLimit){
            for(long pv:score.pvs){
                score.sum += pv;
            }
            
        }else{
            int start = (cutRange - 1)*cnt/(cutRange);
            int end = cnt - lastOut;
            ArrayList<Long> pvs = score.pvs;
            for(int i = start - 1;i < end;i++){
                score.sum += pvs.get(i);
            }
        }
        result.set(score.sum);
        return result;
    }

    @Override
    public Object terminatePartial(AggregationBuffer arg0) throws HiveException {
        // TODO Auto-generated method stub
        return terminate(arg0);
        
    }

}
