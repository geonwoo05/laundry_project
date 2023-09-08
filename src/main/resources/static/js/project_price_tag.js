window.addEventListener('load',function(){

    let drycleaningBtn = document.querySelector('.drycleaning');
    let repairBtn = document.querySelector('.repair');
    let drycleaning = document.querySelector('#drycleaning');
    let repair = document.querySelector('#repair');

    drycleaningBtn.addEventListener('click', function() {
        drycleaning.classList.remove('hidden');
        drycleaningBtn.classList.add('select');
        repair.classList.add('hidden');
        repairBtn.classList.remove('select');

    })

    repairBtn.addEventListener('click', function() {
        repair.classList.remove('hidden');
        repairBtn.classList.add('select');
        drycleaning.classList.add('hidden');
        drycleaningBtn.classList.remove('select');
    })

})