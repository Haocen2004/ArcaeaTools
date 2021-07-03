package xyz.hellocraft.arctools.utils.data;

import android.util.Log;

public class SongData {

    private String sid;
    private String name;
    private int rating_prs;
    private int rating_pst;
    private int rating_ftr;
    private int rating_byd;

    public SongData(){}

    @Override
    public String toString() {
        return "SongData{" +
                "sid='" + sid + '\'' +
                ", name='" + name + '\'' +
                ", rating_prs=" + rating_prs +
                ", rating_pst=" + rating_pst +
                ", rating_ftr=" + rating_ftr +
                ", rating_byd=" + rating_byd +
                '}';
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating_prs(int rating_prs) {
        this.rating_prs = rating_prs;
    }

    public void setRating_pst(int rating_pst) {
        this.rating_pst = rating_pst;
    }

    public void setRating_ftr(int rating_ftr) {
        this.rating_ftr = rating_ftr;
    }

    public void setRating_byd(int rating_byd) {
        this.rating_byd = rating_byd;
    }

    public String getSid() {
        return sid;
    }

    public String getName() {
        return name;
    }

    public int getRating(int difficulty) {
        switch (difficulty) {
            case 0:
                return rating_pst;
            case 1:
                return rating_prs;
            case 2:
                return rating_ftr;
            case 3:
                return rating_byd;
        }
        return -1;
    }

    public int getRating_prs() {
        return rating_prs;
    }

    public int getRating_pst() {
        return rating_pst;
    }

    public int getRating_ftr() {
        return rating_ftr;
    }

    public int getRating_byd() {
        return rating_byd;
    }
}
