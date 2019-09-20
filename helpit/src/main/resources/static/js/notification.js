// var notifications = [];

$(function () {
    $('body').css('min-height',$(window).height());
    $('#notification_bar').height($(window).height()-$('.navbar').innerHeight());
    $('#notification_bar').css('top',$('.navbar').innerHeight());
    $('#bell_nav_a').click(function (e) {
        e.stopPropagation();
        if($('#notification_bar').css('display') == 'none'){
            $('#bell_nav_li').addClass('active');
            $('#notification_bar').show();
        }else{
            $('#bell_nav_li').removeClass('active');
            $('#notification_bar').hide();
        }
    });
    $('body').click(function () {
        $('#bell_nav_li').removeClass('active');
        $('#notification_bar').hide();
    });
    $('#notification_bar').click(function (e) {
        e.stopPropagation();
    });

    var notifications = [];
    if($.session.get('notification')!=null){
        notifications = JSON.parse($.session.get('notification'));
        // $('#bell_nav_li').attr("data-original-title",notifications[0][0]);
    }
    for(var i = 0;i<notifications.length;i++){
        $('#notification_bar').append("<div class=\"notification_div\">\n" +
            "        <p>"+notifications[i][0]+"<small class=\"text-muted\" style=\"float: right;\">"+notifications[i][1]+"</small></p>\n" +
            "    </div> <hr>");
    }
});

function addNotification(message,otime,bell_title) {
    $('#notification_bar').prepend("<div class=\"notification_div\">\n" +
        "        <p>"+message+"<small class=\"text-muted\" style=\"float: right;\">"+otime+"</small></p>\n" +
        "    </div> <hr>");
    $('#bell_nav_li').attr("data-original-title",bell_title);
    $('#bell_nav_li').tooltip('show');

    if($.session.get('notification')==null){
        $.session.set('notification',JSON.stringify([[message,otime]]));
    }else{
        var notifications = JSON.parse($.session.get('notification'));
        notifications.unshift([message,otime]);
        $.session.set('notification',JSON.stringify(notifications));
    }

    setTimeout(
        function () {
            $('#bell_nav_li').attr("data-original-title",'');
            $('#bell_nav_li').tooltip('hide');
        },2500
    );
}