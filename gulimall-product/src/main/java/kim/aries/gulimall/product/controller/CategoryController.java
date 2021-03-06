package kim.aries.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kim.aries.gulimall.product.entity.CategoryEntity;
import kim.aries.gulimall.product.service.CategoryService;
import kim.aries.common.utils.PageUtils;
import kim.aries.common.utils.R;


/**
 * 商品三级分类
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 19:19:11
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询出所有分类以及子类，以树形结构组装起来
     */
    @RequestMapping("/list/tree")
    public R list() {
        List<CategoryEntity> categoryEntityList = categoryService.listWithTree();
        return R.ok().put("data", categoryEntityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateByDetail(category);

        return R.ok();
    }

    /**
     * 前端拖拽之后更新排序
     */
    @RequestMapping("/update/sort")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity[] categorys) {
        categoryService.updateBatchById(Arrays.asList(categorys));
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds) {
//		categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeMenuByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
