
function check() {
  const password = document.querySelector('input[name=password]');
  const confirm = document.querySelector('input[name=confirmpassword]');
  var letter=document.getElementById('password_field');
  if(password.value.length<6 || password.value.length>12){

	document.getElementById('passwordmessage').innerHTML="password must contain 6-12 characters";
	letter.classList.add("is-invalid");
	document.getElementById('submit').disabled = true;
	document.getElementById('confirmpasswordmessage').innerHTML="";
	console.log("password length is not right");
 }
 else{
	letter.classList.remove("is-invalid");
	document.getElementById('passwordmessage').innerHTML="";
	if (confirm.value === password.value) {
    document.getElementById('submit').disabled = false;
    var letter=document.getElementById('confirm_password_field');
    letter.classList.remove("is-invalid");
    console.log("password and confirm password match");
    document.getElementById('confirmpasswordmessage').innerHTML="";

  } else {
    document.getElementById('submit').disabled = true;
    var letter=document.getElementById('confirm_password_field');
    console.log("password and confirm password do not match");
    letter.classList.add("is-invalid");
    document.getElementById('confirmpasswordmessage').innerHTML="does not match with password";

  }
}

}

