package aug.laundry.service;

import aug.laundry.dao.member.MemberMapper;
import aug.laundry.dao.point.PointDao;
import aug.laundry.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl_kgw implements MemberService_kgw{

    private final MemberMapper memberMapper;

    private final ApiExamMemberProfile apiExam;

    private final BCryptService_kgw bc;
    
    private final PointDao pointDao;

    public MemberDto selectOne(Long memberId){
        MemberDto memberDto = memberMapper.selectOne(memberId);
        return memberDto;
    }

    public int checkId(String memberAccount){
        int res = memberMapper.checkId(memberAccount);

        return res;
    }

    public Integer registerUser(MemberDto memberDto){
        String password = bc.encodeBCrypt(memberDto.getMemberPassword());
        memberDto.setMemberPassword(password);
        String inviteCode = getRandomCode();
        memberDto.setMemberMyInviteCode(inviteCode);
        Integer registerRes = memberMapper.registerUser(memberDto);
        MemberDto newbie = memberMapper.selectId(memberDto.getMemberAccount());
        if(registerRes > 0){
            int res = pointDao.registerPoint(newbie.getMemberId());
            System.out.println("포인트 넣기 : " + res);
        }

        if(memberDto.getMemberInviteCode() != null || !memberDto.getMemberInviteCode().equals("")){
            // 추천인 포인트 적립
            Long recommanderId = memberMapper.findRecommender(memberDto.getMemberInviteCode());
            pointDao.addRecommandPoint(recommanderId, 5000, "신규회원에게 추천");

            // 뉴비 포인트 적립
            pointDao.addRecommandPoint(newbie.getMemberId(), 5000, "추천인 코드 작성");

        }
        return registerRes;
    }

    public int inviteCodeCheck(String inviteCode){
        int res = memberMapper.inviteCodeCheck(inviteCode);
        return res;
    }
    
    // 신규유저의 초대코드 생성
    public String getRandomCode(){
        int length = 8; // 생성할 문자열의 길이
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String inviteCode = "";

        Random random = new Random();

        while(true){
            StringBuilder randomCode = new StringBuilder();
            for (int i = 0; i < length; i++) {

                int randomIndex = random.nextInt(characters.length());
                char randomChar = characters.charAt(randomIndex);
                randomCode.append(randomChar);

            }
            inviteCode = randomCode.toString();

            if (inviteCodeCheck(inviteCode) <= 0) {
                System.out.println("초대코드 생성 : " + inviteCode);
                break; // 중복이 아닌 경우 반복문 종료
            }
        }
        return inviteCode;
    }





}
