CREATE TABLE `r_user` (
  `u_id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(128) NOT NULL,
  `nick_name` VARCHAR(1024) NOT NULL DEFAULT '',
  `email` VARCHAR(128) NOT NULL DEFAULT '',
  `phone` VARCHAR(64) NOT NULL DEFAULT '',
  `password` VARCHAR(512) NOT NULL,
  `sex` TINYINT NOT NULL DEFAULT 0,
  `item_gmt_create` DATETIME NOT NULL,
  `item_gmt_update` DATETIME NOT NULL,
  PRIMARY KEY (`u_id`),
  UNIQUE INDEX `r_id_UNIQUE` (`user_name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin
COMMENT = 'User Collection';