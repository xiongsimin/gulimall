package kim.aries.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import kim.aries.common.utils.PageUtils;
import kim.aries.gulimall.product.entity.SpuInfoEntity;
import kim.aries.gulimall.product.vo.spusave.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 17:02:01
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存spu信息
     *
     * @param spuSaveVo
     */
    void saveSpuInfo(SpuSaveVo spuSaveVo);
}

