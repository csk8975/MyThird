/**
 * 
 */

function watermark(settings) {

    var defaultSettings = {
        watermark_txt : "text",
        watermark_x : 20,
        watermark_y : 20,
        watermark_rows : 0,
        watermark_cols : 20,
        watermark_x_space : 20,
        watermark_y_space : 15,
        watermark_color : '#508ef0',
        watermark_alpha : 0.3,
        watermark_fontsize : '18px',
        watermark_font : '微软雅黑',
        watermark_width : 120,
        watermark_height : 80,
        watermark_angle : 15
    };

    if (arguments.length === 1 && typeof arguments[0] === "object") {
        var src = arguments[0] || {};
        for (key in src) {
            if (src[key] && defaultSettings[key]
                    && src[key] === defaultSettings[key])
                continue;
            else if (src[key]) {
                defaultSettings[key] = src[key];
            }
        }
    }

    var oTemp = document.createDocumentFragment();
    var page_width = Math.max(document.body.scrollWidth,
            document.body.clientWidth);
    var page_height = Math.max(document.body.scrollHeight,
            document.body.clientHeight);

    if (defaultSettings.watermark_cols == 0
            || (parseInt(defaultSettings.watermark_x
                    + defaultSettings.watermark_width
                    * defaultSettings.watermark_cols
                    + defaultSettings.watermark_x_space
                    * (defaultSettings.watermark_cols - 1)) > page_width)) {
        defaultSettings.watermark_cols = parseInt((page_width
                - defaultSettings.watermark_x + defaultSettings.watermark_x_space)
                / (defaultSettings.watermark_width + defaultSettings.watermark_x_space));
        defaultSettings.watermark_x_space = parseInt((page_width
                - defaultSettings.watermark_x - defaultSettings.watermark_width
                * defaultSettings.watermark_cols)
                / (defaultSettings.watermark_cols - 1));
    }

    if (defaultSettings.watermark_rows == 0
            || (parseInt(defaultSettings.watermark_y
                    + defaultSettings.watermark_height
                    * defaultSettings.watermark_rows
                    + defaultSettings.watermark_y_space
                    * (defaultSettings.watermark_rows - 1)) > page_height)) {
        defaultSettings.watermark_rows = parseInt((defaultSettings.watermark_y_space
                + page_height - defaultSettings.watermark_y)
                / (defaultSettings.watermark_height + defaultSettings.watermark_y_space));
        defaultSettings.watermark_y_space = parseInt((page_height
                - defaultSettings.watermark_y - defaultSettings.watermark_height
                * defaultSettings.watermark_rows)
                / (defaultSettings.watermark_rows - 1));
    }
    var x;
    var y;
    for (var i = 0; i < defaultSettings.watermark_rows; i++) {
        y = defaultSettings.watermark_y
                + (defaultSettings.watermark_y_space + defaultSettings.watermark_height)
                * i;
        for (var j = 0; j < defaultSettings.watermark_cols; j++) {
            x = defaultSettings.watermark_x
                    + (defaultSettings.watermark_width + defaultSettings.watermark_x_space)
                    * j;
            var mask_div = document.createElement('div');
            mask_div.id = 'mask_div' + i + j;
            mask_div.appendChild(document
                    .createTextNode(defaultSettings.watermark_txt));
            mask_div.style.webkitTransform = "rotate(-"
                    + defaultSettings.watermark_angle + "deg)";
            mask_div.style.MozTransform = "rotate(-"
                    + defaultSettings.watermark_angle + "deg)";
            mask_div.style.msTransform = "rotate(-"
                    + defaultSettings.watermark_angle + "deg)";
            mask_div.style.OTransform = "rotate(-"
                    + defaultSettings.watermark_angle + "deg)";
            mask_div.style.transform = "rotate(-"
                    + defaultSettings.watermark_angle + "deg)";
            mask_div.style.visibility = "";
            mask_div.style.position = "absolute";
            mask_div.style.left = x + 'px';
            mask_div.style.top = y + 'px';
            mask_div.style.overflow = "hidden";
            mask_div.style.zIndex = "9999";
            mask_div.style.opacity = defaultSettings.watermark_alpha;
            mask_div.style.fontSize = defaultSettings.watermark_fontsize;
            mask_div.style.fontFamily = defaultSettings.watermark_font;
            mask_div.style.color = defaultSettings.watermark_color;
            mask_div.style.textAlign = "center";
            mask_div.style.width = defaultSettings.watermark_width + 'px';
            mask_div.style.height = defaultSettings.watermark_height + 'px';
            mask_div.style.display = "block";
            oTemp.appendChild(mask_div);
        }
        ;
    }
    ;
    document.body.appendChild(oTemp);
}

function showDetailReportInfo() {
    var proxy = "report/getDetailReportInfo";
    var verifyToken = sessionStorage.getItem('verifyToken');
    var params = {
        'verifyToken' : verifyToken
    }
    $.getJSON(proxy, params,
            function(result) {
                if (null == result || 200 != result.code) {
                    alert("无权限访问！！！");
                    self.location = '505';
                } else {
                    $('#reportNum').html('天消 ' + result.reportNum);
                    $('#reportLevel').html(result.reportLevel);
                    $('#reportDate').html(result.reportDate);
                    $('#company').html(result.company);
                    $('#reportConclusion').html(
                            "<p>" + result.reportConclusion + "</p>");
                    $('#rectifyComments').html(
                            "<p>" + result.rectifyComments + "</p>");
                    $('#disqualification').html(
                            "<p>" + result.disqualification + "</p>");
                    sessionStorage.setItem('watermark', result.dutyPerson
                            + "\t" + result.dutyTel);
                    watermark({
                        watermark_txt : result.dutyPerson + "\t"
                                + result.dutyTel
                    });
                }
            });
}
//showDetailReportInfo();

function showWaterMark() {
    var proxy = 'report/getWatermark';
    var params = {
        'verifyToken' : sessionStorage.getItem('verifyToken')
    };

    $.getJSON(proxy, params, function(result) {
        if (null == result || 200 != result.code) {
            return;
        } else {
            $('div[id^=mask_div]').remove();
            watermark({
                watermark_txt : result.watermark
            });
        }
    });
}

showWaterMark();

window.onresize = function() {
    $('div[id^=mask_div]').remove();
    showWaterMark();
};
