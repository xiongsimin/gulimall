package kim.aries.gulimall.product.vo;

import kim.aries.gulimall.product.entity.AttrEntity;
import kim.aries.gulimall.product.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author aries
 * @Data 2021-05-30
 */
@Data
public class AttrGroupWithAttrsVo extends AttrGroupEntity {
    private List<AttrEntity> attrs;
}
