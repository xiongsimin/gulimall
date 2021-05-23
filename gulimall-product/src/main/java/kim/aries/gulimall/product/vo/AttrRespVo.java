package kim.aries.gulimall.product.vo;

import lombok.Data;

/**
 * @author xiongsm
 * @Date 2021-05-23
 */
@Data
public class AttrRespVo extends AttrVo {
    /**
     * 所属分类名字
     */
    private String catelogName;
    /**
     * 所属分组名字
     */
    private String groupName;

    /**
     * 拼接分类级联层级
     */
    private Long[] catelogPath;
}
