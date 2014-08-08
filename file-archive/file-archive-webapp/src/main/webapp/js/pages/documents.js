(function () {
    "use strict";

    document.getElementById('upload_btn').addEventListener('click', function() {
        var uploadFile = document.getElementById('upload_file');
        uploadFile.addEventListener('change', function(e) {
            var uploadProgress = document.getElementById('upload_progress');

            if (uploadFile.value) {
                uploadProgress.textContent = null;
            }

            var xhr = new XMLHttpRequest();

            xhr.addEventListener('load', function(e) {
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

            xhr.addEventListener('abort', function(e) {
                uploadProgress.textContent = R.get('fileUpload.result.abort');
            }, false);

            xhr.addEventListener('error', function(e) {
                uploadProgress.textContent = R.get('fileUpload.result.error');
            }, false);

            xhr.addEventListener('loadend', function(e) {
                uploadFile.value = "";
            });

            xhr.open('post', '', true);
//            xhr.setRequestHeader('Content-Type', 'application/json');
//            xhr.overrideMimeType('application/json');
            xhr.responseType = "json";

            xhr.upload.addEventListener('progress', function(e) {
                if (e.lengthComputable) {
                    uploadProgress.textContent = R.get('fileUpload.state.upload', { progress: Math.round((e.loaded * 100) / e.total) });
                }
            }, false);

            xhr.upload.addEventListener('load', function(e) {
                uploadProgress.textContent = R.get('fileUpload.state.uploading');
            });

            var formDate = new FormData();
            var file = e.target.files[0];

            formDate.append("file", file);
            formDate.append("metadata", new Blob([JSON.stringify({
                fileName: file.name,
                documentDate: file.lastModifiedDate,
                uploadedTime: new Date().getTime()
            })], { type: 'application/json'}));
            xhr.send(formDate);
        }, false);

        uploadFile.click();
    });
})();

