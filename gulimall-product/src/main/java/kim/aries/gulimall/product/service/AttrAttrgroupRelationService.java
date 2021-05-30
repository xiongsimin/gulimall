package kim.aries.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import kim.aries.common.utils.PageUtils;
import kim.aries.gulimall.product.entity.AttrAttrgroupRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 17:02:01
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 批量删除属性分组与属性关联关系
     *
     * @param attrAttrGroupRelationEntities
     */
    void batchDeleteAttr(List<AttrAttrgroupRelationEntity> attrAttrGroupRelationEntities);

    void getAttrGroupWithAttr(Long catelogId);
}

