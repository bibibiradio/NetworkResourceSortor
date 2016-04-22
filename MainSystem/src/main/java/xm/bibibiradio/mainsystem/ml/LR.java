package xm.bibibiradio.mainsystem.ml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LR {
    private double stepLength;
    private int wLength;
    private List<Double> w;
    private List<Double> deltaW;
    private double finishCondition;
    private double judgeSum = 0;
    private long trainDataNum = 0;
    
    private Func func;
    
    public boolean oneTurnOver(){
        double oneSum = 0;
        for(int i=0 ; i<wLength ; i++){
            //w.set(i, w.get(i) - stepLength*deltaW.get(i));
            oneSum += Math.pow(deltaW.get(i), 2);
        }
        oneSum = Math.sqrt(oneSum);
        
        for(int i=0 ; i<wLength ; i++){
            deltaW.set(i, deltaW.get(i)/oneSum);
            w.set(i, w.get(i) - stepLength*deltaW.get(i));
        }
        
        double judgeDouble = Math.sqrt(judgeSum/trainDataNum);
        //double judgeDouble = judgeSum;
        System.out.println(w+" "+judgeDouble);
        if(finishCondition >= judgeDouble){
            judgeSum = 0;
            trainDataNum = 0;
            return true;
        }else{
            judgeSum = 0;
            trainDataNum = 0;
            return false;
        }
    }
    
    public void train(Map<Integer,Item> x,double y) throws Exception{
        /*
         * x(i+1)=1
         */
        if(x.size() < wLength + 1){
            x.put(wLength - 1, new Item(0,1.0));
        }
        
        /*
         * sum = Ewixi
         */
        double sum = 0,realSum = 0,deltaSum = 0;
        for(int j=0 ; j<wLength ; j++){
            Double xj = (Double) x.get(j).getContent();
            if(xj != null){
                sum += w.get(j)*xj;
            }
        }
        
        /*
         * realSum = h(Ewixi)
         * deltaSum = dh(Ewixi)/dEwixi
         */
        realSum = (Double)func.getResult(new Item(0,sum)).getContent();
        deltaSum = (Double)func.getDeltaResult(new Item(0,sum)).getContent();
        
        /*
         * deltaWi = d(1/2)(h(Ewixi)-y)^2/dwi = (h(Ewixi)-y)(dh(Ewixi-y)/dEwixi)xi
         * judgeSum = E((h(Ewixi)-y)^2)
         */
        for(int i=0 ; i<wLength ; i++){
            deltaW.set(i, deltaW.get(i) + (realSum-y)*deltaSum*(Double)(x.get(i).getContent()) );
            judgeSum += Math.pow(realSum-y, 2);
        }
        
        trainDataNum++;
    }
    
    public void init(double stepLength,int wLength,double finishCondition,Func func){
        this.stepLength = stepLength;
        this.wLength = wLength + 1;
        this.finishCondition = finishCondition;
        this.func = func;
        
        w = new ArrayList<Double>();
        deltaW = new ArrayList<Double>();
        for(int i=0 ; i<this.wLength ; i++){
           w.add((double)0);
           deltaW.add((double)0);
        }
    }
    
    public double forecast(Map<Integer,Item> x) throws Exception{
        Iterator<Entry<Integer, Item>> iter = x.entrySet().iterator();
        double sum = 0;
        while(iter.hasNext()){
            Entry<Integer, Item> entry = iter.next();
            sum += w.get(entry.getKey())*(Double)(entry.getValue().getContent());
        }
        sum += w.get(wLength - 1);
        return (Double)func.getResult(new Item(0,sum)).getContent();
    }

    public double getStepLength() {
        return stepLength;
    }

    public void setStepLength(double stepLength) {
        this.stepLength = stepLength;
    }

    public int getwLength() {
        return wLength;
    }

    public void setwLength(int wLength) {
        this.wLength = wLength;
    }

    public List<Double> getW() {
        return w;
    }

    public void setW(List<Double> w) {
        this.w = w;
    }

    public List<Double> getDeltaW() {
        return deltaW;
    }

    public void setDeltaW(List<Double> deltaW) {
        this.deltaW = deltaW;
    }

    public double getFinishCondition() {
        return finishCondition;
    }

    public void setFinishCondition(double finishCondition) {
        this.finishCondition = finishCondition;
    }

    public double getJudgeSum() {
        return judgeSum;
    }

    public void setJudgeSum(double judgeSum) {
        this.judgeSum = judgeSum;
    }

    public long getTrainDataNum() {
        return trainDataNum;
    }

    public void setTrainDataNum(long trainDataNum) {
        this.trainDataNum = trainDataNum;
    }

    public Func getFunc() {
        return func;
    }

    public void setFunc(Func func) {
        this.func = func;
    }
}
