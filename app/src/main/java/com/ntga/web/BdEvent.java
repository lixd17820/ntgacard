package com.ntga.web;

import com.ntga.card.BdInfo;

/**
 * Created by lixiaodong on 2017/10/1.
 */

public class BdEvent {
    public BdEvent(String key, String value, BdInfo id) {
        this.key = key;
        this.value = value;
        this.id = id;
    }

    public String key;
    public String value;
    public BdInfo id;
}
