create keyspace file_archive_ks WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 2 };

use file_archive_ks;

create table documents (
  uuid uuid,
  file_name varchar,
  uploaded_by varchar,
  document_date timestamp,
  upload_time timestamp,
  primary key (uuid)
);

create index documents_uploaded_by on documents(uploaded_by);
create index documents_file_name on documents(file_name);
