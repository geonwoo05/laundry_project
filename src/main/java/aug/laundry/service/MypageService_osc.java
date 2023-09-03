package aug.laundry.service;

import aug.laundry.dto.MemberDto;
import aug.laundry.dto.MypageDto;
import org.springframework.stereotype.Service;

@Service
public interface MypageService_osc {
  public String findByName(Long memberId);

  public MypageDto findByNameAndPass(Long memberId);
  public String findByInviteCode(Long memberId);
  public MypageDto findByInfo(Long memberId);
  public int updateAddress(Long memberId, String memberZipcode, String memberAddress, String memberAddressDetails);

  public int updatePhone(Long memberId, String memberPhone);

  public int unregister(Long memberId);
}
