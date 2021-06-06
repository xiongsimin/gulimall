package kim.aries.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author aries
 * @Data 2021-06-06
 */
@Data
public class MergeVo {
    //整单Id
    private Long purchaseId;
    //合并集合[1,2,3,4]
    private List<Long> items;
}
