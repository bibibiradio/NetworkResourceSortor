package xm.bibibiradio.mainsystem.udf;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator.AggregationBuffer;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator.Mode;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.io.LongWritable;

public class ScoreDeepResource extends GenericUDAFEvaluator {
    static final Log log = LogFactory.getLog(ScoreDeepResource.class.getName());
    private PrimitiveObjectInspector inputOI1;
    private PrimitiveObjectInspector inputOI2;
    private PrimitiveObjectInspector inputOI3;
    private LongWritable result;
    
    static class Score implements AggregationBuffer {
        boolean isEmpty;
        long authorScore;
        long midViewerScore;
        ArrayList<Long> viewerScore;
        int cnt;
    }
    
    @Override
    public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
//      assert (parameters.length == 3);
      super.init(m, parameters);
      result = new LongWritable(0);
      if(parameters.length == 1){
          inputOI1 = (PrimitiveObjectInspector) parameters[0];
      }else{
          inputOI1 = (PrimitiveObjectInspector) parameters[0];
          inputOI2 = (PrimitiveObjectInspector) parameters[1];
          inputOI3 = (PrimitiveObjectInspector) parameters[2];
      }
      return PrimitiveObjectInspectorFactory.writableLongObjectInspector;
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
        Long authorScore = PrimitiveObjectInspectorUtils.getLong(params[0], inputOI1);
        Long viewerScore = PrimitiveObjectInspectorUtils.getLong(params[1], inputOI2);
        int midViewerScore = PrimitiveObjectInspectorUtils.getInt(params[2], inputOI3);
        Score score = (Score) arg;
        if(score.cnt > 10){
            return;
        }
        score.midViewerScore = midViewerScore;
        score.cnt++;
        score.isEmpty = false;
        score.viewerScore.add(viewerScore);
        score.authorScore = authorScore;
    }

    @Override
    public void merge(AggregationBuffer arg0, Object arg1) throws HiveException {
        // TODO Auto-generated method stub

    }

    @Override
    public void reset(AggregationBuffer arg0) throws HiveException {
        // TODO Auto-generated method stub
        Score score = (Score) arg0;
        score.isEmpty = true;
        score.viewerScore = new ArrayList<Long>();
        score.authorScore = 0;
        score.midViewerScore = 0;
        score.cnt = 0;
    }

    @Override
    public Object terminate(AggregationBuffer arg0) throws HiveException {
        // TODO Auto-generated method stub
        Score score = (Score) arg0;
        if(score.isEmpty){
            return null;
        }
        long rScore = 0;
        rScore += score.authorScore;
        for(Long vScore : score.viewerScore){
            rScore += vScore;
        }
        int padNum = 10 - score.viewerScore.size();
        rScore += padNum * score.midViewerScore;
        result.set(rScore);
        return result;
    }

    @Override
    public Object terminatePartial(AggregationBuffer arg0) throws HiveException {
        // TODO Auto-generated method stub
        return terminate(arg0);
    }

}
