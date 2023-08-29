package aug.laundry.dao;

import aug.laundry.domain.Member;
import aug.laundry.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    public MemberDto selectOne(Long memberId);



}
