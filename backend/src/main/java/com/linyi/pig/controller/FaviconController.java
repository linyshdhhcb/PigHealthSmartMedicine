package com.linyi.pig.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "忽略favicon.ico")
@RestController
@RequestMapping("/")
@SuppressWarnings({"unchecked", "rawtypes"})
public class FaviconController {

    /**
     * 忽略favicon.ico请求，避免将其视为一个调用的接口（不然ta-token会拦截）
     *
     * @return 返回204 No Content响应，表示成功处理了请求但没有内容返回
     */
    @Operation(summary = "忽略favicon.ico,不是调用的接口")
    @GetMapping("favicon.ico")
    public ResponseEntity<Void> favicon() {
        // 返回204 No Content，表示没有内容但成功处理
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
