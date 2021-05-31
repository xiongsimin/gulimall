package kim.aries.gulimall.product.service.impl;

import kim.aries.gulimall.product.dao.AttrAttrgroupRelationDao;
import kim.aries.gulimall.product.dao.AttrDao;
import kim.aries.gulimall.product.dao.CategoryDao;
import kim.aries.gulimall.product.entity.AttrAttrgroupRelationEntity;
import kim.aries.gulimall.product.entity.AttrEntity;
import kim.aries.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import kim.aries.common.utils.PageUtils;
import kim.aries.common.utils.Query;

import kim.aries.gulimall.product.dao.AttrGroupDao;
import kim.aries.gulimall.product.entity.AttrGroupEntity;
import kim.aries.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrDao attrDao;
    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), new QueryWrapper<AttrGroupEntity>());
            return new PageUtils(page);
        } else {
            String key = (String) params.get("key");
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId);
            if (!StringUtils.isEmpty(key)) {
                wrapper.and((obj) -> {
                    obj.eq("attr_group_id", key).or().like("attr_group_name", key);
                });
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            /*//todo 属性分组界面只显示所属商品分类ID，却不显示名称，此处特殊处理，将名称也展示
            List<AttrGroupEntity> records = page.getRecords();
            List<Long> catelogIds = records.stream().map(e -> {
                return e.getCatelogId();
            }).collect(Collectors.toList());
            //根据id查询出所有商品分类信息
            List<CategoryEntity> categoryEntityList = categoryDao.selectBatchIds(catelogIds);
            Map<Long, String> catelogInfo = new HashMap<>();
            categoryEntityList.stream().forEach(e -> {
                catelogInfo.put(e.getCatId(), e.getName());
            });
            */
            return new PageUtils(page);
        }
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttr(Long catelogId) {
        //查询所有属性分组
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<AttrGroupWithAttrsVo> attrGroupWithAttrList = attrGroupEntities.stream().map(e -> {
            List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", e.getAttrGroupId()));
            List<Long> attrIds = attrAttrgroupRelationEntities.stream().map(relation -> {
                return relation.getAttrId();
            }).collect(Collectors.toList());
            List<AttrEntity> attrs = new ArrayList<>();
            if (attrIds != null && attrIds.size() > 0) {
                attrs = attrDao.selectList(new QueryWrapper<AttrEntity>().in("attr_id", attrIds));
            }
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(e, attrGroupWithAttrsVo);
            attrGroupWithAttrsVo.setAttrs(attrs);
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());
        return attrGroupWithAttrList;
    }
}