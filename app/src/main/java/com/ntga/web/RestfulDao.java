package com.ntga.web;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lixiaodong on 2017/10/1.
 */

public class RestfulDao {
    private static final int POST = 1;
    private static final int GET = 0;
    private String URL_PATH = "/ydjw/services/ydjw/";
    private final String TEST_NETWORK = URL_PATH + "testNetwork";
    private final String ZHBD = URL_PATH + "zhbd";


    public String getUrl() {
        return "http://127.0.0.1:8088";
        //return "http://www.ntjxj.com";
    }

    /**
     * 测试网络连接
     *
     * @return true 可连接 false 不可以连接
     */
    public boolean testNetwork() {
        WebQueryResult<String> re = httpTextClient(getUrl() + TEST_NETWORK, GET);
        String err = GlobalMethod.getErrorMessageFromWeb(re);
        if (TextUtils.isEmpty(err)) {
            return re != null && re.getResult() != null && "OK".equals(re.getResult());
        }
        return false;
    }

    public String zhbd(String sfzh) {
        WebQueryResult<String> re = httpTextClient(getUrl() + ZHBD + "?sfzh=" + sfzh.toUpperCase(), GET);
        String err = GlobalMethod.getErrorMessageFromWeb(re);
        if (TextUtils.isEmpty(err)) {
            return re.getResult();
        }
        return "";
    }

    public WebQueryResult<String> httpTextClient(String url, int method, String... param) {
        Map<String, String> params = new HashMap<>();
        if (param.length > 0 && param.length % 2 == 0) {
            for (int i = 0; i < param.length / 2; i++) {
                params.put(param[2 * i], param[2 * i + 1]);
            }
        }
        return httpTextClient(url, method, params);
    }

    public WebQueryResult<String> httpTextClient(String url, int method, Map<String, String> params) {
        WebQueryResult<String> web = new WebQueryResult<>();
        web.setStatus(400);
        OkHttpClient client = new OkHttpClient();
        Request.Builder reqb = new Request.Builder().url(url);
        if (method == POST) {
            FormBody.Builder mb = new FormBody.Builder();
            if (params != null)
                for (Map.Entry<String, String> m : params.entrySet()) {
                    mb.add(m.getKey(), m.getValue());
                }
            reqb = reqb.post(mb.build());
        }
        Response response = null;
        try {
            response = client.newCall(reqb.build()).execute();
            int code = response.code();
            web.setStatus(code);
            Log.e("ResultDao", "code: " + code);
            if (response.isSuccessful()) {
                String s = response.body().string();
                Log.e("ResultDao", "return: " + s);
                web.setResult(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            web.setStMs(e.getMessage());
        }
        return web;
    }

}
