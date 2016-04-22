package xm.bibibiradio.mainsystem.ml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BqNode {
    private int nodeId;
    private List<Double> w;
    private List<Double> deltaW;
    private Func func;
    private int wLength;
    private double stepLength;
    private int type;
    private double output;
    
    private List<BqNode> prevNodes;
    private List<BqNode> nextNodes;
    private BqNode nextNode;
    
    public void init(int type,int wLength,BqNode nextNode,List<BqNode> prevNodes,List<BqNode> nextNodes,double stepLength,Func func){
        this.type = type;
        this.nextNode = nextNode;
        this.prevNodes = prevNodes;
        this.nextNodes = nextNodes;
        this.stepLength = stepLength;
        this.func = func;
        
        if(type == 0){
            this.wLength = wLength + 1;
        }else{
            this.wLength = prevNodes.size() + 1;
        }
        
        w = new ArrayList<Double>();
        deltaW = new ArrayList<Double>();
        for(int i=0;i<this.wLength;i++){
            w.add(0.0);
            deltaW.add(0.0);
        }
        
        output = 0;
    }
    
    public void train(Map<Integer,Item> x) throws Exception{
        if(type != 0){
            x = new HashMap<Integer,Item>();
            
            int i = 0;
            for(BqNode node : prevNodes){
                x.put(i, new Item(0,node.getOutput()));
                i++;
            }
        }
        
        /*
         * x(i+1)=1
         */
        if(x.size() < wLength + 1){
            x.put(wLength - 1, new Item(0,1.0));
        }
        
        /*
         * sum = Ewixi
         */
        double sum = 0;
        for(int j=0 ; j<wLength ; j++){
            Double xj = (Double) x.get(j).getContent();
            if(xj != null){
                sum += w.get(j)*xj;
            }
        }
        
        /*
         * realSum = h(Ewixi)
         */
        output = (Double)func.getResult(new Item(0,sum)).getContent();
        
        if(nextNode != null){
            nextNode.train(x);
        }
        
    }
    
    public void bq(double delta){
        
    }
    
    public int getNodeId() {
        return nodeId;
    }
    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
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
    public Func getFunc() {
        return func;
    }
    public void setFunc(Func func) {
        this.func = func;
    }
    public int getwLength() {
        return wLength;
    }
    public void setwLength(int wLength) {
        this.wLength = wLength;
    }
    public double getStepLength() {
        return stepLength;
    }
    public void setStepLength(double stepLength) {
        this.stepLength = stepLength;
    }
    public BqNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(BqNode nextNode) {
        this.nextNode = nextNode;
    }

    public List<BqNode> getPrevNodes() {
        return prevNodes;
    }
    public void setPrevNodes(List<BqNode> prevNodes) {
        this.prevNodes = prevNodes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public List<BqNode> getNextNodes() {
        return nextNodes;
    }

    public void setNextNodes(List<BqNode> nextNodes) {
        this.nextNodes = nextNodes;
    }
    
    
}
