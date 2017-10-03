package com.ntga.web;

import android.text.TextUtils;

import java.net.HttpURLConnection;

/**
 * Created by lixiaodong on 2017/10/1.
 */

class GlobalMethod {

    public static <E> String getErrorMessageFromWeb(WebQueryResult<E> webResult) {
        String err = "";
        if (webResult == null)
            return "网络连接失败，请检查配查或与管理员联系！";
        String ms = webResult.getStMs();
        if (!TextUtils.isEmpty(ms))
            return ms;
        if (webResult.getStatus() != HttpURLConnection.HTTP_OK) {
            // 服务器返回数据正确性验证， 网络状态正常
            if (webResult.getStatus() == 204) {
                return "未查询到符合条件的记录！";
            } else if (webResult.getStatus() == 500) {
                return "服务不能提供，请与管理员联系！";
            } else if (webResult.getStatus() == 404) {
                return "该查询在服务器不能实现，请与管理员联系！";
            } else {
                return "服务器出现未知错误";
            }
        }

        if (webResult.getResult() == null) {
            return "未能获取数据";
        }
        return err;
    }
}
