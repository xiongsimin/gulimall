package kim.aries.gulimall.member.dao;

import kim.aries.gulimall.member.entity.MemberLevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级
 * 
 * @author aries
 * @email aries_test@qq.com
 * @date 2021-04-04 20:22:36
 */
@Mapper
public interface MemberLevelDao extends BaseMapper<MemberLevelEntity> {
	
}
