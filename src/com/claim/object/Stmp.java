package com.claim.object;

import com.claim.support.StringOpUtil;

public class Stmp {

    private String stmp;
    private String stmpY;
    private String stmpM;
    private String stmpN;
    private String stmpYM;

    public String getStmpYM() {
        return StringOpUtil.removeNull(stmpYM);
    }

    public void setStmpYM(String stmpYM) {
        this.stmpYM = stmpYM;
    }

    public String getStmp() {
        return stmp;
    }

    public void setStmp(String stmp) {
        this.stmp = stmp;
    }

    public String getStmpY() {
        return stmpY;
    }

    public void setStmpY(String stmpY) {
        this.stmpY = stmpY;
    }

    public String getStmpM() {
        return stmpM;
    }

    public void setStmpM(String stmpM) {
        this.stmpM = stmpM;
    }

    public String getStmpN() {
        return stmpN;
    }

    public void setStmpN(String stmpN) {
        this.stmpN = stmpN;
    }
}
