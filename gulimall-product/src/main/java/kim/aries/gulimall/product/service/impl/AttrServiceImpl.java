package kim.aries.gulimall.product.service.impl;

import kim.aries.common.constant.ProductConstant;
import kim.aries.gulimall.product.dao.AttrAttrgroupRelationDao;
import kim.aries.gulimall.product.dao.AttrGroupDao;
import kim.aries.gulimall.product.dao.CategoryDao;
import kim.aries.gulimall.product.entity.AttrAttrgroupRelationEntity;
import kim.aries.gulimall.product.entity.AttrGroupEntity;
import kim.aries.gulimall.product.entity.CategoryEntity;
import kim.aries.gulimall.product.service.CategoryService;
import kim.aries.gulimall.product.vo.AttrRespVo;
import kim.aries.gulimall.product.vo.AttrVo;
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

import kim.aries.gulimall.product.dao.AttrDao;
import kim.aries.gulimall.product.entity.AttrEntity;
import kim.aries.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryService categoryService;
    @Autowired
    AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        //保存基本数据
        this.save(attrEntity);
        //基本属性需要维护属性分组关系
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //保存关联关系
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            this.attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public PageUtils queryPageByCatelogId(Map<String, Object> params, Long catelogId, int attrType) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type", attrType == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() ? 1 : 0);
        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((wreapper) -> {
                wreapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params),
                queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> attrEntityList = page.getRecords();
        List<AttrRespVo> attrRespVoList = attrEntityList.stream().map(attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            //查询属性分组名
            if (attrAttrgroupRelationEntity != null && attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            //查询商品分类名
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            attrRespVo.setCatelogName(categoryEntity.getName());
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrRespVoList);
        return pageUtils;
    }

    @Override
    public AttrRespVo getDetail(Long attrId) {
//"attrGroupId": 1, //分组id
//		"catelogId": 225, //分类id
//		"catelogPath": [2, 34, 225] //分类完整路径
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        if (attrEntity != null) {
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
                //查询属性分组id
                AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
                attrRespVo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
            }
            //组装商品分类完整路径
            Long[] catelogPath = categoryService.getCatelogPath(attrEntity.getCatelogId());
            attrRespVo.setCatelogPath(catelogPath);
        }
        return attrRespVo;
    }

    @Override
    @Transactional
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.save(attrEntity);
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //更新属性与属性分组关系
            attrAttrgroupRelationDao.delete(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public List<AttrEntity> getAttrListByGroupId(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> attrAttrGroupRelationEntityList = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> attrIds = attrAttrGroupRelationEntityList.stream().map(e -> {
            return e.getAttrId();
        }).collect(Collectors.toList());
        List<AttrEntity> attrEntityList = new ArrayList<>();
        if (attrIds != null && attrIds.size() > 0) {
            attrEntityList = this.listByIds(attrIds);
        }
        return attrEntityList;
    }

    @Override
    public PageUtils getNoAttrRelationList(Long attrgroupId, Map<String, Object> params) {
        //查询当前分组
        AttrGroupEntity attrGroupEntity = this.attrGroupDao.selectById(attrgroupId);
        //当前分组所属商品分类
        Long catelogId = attrGroupEntity.getCatelogId();
        //查询分类下所有分组
        List<AttrGroupEntity> attrGroupEntityList = this.attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> attrGroupIds = attrGroupEntityList.stream().map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        //查询当前分组所属分类下的所有已经存在关联关系的分组
        List<AttrAttrgroupRelationEntity> attrAttrGroupRelationEntityList = this.attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroupIds));
        List<Long> attrIds = attrAttrGroupRelationEntityList.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId);
        if (attrIds != null && attrIds.size() > 0) {
            wrapper.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(w -> {
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }
}