/**
 * 
 */


function submitInfoData() {
    var proxy = "report/submitExtractCode";
    var reportNum = $("#extracteCode").val();
    var ownerName = $("#ownerName").val();
    var dutyTel = $("#dutyTel").val();
    console.info(reportNum);
    console.info(dutyPerson);
    console.info(dutyTel);
    var params = {
         'reportNum' : reportNum,
         'ownerName': ownerName,
         'dutyTel' : dutyTel
    }
    $.getJSON(proxy, params, function(result){
        if(result == null || 200 != result.code) {
            alert("输入信息有误！");
            self.location = 'getReportPage';
        } else {
            //sessionStorage.setItem('verifyToken', result.verifyToken);
            window.location.href ="showDetailReportPage";
        }
    })
}
