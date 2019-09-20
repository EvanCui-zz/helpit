
$(function () {
    var checkAccount = $.session.get('checkAccount');

    if(checkAccount==null){
        $.ajax({
            method: "POST",
            url: "/user/isBlank",
            data: { upn: upn},
            async:true,
            dataType:"json"
        }).done(function (data) {
            // var isBlank = true;
            // for(var i = 0;i<data.length;i++){
            //     if(data[i]['account']!=null){
            //         isBlank = false;
            //         break;
            //     }
            // }
            checkAccount = data;
            $.session.set('checkAccount',checkAccount);
        });
    }else{
        checkAccount = JSON.parse(checkAccount);
    }

    if(checkAccount){
        $('#account_nav').addClass('red-point');
        $('#account_nav').attr('title','link accounts');
    }else{
        $('#account_nav').removeClass('red-point');
        $('#account_nav').removeAttr('title');
    }

    $.ajax({
        method: "POST",
        url: "/invitation/getUnreadNum",
        data: { upn: upn},
        async:true
    }).done(function (data) {
        if(data>0){
            $('#invite_num_badge').text(data);
            $('#invite_num_badge').show();
        }else{
            $('#invite_num_badge').hide();
        }
    });
});