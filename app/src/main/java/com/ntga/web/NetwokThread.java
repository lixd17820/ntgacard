package com.ntga.web;

import android.app.admin.NetworkEvent;
import android.os.Parcel;
import android.text.TextUtils;

import com.ntga.card.BdInfo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by lixiaodong on 2017/10/1.
 */

public class NetwokThread extends Thread {

    private int from;
    private int catalog;
    private Object[] param;

    public NetwokThread(int from, int catalog, Object[] param) {
        this.from = from;
        this.catalog = catalog;
        this.param = param;
    }

    @Override
    public void run() {
        RestfulDao dao = new RestfulDao();
        if (catalog == 0) {
            boolean isConn = dao.testNetwork();
            EventBus.getDefault().post(new NetwrokEvent(isConn, from));
        } else if (catalog == 1) {
            String v = dao.zhbd((String) param[0]);
            BdEvent bd = new BdEvent("0", "", (BdInfo) param[1]);
            JSONObject obj = null;
            if (!TextUtils.isEmpty(v) || v.startsWith("{")) {
                try {
                    obj = new JSONObject(v);
                    bd.key = obj.optString("key");
                    bd.value = obj.optString("value");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            EventBus.getDefault().post(bd);
        }
        dao.zhbd("32060219721130201X");

    }
}
