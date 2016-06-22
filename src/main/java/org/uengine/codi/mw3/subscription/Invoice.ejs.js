var org_uengine_codi_mw3_subscription_Invoice = function(objectId, className){

    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[this.objectId];

    this.drawInvoice();
    this.drawSettings();
};

org_uengine_codi_mw3_subscription_Invoice.prototype = {
    drawInvoice: function() {
        var jsonObject = JSON.parse(this.object.invoiceList);

        $('#invoice').DataTable({
            aaData: jsonObject,
            bPaginate: false,
            columnDefs: [{
                targets: 3,
                data: null,
                render: function ( data, type, full, meta ) {
                    var html =  '<ul>';
                    html += '<li><span class="hide" title="11"></span>';
                    html += 'Amount: $'+ full.amount + '('+ full.currency +')</li>';
                    html += '<li>Balance: $'+ full.balance + '('+ full.currency +')</li>';
                    html += '</ul>';
                    return html;
                }
            }],
            aoColumns: [
                { "mDataProp": "invoiceNumber" },
                { "mDataProp": "invoiceId" },
                { "mDataProp": "invoiceDate" },
                { "mDataProp": "amount" }
            ]
        });
    },

    drawSettings: function() {
        $('#invoice_info').remove();
        $('#objDiv_' + this.objectId).parent().prev().prev().remove();
        $('#objDiv_' + this.objectId).css('marginLeft', '50px');
    }
}