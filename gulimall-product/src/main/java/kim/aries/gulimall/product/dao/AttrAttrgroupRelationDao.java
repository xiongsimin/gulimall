package kim.aries.gulimall.product.dao;

import kim.aries.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 17:02:01
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteRelations(@Param("relationList") List<AttrAttrgroupRelationEntity> attrAttrGroupRelationEntities);
}
