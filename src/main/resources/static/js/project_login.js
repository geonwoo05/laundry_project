const redirectURL = document.querySelector('#redirectURL').value;
const naverRedirectUrl = 'http://localhost:8080/login/naver_callback';
<<<<<<< Updated upstream
//const naverRedirectUrl = 'https://1ba1-183-96-143-101.ngrok-free.app/login/naver_callback'
const kakaoRedirectUrl = 'http://localhost:8080/login/kakaoLogin'
//const kakaoRedirectUrl = 'https://1ba1-183-96-143-101.ngrok-free.app/login/kakaoLogin'

function kakaoLogin(){
    if(redirectURL != ''){
        window.location.href = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=6ceec1b5aece169e4582fd82601abd44&state="+ redirectURL +"&redirect_uri=" + kakaoRedirectUrl;
    }else{
        window.location.href = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=6ceec1b5aece169e4582fd82601abd44&redirect_uri=" + kakaoRedirectUrl;
    }

}
function naverLogin(){
    if(redirectURL != ''){
        window.location.href = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=1zyMGeTLflNJkh2bmICN&state="+redirectURL+"&redirect_uri=" + naverRedirectUrl;
    }else{
        window.location.href = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=1zyMGeTLflNJkh2bmICN&state="+redirectURL+"&redirect_uri=" + naverRedirectUrl;
    }

}

function formSubmit(){
    document.querySelector('.login_form').submit();
=======
const kakaoRedirectUrl = 'http://localhost:8080/login/kakaoLogin'


function kakaoLogin(){
    window.location.href = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=6ceec1b5aece169e4582fd82601abd44&state="+ redirectURL +"&redirect_uri=" + kakaoRedirectUrl;
}

function naverLogin(){
    window.location.href = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=1zyMGeTLflNJkh2bmICN&state="+ redirectURL +"&redirect_uri=" + naverRedirectUrl;
>>>>>>> Stashed changes
}