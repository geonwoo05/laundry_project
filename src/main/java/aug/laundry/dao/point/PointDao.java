package aug.laundry.dao.point;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PointDao {

    private final PointMapper pointMapper;

    public Integer findByMemberId(Long memberId) {
        return pointMapper.findByMemberId(memberId);
    }
    public Integer addPoint(Long memberId, int point, String reason) { return pointMapper.addPoint(memberId,point,reason); }
}
