package com.ntga.web;

/**
 * Created by lixiaodong on 2017/10/1.
 */

public class NetwrokEvent {

    public NetwrokEvent(boolean isConn, int from) {
        this.isConn = isConn;
        this.from = from;
    }

    public boolean isConn;
    public int from;
}
