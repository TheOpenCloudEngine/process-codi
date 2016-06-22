var org_uengine_codi_mw3_subscription_Invoice = function(objectId, className){

    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[this.objectId];

    var jsonObjet = JSON.parse(this.object.invoiceList);

    $('#invoice').dataTable({
        "aaData": jsonObjet,
        searching:false,
        bPaginate: false,
        "columnDefs": [ {
            "targets": 3,
            "data": null,
            "render": function ( data, type, full, meta ) {
                var html =  '<ul>';
                    html += '<li><span class="hide" title="11"></span>';
                    html += 'Amount: $'+ full.amount + '('+ full.currency +')</li>';
                    html += '<li>Balance: $'+ full.balance + '('+ full.currency +')</li>';
                    html += '</ul>';
                return html;
            }
        } ],
        "aoColumns": [
            { "mDataProp": "invoiceNumber" },
            { "mDataProp": "invoiceId" },
            { "mDataProp": "invoiceDate" },
            { "mDataProp": "amount" }
        ]
    });

    $('#invoice').width('650');
    $('#invoice_info').hide();
}
