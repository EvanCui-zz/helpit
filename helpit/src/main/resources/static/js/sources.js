$(function () {
    $.ajax({
        method: "POST",
        url: "/source/getAll",
        async: true,
        dataType: "json"
    }).done(function (data) {
        var content = '';
        for (var i = 0; i < data.length; i++) {
            content += '<div style="margin-top: 10px;display: inline-block;width: 24%;"><input onMouseOver="$(this).tooltip(\'show\')" data-toggle="tooltip" data-placement="left" title="click to subscribe" name="source_checkbox" type="checkbox" value="'
                + data[i] + '"/><span style="margin-left: 5px;word-break:break-all;">' + data[i] + '</span></div>';
        }
        $('#all_sources_group').html(content);
        // $('[data-toggle="tooltip"]').tooltip();

        $.ajax({
            method: "POST",
            url: "/source/getSubscribed",
            data: {upn: upn},
            async: false
        }).done(function (data) {
            for (var i = 0; i < data.length; i++) {
                $('input[value="' + data[i] + '"]').attr('checked', true);
                $('input[value="' + data[i] + '"]').attr("data-original-title","click to unsubscribe");
            }
            changeSubscribe();
        });
    });

});

function changeSubscribe() {
    $('input[name="source_checkbox"]').change(function () {
        var source = $(this).val();
        var message = '';
        var otime = new Date().toLocaleString();
        var bell_title = '';
        // var timestamp = new Date().getTime();
        // var content = "<div class=\"toast\" role=\"alert\" aria-live=\"assertive\" aria-atomic=\"true\" data-delay=\"10000\" id='" + timestamp + "'>\n" +
        //     "            <div class=\"toast-header\">\n" +
        //     "                <strong class=\"mr-auto\">helpit</strong>\n" +
        //     "<!--                <small class=\"text-muted\">just now</small>-->\n" +
        //     "                <button type=\"button\" class=\"ml-2 mb-1 close\" data-dismiss=\"toast\" aria-label=\"Close\">\n" +
        //     "                    <span aria-hidden=\"true\">&times;</span>\n" +
        //     "                </button>\n" +
        //     "            </div>\n" +
        //     "            <div class=\"toast-body\">\n";

        if (this.checked) {
            $.ajax({
                method: "POST",
                url: "/source/subscribe",
                data: {upn: upn, source: source},
                async: false
            }).done(function (data) {
                if (data == 'success') {
                    message = "Subscribe source " + source + " successfully.";
                    bell_title = "Subscribe source " + source + " successfully.";
                    // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                    //     "        <p>Subscribe source "+source+" successfully.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                    //     "    </div> <hr>");
                    // $('#bell_nav_li').attr("data-original-title","Subscribe source "+source+" successfully.");
                    // $('#bell_nav_li').tooltip('show');
                    // $("#notifications").append(content +
                    //     "                Subscribe successfully.\n" +
                    //     "            </div>\n" +
                    //     "        </div>");
                    $("input[value='" + source + "']").attr("checked", true);
                    $('input[value="' + source + '"]').attr("data-original-title","click to unsubscribe");
                    $('input[value="' + source + '"]').tooltip('hide');
                } else {
                    message = "Subscribe source " + source + " failed,please try again.";
                    bell_title = "Subscribe source " + source + " failed.";
                    // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                    //     "        <p>Subscribe source "+source+" failed,please try again.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                    //     "    </div> <hr>");
                    // $('#bell_nav_li').attr("data-original-title","Subscribe source "+source+" failed.");
                    // $('#bell_nav_li').tooltip('show');
                    // $("#notifications").append(content +
                    //     "                Subscribe failed,please try again.\n" +
                    //     "            </div>\n" +
                    //     "        </div>");
                }
            });
        } else {
            $.ajax({
                method: "POST",
                url: "/source/unsubscribe",
                data: {upn: upn, source: source},
                async: false
            }).done(function (data) {
                if (data == 'success') {
                    message = "Unsubscribe source " + source + " successfully.";
                    bell_title = "Unsubscribe source " + source + " successfully.";
                    // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                    //     "        <p>Unsubscribe source "+source+" successfully.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                    //     "    </div> <hr>");
                    // $('#bell_nav_li').attr("data-original-title","Unsubscribe source "+source+" successfully.");
                    // $('#bell_nav_li').tooltip('show');
                    // $("#notifications").append(content +
                    //     "                Unsubscribe successfully.\n" +
                    //     "            </div>\n" +
                    //     "        </div>");
                    $("input[value='" + source + "']").removeAttr("checked");
                    $('input[value="' + source + '"]').attr("data-original-title","click to subscribe");
                    $('input[value="' + source + '"]').tooltip('hide');
                } else {
                    message = "Unsubscribe source " + source + " failed,please try again.";
                    bell_title = "Unsubscribe source " + source + " failed.";
                    // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                    //     "        <p>Unsubscribe source "+source+" failed,please try again.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                    //     "    </div> <hr>");
                    // $('#bell_nav_li').attr("data-original-title","Unsubscribe source "+source+" failed.");
                    // $('#bell_nav_li').tooltip('show');
                    // $("#notifications").append(content +
                    //     "                Unsubscribe failed,please try again.\n" +
                    //     "            </div>\n" +
                    //     "        </div>");
                }
            });
        }
        addNotification(message, otime, bell_title);
        // $('#' + timestamp).toast('show');
    });
}

function addSourceInput() {
    $('#add_sources_group').append('<input type="text" class="form-control" style="width:70%;margin-top: 20px;">');
}