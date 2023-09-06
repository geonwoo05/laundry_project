package aug.laundry.service;

import aug.laundry.dao.member.MemberMapper;
import aug.laundry.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl_kgw implements MemberService_kgw{

    private final MemberMapper memberMapper;

    private final ApiExamMemberProfile apiExam;

    private final BCryptService_kgw bc;

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
        Integer res = memberMapper.registerUser(memberDto);
        return res;
    }

    public int inviteCodeCheck(String inviteCode){
        int res = memberMapper.inviteCodeCheck(inviteCode);
        return res;
    }





}
