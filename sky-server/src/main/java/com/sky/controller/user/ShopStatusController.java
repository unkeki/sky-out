package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController("UserShopStatusController")
@RequestMapping("user/shop")
@Api(tags = "用户端营业状态设置")
@Slf4j
public class ShopStatusController {

    private final static String SHOP_STATUS_KEY = "SHOP_STATUS";

    private final RedisTemplate redisTemplate;

    public ShopStatusController(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/status")
    @ApiOperation("修改营业状态：1、营业；0、打烊")
    public Result<Integer> getStatus(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer status = (Integer) valueOperations.get(SHOP_STATUS_KEY);
        log.info("当前营业状态为：{}", Objects.equals(status, StatusConstant.ENABLE) ? "营业" : "打烊");
        return Result.success(status);
    }
}
