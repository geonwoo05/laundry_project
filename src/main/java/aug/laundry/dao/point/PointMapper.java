package aug.laundry.dao.point;

import aug.laundry.domain.Drycleaning;
import aug.laundry.domain.Repair;
import aug.laundry.dto.OrdersResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PointMapper {

    Integer findByMemberId(Long memberId);

}