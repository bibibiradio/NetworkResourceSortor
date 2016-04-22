CREATE TABLE `test_ibatis` (
  `ibatis_id` BIGINT NOT NULL AUTO_INCREMENT,
  `key` VARCHAR(45) NOT NULL,
  `value` TEXT NOT NULL,
  PRIMARY KEY (`ibatis_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

ALTER TABLE `test_ibatis` 
ADD COLUMN `int_key` BIGINT NOT NULL AFTER `key`;