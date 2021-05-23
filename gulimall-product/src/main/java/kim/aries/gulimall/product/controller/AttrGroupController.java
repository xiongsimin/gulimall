package kim.aries.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import kim.aries.gulimall.product.entity.AttrAttrgroupRelationEntity;
import kim.aries.gulimall.product.entity.AttrEntity;
import kim.aries.gulimall.product.service.AttrAttrgroupRelationService;
import kim.aries.gulimall.product.service.AttrService;
import kim.aries.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kim.aries.gulimall.product.entity.AttrGroupEntity;
import kim.aries.gulimall.product.service.AttrGroupService;
import kim.aries.common.utils.PageUtils;
import kim.aries.common.utils.R;


/**
 * 属性分组
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 19:19:11
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //生成的Controller文件中每个Controller都生成Shiro注解
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId) {
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        attrGroup.setCatelogPath(categoryService.getCatelogPath(attrGroup.getCatelogId()));

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    ///product/attrgroup/{attrgroupId}/attr/relation  获取属性分组的关联的所有属性
    @GetMapping("/{attrgroupId}/attr/relation")
    public R getAttrRelationList(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> attrEntityList = attrService.getAttrListByGroupId(attrgroupId);
        return R.ok().put("data", attrEntityList);
    }

    //attr/relation/delete
    @PostMapping("/attr/relation/delete")
    public R batchDeleteAttr(@RequestBody List<AttrAttrgroupRelationEntity> attrAttrGroupRelationEntities) {
        if (attrAttrGroupRelationEntities != null && attrAttrGroupRelationEntities.size() > 0) {
            attrAttrgroupRelationService.batchDeleteAttr(attrAttrGroupRelationEntities);
        }
        return R.ok();
    }

}
