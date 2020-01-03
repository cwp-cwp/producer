package com.cwp.produce.controller;

import com.cwp.produce.utils.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/produce")
public class TestController {

    /**
     * http://127.0.0.1:8080/producer/produce/hello
     *
     * @return
     */
    @RequestMapping("/hello")
    @ResponseBody
    public Result testHello() {
        return new Result(200, "请求成功", "hello...");
    }

}
