CREATE TABLE `author` (
  `author_id` BIGINT NOT NULL AUTO_INCREMENT,
  `author_name` VARCHAR(128) NOT NULL,
  `author_site` INT NOT NULL,
  `author_category` VARCHAR(64) NOT NULL DEFAULT '',
  `author_sex` TINYINT NOT NULL DEFAULT 0,
  `author_level` BIGINT NOT NULL DEFAULT 0,
  `author_url` VARCHAR(1024) NOT NULL DEFAULT '',
  `author_show_url` VARCHAR(1024) NOT NULL DEFAULT '',
  `resource_num` INT NOT NULL DEFAULT 0,
  `score` BIGINT NOT NULL DEFAULT 0,
  `gmt_insert_time` DATETIME NOT NULL,
  `gmt_update_time` DATETIME NOT NULL,
  PRIMARY KEY (`author_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'author';