package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController("AdminShopStatusController")
@RequestMapping("admin/shop")
@Api(tags = "管理端营业状态设置")
@Slf4j
public class ShopStatusController {

    private final static String SHOP_STATUS_KEY = "SHOP_STATUS";

    private final RedisTemplate redisTemplate;

    public ShopStatusController(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PutMapping("/{status}")
    @ApiOperation("修改营业状态：1、营业；0、打烊")
    public Result changeStatus(@PathVariable("status") Integer status){
        log.info("当前营业状态为：{}", Objects.equals(status, StatusConstant.ENABLE) ? "营业" : "打烊");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(SHOP_STATUS_KEY, status);
        return Result.success();
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
