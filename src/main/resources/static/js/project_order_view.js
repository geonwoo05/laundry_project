window.addEventListener('load', function(){

    if(document.querySelector('#couponPrice').value==''){
        document.querySelector('#coupon-row').style.display = 'none';
    }

    if(document.querySelector('#point').value==''){
        document.querySelector('#point-row').style.display = 'none';
    }





    let couponBtn = document.querySelector('#selectCoupon');
    couponBtn.addEventListener('click', function(){
        var options = 'width=600, height=400, top=100, left=100, resizable=yes, scrollbars=yes';
            window.open('/orders/2/members/1/coupons', '_blank');
    })



//    서버에서 계산해온 값
    let totalPriceFromServer = parseInt(document.querySelector('#totalPrice').textContent);

//    let pointValue = parseInt(document.querySelector('#point').value);
    let couponId = parseInt(document.querySelector('#coupon').value);



    document.querySelector('#point').addEventListener('input', function(){

        let couponPrice = parseInt(document.querySelector('#couponPrice').value);

        let pointRow = document.querySelector('#point-row');

        if(document.querySelector('#point').value==''){
            document.querySelector('#point-row').style.display = 'none';
        }

        if(document.querySelector('#point').value != ''){
            pointRow.style.display = '';
            pointRow.querySelector('.content.discount').textContent = '- ' + Number(document.querySelector('#point').value).toLocaleString() + '원';
        }



        if(isNaN(couponPrice)){
            //입력되었을때 서버에서 계산해온 값으로 초기화
            document.querySelector('#totalPrice').innerHTML = totalPriceFromServer;
            let pointValue = parseInt(document.querySelector('#point').value);

            if(!isNaN(pointValue)){
                let temp = totalPriceFromServer - pointValue;
                document.querySelector('#totalPrice').innerHTML = temp;
            }
        }
        else if(!isNaN(couponPrice)){
            document.querySelector('#totalPrice').innerHTML = totalPriceFromServer - couponPrice;
            let pointValue = parseInt(document.querySelector('#point').value);

            if(!isNaN(pointValue)){
                let temp = totalPriceFromServer - couponPrice - pointValue;
                document.querySelector('#totalPrice').innerHTML = temp;
            }
        }







    })













    var IMP = window.IMP;
    IMP.init("imp05521551");
});

function formatDate(date) {
        var year = date.getFullYear();
        var month = ('0' + (date.getMonth() + 1)).slice(-2);
        var day = ('0' + date.getDate()).slice(-2);
        var hours = ('0' + date.getHours()).slice(-2);
        var minutes = ('0' + date.getMinutes()).slice(-2);
        var seconds = ('0' + date.getSeconds()).slice(-2);
        var milliseconds = ('00' + date.getMilliseconds()).slice(-3);

        return year + '_' + month + '_' + day + '_' + hours + minutes + seconds + milliseconds;
    }

function requestPay() {

    let ordersId = document.querySelector('#ordersId').textContent;
    let couponListId = document.querySelector('input[name=coupon]').value;
    let couponPrice = document.querySelector('input[name=couponPrice]').value;
    let pointPrice = document.querySelector('input[name=point]').value;


    var merchantUId = 'p'+ordersId+'_'+formatDate(new Date());

    var IMP = window.IMP;

    IMP.request_pay(
      {
        pg: "inicis",
        pay_method: "card",
        merchant_uid: merchantUId,
        name: "결제테스트",
        amount: 100,
        buyer_name: "구매자",
        buyer_tel: "010-1234-5678",
        m_redirect_url: "localhost:8080/orders/" + ordersId
                        + "/payment/complete?"
                        + "couponListId=" + couponListId
                        + "&couponPrice=" + couponPrice
                        + "&pointPrice=" + pointPrice
          // ... (이하 코드 생략)
          // orders/2/payment/validation?couponListId=&couponPrice=&pointPrice=&imp_uid=imp_205309550611&merchant_uid=merchant_test_Wed+Sep+06+2023+11%3A45%3A08+G&imp_success=true
          // localhost:8080/orders/2/payment?point=&coupon=&couponPrice=#
      },
        function (rsp) {
          console.log(rsp);
//                            if (rsp.success) {// 결제성공시 로직
//                                        let data = {
//                            				imp_uid:rsp.imp_uid,
//                            				amount:rsp.paid_amount
//                            			};
//                                        //결제 검증
//                                        $.ajax({
//                            				type:"POST",
//                            				url:"verifyIamport",
//                            				data:JSON.stringify(data),
//                            				contentType:"application/json; charset=utf-8",
//                            				dataType:"json",
//                            				success: function(result) {
//                            					alert("결제검증 완료");
//                            					//self.close();
//                            				},
//                            				error: function(result){
//                            					alert(result.responseText);
////                            					cancelPayments(rsp);
//                            				}
//                            			});
//
//                                    } else {// 결제 실패 시 로직
//                            			alert("결재 실패");
//                            			alert(rsp.error_msg);
//                            			console.log(rsp);
//                                    }
                      // callback
                      //rsp.imp_uid 값으로 결제 단건조회 API를 호출하여 결제결과를 판단합니다.
    }
  );
}
