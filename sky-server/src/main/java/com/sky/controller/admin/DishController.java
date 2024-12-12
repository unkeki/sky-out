package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Api(tags = "菜品管理")
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    private final DishService dishService;
    private final RedisTemplate redisTemplate;

    public DishController(DishService dishService,
                          RedisTemplate redisTemplate){
        this.dishService = dishService;
        this.redisTemplate = redisTemplate;
    }

    @ApiOperation("新增菜品")
    @PostMapping()
    public Result save(@RequestBody DishDTO dishDTO){
        String key ="dish_" + dishDTO.getCategoryId();
        delCache(key);

        dishService.insert(dishDTO);
        return Result.success();
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        return Result.success(dishService.pageQuery(dishPageQueryDTO));
    }

    @ApiOperation("删除菜品")
    @DeleteMapping()
    public Result del(@RequestParam List<Long> ids){
        delCache("dish_*");
        dishService.del(ids);
        return Result.success();
    }

    @ApiOperation("获取菜品")
    @GetMapping("/{id}")
    public Result<DishVO> get(@PathVariable("id") Long id){
        return Result.success(dishService.getByIdWithFlavor(id));
    }

    @ApiOperation("修改菜品")
    @PutMapping()
    public Result update(@RequestBody DishDTO dishDTO){
        delCache("dish_*");
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

    @ApiOperation("修改菜品状态")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable("status") Integer status, @RequestParam Long id){
        delCache("dish_*");
        dishService.startOrStop(status, id);
        return Result.success();
    }

    private void delCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        List<DishVO> list = dishService.listWithFlavor(dish);

        return Result.success(list);
    }

}
