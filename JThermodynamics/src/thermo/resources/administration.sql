SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `administration` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `administration` ;

-- -----------------------------------------------------
-- Table `administration`.`UserInformation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `administration`.`UserInformation` ;

CREATE  TABLE IF NOT EXISTS `administration`.`UserInformation` (
  `UserName` VARCHAR(45) NOT NULL ,
  `LocalRootAddress` VARCHAR(45) NOT NULL DEFAULT './reactionData' ,
  `RelativeRemoteAddress` VARCHAR(45) NOT NULL DEFAULT '.' ,
  INDEX `UserName_idx` (`UserName` ASC) ,
  CONSTRAINT `UserName`
    FOREIGN KEY (`UserName` )
    REFERENCES `administration`.`UserInformation` (`UserName` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `administration`.`Server`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `administration`.`Server` ;

CREATE  TABLE IF NOT EXISTS `administration`.`Server` (
  `UserName` VARCHAR(45) NOT NULL ,
  `ServerAddress` VARCHAR(45) NULL DEFAULT 'localhost' ,
  `DatabaseName` VARCHAR(45) NULL DEFAULT 'Thermodynamics' ,
  PRIMARY KEY (`UserName`) ,
  CONSTRAINT `UserName`
    FOREIGN KEY (`UserName` )
    REFERENCES `administration`.`UserInformation` (`UserName` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `administration`.`UserInformation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `administration`.`UserInformation` ;

CREATE  TABLE IF NOT EXISTS `administration`.`UserInformation` (
  `UserName` VARCHAR(45) NOT NULL ,
  `LocalRootAddress` VARCHAR(45) NOT NULL DEFAULT './reactionData' ,
  `RelativeRemoteAddress` VARCHAR(45) NOT NULL DEFAULT '.' ,
  INDEX `UserName_idx` (`UserName` ASC) ,
  CONSTRAINT `UserName`
    FOREIGN KEY (`UserName` )
    REFERENCES `administration`.`UserInformation` (`UserName` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `administration` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
