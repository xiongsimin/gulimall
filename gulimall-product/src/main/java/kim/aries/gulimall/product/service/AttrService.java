package kim.aries.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import kim.aries.common.utils.PageUtils;
import kim.aries.gulimall.product.entity.AttrEntity;
import kim.aries.gulimall.product.vo.AttrRespVo;
import kim.aries.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 17:02:01
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存（包括保存分类关系）
     *
     * @param attr
     */
    void saveAttr(AttrVo attr);

    /**
     * 根据商品分类查询属性列表
     *
     * @param params
     * @param catelogId
     * @param attrType
     * @return
     */
    PageUtils queryPageByCatelogId(Map<String, Object> params, Long catelogId, String attrType);

    /**
     * 查询属性详情
     *
     * @param attrId
     * @return
     */
    AttrRespVo getDetail(Long attrId);

    /**
     * 更新属性
     *
     * @param attr
     */
    void updateAttr(AttrVo attr);

    /**
     * 获取属性分组下的所有属性
     *
     * @param attrgroupId
     * @return
     */
    List<AttrEntity> getAttrListByGroupId(Long attrgroupId);

    /**
     * 查询还没有被关联过的属性（查询当前分组可以关联的属性）
     *
     * @param attrgroupId
     * @param params
     * @return
     */
    PageUtils getNoAttrRelationList(Long attrgroupId, Map<String, Object> params);
}

