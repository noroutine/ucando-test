(function () {
    "use strict";


    var Document = Backbone.Model.extend({});

    var Documents = Backbone.Collection.extend({
        model: Document,
        url: 'http://localhost:8080/file-archive-webapp/documents/all'
    });

    var documents = new Documents();

    var DeleteCell = Backgrid.Cell.extend({
        template: _.template(" x "),
        events: {
            "click": "deleteRow"
        },
        deleteRow: function (e) {
            e.preventDefault();
            var self = this;
            $.ajax({ url: 'http://localhost:8080/file-archive-webapp/documents/' + self.model.attributes.uuid, type: 'delete'})
                .success(function() {
                    self.model.collection.remove(self.model);
                });
        },
        render: function () {
            this.$el.html(this.template());
            this.delegateEvents();
            return this;
        }
    });

    var DownloadCell = Backgrid.Cell.extend({
        template: _.template("<a href='http://localhost:8080/file-archive-webapp/documents/<%= uuid%>/download/<%= fileName %>'><%= fileName %></a>"),
        render: function () {
            this.$el.html(this.template(this.model.attributes));
            return this;
        }
    });

    var columns = [
//        {
//            name: "uuid",
//            label: "UUID",
//            editable: false,
//            // The cell type can be a reference of a Backgrid.Cell subclass, any Backgrid.Cell subclass instances like *id* above, or a string
//            cell: "string" // This is converted to "StringCell" and a corresponding class in the Backgrid package namespace is looked up
//        },
        {
            name: "fileName",
            label: "Name",
            editable: false,
            // The cell type can be a reference of a Backgrid.Cell subclass, any Backgrid.Cell subclass instances like *id* above, or a string
            cell: DownloadCell // This is converted to "StringCell" and a corresponding class in the Backgrid package namespace is looked up
        },
        {
            name: "uploadedBy",
            label: "Uploaded By",
            editable: false,
            // The cell type can be a reference of a Backgrid.Cell subclass, any Backgrid.Cell subclass instances like *id* above, or a string
            cell: "string" // This is converted to "StringCell" and a corresponding class in the Backgrid package namespace is looked up
        },
        {
            name: "uploadTime",
            label: "Uploaded",
            editable: false,
            cell: "date"
        },
        {
            name: "documentDate",
            label: "Modified",
            editable: false,
            cell: "date"
        },
        {
            name: '',
            cell: DeleteCell
        }
    ];

    // Initialize a new Grid instance
    var grid = new Backgrid.Grid({
        columns: columns,
        collection: documents
    });

    // Render the grid and attach the root to your HTML document
    $("#documents_view").append(grid.render().el);

    // Fetch some countries from the url
    documents.fetch({reset: true});


    var uploadFile = $('#upload_file');

    uploadFile.on('change', function (e) {
        var uploadProgress = document.getElementById('upload_progress');

        if (uploadFile.val()) {
            uploadProgress.textContent = null;
        }

        var xhr = new XMLHttpRequest();

        xhr.addEventListener('load', function (e) {
            if (xhr.response) {
                if (xhr.response.ok) {
                    uploadProgress.textContent = R.get('fileUpload.result.ok');
                } else {
                    uploadProgress.textContent = R.get('fileUpload.result.uploadError');
                }
            } else {
                uploadProgress.textContent = R.get('fileUpload.result.uploadError');
            }
        }, false);

        xhr.addEventListener('abort', function (e) {
            uploadProgress.textContent = R.get('fileUpload.result.abort');
        }, false);

        xhr.addEventListener('error', function (e) {
            uploadProgress.textContent = R.get('fileUpload.result.error');
        }, false);

        xhr.addEventListener('loadend', function (e) {
            uploadFile.val("");
            documents.fetch();
        });

        xhr.open('post', '', true);
        xhr.responseType = "json";

        xhr.upload.addEventListener('progress', function (e) {
            if (e.lengthComputable) {
                uploadProgress.textContent = R.get('fileUpload.state.upload', { progress: Math.round((e.loaded * 100) / e.total) });
            }
        }, false);

        xhr.upload.addEventListener('load', function (e) {
            uploadProgress.textContent = R.get('fileUpload.state.uploading');
        });

        var formDate = new FormData();
        var file = e.target.files[0];

        formDate.append("content", file, "content");
        formDate.append("metadata", new Blob([JSON.stringify({
            fileName: file.name,
            documentDate: file.lastModifiedDate,
            uploadTime: new Date().getTime()
        })], { type: 'application/json'}));
        xhr.send(formDate);
    });

    $('#upload_btn').on('click', function () {
        uploadFile.click();
    });

    $('#picker_from').datetimepicker();
    $('#picker_to').datetimepicker();
})();

