var sources = [];
var accounts = {};

$(function () {
    // $('body').loading({
    //     overlay: $('#custom-overlay')
    // });

    $.ajax({
        method: "POST",
        url: "/user/getAccounts",
        data: { upn: upn },
        async:true,
        dataType:"json"
    }).done(function (data) {
        // sources = data;
        var tmp = '';
        for(var i = 0;i<data.length;i++){
            sources[i] = data[i]['source'];
            var accountId = '';
            if(data[i]['account']!=null)
                accountId = data[i]['account'];
            accounts[ data[i]['source']] = data[i]['account'];
            tmp+='<div class="form-group row">\n' +
                '        <label class="col-sm-3 col-form-label">'+data[i]['source']+'</label>\n' +
                '        <div class="col-sm-6" style="display: inline-block;">\n' +
                '            <input type="number" class="form-control accountInput" name="'+data[i]['source']+'" value="'+accountId+'" readonly="readonly" onfocus="input_focus(this)">\n' +
                '        </div><span style="color:red;display: none;" id="'+data[i]['source']+'_error">wrong number</span>' +
                '    </div>';
        }
        $('#account_div').html(tmp);
        // $('body').loading('stop');

        $.ajax({
            method: "POST",
            url: "/user/getSuggestedAccounts",
            data: { upn: upn},
            async:true,
            dataType:"json"
        }).done(function (data) {

            for(var i = 0;i<data.length;i++){
                var source = data[i]['source'];
                $('input[name="'+source+'"]').attr('list',source+'_datalist');

                if($('#'+source+'_datalist').length<1){
                    $('input[name="'+source+'"]').after('<datalist id="'+source+'_datalist"><option value="'
                        +data[i]['account']+'">'+data[i]['display_name']+'</option></datalist>');
                }else{
                    $('#'+source+'_datalist').append('<option value="'+data[i]['account']+'">'+data[i]['display_name']+'</option>');
                }
            }
        });
    });

    $.ajax({
        method: "POST",
        url: "/user/getSendEmail",
        data: { upn: upn},
        async:true
    }).done(function (data) {
        if(data){
            $('#emailChoice_input').prop("checked", true);
        }
    });

    changeSendEmail();
});

function edit() {
    for(var i = 0;i<sources.length;i++){
        $('input[name="'+sources[i]+'"]').attr("placeholder","enter you "+sources[i]+" account id");
        $('input[name="'+sources[i]+'"]').removeAttr("readonly");
    }
    $('#account_save_button').show();
    $('#account_cancel').show();
}

function save() {
    // var otime = new Date();
    // var timestamp = new Date().getTime();
    // var content = "<div class=\"toast\" role=\"alert\" aria-live=\"assertive\" aria-atomic=\"true\" data-delay=\"10000\" id='"+timestamp+"'>\n" +
    //     "            <div class=\"toast-header\">\n" +
    //     "                <strong class=\"mr-auto\">helpit</strong>\n" +
    //     "<!--                <small class=\"text-muted\">just now</small>-->\n" +
    //     "                <button type=\"button\" class=\"ml-2 mb-1 close\" data-dismiss=\"toast\" aria-label=\"Close\">\n" +
    //     "                    <span aria-hidden=\"true\">&times;</span>\n" +
    //     "                </button>\n" +
    //     "            </div>\n" +
    //     "            <div class=\"toast-body\">\n";

    // $('body').loading({
    //     overlay: $('#custom-overlay')
    // });

    if(check()){
        saveData();
    }else{
        addNotification("Save accounts failed,please check accounts.",new Date().toLocaleString(),"Save accounts failed.");
        // $('#notification_bar').append("<div class=\"notification_div\">\n" +
        //     "        <p>Save accounts failed,please check accounts.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
        //     "    </div> <hr>");
        // $('#bell_nav_li').attr("data-original-title","Save accounts failed.");
        // $('#bell_nav_li').tooltip('show');
        // $("#notifications").append(content+
        //     "                Save failed,please check accounts\n" +
        //     "            </div>\n" +
        //     "        </div>");
    }

    // $('body').loading('stop');
    // $('#'+timestamp).toast('show');
}

function saveData() {
    var isBlank = true;
    var accounts = [];
    for(var i = 0;i<sources.length;i++){
        var id = $('input[name="'+sources[i]+'"]').val();
        accounts[i] = {upn:upn,name:username,source:sources[i],account:id};
        if(id.trim().length>0){
            isBlank = false;
        }
    }
    var message = '';
    var otime = new Date().toLocaleString();
    var bell_title = '';

    $.ajax({
        method: "POST",
        url: "/user/modifyAccounts",
        data: JSON.stringify(accounts),
        async:false,
        contentType:"application/json",
        dataType:"json"
    }).done(function (data) {
        if(data){
            message = "Save accounts successfully.";
            bell_title = "Save accounts successfully.";
            // $('#notification_bar').append("<div class=\"notification_div\">\n" +
            //     "        <p>Save accounts successfully.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
            //     "    </div> <hr>");
            // $('#bell_nav_li').attr("data-original-title","Save accounts successfully.");
            // $('#bell_nav_li').tooltip('show');
            // $("#notifications").append(content+
            //     "                Save successfully.\n" +
            //     "            </div>\n" +
            //     "        </div>");

            for(var i = 0;i<sources.length;i++){
                $('input[name="'+sources[i]+'"]').removeAttr("placeholder");
                $('input[name="'+sources[i]+'"]').attr("readonly","readonly");
            }
            $('#account_save_button').hide();
            $('#account_cancel').hide();

            if(!isBlank){
                $('#account_nav').removeClass('red-point');
                $('#account_nav').removeAttr('title');
                $.session.set('checkAccount',false);
            }else{
                $('#account_nav').addClass('red-point');
                $('#account_nav').attr('title','edit accounts');
                $.session.set('checkAccount',true);
            }
        }else{
            message = "Save accounts failed,please check accounts.";
            bell_title = "Save accounts failed.";
            // $('#notification_bar').append("<div class=\"notification_div\">\n" +
            //     "        <p>Save accounts failed,please check accounts.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
            //     "    </div> <hr>");
            // $('#bell_nav_li').attr("data-original-title","Save accounts failed.");
            // $('#bell_nav_li').tooltip('show');
            // $("#notifications").append(content+
            //     "                Save failed,please check accounts.\n" +
            //     "            </div>\n" +
            //     "        </div>");
        }
        addNotification(message,otime,bell_title);
    });
}

function check() {
    var isOk = true;
    for(var i = 0;i<sources.length;i++){
        var id_str = $('input[name="'+sources[i]+'"]').val();
        if(id_str==null||id_str.length==0)
            continue;
        for(var j = 0;j<id_str.length;j++){
            if(id_str[j]<'0'||id_str[j]>'9'){
                isOk=false;
                $('#'+sources[i]+'_error').show();
            }
        }
        if(isOk){
            var id = parseInt(id_str);
            $.ajax({
                method: "POST",
                url: "/user/checkAccount",
                data: { id: id,source:sources[i] },
                async:false,
                dataType:"json"
            }).done(function (data) {
                if(!data){
                    isOk=false;
                    $('#'+sources[i]+'_error').show();
                }
            });
        }
    }
    return isOk;
}

function cancelEdit() {
    $('.accountInput').each(function () {
        var stmp = $(this).attr('name');
        $(this).val(accounts[stmp]);
        $(this).removeAttr("placeholder");
        $(this).attr("readonly","readonly");
    });

    $('#account_save_button').hide();
    $('#account_cancel').hide();
}

function input_focus(e) {

    var id = $(e).attr("name")+'_error';
    $('#'+id).hide();
}

function changeSendEmail() {
    $('#emailChoice_input').change(function () {
        var sendEmail = this.checked;
        $.ajax({
            method: "POST",
            url: "/user/changeSendEmail",
            data: { upn: upn,sendEmail:sendEmail },
            async:false
        }).done(function (data) {
            var message = '';
            var otime = new Date().toLocaleString();
            // var bell_title = '';
            if(sendEmail){
                message = 'Set to allow email notifications successfully.';
            }else{
                message = 'Turn off email notifications successfully.';
            }
            addNotification(message,otime,message);
        });
    });
}