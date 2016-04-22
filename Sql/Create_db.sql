CREATE SCHEMA `networkresourcesort` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
CREATE SCHEMA `networkresourcesortdev` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;

grant all on networkresourcesort.* to resource_owner@'%' identified by '123456';
grant all on networkresourcesort.* to resource_owner@'127.0.0.1';
grant all on networkresourcesort.* to resource_owner@'localhost';
grant all on networkresourcesort.* to resource_owner@'::1';

grant all on networkresourcesortdev.* to resource_owner@'%';
grant all on networkresourcesortdev.* to resource_owner@'127.0.0.1';
grant all on networkresourcesortdev.* to resource_owner@'localhost';
grant all on networkresourcesortdev.* to resource_owner@'::1';
flush privileges;

