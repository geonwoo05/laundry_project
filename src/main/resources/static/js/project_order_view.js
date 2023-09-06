window.addEventListener('load', function(){

    let couponBtn = document.querySelector('#selectCoupon');
    couponBtn.addEventListener('click', function(){
        var options = 'width=600, height=400, top=100, left=100, resizable=yes, scrollbars=yes';
            window.open('/orders/members/1/coupons', '_blank');
    })


    var IMP = window.IMP;
    IMP.init("imp05521551");
});

function requestPay() {

    let ordersId = document.querySelector('#ordersId').textContent;
    let couponListId = document.querySelector('input[name=coupon]').value;
    let couponPrice = document.querySelector('input[name=couponPrice]').value;
    let pointPrice = document.querySelector('input[name=point]').value;

    var IMP = window.IMP;
    IMP.request_pay(
      {
        pg: "inicis",
        pay_method: "card",
        merchant_uid: "merchant_test_" + new Date(),
        name: "결제테스트",
        amount: 100,
        buyer_email: "Iamport@chai.finance",
        buyer_name: "구매자",
        buyer_tel: "010-1234-5678",
        m_redirect_url: "10.0.2.2:8080/orders/" + ordersId
                        + "/payment/validation?"
                        + "couponListId=" + couponListId
                        + "&couponPrice=" + couponPrice
                        + "&pointPrice=" + pointPrice
          // ... (이하 코드 생략)
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
