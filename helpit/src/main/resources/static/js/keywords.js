$(function () {
    // $('body').loading({
    //     overlay: $('#custom-overlay')
    // });
    $.when(
        $.ajax({
            method: "POST",
            url: "/keyword/getHotByNum",
            data:{num:16},
            async:true,
            dataType:"json"
        }).done(function (data) {
            $('#hot_keywords_group').html(generateGroupContent(data));
        }),
        $.ajax({
            method: "POST",
            url: "/keyword/getMyHot",
            async:true,
            data: {upn: upn},
            dataType:"json"
        }).done(function (data) {
            $('#my_often_keywords_group').html(generateGroupContent(data));
        }),
        $.ajax({
            method: "POST",
            url: "/keyword/getMyRecent",
            async:true,
            data: {upn: upn},
            dataType:"json"
        }).done(function (data) {
            $('#my_recent_keywords_group').html(generateGroupContent(data));
        })
    ).done(function (a,b,c) {
        searchByKeyword();
    });

    // $.ajax({
    //     method: "POST",
    //     url: "/keyword/getMyHistory",
    //     async:false,
    //     data: {upn: upn},
    //     dataType:"json"
    // }).done(function (data) {
    //     var content = '';
    //     for(var i = 0;i<data.length;i++){
    //         content+='<div style="margin-top: 10px;"><span style="display:inline-block;width:30%;">'+data[i]['keyword']+'</span><span style="display:inline-block;">'+data[i]['time']+'</span></div>';
    //     }
    //     $('#history_keywords_group').html(content);
    //     // $('body').loading('stop');
    // });
});

function generateGroupContent(data) {
    var content = '';
    for(var i = 0;i<data.length;i++){
        content+='<div onMouseOver="$(this).tooltip(\'show\')" data-toggle="tooltip" data-placement="left" title="click to search" class="keyword_div"><a href="#">'+data[i]['keyword']+'</a></div>';
    }
    return content;
}

function searchByKeyword() {
    $('.keyword_div').click(function () {
        var keyword = $(this).find('a').eq(0).html();
        var url = window.location.href;
        var tmp = url.split('//');
        var redirectUrl = tmp[0]+'//';
        var arr = tmp[1].split('/');
        redirectUrl+=arr[0]+'/all?keyword='+encodeURIComponent(keyword);
        window.location.href=redirectUrl;
    });
}

function showHistory() {
    if($('#history_keywords_group').css('display') == 'none'){
        $('#history_keywords_group').show();
        $('#browser_i').removeClass('fa-angle-double-down');
        $('#browser_i').addClass('fa-angle-double-up');
        // $('#browser_h').html('Hide History<span style="margin-left: 10px;">\n' +
        //     '        <i class="fas fa-angle-double-up" style="font-size: 16px;"></i></span>');
        $("html, body").animate({scrollTop:$('#history_keywords_group').offset().top}, 300);
    } else {
        $('#history_keywords_group').hide();
        $('#browser_i').removeClass('fa-angle-double-up');
        $('#browser_i').addClass('fa-angle-double-down');
        // $('#browser_h').html('Show My Search History<span style="margin-left: 10px;">\n' +
        //     '        <i class="fas fa-angle-double-down" style="font-size: 16px;"></i></span>');
    }
}