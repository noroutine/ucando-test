create  table users (
  username varchar(45) not null ,
  password varchar(45) not null ,
  enabled tinyint not null default 1 ,
  primary key (username)
);

create table user_roles (
  user_role_id int(11) not null auto_increment,
  username varchar(45) not null,
  role varchar(45) not null,
  primary key (user_role_id),
  unique key uni_username_role (role,username),
  key fk_username_idx (username),
  constraint fk_username foreign key (username) references users (username)
);
  
-- some predefined users
insert into users(username,password,enabled) values ('admin', 'LetMe1n!', true);
insert into users(username,password,enabled) values ('joe',   'LetMe1n!', true);
insert into users(username,password,enabled) values ('jack',  'LetMe1n!', true);
insert into users(username,password,enabled) values ('jill',  'LetMe1n!', true);

-- user roles
insert into user_roles (username, role) values ('admin',      'ROLE_ADMIN');
insert into user_roles (username, role) values ('admin',      'ROLE_USER');
insert into user_roles (username, role) values ('joe',        'ROLE_USER');
insert into user_roles (username, role) values ('jack',       'ROLE_USER');
insert into user_roles (username, role) values ('jill',       'ROLE_USER');
