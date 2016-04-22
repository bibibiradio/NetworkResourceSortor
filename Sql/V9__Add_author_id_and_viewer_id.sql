CREATE TABLE `viewer_table` (
  `viewer_id` BIGINT NOT NULL AUTO_INCREMENT,
  `viewer_type` VARCHAR(64) NOT NULL,
  `viewer_name` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`viewer_id`),
  UNIQUE INDEX `viewer_id_UNIQUE` (`viewer_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'Viewer_table';

CREATE TABLE `author_table` (
  `author_id` BIGINT NOT NULL AUTO_INCREMENT,
  `author_type` VARCHAR(64) NOT NULL,
  `author_name` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`author_id`),
  UNIQUE INDEX `author_id_UNIQUE` (`author_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'Author_table';

alter table resources add column author_id bigint;
alter table viewtable add column viewer_id bigint;

create index author_name_author_type USING BTREE on author_table (author_name(128),author_type(16));
create index viewer_name_viewer_type USING BTREE on viewer_table (viewer_name(128),viewer_type(16));

insert into author_table(author_name,author_type) select author,r_type from resources group by author,r_type;
update resources set author_id = (select author_id from author_table where author_name=resources.author and author_type=resources.r_type);
insert into viewer_table(viewer_name,viewer_type) select viewer_name,view_type from viewtable group by viewer_name,view_type;
update viewtable set viewer_id = (select viewer_id from viewer_table where viewer_name=viewtable.viewer_name and viewer_type=viewtable.view_type);

alter table viewer_score drop column viewer_name;
alter table author_score drop column author_name;