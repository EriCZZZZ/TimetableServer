DROP TABLE IF EXISTS `timetable`.`user`;
CREATE TABLE `timetable`.`user` (
  `id_key` INT NOT NULL AUTO_INCREMENT COMMENT '表关联时用户的唯一标识符',
  `id` VARCHAR(32) NOT NULL COMMENT '用户的登陆名',
  `password` VARCHAR(64) NOT NULL,
  `email` VARCHAR(32) NOT NULL,
  `school_id` INT NULL,
  `student_id` VARCHAR(32) NULL,
  `register_date` DATETIME NOT NULL,
  PRIMARY KEY (`id_key`),
  UNIQUE INDEX `id_UNIQUE` (`id_key` ASC));

DROP TABLE IF EXISTS `timetable`.`privilege_table`;
CREATE TABLE `timetable`.`privilege_table` (
  `id_key` INT NOT NULL COMMENT '唯一的用户标识符',
  `privilege` VARCHAR(32) NOT NULL COMMENT '权限列表',
  PRIMARY KEY (`id_key`),
  UNIQUE INDEX `id_key_UNIQUE` (`id_key` ASC));

