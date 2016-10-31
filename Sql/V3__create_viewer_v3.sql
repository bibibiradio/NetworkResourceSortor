CREATE TABLE `viewer` (
  `viewer_id` BIGINT NOT NULL AUTO_INCREMENT,
  `viewer_name` VARCHAR(256) NOT NULL,
  `viewer_site` INT NOT NULL,
  `viewer_category` VARCHAR(64) NOT NULL DEFAULT '',
  `viewer_sex` TINYINT NOT NULL DEFAULT 0,
  `viewer_level` BIGINT NOT NULL DEFAULT 0,
  `viewer_url` VARCHAR(1024) NOT NULL,
  `viewer_show_url` VARCHAR(1024) NOT NULL DEFAULT '',
  `view_cnt` BIGINT NOT NULL DEFAULT 0,
  `score` BIGINT NOT NULL DEFAULT 0,
  `gmt_insert_time` DATETIME NOT NULL,
  `gmt_update_time` DATETIME NOT NULL,
  PRIMARY KEY (`viewer_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'Viewer Collection';