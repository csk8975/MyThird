/**
 * 
 */
$(function(){
    function isLogin() {
        var token = sessionStorage.getItem('token');
        var userName = sessionStorage.getItem('userName');
        if(null == token) {
            self.location ="login";
        } else {
            var proxy = "verifyToken";
            var params = {
                'token' : token
            }
            $.getJSON(proxy, params, function(result) {
                if(null == result || 200 != result.code) {
                    self.location="505";
                } else if(1 != result.role){
                    //用户权限为1表示具备管查看该页面的权限
                    self.location="nopermissions";
                } else {
                    $('#userName').html(result.userName);
                    $('.wrapper').css('opacity','1');
                }
            });
        }
    }
    
    function signout() {
        sessionStorage.setItem('token', null);
        self.location="login";
    }
    
    $('#signoutButton').click(function(){
        $.getJSON('logout', null, function(result) {});
    });
    //isLogin();
});