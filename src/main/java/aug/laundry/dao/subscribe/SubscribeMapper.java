package aug.laundry.dao.subscribe;

import aug.laundry.dto.SubscriptionPayDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SubscribeMapper {

    int insertJoinSubscribe(SubscriptionPayDto subData);

    int updateMemberSubscribe(@Param("selectMonth") int selectMonth, @Param("memberId") Long memberId);
}
