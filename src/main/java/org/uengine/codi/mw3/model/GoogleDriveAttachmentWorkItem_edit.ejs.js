var org_uengine_codi_mw3_model_GoogleDriveAttachmentWorkItem_edit = function(objectId, className) {
    this.objectId = objectId;
    this.className = className;

    this.handleClientLoad();
}

org_uengine_codi_mw3_model_GoogleDriveAttachmentWorkItem_edit.prototype = {

    CLIENT_ID : '595090371317-ta38nu8jcdluqch5cfafne9iood01589.apps.googleusercontent.com',

    SCOPES : ['https://www.googleapis.com/auth/drive','https://www.googleapis.com/auth/drive.install','profile'],

    APP_ID : '595090371317',

    /**
     * Called when the client library is loaded to start the auth flow.
     */
    handleClientLoad : function() {
        window.setTimeout(this.checkAuth.bind(this), 1);
    },

    /**
     * Check if the current user has authorized the application.
     */
    checkAuth :function() {
        gapi.auth.authorize(
            {'client_id': this.CLIENT_ID, 'scope': this.SCOPES, 'immediate': true},
            this.handleAuthResult.bind(this));
    },

    /**
     * Called when authorization server replies.
     *
     * @param {Object} authResult Authorization result.
     */
    handleAuthResult : function(authResult) {
        var authButton = document.getElementById('authorizeButton_' + this.objectId);
        //var filePicker = document.getElementById('filePicker_' + this.objectId);
        var upload = document.getElementById('upload_' + this.objectId);
        var addFile = document.getElementById('addFile_' + this.objectId)
        authButton.style.display = 'none';
        upload.style.display = 'none';
        //filePicker.style.display = 'none';
        if (authResult && !authResult.error) {
            // Access token has been successfully retrieved, requests can be sent to the API.
            upload.style.display = 'block';
            addFile.onclick = this.uploadFile.bind(this);
            //filePicker.onchange = this.uploadFile.bind(this);
        } else {
            // No access token could be retrieved, show the button to start the authorization flow.
            authButton.style.display = 'block';
            authButton.onclick = function() {
                gapi.auth.authorize(
                    {'client_id': this.CLIENT_ID, 'scope': this.SCOPES, 'immediate': false},
                    this.handleAuthResult.bind(this));
            }.bind(this);
        }
    },

    /**
     * Start the file upload.
     *
     * @param {Object} evt Arguments from the file selector.
     */
    uploadFile : function(evt) {
        gapi.client.load('drive', 'v2', function() {
            var file = document.getElementById('filePicker_' + this.objectId).files[0]
            //var file = evt.target.files[0];
            this.insertFile(file);
        }.bind(this));
    },

    /**
     * Insert new file.
     *
     * @param {File} fileData File object to read data from.
     * @param {Function} callback Function to call when the request is complete.
     */
    insertFile : function(fileData, callback) {
        const boundary = '-------314159265358979323846';
        const delimiter = "\r\n--" + boundary + "\r\n";
        const close_delim = "\r\n--" + boundary + "--";

        var reader = new FileReader();
        reader.readAsBinaryString(fileData);
        reader.onload = function (e) {
            var contentType = fileData.type || 'application/octet-stream';
            var metadata = {
                'title': fileData.name,
                'mimeType': contentType
            };

            var base64Data = btoa(reader.result);
            var multipartRequestBody =
                delimiter +
                'Content-Type: application/json\r\n\r\n' +
                JSON.stringify(metadata) +
                delimiter +
                'Content-Type: ' + contentType + '\r\n' +
                'Content-Transfer-Encoding: base64\r\n' +
                '\r\n' +
                base64Data +
                close_delim;

            var request = gapi.client.request({
                'path': '/upload/drive/v2/files',
                'method': 'POST',
                'params': {'uploadType': 'multipart'},
                'headers': {
                    'Content-Type': 'multipart/mixed; boundary="' + boundary + '"'
                },
                'body': multipartRequestBody
            });
            if (!callback) {
                callback = function (file) {
                    debugger;
                    mw3.objects[this.objectId].title = file.title;
                    mw3.objects[this.objectId].ext1 = file.alternateLink;
                    mw3.objects[this.objectId].type = 'googleDoc';
                    mw3.objects[this.objectId].extFile = file.title;
                    mw3.objects[this.objectId].content = file.webContentLink;

                    mw3.call(this.objectId, 'add');

                    gapi.load('drive-share', function(){
                        s = new gapi.drive.share.ShareClient(this.APP_ID);
                        s.setItemIds([file.id]);
                        s.showSettingsDialog();
                    }.bind(this));

                    console.log(file)
                }.bind(this);
            }
            request.execute(callback);
        }.bind(this)
    }
}