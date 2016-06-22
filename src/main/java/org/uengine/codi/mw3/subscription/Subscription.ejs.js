var org_uengine_codi_mw3_subscription_Subscription = function(objectId, className){
    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[this.objectId];

    this.drawInformation();
    this.drawSubscription();
    this.drawSettings();
};

org_uengine_codi_mw3_subscription_Subscription.prototype = {
    drawInformation: function() {
        var subscriptionInfoDataSet = [
            [
                'One Time (Install free)',
                '0',
                '0',
                '0'
            ],
            [
                'Recurring (Per month)',
                '0',
                '$10',
                '$10'
            ],
            [
                'Usage-based',
                'N/A',
                'N/A',
                'Default 30GB per team, Overage: $10 for 1GB / month'
            ],
            [
                'Services',
                'Practice / Method Authoring(Workspace - up to 1 project instances per a month, up to 5 participants limited.)',
                'including Free Services(Workspace - up to 5 project instances per a month, up to 15 participants per project instance.)',
                'including Basic Services(Workspace - unlimited instances and participants, Free File attachment)'
            ]
        ];

        var subscriptionInfoTable = $('#subscriptionInfo').DataTable({
            dom: 'ftipr',
            data: subscriptionInfoDataSet,
            bPaginate: false,
            columnDefs: [ {
                targets: 0,
                orderable: false
            }]
        });
    },

    drawSubscription: function() {
        var jsonObject = JSON.parse(this.object.subscriptInfo);
        var subscriptionDataSet = [
            [
                jsonObject.productCategory,
                jsonObject.productName + "(" + jsonObject.billingPeriod.toLowerCase() +")",
                jsonObject.startDate,
                jsonObject.chargedThroughDate
            ]
        ];

        var subscriptionTable = $('#subscription').DataTable({
            dom: 'ftipr',
            data: subscriptionDataSet,
            searching:false,
            bPaginate: false
        });
    },

    drawSettings: function() {
        $('#subscriptionInfo_info').remove();
        $('#subscription_info').remove();
        $('#objDiv_' + this.objectId).parent().prev().prev().remove();
        $('#objDiv_' + this.objectId).css('marginLeft', '50px');
    }
}
