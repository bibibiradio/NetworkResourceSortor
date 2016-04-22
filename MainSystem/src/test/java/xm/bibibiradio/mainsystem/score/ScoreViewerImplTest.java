package xm.bibibiradio.mainsystem.score;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScoreViewerImplTest {
    private static ScoreViewerImpl score;
    @Before
    public void setUp() throws Exception {
        if(score == null){
            score = new ScoreViewerImpl();
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        score.start("");
    }

}
