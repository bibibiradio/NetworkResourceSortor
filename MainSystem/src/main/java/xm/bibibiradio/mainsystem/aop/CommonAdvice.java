package xm.bibibiradio.mainsystem.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.bibibiradio.executetesttime.ExecuteTimeTimerThreadImpl;

@Aspect
public class CommonAdvice {
	private static Logger logger = Logger.getLogger(CommonAdvice.class);
	
	@Pointcut("execution(* *.testAop(*))")
	public void pointCutTest(){
		
	}
	
	@Pointcut("execution(public void xm.bibibiradio.mainsystem..start(*))")
	public void pointCutStartMethod(){
		
	}
	
	@Around("pointCutStartMethod()")
	public Object startTraceExecuteTime(ProceedingJoinPoint joinPoint) throws Throwable{
		try{
			ExecuteTimeTimerThreadImpl.GetExecuteTimeTimerMng().start(joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
			return joinPoint.proceed();
		}finally{
			ExecuteTimeTimerThreadImpl.GetExecuteTimeTimerMng().end(joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
		}
	}
	
	@Around("pointCutTest()")
	public Object trace1(ProceedingJoinPoint joinPoint) throws Throwable{
		try{
			logger.info("sign:"+joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
			logger.info("arg:"+(String)joinPoint.getArgs()[0]);
			return joinPoint.proceed();
		}finally{
			logger.info("end");
		}
	}
	
	@Around("pointCutTest()")
	public Object trace2(ProceedingJoinPoint joinPoint) throws Throwable{
		try{
			logger.info("sign:"+joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
			logger.info("arg:"+(String)joinPoint.getArgs()[0]);
			return joinPoint.proceed();
		}finally{
			logger.info("end");
		}
	}
}
