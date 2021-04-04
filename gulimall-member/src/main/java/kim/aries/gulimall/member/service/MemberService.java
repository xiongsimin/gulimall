package kim.aries.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import kim.aries.common.utils.PageUtils;
import kim.aries.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 20:22:36
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

