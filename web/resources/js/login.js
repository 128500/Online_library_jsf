function checkValue(form, message){
    
    var inputValue = form[form.id + ":user_name"];
    
    if(inputValue.value === ''){
        alert(message);
        inputValue.focus();
        return false;
    }
    
    return true;
}