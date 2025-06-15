

function checkPasswords(){
    const pass1 = document.getElementById('password').value;
    const pass2 = document.getElementById('confirm-password').value;
    const err= document.getElementById('err_confirmpassword');
    console.log("Password 1: " + pass1);
    console.log("Password 2: " + pass2);


    if (pass1 === pass2) {
            console.log("Passwords match.");
            err.innerHTML="";
            return true;
        } else {
            console.log("Passwords do not match.");
            err.innerHTML="Passwords do not match.";
            return false;
        }
}


function checkErrorMessage(){
    const companynamemessage=document.getElementById('companyname').value;
    const emailmessage=document.getElementById('email').value;
    const contactmessage=document.getElementById('contact').value;
    const addressmessage=document.getElementById('address').value;
    const descriptionmessage=document.getElementById('description').value;
    const pass1 = document.getElementById('password').value;
    const pass2 = document.getElementById('confirm-password').value;
    const err= document.getElementById('err_confirmpassword');

    if(companynamemessage.length>0){
        document.getElementById('companynameerrormessage').innerHTML="";
    }
    if(emailmessage.length>0){
       document.getElementById('emailerrormessage').innerHTML="";
    }
    if(contactmessage.length>0){
        document.getElementById('contacterrormessage').innerHTML="";
    }
    if(addressmessage.length>0){
        document.getElementById('addresserrormessage').innerHTML="";
    }
    if(descriptionmessage.length>0){
        document.getElementById('descriptionerrormessage').innerHTML="";
    }

    if(pass1.length>0){
            document.getElementById('passworderrormessage').innerHTML="";
    }

      if (pass1 === pass2) {
            console.log("Passwords match.");
            err.innerHTML="";
      } else{
             console.log("Passwords do not match.");
             err.innerHTML="Passwords do not match.";
      }

}

