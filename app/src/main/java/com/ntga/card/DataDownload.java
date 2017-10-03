package com.ntga.card;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by lixiaodong on 2017/10/1.
 */
@Entity
public class DataDownload {
    //下载数据存档
    @Id
    long id;
    private int dataCatalog;
    private String xzsj;
    private long count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDataCatalog() {
        return dataCatalog;
    }

    public void setDataCatalog(int dataCatalog) {
        this.dataCatalog = dataCatalog;
    }

    public String getXzsj() {
        return xzsj;
    }

    public void setXzsj(String xzsj) {
        this.xzsj = xzsj;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
