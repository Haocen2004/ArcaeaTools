package xyz.hellocraft.arctools.utils.data;

public class ScoreData {
    private String sid;
    private int score;
    private int shinyPerfectCount;
    private int perfectCount;
    private int farCount;
    private int lostCount;
    private int difficulty;
    private double ptt;
    private String diffType;
    private SongData songData;

    public void setSongData(SongData songData) {
        this.songData = songData;
    }

    public SongData getSongData() {
        return songData;
    }

    public void setDiffType(String diffType) {
        this.diffType = diffType;
    }

    public double getPtt() {
        return ptt;
    }

    public void setPtt(double ptt) {
        this.ptt = ptt;
    }

    public ScoreData() {}

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getShinyPerfectCount() {
        return shinyPerfectCount;
    }

    public void setShinyPerfectCount(int shinyPerfectCount) {
        this.shinyPerfectCount = shinyPerfectCount;
    }

    public int getPerfectCount() {
        return perfectCount;
    }

    public void setPerfectCount(int perfectCount) {
        this.perfectCount = perfectCount;
    }

    public int getFarCount() {
        return farCount;
    }

    public void setFarCount(int farCount) {
        this.farCount = farCount;
    }

    public int getLostCount() {
        return lostCount;
    }

    public void setLostCount(int lostCount) {
        this.lostCount = lostCount;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        switch (difficulty){
            case 0:
                diffType="pst";
                break;
            case 1:
                diffType="prs";
                break;
            case 2:
                diffType="ftr";
                break;
            case 3:
                diffType="byd";
                break;
        }
    }

    public String getDiffType() {
        return diffType;
    }

    @Override
    public String toString() {
        return "ScoreData{" +
                "sid='" + sid + '\'' +
                ", score=" + score +
                ", shinyPerfectCount=" + shinyPerfectCount +
                ", perfectCount=" + perfectCount +
                ", farCount=" + farCount +
                ", lostCount=" + lostCount +
                ", difficulty=" + difficulty +
                ", ptt=" + ptt +
                '}';
    }
}
