// 2013-08-15 add view mode for ace editor
ace.view = function(el){
    var editor = ace.edit(el);

    editor.setReadOnly(true);
    editor.renderer.setHighlightGutterLine(false);
    $(editor.renderer.$cursorLayer.element).remove();

    return editor;
};

var org_metaworks_example_ide_SourceCode = function(objectId, className){
    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[this.objectId];
    this.divName = "objDiv_" + objectId;
    this.objectDiv = $("#" + this.divName);

    if(this.object == null)
        return true;

    this.when = this.objectDiv.text();
    this.objectDiv.text('');

    this.objectDiv.css("width", "100%").css("height", '100%'); //.css("overflow", "hidden");
    this.objectDiv.addClass('mw3_editor').addClass('mw3_resize').attr('objectId', objectId);

    this.loadRequestAssist = false;
    this.lastCommandString = "";

    if(this.object.id && this.object.id.indexOf('.') > -1){
        this.language = this.object.id.substring(this.object.id.lastIndexOf('.')+1);

        if(this.language == 'js')
            this.language = 'javascript';

    }
};

org_metaworks_example_ide_SourceCode.prototype = {
    loaded : function(){
        var objectId = this.objectId;
        var language = this.language;

        if(language){
            var url = 'scripts/ace-1.1.0/build/src-min-noconflict/mode-' + language + '.js';

            $.ajax({
                async:false,
                url: url,
                type:'GET',
                error:function(xhr){
                    language = 'java';
                }
            });
        }else{
            language = 'java';
        }

        this.language = language;

        mw3.getFaceHelper(objectId).load();

        //mw3.importScript('scripts/ace-1.1.0/build/src-noconflict/ace.js', function(){

        //mw3.importScript('scripts/ace-1.1.0/src-min-noconflict/theme-eclipse.js', function(){
        //	mw3.importScript('scripts/ace-1.1.0/src-min-noconflict/ace/ext/static_highlight', function(){
        /*					var url = null;

         if(language){


         if(typeof mw3.loadedScripts[url] == 'undefined'){
         if(!mw3.importScript(url)){
         mw3.loadedScripts[url] = null;
         url = null;
         }

         }else if(mw3.loadedScripts[url] == null)
         url = null;
         }

         if(!url){
         mw3.getFaceHelper(objectId).language = 'java';
         url = 'scripts/ace-1.1.0/src-min-noconflict/mode-java.js';
         }

         mw3.importScript(url, function(){

         });*/
        //	});
        //});
        //});
    },
    load : function(){
        var objectId = this.objectId;
        var object = this.object;

        var pre = this.objectDiv.children('pre');

        if(this.when == mw3.WHEN_NEW || this.when == mw3.WHEN_EDIT)	// editable
            this.editor = ace.edit(this.divName);
        else														// readonly
            this.editor = ace.view(this.divName);

        this.editor.setTheme("ace/theme/textmate");
        this.editor.getSession().setMode("ace/mode/" + this.language);

        if(object.code)
            this.editor.getSession().setValue(object.code);

    },
    bindKey : function(win, mac) {
        return {
            win: win,
            mac: mac,
            sender: "editor"
        };
    },
    change : function(event){
        if(!this.loadRequestAssist){
            var objectId = this.objectId;

            if (this.timeout) {
                clearTimeout(this.timeout);
            }

            this.timeout = setTimeout(function() {
                mw3.getFaceHelper(objectId).assistEventCall();
            }, 100);
        }
    },
    startLoading : function(serviceMethodName){
        mw3.log('startLoading : ' + serviceMethodName);

        if('requestAssist' == serviceMethodName){
            this.loadRequestAssist = true;
        }
    },
    endLoading : function(serviceMethodName){
        mw3.log('endLoading : ' + serviceMethodName);

        if('requestAssist' == serviceMethodName){
            this.loadRequestAssist = false;

            var assist = mw3.getAutowiredObject('org.metaworks.example.ide.CodeAssist');

            if(assist != null){
                if(this.assistType == 'requestAssist'){
                    $('#' + mw3._getObjectDivId(assist.__objectId)).children().show();

                    this.assistEventCall();
                }else{
                    if(assist.assistances.length > 1){
                        $('#' + mw3._getObjectDivId(assist.__objectId)).children().show();

                        this.assistEventCall();
                    }else if(assist.assistances.length == 1){
                        this.organizeImports(assist.__objectId);
                    }
                }
            }
        }
    },
    showError : function(message, serviceMethodName){
        if('requestAssist' == serviceMethodName)
            this.loadRequestAssist = false;
    },
    getValue : function(){

        var object = mw3.objects[this.objectId];

        if(object==null){
            object = {code: [], className: this.className};
        }else{
            if(this.editor)
                object.code = this.editor.getSession().getValue();
        }
        //object.code = this.editor.getSession().doc.getAllLines();

        return object;
    },
    importPackage : function(importName, startPosition){
        var packageName = '';
        var className = '';

        var pos = importName.lastIndexOf('.');
        if(pos == -1){
            className = importName;
        }else{
            packageName = importName.substring(0, pos);
            className = importName.substring(pos + 1);
        }

        var importedList = [];
        var lastLine = 0;

        for(var i=0; i<startPosition; i++) {
            var fullLine = this.editor.getSession().doc.getLine(i);
            fullLine = fullLine.trim();

            if(fullLine.indexOf(';') == -1)
                fullLine += ';';

            var lines = fullLine.split(';');
            for(var j=0; j<lines.length; j++){
                var line = lines[j].trim();

                if(line.indexOf('import') != -1){
                    if(fullLine.indexOf('import') == 0)
                        importedList.push(line.substring('import'.length).trim());

                    lastLine = i;
                }else if(fullLine.indexOf('package') == 0){
                    lastLine = i;
                }
            }
        }

        var exist = false;
        if(this.getPackageName() == packageName)
            exist = true;

        if(!exist){
            for(var i=0; i<importedList.length; i++){
                if(importedList[i] == importName || importedList[i] == packageName + '.*'){
                    exist = true;

                    break;
                }
            }
        }

        if(!exist){
            var pos = {row:lastLine+1,column:0};

            this.editor.getSession().doc.insertNewLine(pos);
            this.editor.getSession().doc.insertInLine(pos, 'import ' + packageName + '.' + className + ';');
        }
    },
    requestAssist : function(command){
        if(!this.loadRequestAssist){
            var assist = mw3.getAutowiredObject('org.metaworks.example.ide.CodeAssist');

            if(assist == null){
                this.lastCommandString = command;

                if(command.length > 0){
                    var object = mw3.getObject(this.objectId);

                    object.lineAssistRequested = command;
                    object.clientObjectId = this.objectId;
                    object.requestAssist();
                }
            }
        }
    },
    showOrganizeImports: function(command){
        this.requestAssist('-oi ' + command);
    },
    organizeImports: function(assistId){
        var selectedValue = mw3.getFaceHelper(assistId).select();
        mw3.log(selectedValue);

        var position = this.getSourcePosition();

        var temp = selectedValue.split('-');


        /*
         this.importPackage(temp[1].trim(), temp[0].trim(), Number(position));
         mw3.removeObject(assistId);
         */	},

    getCommandString : function(){
        var whereEnd = this.editor.getCursorPosition();
        var whereStart = {column: 0, row: whereEnd.row};

        var pos;
        var command = this.editor.getSession().doc.getTextRange({start: whereStart, end: whereEnd});
        command = command.trim();

        // check last command line
        pos = command.indexOf(';');
        if(pos > -1)
            command = command.substring(pos + 1);

        //A-Z0-9
        var pattern = /^[A-Za-z0-9]*$/;
        var expression = '';
        for (var i = command.length - 1; i >= 0; i--){
            var charAt = command.charAt(i);
            if(!(pattern.test(charAt) || charAt == '.')){
                break;
            }

            expression = charAt + expression;
        }

        if(command.indexOf('import ') == 0)
            expression = 'import ' + expression;
        if(command.indexOf('@') == 0)
            expression = '@' + expression;

        return expression;

    },
    closeAssist : function(){
        var assist = mw3.getAutowiredObject('org.metaworks.example.ide.CodeAssist');
        if(assist != null)
            mw3.removeObject(assist.__objectId);

    },
    assistEventCall : function(){

        var assist = mw3.getAutowiredObject('org.metaworks.example.ide.CodeAssist');

        if(assist){
            var command = this.getCommandString();

            if(command.indexOf("import ") == 0)
                command = command.substring("import ".length);

            if(command.indexOf("@") == 0)
                command = command.substring("@".length);

            command = command.trim();
            if(command != this.lastCommandString){
                this.lastCommandString = command;

                mw3.getFaceHelper(assist.__objectId).change(command);
            }
        }


    },
    getSourcePosition: function(){
        var whereEnd = this.editor.getCursorPosition();

        var search = this.editor.$search;

        search.set({needle:'public class'});
        var range = search.find(this.editor.getSession());

        if(range == null || range.start.row > whereEnd.row)
            return 'import';
        else
            return range.start.row;
    },
    getPackageName: function(){
        var search = this.editor.$search;

        search.set({needle:'package'});
        var range = search.find(this.editor.getSession());
        var result = '';

        if(range != null){
            var line = this.editor.getSession().getLine(range.start.row);
            line = line.trim();

            if(line.indexOf(';') != -1){
                var temp = line.split(';');

                for(var i=0;i<temp.length;i++){
                    if(temp[i].indexOf('package') != -1){
                        result = temp[i];

                        break;
                    }
                }
            }else{
                result = line;
            }
        }else{
            return '';
        }

        result = result.trim();
        if(result.indexOf('package') != 0){
            return '';
        }

        return result.substring('package'.length).trim();
    },

    selectAssist : function(e){
        var assist = mw3.getAutowiredObject('org.metaworks.example.ide.CodeAssist');

        if(assist != null){
            if(typeof e != 'undefined')
                this.event.stopEvent(e);

            if(this.assistType == 'requestAssist'){
                var selectedValue = mw3.getFaceHelper(assist.__objectId).getSelectedValue();
                var selectedValues = selectedValue.split('/');

                // remove assist
                mw3.removeObject(assist.__objectId);

                // check select value
                if(selectedValue.indexOf('/') == -1 && selectedValues.length < 2){
                    return false;
                }

                var commandString = this.getCommandString();
                var insertString = '';
                var insertPackage = '';
                var isImport = false;

                if(commandString.indexOf('@') == 0)
                    commandString = commandString.substring('@'.length);

                if(commandString.indexOf("import ") == 0){
                    isImport = true;

                    commandString = commandString.substring("import ".length);
                }

                var whereEnd = this.editor.getCursorPosition();
                var whereStart = {column: whereEnd.column - commandString.length, row: whereEnd.row};

                if(selectedValues[2] == 'package'){
                    insertString = selectedValues[0];

                    if(isImport)
                        insertString += ';';
                }else if(selectedValues[2] == 'class' || selectedValues[2] == 'annotation'){
                    if(commandString.charAt(commandString.length-1) == '.'){
                        insertString = selectedValues[1] + '.' + selectedValues[0];
                        insertPackage = selectedValues[1] + '.' + selectedValues[0];
                    }else{
                        insertString = selectedValues[0];
                        insertPackage = selectedValues[1] + '.' + selectedValues[0];
                    }

                    if(isImport)
                        insertString += ';';
                }else{
                    var pos = this.getCommandString().lastIndexOf('.');
                    whereStart = {column: whereEnd.column - (commandString.length - (pos + 1)), row: whereEnd.row};

                    insertString = selectedValues[0];
                    insertPackage = selectedValues[1];
                }

                if(whereStart.column != whereEnd.column){
                    var selectionRange = {start:whereStart, end:whereEnd};

                    this.editor.getSelection().setSelectionRange(selectionRange, false);
                    this.editor.remove('left');
                }
                this.editor.insert(insertString);

                if(!isImport){
                    // import package
                    if(insertPackage.length > 0){
                        var position = this.getSourcePosition();

                        if(position != 'import'){
                            this.importPackage(insertPackage, Number(position));
                        }
                    }
                }
            }else{
                this.organizeImports(assist.__objectId);
            }
        }
    },
    resize : function(e){
        this.editor.resize();
    }

};