package xm.bibibiradio.mainsystem.score;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScoreAuthorImplTest {
    private static ScoreAuthorImpl scoreAuthorImpl;
    @Before
    public void setUp() throws Exception {
        if(scoreAuthorImpl == null){
            scoreAuthorImpl = new ScoreAuthorImpl();
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        scoreAuthorImpl.start("");
    }

}
