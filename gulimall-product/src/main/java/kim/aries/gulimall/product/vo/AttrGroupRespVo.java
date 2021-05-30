package kim.aries.gulimall.product.vo;

import kim.aries.gulimall.product.entity.AttrGroupEntity;
import lombok.Data;

/**
 * @Author aries
 * @Data 2021-05-30
 */
@Data
public class AttrGroupRespVo extends AttrGroupEntity {
    //商品分类名称
    private String catelogName;
}
