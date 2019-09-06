package com.springboot.controller;

import cn.wanghaomiao.seimi.core.Seimi;
import com.springboot.dao.productDao;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wxl on 2019/9/5.
 */
@RestController
public class productController {
    @Autowired
    private productDao productDao;

    @RequestMapping(value = "/getIndieDate", method = RequestMethod.GET)
    public JSONObject getIndieDate(@RequestParam(value = "page_num",required = true) Integer page_num,
                            @RequestParam(value = "per_page",required = true) Integer per_page,
                                   @RequestParam(value = "sortType",required = false) String sortType,
                                   @RequestParam(value = "query",required = false) String query) {
        System.out.println(query);
        JSONObject obj = new JSONObject();
        Map<String,Object> paramJson = new HashMap<>();
        paramJson.put("category_main",null);
        paramJson.put("category_top_level",null);
        paramJson.put("page_num",1);
        paramJson.put("per_page",1000);
        paramJson.put("project_timing","all");
        paramJson.put("project_type","campaign");
        if (query!=null)
            paramJson.put("q",query);
        else
            paramJson.put("q","");
        paramJson.put("sort","trending");
        paramJson.put("tags",new JSONArray());
        JSONObject jsonObj = JSONObject.fromObject(paramJson);
        System.out.println(jsonObj.toString());
//        obj.put("UV",111);
        obj = doPostJson("https://www.indiegogo.com/private_api/discover",jsonObj);
        System.out.println(obj.toString());
        JSONObject dataObj = JSONObject.fromObject(com.alibaba.fastjson.JSONObject.parse(obj.toString()).toString());
        JSONObject ablesObj = dataObj.getJSONObject("response");
        JSONArray ableArr = ablesObj.getJSONArray("discoverables");
        System.out.println(ableArr);
        if (sortType != null){
            if (sortType.equalsIgnoreCase("amount"))
                ableArr = sortByAmount(ableArr);
            else if (sortType.equalsIgnoreCase("percent"))
                ableArr = sortByPercent(ableArr);
        }
        ablesObj.put("discoverables", ableArr);

        return JSONOutput.getJsonOutput(!obj.isEmpty(), JSONUtils.toJSONString(ablesObj));
    }

    private static JSONArray sortByAmount(JSONArray dataArr){
        int count = dataArr.size();
        for (int i = 0; i < count; i++) {
            for (int j = i+1; j < count; j++) {
                if(dataArr.getJSONObject(i).getInt("funds_raised_amount") < dataArr.getJSONObject(j).getInt("funds_raised_amount")){
                    Object tt = dataArr.get(i);
                    dataArr.set(i,dataArr.get(j));
                    dataArr.set(j,tt);
                }
            }
        }

        return dataArr;
    }

    private static JSONArray sortByPercent(JSONArray dataArr){
        int count = dataArr.size();
        for (int i = 0; i < count; i++) {
            for (int j = i+1; j < count; j++) {
                if(dataArr.getJSONObject(i).getDouble("funds_raised_percent") < dataArr.getJSONObject(j).getDouble("funds_raised_percent")){
                    Object tt = dataArr.get(i);
                    dataArr.set(i,dataArr.get(j));
                    dataArr.set(j,tt);
                }
            }
        }

        return dataArr;

    }


    private static JSONObject  doPostJson(String url, JSONObject json){
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();//设置请求和传输超时时间
        post.setHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Basic YWRtaW46");
        post.setConfig(requestConfig);
        JSONObject response = null;
        try {
            StringEntity s = new StringEntity(json.toString(), "utf-8");
            s.setContentEncoding("UTF-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = res.getEntity();
                String result = EntityUtils.toString(res.getEntity());
                response = JSONObject.fromObject(result);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
