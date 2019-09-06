package com.springboot.seimi.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.http.HttpMethod;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import com.springboot.dao.productDao;
import com.springboot.model.product;
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
import org.seimicrawler.xpath.JXDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wxl on 2019/9/3.
 */
@Crawler(name = "basic")
public class Basic extends BaseSeimiCrawler{
    @Autowired
    private productDao productDao;

    @Override
    public String[] startUrls() {
        String url = "https://www.indiegogo.com/explore/all?project_type=campaign&project_timing=all&sort=trending";
        return new String[]{url};
    }


    private static int numb=0;
    @Override
    public void start(Response response) {
        System.out.println("response= "+response.getMeta());
        JXDocument doc = response.document();
        try {
            List<Object> urls = doc.sel("//body/script");
            logger.info("{}", urls.size());
            for(Object s: urls){
                int index = s.toString().indexOf("negative_captcha");
                if(index != -1){
                    String str = s.toString();
                    String[] StrArr= str.split("=");
//                    System.out.println("s= "+StrArr.toString());
                    for (String strItem: StrArr) {
                        if(strItem.indexOf("\"header\":\"Production Ready\"") != -1){

                            String jsonStr = strItem.substring((strItem.indexOf("categories")+"categories".length()+2), (strItem.lastIndexOf(";")-1));
                            System.out.println(jsonStr);
                            JSONArray jsonArray = JSONArray.fromObject(jsonStr);
//                            System.out.println("jsonArray0= "+jsonArray.getJSONObject(0));
                            for(int i=0; i<jsonArray.size(); i++){
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                String name = jsonObj.getString("name");
                                String link = jsonObj.getString("link");
                                JSONArray categoriesArr = jsonObj.getJSONArray("categories");
                                System.out.println("name= "+name);
                                System.out.println("link= "+link);
                                System.out.println("categoriesArr= "+categoriesArr);
                                System.out.println("categoriesArr.size()= "+categoriesArr.size());
                                for (int j = 0; j < categoriesArr.size(); j++) {
                                    JSONObject categorieObj = categoriesArr.getJSONObject(j);
                                    String categorieName = categorieObj.getString("name");
                                    String categorielink = categorieObj.getString("link");

                                    Map<String,Object> paramJson = new HashMap<>();
                                    paramJson.put("category_main",categorieName);
                                    paramJson.put("category_top_level",name);
                                    paramJson.put("page_num",1);
                                    paramJson.put("per_page",1000);
                                    paramJson.put("project_timing","all");
                                    paramJson.put("project_type","campaign");
                                    paramJson.put("q","");
                                    paramJson.put("sort","trending");
                                    paramJson.put("tags",new JSONArray());
                                    JSONObject paramsObj = JSONObject.fromObject(paramJson);
                                    System.out.println("paramsObj= "+paramsObj.toString());
                                    Map<String,String> params = new HashMap<>();
                                    params.put("data",paramJson.toString());

                                    JSONObject resultObj = doPostJson("https://www.indiegogo.com/private_api/discover",paramsObj);
                                    System.out.println("mResponse= "+resultObj.toString());
                                    JSONObject mResponse = resultObj.getJSONObject("response");
                                    JSONArray discoverablesArr = mResponse.getJSONArray("discoverables");
//                                    System.out.println("discoverablesArr.size()= "+discoverablesArr.size());
                                    for (int k = 0; k < discoverablesArr.size(); k++) {
                                        JSONObject ableObj = discoverablesArr.getJSONObject(k);
                                        String clickthrough_url = ableObj.getString("clickthrough_url");
                                        Request request = Request.build("https://www.indiegogo.com/"+clickthrough_url,Basic::getTitle);
                                        request.setMaxReqCount(3);

                                        push(request);
                                    }
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
//                push(Request.build(s.toString(), Basic::getTitle));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getTitle(Response response){
//        System.out.println("getTitle= "+response);
        numb++;
        System.out.println("numb= "+  numb);
        JXDocument doc = response.document();
        try{
            List<Object> urls = doc.sel("//body/script");
            logger.info("{}", urls.size());
            for(Object s: urls){
                int index = s.toString().indexOf("negative_captcha");
                if(index != -1){
                    String str = s.toString();
                    logger.info("url:{}", response.getUrl());
//                    System.out.println("str= "+str);
                    String[] StrArr= str.split(";");
//                    System.out.println("s= "+StrArr.toString());
                    for (String strItem: StrArr) {
                        if(strItem.indexOf("gon.campaign=") != -1){
                            System.out.println("strItem= "+ strItem);
                            String[] campaign= strItem.split("=");
                            if(campaign.length<2)
                                break;
                            String jsonStr = campaign[1];
                            System.out.println("jsondata= "+ jsonStr);
                            JSONObject jsonObj = JSONObject.fromObject(campaign[1]);
                            String total_amount_sold = jsonObj.getString("total_amount_sold");//产品金额
                            String contributions_count = jsonObj.getString("contributions_count");//支持人数
                            String title = jsonObj.getString("title");//产品名称
                            String goal_percentage = jsonObj.getString("goal_percentage");//完成率
                            String days_left = String.valueOf(doc.sel("//meta[@name=\"sailthru.displayed_days_left\"]/@content").get(0));//获取剩余天数
                            JSONObject categoryObj = jsonObj.getJSONObject("category");
                            String categoryName = categoryObj.getString("name");//类别名称

                            product mProduct = new product();
                            mProduct.setProductAmount(total_amount_sold);
                            mProduct.setProductName(title);
                            mProduct.setPct_funded(goal_percentage);
                            mProduct.setCountributions(contributions_count);
                            mProduct.setDays_left(days_left);
                            mProduct.setCategoryName(categoryName);
                            System.out.println("mProduct= "+ mProduct.toString());
                            productDao.insertProduct(mProduct);
                            break;
                        }
                    }
                    break;
                }
            }

//            List pct_fundedList = doc.sel("//meta[@name=\"sailthru.displayed_pct_funded\"]/@content");
////            logger.info("List:{}", pct_fundedList);
//            if (pct_fundedList.size()>0){
//                String project_title = String.valueOf(doc.sel("//meta[@name=\"sailthru.project_title\"]/@content").get(0));//获取产品名称
////                String project_amount = String.valueOf(doc.sel("//head/script[last()]"));//获取产品金额
//                String pct_funded = String.valueOf(doc.sel("//meta[@name=\"sailthru.displayed_pct_funded\"]/@content").get(0));//获取完成率
//                String contributions = String.valueOf(doc.sel("//meta[@name=\"sailthru.displayed_contributions\"]/@content").get(0)); //获取支持人数
//                String days_left = String.valueOf(doc.sel("//meta[@name=\"sailthru.displayed_days_left\"]/@content").get(0));//获取剩余天数
////                product mProduct = new product();
////                mProduct.setProductAmount("");
////                mProduct.setProductName(project_title);
////                mProduct.setPct_funded(pct_funded);
////                mProduct.setCountributions(contributions);
////                mProduct.setDays_left(days_left);
////                productDao.insertProduct(mProduct);
//            }
            //do something
        }catch (Exception e){
            e.printStackTrace();
        }
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

    private void saveDataToFile(String fileName,String data) {
        BufferedWriter writer = null;
        File file = new File("d:\\"+ fileName + ".json");
        //如果文件不存在，则新建一个
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), "UTF-8"));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件写入成功！");
    }
}
