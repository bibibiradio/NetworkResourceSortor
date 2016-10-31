CREATE TABLE `viewtable` (
  `view_id` BIGINT NOT NULL AUTO_INCREMENT,
  `r_id` BIGINT NOT NULL,
  `viewer_id` BIGINT NOT NULL,
  `floor` INT NOT NULL,
  `reply_count` INT NOT NULL,
  `view_content` VARCHAR(2048) NOT NULL DEFAULT '',
  `gmt_insert_time` DATETIME NOT NULL,
  `gmt_view_time` DATETIME NOT NULL,
  PRIMARY KEY (`view_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'View Collection';