package kim.aries.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import kim.aries.common.utils.PageUtils;
import kim.aries.common.utils.Query;

import kim.aries.gulimall.product.dao.CategoryDao;
import kim.aries.gulimall.product.entity.CategoryEntity;
import kim.aries.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查出所有分类
        List<CategoryEntity> categoryEntityList = baseMapper.selectList(null);
        //2、组装成树形结构
        //2.1、找到所有的一级分类
        List<CategoryEntity> firstLevelMenus = categoryEntityList.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map((menu) -> {
                    menu.setChildren(getChildren(menu, categoryEntityList));
                    return menu;
                })
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
        return firstLevelMenus;
    }

    @Override
    public void removeMenuByIds(List<Long> catIds) {
        //TODO 检查当前被删除的菜单是否被别的地方引用
        baseMapper.deleteBatchIds(catIds);
    }

    @Override
    public Long[] getCatelogPath(Long catelogId) {
        List<Long> tempPath = new ArrayList<>();
        while (catelogId != 0) {
            tempPath.add(catelogId);
            catelogId = baseMapper.selectById(catelogId).getParentCid();
        }
        Collections.reverse(tempPath);
        return tempPath.toArray(new Long[tempPath.size()]);
    }

    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> categoryEntityList) {
        return categoryEntityList.stream()
                .filter(item -> item.getParentCid() == root.getCatId())
                .map(item -> {
                    //找到子菜单
                    item.setChildren(getChildren(item, categoryEntityList));
                    return item;
                })
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }

}