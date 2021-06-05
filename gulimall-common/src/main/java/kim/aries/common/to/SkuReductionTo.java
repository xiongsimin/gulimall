package kim.aries.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author aries
 * @Data 2021-06-03
 */
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;

    private List<MemberPrice> memberPrice;
}
