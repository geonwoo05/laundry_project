package aug.laundry.dao;

import aug.laundry.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    public int checkSocialId(String id);

    public int registerSocialUser(MemberDto memberDto);

    public int registerSocialNumber(String id);

    public MemberDto login(String memberAccount);

}
