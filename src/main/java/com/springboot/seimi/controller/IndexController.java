package com.springboot.seimi.controller;

import cn.wanghaomiao.seimi.spring.common.CrawlerCache;
import cn.wanghaomiao.seimi.struct.CrawlerModel;
import cn.wanghaomiao.seimi.struct.Request;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wxl on 2019/9/3.
 */
@RestController
@RequestMapping(value = "/seimi")
public class IndexController {

    @RequestMapping(value = "/info/{cname}")
    public String crawler(@PathVariable String cname){
        CrawlerModel model = CrawlerCache.getCrawlerModel(cname);
        if (model == null){
            return  "not find" + cname;
        }
        return  model.queueInfo();
    }

    @RequestMapping(value = "send_req")
    public String sendRequest(Request request){
        CrawlerCache.consumeRequest(request);
        return "consume suc";
    }
}
