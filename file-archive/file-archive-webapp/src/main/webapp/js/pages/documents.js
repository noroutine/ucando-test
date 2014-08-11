(function () {
    "use strict";

    var uploadInProgress = false;

    var Document = Backbone.Model.extend({});

    var Documents = Backbone.Collection.extend({
        model: Document,
        url: CONTEXT_URL + 'documents/all'
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
            $.ajax({ url: CONTEXT_URL + 'documents/' + self.model.attributes.uuid, type: 'delete'})
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
        template: _.template("<a href='" + CONTEXT_URL + "documents/<%= uuid%>/download/<%= fileName %>'><%= fileName %></a>"),
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
        uploadInProgress = true;
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
            uploadInProgress = false;
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

    $('#search_by_name').on('click', filterByName);
    $('#search_by_upload_time').on('click', filterByUploadTime);
    $('#search_by_doc_date').on('click', filterByDocumentDate);
    // when all tab is shown, we need to reset the data
    $(document).on('shown.bs.tab', 'a[data-toggle="tab"][href="#all"]', showAll);

    function showAll() {
        documents.url = CONTEXT_URL + 'documents/all';
        documents.fetch();
    }

    function filterByName() {
        var nameFilter = $('#name_filter').val();
        if (nameFilter) {
            documents.url = CONTEXT_URL + 'documents/filter/uploadedBy?uploadedBy=' + nameFilter;
            documents.fetch();
        } else {
            showAll();
        }
    }

    function filterByUploadTime() {
        var fromTime = $('#picker_upload_time_from').data("DateTimePicker").getDate();
        var toTime = $('#picker_upload_time_to').data("DateTimePicker").getDate();
        documents.url = CONTEXT_URL + 'documents/filter/uploadedTime?from=' + fromTime + '&to=' + toTime;
        documents.fetch();
    }

    function filterByDocumentDate() {
        var fromTime = $('#picker_doc_date_from').data("DateTimePicker").getDate();
        var toTime = $('#picker_doc_date_to').data("DateTimePicker").getDate();
        documents.url = CONTEXT_URL + 'documents/filter/documentDate?from=' + fromTime + '&to=' + toTime;
        documents.fetch();
    }

    $('#picker_upload_time_from').datetimepicker();
    $('#picker_upload_time_to').datetimepicker();
    $('#picker_doc_date_from').datetimepicker();
    $('#picker_doc_date_to').datetimepicker();

    window.addEventListener('beforeunload', function (e) {
        if (uploadInProgress) {
            var confirmationMessage = 'Upload is in progress. Do you really want to leave?';
            (e || window.event).returnValue = confirmationMessage;     //Gecko + IE
            return confirmationMessage;
        }
    });
})();

