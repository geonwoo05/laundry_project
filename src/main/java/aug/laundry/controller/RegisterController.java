package aug.laundry.controller;

import aug.laundry.dto.MemberDto;
import aug.laundry.service.MemberService_kgw;
import aug.laundry.service.SendSmsService_kgw;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final SendSmsService_kgw sendSms;
    private final MemberService_kgw memberService;

    @ResponseBody
    @GetMapping("/id/check/{memberAccount}")
    public Map<String, Object> idCheck(@PathVariable String memberAccount){
        Map<String, Object> msg = new HashMap<>();
        int res = memberService.checkId(memberAccount);

        if(res <= 0){
            msg.put("res", res);
            msg.put("msg", "사용가능한 아이디입니다.");
        }else{
            msg.put("res", res);
            msg.put("msg", "아이디가 중복됩니다.");
        }


        return msg;
    }


    @ResponseBody
    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    public Map<String, String> registerUser(@Valid MemberDto memberDto, BindingResult bindingResult){
        Map<String, String> validation = new HashMap<>();
        if(bindingResult.hasErrors()) {
            throw new IllegalArgumentException("validation 실패");
            /*
            System.out.println("spring validation 실패!");
            validation.put("validation", "실패");
            bindingResult.getAllErrors()
                    .forEach(objectError -> {
                        System.err.println("code : " + objectError.getCode());
                        System.err.println("defaultMessage : " + objectError.getDefaultMessage());
                        System.err.println("objectName : " + objectError.getObjectName());
                    });
            return validation;
            
             */
        }

        validation.put("validation", "성공");
        return validation;

    }













}
