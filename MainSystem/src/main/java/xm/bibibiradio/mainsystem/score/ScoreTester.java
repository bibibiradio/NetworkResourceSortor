package xm.bibibiradio.mainsystem.score;

public class ScoreTester {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ScoreResoucesImportImpl importR = new ScoreResoucesImportImpl();
        ScoreViewerHiveShellImpl viewerScore = new ScoreViewerHiveShellImpl();
        ScoreAuthorHiveShellImpl authorScore = new ScoreAuthorHiveShellImpl();
        
        importR.start("mainSystemConf.properties");
        viewerScore.start("mainSystemConf.properties");
        authorScore.start("mainSystemConf.properties");
    }

}
