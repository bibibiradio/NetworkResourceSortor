CREATE TABLE `posted_resource` (
  `posted_id` BIGINT NOT NULL AUTO_INCREMENT,
  `u_id` BIGINT NOT NULL,
  `r_id` BIGINT NOT NULL,
  `item_gmt_create` DATETIME NOT NULL,
  PRIMARY KEY (`posted_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'Posted Resources List';