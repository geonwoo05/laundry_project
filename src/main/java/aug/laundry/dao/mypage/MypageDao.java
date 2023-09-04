package aug.laundry.dao.mypage;

import aug.laundry.dto.MypageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MypageDao {

  private final MypageMapper mypageMapper;

  public String findByName(Long memberId){ return mypageMapper.findByName(memberId); }

  public MypageDto findByNameAndPass(Long memberId){
    return mypageMapper.findByNameAndPass(memberId);
  }

  public String findByInviteCode(Long memberId){ return mypageMapper.findByInviteCode(memberId);}

  public MypageDto findByInfo(Long memberId){ return mypageMapper.findByInfo(memberId); }

  public int updateAddress(Long memberId, String memberZipcode, String memberAddress, String memberAddressDetails){
        return mypageMapper.updateAddress(memberId, memberZipcode, memberAddress, memberAddressDetails);
  }

  public int updatePhone(Long memberId, String memberPhone){ return mypageMapper.updatePhone(memberId, memberPhone); }

  public int unregister(Long memberId) { return mypageMapper.unregister(memberId); }

  public int updatePassword(Long memberId, String memberPassword){ return mypageMapper.updatePassword(memberId, memberPassword); }
}
