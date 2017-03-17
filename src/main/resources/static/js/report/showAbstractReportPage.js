/**
 * 
 */

$(function() {
    function showAbstractReportPage() {
        var proxy = "report/getAbstractReportInfo";
        var reportNum = $(location).attr('href').split('?')[1].split('&')[0].split('=')[1];
        console.info(reportNum);
        var params = {
            'reportNum' : reportNum
        }
        $.getJSON(proxy, params, function(result) {
            if(null == result || 200 != result.code) {
                alert('您所查找的报告' + reportNum + '不存在');
                self.location = '505';
            } else {
                var date = new Date();
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                var day = date.getDate();
                $('.reportNum').html(result.reportNum);
                $('#detectDate').html(result.reportDate);
                $('#projectName').html(result.projectName);
                $('#checkbox_a' + result.riskLevel).attr("checked", true);
                $('#publishData').html(year + '-' + month + '-' + day);
            }
        });
    }
    showAbstractReportPage();
});