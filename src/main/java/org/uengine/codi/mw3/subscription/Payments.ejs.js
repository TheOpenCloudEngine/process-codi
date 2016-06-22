var org_uengine_codi_mw3_subscription_Payments = function(objectId, className){

    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[this.objectId];

    this.drawPayments();
    this.drawSettings();

};

org_uengine_codi_mw3_subscription_Payments.prototype = {
    drawPayments: function() {
        console.log(this.object.paymentsInfo);
    },

    drawSettings: function() {
    }
}