window.addEventListener('load', function(){
document.querySelector('#sendSmsBtn').addEventListener('click',function(e){

    e.preventDefault();

    let phonenumber = document.querySelector('#phone').value;
    let errors = document.querySelector('.errors');

    fetch("/sendSms?phonenumber="+phonenumber)
    .then(response => response.json())
    .then(map => {
    		alert("인증번호가 전송되었습니다.");
    		phone_check_number = map.number;
    		//document.querySelector('.phonenumber-check-input').disabled = false;
    		document.querySelector('#smsValueCheck').addEventListener('click', function(){
    			let inputCode = document.querySelector('#phoneCheck').value;

    			if(phone_check_number == inputCode){
    		        alert('인증되었습니다.');
    			}else{
    				errors.innerHTML = '인증번호가 불일치 합니다. 다시 확인해주세요!';
    		        errors.style.color = 'red';
    			}
    		})
    	})

    })
});