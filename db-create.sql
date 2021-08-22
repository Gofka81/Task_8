-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema p8db
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `p8db` ;

-- -----------------------------------------------------
-- Schema p8db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `p8db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `p8db` ;

-- -----------------------------------------------------
-- Table `p8db`.`teams`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `p8db`.`teams` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `p8db`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `p8db`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE UNIQUE INDEX `login` ON `p8db`.`users` (`login` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `p8db`.`teams_users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `p8db`.`teams_users` (
  `teams_id` INT NOT NULL,
  `users_id` INT NOT NULL,
  PRIMARY KEY (`teams_id`, `users_id`),
  CONSTRAINT `fk_teams_has_users_teams`
    FOREIGN KEY (`teams_id`)
    REFERENCES `p8db`.`teams` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_teams_has_users_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `p8db`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE INDEX `fk_teams_has_users_users1_idx` ON `p8db`.`teams_users` (`users_id` ASC) VISIBLE;

CREATE INDEX `fk_teams_has_users_teams_idx` ON `p8db`.`teams_users` (`teams_id` ASC) VISIBLE;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
