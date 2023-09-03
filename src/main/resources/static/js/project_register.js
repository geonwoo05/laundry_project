function sample6_execDaumPostcode(event) {
            event.preventDefault();
            new daum.Postcode({
                oncomplete: function(data) {
                    // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                    // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                    // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                    var addr = ''; // 주소 변수
                    var extraAddr = ''; // 참고항목 변수

                    //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                    if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                        addr = data.roadAddress;
                    } else { // 사용자가 지번 주소를 선택했을 경우(J)
                        addr = data.jibunAddress;
                    }

                    // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                    if(data.userSelectedType === 'R'){
                        // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                        // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                        if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                            extraAddr += data.bname;
                        }
                        // 건물명이 있고, 공동주택일 경우 추가한다.
                        if(data.buildingName !== '' && data.apartment === 'Y'){
                            extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                        }
                        // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                        if(extraAddr !== ''){
                            extraAddr = ' (' + extraAddr + ')';
                        }
                        // 조합된 참고항목을 해당 필드에 넣는다.
                        document.querySelector("#register_extraAddress").value = '';

                    } else {
                        document.querySelector("#register_extraAddress").value = '';
                    }

                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.querySelector('#register_postcode').value = data.zonecode;
                    document.querySelector("#register_address").value = addr + extraAddr;
                    // 커서를 상세주소 필드로 이동한다.
                    document.querySelector("#register_detailAddress").focus();
                }
            }).open();
        }


document.querySelector('#sendSmsBtn').addEventListener('click',function(){
    let phonenumber = document.querySelector('.phoneNumber').value;
    let resultMsg = document.querySelector('#phonenumber-check-warn');

    fetch("/sendSms?phonenumber="+phonenumber)
    .then(response => response.json())
    .then(map => {
    		alert("인증번호가 전송되었습니다.");
    		phone_check_number = map.number;
    		//document.querySelector('.phonenumber-check-input').disabled = false;
    		document.querySelector('.phonenumber-check-button').addEventListener('click', function(){
    			let inputCode = document.querySelector('.phonenumber-check-input').value;

    			if(phone_check_number == inputCode){
    		        alert('인증되었습니다.');
    			}else{
    				resultMsg.innerHTML = '인증번호가 불일치 합니다. 다시 확인해주세요!';
    		        resultMsg.style.color = 'red';
    			}
    		})
    	})

})


function idcheck(){
    let memberAccount = document.querySelector("#member_account").value;
    let checkMsg = document.querySelector('#checkMsg');
    if(memberAccount == null || memberAccount == ''){
        checkMsg.style.color = 'red';
        checkMsg.innerHTML = "아이디를 입력해주세요.";
    }else{
        fetch('/id/check/' + memberAccount)
            .then(response => response.json())
            .then(map => {
                console.log(map);
                if(map.res > 0){
                    checkMsg.style.color = 'red';
                    checkMsg.innerHTML = map.msg;
                }else{
                    checkMsg.style.color = '#3CB371';
                    checkMsg.innerHTML = map.msg;
                }
            })
    }
}

function passwordCheck(){
    let password = document.querySelector('.password-input').value;
    let passwordCheckMsg = document.querySelector('.passwordCheck1');

    // 문자열이 8~15자인 경우
    let regex1 = /^.{8,15}$/;

    // 대소문자, 숫자, 특수문자가 적어도 하나 이상 존재하는 경우
    let regex2 = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#$%^&+=!]).*$/;

    if(regex1.test(password)){
        if(regex1.test(password) && regex2.test(password)){
            passwordCheckMsg.style.color = '#3CB371';
            passwordCheckMsg.innerHTML = '사용가능한 비밀번호입니다.';
        }else{
            passwordCheckMsg.style.color = 'red';
            passwordCheckMsg.innerHTML = '대소문자, 숫자, 특수문자가 적어도 하나씩 있어야 합니다.';
        }

    }else{
        passwordCheckMsg.style.color = 'red';
        passwordCheckMsg.innerHTML = '비밀번호는 8~15자 사이로 정해주세요.';
    }

    // 비밀번호 -> 비밀번호 확인 -> 비밀번호 순으로 입력할 경우 대비
    passwordValidation()
}

function passwordValidation(){
    let password = document.querySelector('.password-input').value;
    let passwordCheckInput = document.querySelector('.password-check-input').value;
    let passwordCheckMsg = document.querySelector('.passwordCheck2');

    if(password === passwordCheckInput){
        passwordCheckMsg.style.color = '#3CB371';
        passwordCheckMsg.innerHTML = '비밀번호가 일치합니다.';
    }else{
        passwordCheckMsg.style.color = 'red';
        passwordCheckMsg.innerHTML = '비밀번호가 일치하지 않습니다.';
    }


}