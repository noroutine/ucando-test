drop table if exists documents;
create table documents (
  uuid varchar(255) not null,
  file_name varchar(255) not null,
  uploaded_by varchar(255) not null,
  document_date timestamp,
  upload_time timestamp,
  content longblob
);

show table status where name ='documents'\G
