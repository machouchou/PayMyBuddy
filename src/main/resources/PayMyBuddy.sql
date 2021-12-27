-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema paymybuddy
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `friends`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `friends` ;

CREATE TABLE IF NOT EXISTS `friends` (
  `user_id` INT NOT NULL,
  `friend_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `friend_id`),
  INDEX `friend_id_idx` (`friend_id` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user` ;

CREATE TABLE IF NOT EXISTS `user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(70) NOT NULL,
  `last_name` VARCHAR(70) NOT NULL,
  `birthDate` DATE NULL DEFAULT NULL,
  `address` VARCHAR(100) NULL DEFAULT NULL,
  `country` VARCHAR(30) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `fk_user_friend`
    FOREIGN KEY (`user_id`)
    REFERENCES `friends` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

insert into user(first_name, last_name, birthDate, address, country) values(Raphael, Dupont, '1973-01-17', 
'42 Rue du 8 Mai 1945 75010 Paris', 'France');
commit;


-- -----------------------------------------------------
-- Table `account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `account` ;

CREATE TABLE IF NOT EXISTS `account` (
  `account_id` INT NOT NULL,
  `account_number` VARCHAR(40) NOT NULL,
  `amount_balance` DECIMAL(6,2) NOT NULL,
  `currency` VARCHAR(30) NULL DEFAULT NULL,
  `fk_account_user` INT NOT NULL,
  PRIMARY KEY (`account_id`),
  INDEX `fk_account_user_idx` (`fk_account_user` ASC) VISIBLE,
  CONSTRAINT `fk_account_user`
    FOREIGN KEY (`fk_account_user`)
    REFERENCES `user` (`user_id`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `appaccount`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `appaccount` ;

CREATE TABLE IF NOT EXISTS `appaccount` (
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(50) NOT NULL,
  `fk_appAccount_user` INT NOT NULL,
  PRIMARY KEY (`email`),
  UNIQUE INDEX `email` (`email` ASC) VISIBLE,
  INDEX `fk_appAccount_user_idx` (`fk_appAccount_user` ASC) VISIBLE,
  CONSTRAINT `fk_appAccount_user`
    FOREIGN KEY (`fk_appAccount_user`)
    REFERENCES `user` (`user_id`)
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `transaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `transaction` ;

CREATE TABLE IF NOT EXISTS `transaction` (
  `transaction_id` INT NOT NULL,
  `email_sender` VARCHAR(100) NOT NULL,
  `email_receiver` VARCHAR(100) NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  `transaction_date` DATETIME NOT NULL,
  `amount` DECIMAL(6,2) NOT NULL,
  `fees` DECIMAL(6,2) NOT NULL,
  `fk_transaction_user` INT NOT NULL,
  PRIMARY KEY (`transaction_id`),
  INDEX `fk_transaction_user_idx` (`fk_transaction_user` ASC) INVISIBLE,
  CONSTRAINT `fk_transaction_user`
    FOREIGN KEY (`fk_transaction_user`)
    REFERENCES `user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
