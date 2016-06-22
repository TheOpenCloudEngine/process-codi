var org_uengine_codi_mw3_subscription_Subscription = function(objectId, className){

    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[this.objectId];

    $("#objDiv_"+this.objectId).find(".tbl_th").attr("style","background:#ddd;font-weight:bold;border:1px solid #ccc;border-left:0 none;display:table-cell;width:232px;height:30px;border-right:1px solid #ccc;border-bottom:1px solid #ccc;text-align: center; vertical-align: middle;");
    $("#objDiv_"+this.objectId).find(".tbl_td").attr("style","display:table-cell;width:232px;height:30px;border-right:1px solid #ccc;border-bottom:1px solid #ccc;text-align: center; vertical-align: middle;");
    $("#objDiv_"+this.objectId).find(".tbl_th").first().css("width","150px").css("border-left","1px solid #ccc");
    $("#objDiv_"+this.objectId).find(".tbl_td").first().css("width","150px").css("border-left","1px solid #ccc");

    var jsonObjet = JSON.parse(this.object.subscriptInfo);

    var dataSet = [
        [
            jsonObjet.productCategory,
            jsonObjet.productName + "(" + jsonObjet.billingPeriod.toLowerCase() +")",
            jsonObjet.startDate,
            jsonObjet.chargedThroughDate
        ]
    ];
    var table = $('#subscription').DataTable({
        dom: 'ftipr',
        data: dataSet,
        searching:false,
        bPaginate: false,
        columns: [
            { data: '0' },
            { data: '1' },
            { data: '2' },
            { data: '3' },
        ]
    });

    $('#subscription').width('650');
    $('#subscription_info').hide();
}
