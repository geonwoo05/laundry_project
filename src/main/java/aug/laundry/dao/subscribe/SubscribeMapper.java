package aug.laundry.dao.subscribe;

import aug.laundry.dto.SubscriptionPayDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SubscribeMapper {

    int insertJoinSubscribe(SubscriptionPayDto subData);
    int updateMemberSubscribe(@Param("selectMonth") int selectMonth, @Param("memberId") Long memberId);
    int updateNextMerchantId(@Param("merchantUid") String merchantUid, @Param("merchantUidRe") String merchantUidRe);
    SubscriptionPayDto getScheduleInfo(Long memberId);
    int updateCancel(String merchantUid);// 구독취소 업데이트
    int getRepayCount(String merchantUid);
    int updateRepayCount(String merchantUid);

}
