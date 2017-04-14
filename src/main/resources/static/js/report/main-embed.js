/**
 * 
 */

function deleteReportByReportNumEmbed(reportNum) {
        layer.confirm("确认删除报告："+reportNum+"?",{
            title : '删除检测报告：',
            area : '650px',
            btn : [ '删除', '取消' ],
            yes : function(index, layero) {
                //layer.msg("正在删除检测报告，请稍候...");
                $.get("deleteReportByReportNum?reportNum=" + reportNum, function(data,status) {
                    if(data.code == 200){
                        layer.msg("删除成功");
                    }
                    else{
                        layer.msg("删除失败：" + data.message);
                    }
                    setTimeout(function(){self.location = 'embeddedMain';},500);
                });
            },
            btn2 : function(index, layero) {
            },
            shadeClose : true,
        });
    }

$(function() {
    // show reportList
    function showReportList() {
        var proxy = "getReportList";
        /*
         * var params = { 'projectName' : null, 'reportNum' : null,
         * 'projectAddress' : null, 'riskLevel' : null, 'qaName' : null, 'token' :
         * sessionStorage.getItem('token') }
         */
        $
                .getJSON(
                        proxy,
                        function(result) {
                            if (null != result && 200 == result.code) {
                                var data = new Array();
                                var index = 0;
                                for ( var d in result.data) {
                                    var item = new Array()
                                    item[0] = result.data[d].reportNum; // 报告编码
                                    item[1] = result.data[d].projectName; // 报告名称
                                    item[2] = result.data[d].projectAddress; // 项目地址
                                    item[3] = result.data[d].riskLevel; // 风险等级
                                    item[4] = result.data[d].qaName; // 检测单位
                                    item[5] = result.data[d].contactTel; // 联系电话
                                    item[6] = '<div class="table-toolbar tc">'
                                            + '<a class="evaluateReport" target="_blank" href="showAbstractReportPage?reportNum='
                                            + result.data[d].reportNum
                                            + '">分析报告</a>;'
                                            + '<a class="detectionReport" target="_blank" href="fetchReport/'
                                            + result.data[d].reportNum
                                            + '">检测报告</a>;'
                                            + '<a class="deleteReport" " href="JavaScript: " onclick="deleteReportByReportNumEmbed(\''
                                            + result.data[d].reportNum
                                            + '\')">删除</a>' + '</div>'; //
                                    data[index++] = item;
                                }
                                $("#reportListTable").dataTable({
                                    "data" : data,
                                    language : {
                                        url : "css/datatables/Chinese.json"
                                    }
                                });
                            }
                        });
    }
    
    // 文件上传
    $(":file").filestyle({
        icon : false,
        buttonText : "选择文件"
    });

    // 导入检测报告数据
    $('#btn-import-check-report').on('click', function() {
        layer.open({
            type : 1,
            title : '导入检测报告',
            area : '650px',
            btn : [ '导入', '关闭' ],
            yes : function(index, layero) {
                layer.msg("正在导入检测报告，请稍候...");
                $("#import-dialog").submit();
            },
            btn2 : function(index, layero) {

            },
            shadeClose : true,
            content : $('#import-dialog')
        });
    });
    
    // 导入评分结果数据
    $('#btn-import-risk-level').on('click', function() {
        layer.open({
            type : 1,
            title : '导入评定结果',
            area : '650px',
            btn : [ '导入', '关闭' ],
            yes : function(index, layero) {
                layer.msg("正在导入评定结果，请稍候...");
                $("#import-risk-level-dialog").submit();
            },
            btn2 : function(index, layero) {

            },
            shadeClose : true,
            content : $('#import-risk-level-dialog')
        });
    });
    
    // 批量删除
    $('.btn-batchdelete').on('click', function() {
        layer.confirm('是否批量删除选中数据记录?', {
            icon : 0,
            title : '批量删除'
        }, {
            btn : [ '确认', '取消' ]
        });
    });
    // 提示信息
    // 删除
    $('.btn-delete').on('click', function() {
        layer.confirm('是否删除本条数据记录?', {
            icon : 0,
            title : '删除数据'
        }, {
            btn : [ '确认', '取消' ]
        });
    });
    // 提示信息
    $('.btn-export').on('click', function() {
        layer.confirm('是否导出评估报告?', {
            icon : 0,
            title : '导出报告'
        }, {
            btn : [ '确认', '取消' ]
        });
    });

    showReportList();

    $('.evaluateReport').click(function() {
        console.info($(this).attr("reportNum"));
    });

    $('.detectionReport').click(function() {
        console.info($(this).attr("reportNum"));
    });

    $('.deleteReport').click(function() {
        console.info($(this).attr("reportNum"));
    });
});