package com.springboot.controller;

import net.sf.json.JSONObject;

/**
 * Created by winnie on 2017/6/16.
 */
public class JSONOutput {

    public static JSONObject getJsonOutput(boolean result) {
        JSONObject jsonObject = new JSONObject();
        if (result) {
            jsonObject.put("resultStatus", 200);
            jsonObject.put("resultDescription", "OK");
        } else {
            jsonObject.put("resultStatus", 400);
            jsonObject.put("resultDescription", "ERROR");
        }
        return jsonObject;
    }

    public static JSONObject getJsonOutput(boolean result, Object obj) {
        JSONObject jsonObject = new JSONObject();
        if (result) {
            jsonObject.put("resultStatus", 200);
            jsonObject.put("resultDescription", "OK");
            jsonObject.put("resultData", obj);
        } else {
            jsonObject.put("resultStatus", 400);
            jsonObject.put("resultDescription", "ERROR");
            //jsonObject.put("resultDescription", String.valueOf(obj));
        }
        return jsonObject;
    }

    public static JSONObject getJsonOutput(boolean result, int totalCount, int pageNum, int pageSize, String jsonString) {
        JSONObject jsonObject = new JSONObject();
        if (result) {
            jsonObject.put("resultStatus", 200);
            jsonObject.put("resultDescription", "OK");
            jsonObject.put("totalCount", totalCount);
            jsonObject.put("pageNum", pageNum);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("resultData", jsonString);
        } else {
            jsonObject.put("resultStatus", 400);
            jsonObject.put("resultDescription", "ERROR");
        }
        return jsonObject;
    }

    public static JSONObject getJsonOutput(boolean result, int totalCount, int uv,int pageNum, int pageSize, String jsonString) {
        JSONObject jsonObject = new JSONObject();
        if (result) {
            jsonObject.put("resultStatus", 200);
            jsonObject.put("resultDescription", "OK");
            jsonObject.put("totalCount", totalCount);
            jsonObject.put("uv", uv);
            jsonObject.put("pageNum", pageNum);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("resultData", jsonString);
        } else {
            jsonObject.put("resultStatus", 400);
            jsonObject.put("resultDescription", "ERROR");
        }
        return jsonObject;
    }
}
