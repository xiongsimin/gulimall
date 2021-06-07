package kim.aries.gulimall.ware.vo;

import lombok.Data;

/**
 * @Author aries
 * @Data 2021-06-07
 */
@Data
public class PurchaseItemDoneVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
