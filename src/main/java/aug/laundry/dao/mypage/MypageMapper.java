package aug.laundry.dao.mypage;

import aug.laundry.dto.MemberDto;
import aug.laundry.dto.MypageDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MypageMapper {
  public String findByName(Long memberId);

  public MypageDto findByNameAndPass(@Param("memberId") Long memberId);

  public String findByInviteCode(Long memberId);

  public MypageDto findByInfo(Long memberId);

  public int updateAddress(@Param("memberId") Long memberId, @Param("memberZipcode") String memberZipcode,
                           @Param("memberAddress") String memberAddress, @Param("memberAddressDetails") String memberAddressDetails);

  public int updatePhone(@Param("memberId") Long memberId, @Param("memberPhone") String memberPhone);

  public int unregister(Long memberId);

  public int updatePassword(@Param("memberId") Long memberId, @Param("memberPassword") String memberPassword);
}