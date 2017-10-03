package com.ntga.card;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by lixiaodong on 2017/10/1.
 */
@Entity
public class BdInfo {
    @Id
    long id;
    private String xm;
    private String sfzh;
    private String bdsj;
    private String bdjg;
    private int bdfs;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getBdsj() {
        return bdsj;
    }

    public void setBdsj(String bdsj) {
        this.bdsj = bdsj;
    }

    public String getBdjg() {
        return bdjg;
    }

    public void setBdjg(String bdjg) {
        this.bdjg = bdjg;
    }

    public int getBdfs() {
        return bdfs;
    }

    public void setBdfs(int bdfs) {
        this.bdfs = bdfs;
    }
}
