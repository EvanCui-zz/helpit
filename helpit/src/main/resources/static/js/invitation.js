$(function () {
    $.ajax({
        method: "POST",
        url: "/invitation/getUnreadReceived",
        data: {receiver:upn},
        async:true,
        dataType:"json"
    }).done(function (data) {
        $('#invitations_panel').html(getInvitationsHtml(data));
    });
});

function getInvitationsHtml(data) {
    var content = '';
    for(var i= 0;i<data.length;i++){
        content+='<div class="card" style="margin-top: 20px;">\n' +
            '            <div class="card-header">\n' +
            '                From <span style="margin-left: 7px;" class="sender_span">'+data[i]['sender']+'</span>\n' +
            '            </div>\n' +
            '            <div class="card-body">\n' +
            '                <blockquote class="blockquote mb-0">\n' +
            '                    <p>'+data[i]['title']+'</p>\n' +
            '                    <footer class="blockquote-footer">Click <a href="'+data[i]['web_url']
            +'" style="font-style: italic;" target="_blank" onclick="readInvitation(this)">here</a> to see more</footer>\n' +
            '                </blockquote>\n' +
            '            </div>\n' +
            '        </div>';
    }
    return content;
}

function readInvitation(e) {
    var web_url = $(e).attr('href');
    var sender = $(e).parent().parent().parent().parent().find('.sender_span').eq(0).text();
    console.log(sender);

    $.ajax({
        method: "POST",
        url: "/invitation/read",
        data: {sender:sender,receiver:upn,web_url:web_url},
        async:false
    }).done(function (data) {
        var num = parseInt($('#invite_num_badge').text())-1;
        if(num>0) {
            $('#invite_num_badge').text(num);
            $('#invite_num_badge').show();
        }else
            $('#invite_num_badge').hide();
        $(e).parent().parent().parent().parent().remove();
    });
}