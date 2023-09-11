window.addEventListener('load', function(){

    // 일반 세탁, 빠른세탁 선택시 border 변경 로직
    let options = document.querySelectorAll('.option')[0];
    let labels = document.querySelectorAll('.option label');
    options.addEventListener('click', function(event) {

        if (event.target.tagName === 'INPUT' && event.target.type === 'radio') {
            labels.forEach(label => {
                label.classList.remove('select1');
            });

            let addLabel = document.querySelector(`label[for="${event.target.id}"]`);
            addLabel.classList.add('select1');
        }
    });

    let checkboxs = document.querySelectorAll('.optionbox input[type=checkbox]');
    let boxs = document.querySelectorAll('.optionbox');

    boxs.forEach(button => {
        button.addEventListener('click', function(event) {

            let inputNode = button.querySelector('input[type="checkbox"]');
            if (inputNode) {
                inputNode.click();
            }

        });
    });

    let drycleaning = document.querySelector('#dryBtn');
    let common = document.querySelector('#commonBtn');
    let repair = document.querySelector('#repairBtn');

    drycleaning.addEventListener('click', function(){
        var options = 'width=600, height=400, top=100, left=100, resizable=yes, scrollbars=yes';
        window.open('/laundry/dry', '_blank');
    })

    common.addEventListener('click', function(){

    })
    repair.addEventListener('click', function(){
        var options = 'width=600, height=400, top=100, left=100, resizable=yes, scrollbars=yes';
        window.open('/laundry/repair', '_blank')
    })
})