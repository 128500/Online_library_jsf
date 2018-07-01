function checkValue(form, message_name, message_password) {

    var user_name = form[form.id + ":user_name"];
    var user_password = form[form.id + ":password"];

    if (user_name.value == '' && user_password.value == '') {
        alert(message_name + "\n" + message_password);
        user_name.style.backgroundColor = "#FFBCA0";
        user_name.focus();
        user_password.style.backgroundColor = "#FFBCA0";
        return false;
    }
    else {
        user_name.style.backgroundColor = "#fff";
        user_password.style.backgroundColor = "#fff";
    }

    if (user_name.value == '') {
        alert(message_name);
        user_name.style.backgroundColor = "#FFBCA0";
        user_name.focus();
        return false;
    }
    else{
        user_name.style.backgroundColor = "#fff";
    }

    if (user_password.value == '') {
        alert(message_password);
        user_password.style.backgroundColor = "#FFBCA0";
        user_password.focus();
        return false;
    }
    else{
        user_password.style.backgroundColor = "#fff";
    }

    return true;
}
