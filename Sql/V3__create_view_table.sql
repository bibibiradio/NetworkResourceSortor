CREATE TABLE `viewtable` (
  `view_id` BIGINT NOT NULL AUTO_INCREMENT,
  `r_id` BIGINT NOT NULL,
  `viewer_name` VARCHAR(256) NOT NULL,
  `viewer_source` INT NOT NULL,
  `floor` INT NOT NULL,
  `reply_count` INT NOT NULL,
  `view_content` TEXT NOT NULL,
  `gmt_insert_time` DATETIME NOT NULL,
  `gmt_view_time` DATETIME NOT NULL,
  PRIMARY KEY (`view_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'View Collection';

ALTER TABLE `viewtable` 
ADD COLUMN `view_type` VARCHAR(64) NOT NULL AFTER `viewer_name`;