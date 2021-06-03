package kim.aries.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author aries
 * @Data 2021-06-03
 */
@Data
public class SpuBoundsTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
