function confirm(){
    if(!joinform.email.value){
        alert("이메일을 입력하세요");
        return false;
    }
    if(!joinform.password.value){
        alert("비밀번호를 입력하세요");
        return false;
    }
    if(!joinform.name.value){
        alert("닉네임을 입력하세요");
        return false;
    }
    if(joinform.isConfirmed.value != "1"){
        alert("이메일 인증이 완료되지 않았습니다.");
        return false;
    }
}
function unConfirm(){
    joinform.isConfirmed.value = "0";
}
function checkEmail(){
    if(!joinform.email.value){
        alert("이메일을 입력하세요");
        return;
    }
    const url = "/users/mailVerify?email="+joinform.email.value;
    window.open(url, "confirm", "menubar=no, scrollbar=no, status=no, width=400, height=300");
}
function verifySuccess(){
    opener.document.joinform.isConfirmed.value = "1";
    self.close();
}