CREATE TABLE `resources` (
  `r_id` BIGINT NOT NULL AUTO_INCREMENT,
  `r_title` VARCHAR(1024) NOT NULL DEFAULT '',
  `r_category` VARCHAR(64) NOT NULL DEFAULT '',
  `r_type` TINYINT NOT NULL,
  `r_site` INT NOT NULL,
  `r_inner_id` BIGINT NOT NULL DEFAULT 0,
  `author_id` BIGINT NOT NULL DEFAULT 0,
  `r_duration` BIGINT NOT NULL DEFAULT -1,
  `r_pv` BIGINT NOT NULL DEFAULT -1,
  `r_comment` BIGINT NOT NULL DEFAULT -1,
  `r_danmu` BIGINT NOT NULL DEFAULT -1,
  `r_collect` BIGINT NOT NULL DEFAULT -1,
  `r_tags` VARCHAR(1024) NOT NULL DEFAULT '',
  `r_url` VARCHAR(1024) NOT NULL DEFAULT '',
  `r_show_url` VARCHAR(1024) NOT NULL DEFAULT '',
  `r_coin` BIGINT NOT NULL DEFAULT -1,
  `is_valid` INT NOT NULL DEFAULT 0,
  `other_detail` VARCHAR(256) NOT NULL DEFAULT '',
  `r_gmt_create` DATETIME NOT NULL,
  `item_gmt_insert` DATETIME NOT NULL,
  `score` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`r_id`),
  UNIQUE INDEX `r_id_UNIQUE` (`r_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'Resource Collection';
