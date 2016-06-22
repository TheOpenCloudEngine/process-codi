var org_uengine_codi_mw3_subscription_Subscription = function(objectId, className){
    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[this.objectId];

    this.drawSubscription();
    this.drawSettings();
};

org_uengine_codi_mw3_subscription_Subscription.prototype = {
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
        $('#subscription_info').remove();
        $('#objDiv_' + this.objectId).parent().prev().prev().remove();
        $('#objDiv_' + this.objectId).css('marginLeft', '50px');
    }
}
