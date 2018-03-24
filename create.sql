create table auth.privilege (id int8 not null, creation_time timestamp, name varchar(255), primary key (id))
create table auth.role (id int8 not null, creation_time timestamp, name varchar(255), primary key (id))
create table auth.user (id int8 not null, creation_time timestamp, email varchar(255), enabled boolean not null, first_name varchar(255), last_name varchar(255), password varchar(255), token_expired boolean not null, primary key (id))
create table auth.verification_token (id int8 not null, creation_time timestamp, expiry_date timestamp, token varchar(255), user_id int8 not null, primary key (id))
create table game.game (id int8 not null, creation_time timestamp, capacity int4 not null, date_time timestamp not null, description varchar(255), status int4 not null, location_id int8, primary key (id))
create table game.location (id int8 not null, creation_time timestamp, address varchar(255), description varchar(255), latitude float8, longitude float8, primary key (id))
create table game.player (id int8 not null, creation_time timestamp, email varchar(255), first_name varchar(255) not null, last_name varchar(255), roster_order int4, type int4 not null, user_id int8, primary key (id))
create sequence hibernate_sequence start 1 increment 1
create table players (game_id int8 not null, player_id int8 not null, primary key (game_id, player_id))
create table roles_privileges (role_id int8 not null, privilege_id int8 not null, primary key (role_id, privilege_id))
create table users_roles (user_id int8 not null, role_id int8 not null, primary key (user_id, role_id))
alter table auth.verification_token add constraint FK_VERIFY_USER foreign key (user_id) references auth.user
alter table game.game add constraint FK9i9qkunl208s2lyr6cwtnh56q foreign key (location_id) references game.location
alter table game.player add constraint FKj57d4kgk8qu7i73lu7xsbq8fb foreign key (user_id) references auth.user
alter table players add constraint FKihptitk4lnv75may66qnaycyd foreign key (player_id) references game.player
alter table players add constraint FKp6fpr78wx0dw2gymua6lw4t3w foreign key (game_id) references game.game
alter table roles_privileges add constraint FK5yjwxw2gvfyu76j3rgqwo685u foreign key (privilege_id) references auth.privilege
alter table roles_privileges add constraint FK9h2vewsqh8luhfq71xokh4who foreign key (role_id) references auth.role
alter table users_roles add constraint FKt4v0rrweyk393bdgt107vdx0x foreign key (role_id) references auth.role
alter table users_roles add constraint FKgd3iendaoyh04b95ykqise6qh foreign key (user_id) references auth.user
