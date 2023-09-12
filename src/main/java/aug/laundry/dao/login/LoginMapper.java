package aug.laundry.dao.login;
import aug.laundry.dto.AdminDto;
import aug.laundry.dto.MemberDto;
import aug.laundry.dto.QuickRiderDto;
import aug.laundry.dto.RiderDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {
    public int checkSocialId(String id);

    public int registerSocialUser(MemberDto memberDto);

    public int registerSocialNumber(String id);

    public MemberDto socialLogin(@Param("memberAccount") String memberAccount, @Param("memberSocial") String memberSocial);

    public MemberDto login(String memberAccount);

    public AdminDto adminLogin(String adminEmail);

    public RiderDto riderLogin(String riderEmail);

    public QuickRiderDto quickRiderLogin(String quickRiderEmail);

}
