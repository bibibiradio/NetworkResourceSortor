CREATE TABLE `author_score` (
  `author_id` BIGINT NOT NULL,
  `author_name` VARCHAR(128) NOT NULL,
  `author_type` VARCHAR(64) NOT NULL,
  `author_category` VARCHAR(64) NOT NULL,
  `resource_num` INT NOT NULL,
  `score` BIGINT NOT NULL,
  `gmt_insert_time` DATETIME NOT NULL,
  `gmt_update_time` DATETIME NOT NULL,
  PRIMARY KEY (`author_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'author score';

ALTER TABLE `author_score` 
CHANGE COLUMN `author_id` `author_id` BIGINT(20) NOT NULL AUTO_INCREMENT ;