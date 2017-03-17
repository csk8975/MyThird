/**
 * 
 */
$(function(){
    $('.wrapper').css('opacity','1');
    function userLogin() {
        var proxy = "userLogin";
        var params = {
            'loginName' : $('#loginName').val(),
            'userPassword' : md5($('#userPassword').val())
        }
        $.getJSON(proxy, params, function(result) {
            if(null == result || 200 != result.code) {
                self.location = 'loginPage';
                alert('账号密码错误');
                $('#loginName').val('');
                $('#userPassword').val('');
            } else {
                //sessionStorage.setItem('token', result.token);
                self.location = 'main';
            }
        });
    }
    
    function isLogin() {
        var token = sessionStorage.getItem('token');
        //判断当前是否已经登录
        if(null != token) {
            var proxy = "verifyToken";
            var params = {
                'token' : token
            }
            $.getJSON(proxy, params, function(result) {
                if(null != result && 200 == result.code) {
                    self.location="main";
                } 
            });
        }
    }
    
    //isLogin();
    
    $('#submitLogin').click(function(){
        userLogin();
    });
    

	document.onkeydown = function(e) {
		var ev = document.all ? window.event : e;
		if (ev.keyCode == 13) {
			userLogin();
		}
	}
});