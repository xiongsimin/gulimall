package kim.aries.gulimall.product.service.impl;

import kim.aries.gulimall.product.dao.BrandDao;
import kim.aries.gulimall.product.dao.CategoryDao;
import kim.aries.gulimall.product.entity.BrandEntity;
import kim.aries.gulimall.product.entity.CategoryEntity;
import kim.aries.gulimall.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import kim.aries.common.utils.PageUtils;
import kim.aries.common.utils.Query;

import kim.aries.gulimall.product.dao.CategoryBrandRelationDao;
import kim.aries.gulimall.product.entity.CategoryBrandRelationEntity;
import kim.aries.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        CategoryEntity categoryEntity = categoryDao.selectById(categoryBrandRelation.getCatelogId());
        BrandEntity brandEntity = brandDao.selectById(categoryBrandRelation.getBrandId());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        categoryBrandRelation.setBrandName(brandEntity.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public List<BrandVo> listBrandByCatelog(Long catId) {
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities = baseMapper.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<BrandVo> brandVos = categoryBrandRelationEntities.stream().map(e -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(e.getBrandId());
            brandVo.setBrandName(e.getBrandName());
            return brandVo;
        }).collect(Collectors.toList());
        return brandVos;
    }
}