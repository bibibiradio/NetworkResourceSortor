package xm.bibibiradio.mainsystem.ml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LRTest {
    static private LR     lr;
    static private Random rand;

    private class OneLineFunc implements Func {

        @Override
        public Item getResult(Item item) throws Exception {
            // TODO Auto-generated method stub
            return item;
        }

        @Override
        public Item getDeltaResult(Item item) throws Exception {
            // TODO Auto-generated method stub
            return new Item(0, 1.0);
        }

    }

    private class LogisticFunc implements Func {

        @Override
        public Item getResult(Item item) throws Exception {
            // TODO Auto-generated method stub
            return new Item(0, 1.0 / (1.0 + Math.exp(-(Double) item.getContent())));
        }

        @Override
        public Item getDeltaResult(Item item) throws Exception {
            // TODO Auto-generated method stub
            double x = (Double)item.getContent();
            double rt = 1.0/(Math.exp(x)+Math.exp(-x)+2);
            return new Item(0, rt);
        }

    }

    @Before
    public void setUp() throws Exception {

        rand = new Random();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test1() {
        try {
            //LR
            List<Map<Integer, Item>> trainData;
            trainData = new ArrayList<Map<Integer, Item>>();
            boolean isTrainOk = false;
            int trainLoop = 0;
            HashMap<Integer, Item> test;
            
            lr = new LR();
            lr.init(0.1, 2, 5, new OneLineFunc());

            for (int i = 0; i < 10000; i++) {
                double x1, x2, y;
                x1 = rand.nextInt() % 100;
                x2 = rand.nextInt() % 100;
                y = x1 + 2 * x2 + 3;

                HashMap<Integer, Item> m1 = new HashMap<Integer, Item>();
                m1.put(0, new Item(0, x1));
                m1.put(1, new Item(0, x2));
                m1.put(100, new Item(0, y));

                trainData.add(m1);
            }

            while (!(isTrainOk || trainLoop > 500000)) {
                //System.out.println(lr.getW());
                for (Map<Integer, Item> x : trainData) {
                    lr.train(x, (Double) x.get(100).getContent());
                    //isTrainOk = lr.oneTurnOver();
                }
                //Thread.sleep(1000);
                isTrainOk = lr.oneTurnOver();
                trainLoop++;
            }

            System.out.println(lr.getW());
            test = new HashMap<Integer, Item>();
            test.put(0, new Item(0, 8.0));
            test.put(1, new Item(0, 7.0));
            System.out.println(lr.forecast(test));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Test
    public void test2(){
        try{
            //LOGISTIC
            List<Map<Integer, Item>> trainData;
            trainData = new ArrayList<Map<Integer, Item>>();
            boolean isTrainOk = false;
            int trainLoop = 0;
            HashMap<Integer, Item> test;
            
            trainData = new ArrayList<Map<Integer, Item>>();
            lr = new LR();
            Func func = new LogisticFunc();
            lr.init(0.1, 2, 0.01, func);
            
            //System.out.println(func.getResult(new Item(0,100000.0)).getContent());
            
            for (int i = 0; i < 10000; i++) {
                double x1, x2, y;
                x1 = rand.nextInt() % 100;
                x2 = rand.nextInt() % 100;
                y = x1 + 2 * x2 + 3;
                double z /*= 1.0 / (1.0 + Math.exp(-y))*/;
                
                if(y >= 0){
                    z = 1;
                }else{
                    z = 0;
                }
                
                HashMap<Integer, Item> m1 = new HashMap<Integer, Item>();
                m1.put(0, new Item(0, x1));
                m1.put(1, new Item(0, x2));
                m1.put(100, new Item(0, z));

                trainData.add(m1);
            }

            while (!(isTrainOk || trainLoop > 500000)) {
                //System.out.println(lr.getW());
                for (Map<Integer, Item> x : trainData) {
                    lr.train(x, (Double) x.get(100).getContent());
                    //isTrainOk = lr.oneTurnOver();
                }
                isTrainOk = lr.oneTurnOver();
                trainLoop++;
            }

            System.out.println(lr.getW());
            int rt;
            test = new HashMap<Integer, Item>();
            test.put(0, new Item(0, 8.0));
            test.put(1, new Item(0, 7.0));
            if(lr.forecast(test) > 0.5){
                rt = 1;
            }else{
                rt = -1;
            }
            System.out.println(rt);
            
            test = new HashMap<Integer, Item>();
            test.put(0, new Item(0, 8.0));
            test.put(1, new Item(0, -7.0));
            if(lr.forecast(test) > 0.5){
                rt = 1;
            }else{
                rt = -1;
            }
            System.out.println(rt);
            
            test = new HashMap<Integer, Item>();
            test.put(0, new Item(0, -12.0));
            test.put(1, new Item(0, 2.0));
            if(lr.forecast(test) > 0.5){
                rt = 1;
            }else{
                rt = -1;
            }
            System.out.println(rt);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
