package xm.bibibiradio.mainsystem.udf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;

public class ScoreDeepResourceResolver extends AbstractGenericUDAFResolver {
    static final Log log = LogFactory.getLog(ScoreAuthorResolver.class.getName());
    @Override
    public ScoreDeepResource getEvaluator(TypeInfo[] parameters)
      throws SemanticException {
      // Type-checking goes here!
      return new ScoreDeepResource(); 
    }
}
