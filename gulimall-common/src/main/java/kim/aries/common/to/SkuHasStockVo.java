package kim.aries.common.to;

import lombok.Data;

/**
 * @Author aries
 * @Data 2021-07-04
 */
@Data
public class SkuHasStockVo {

    private Long skuId;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 是否有库存
     */
    private Boolean hasStock;
}
