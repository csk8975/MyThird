/**
 * 
 */


function submitNewPass() {
    var proxy = "submitNewPass";
    var oldPass = $("#oldPass").val();
    var newPass1 = $("#newPass1").val();
    var newPass2 = $("#newPass2").val();
    var params = {
            'oldPass' : md5(oldPass),
            'newPass1': md5(newPass1),
            'newPass2' : md5(newPass2)
       }
    if(newPass1 == newPass2){
        $.getJSON(proxy, params, function(result){
        });
    }


}
