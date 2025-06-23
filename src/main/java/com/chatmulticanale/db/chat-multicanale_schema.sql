-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mydb` ;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Utente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Utente` (
  `ID_Utente` INT NOT NULL AUTO_INCREMENT,
  `Username` VARCHAR(45) NOT NULL,
  `Password_Hash` VARCHAR(255) NOT NULL,
  `Nome_Utente` VARCHAR(45) NOT NULL,
  `Cognome_Utente` VARCHAR(45) NOT NULL,
  `Ruolo` ENUM('Amministratore', 'CapoProgetto', 'Dipendente') NOT NULL,
  PRIMARY KEY (`ID_Utente`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `Username_UNIQUE` ON `mydb`.`Utente` (`Username` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `mydb`.`Progetto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Progetto` (
  `ID_Progetto` INT NOT NULL AUTO_INCREMENT,
  `Nome_Progetto` VARCHAR(45) NOT NULL,
  `Descrizione_Progetto` MEDIUMTEXT NULL,
  `Utente_Responsabile` INT NULL,
  PRIMARY KEY (`ID_Progetto`),
  CONSTRAINT `fk_Progetto_Utente1`
    FOREIGN KEY (`Utente_Responsabile`)
    REFERENCES `mydb`.`Utente` (`ID_Utente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `Nome_Progetto_UNIQUE` ON `mydb`.`Progetto` (`Nome_Progetto` ASC) VISIBLE;

CREATE INDEX `fk_Progetto_Utente1_idx` ON `mydb`.`Progetto` (`Utente_Responsabile` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `mydb`.`CanaleProgetto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`CanaleProgetto` (
  `ID_Canale` INT NOT NULL AUTO_INCREMENT,
  `Nome_Canale` VARCHAR(45) NOT NULL,
  `Descrizione_Canale` MEDIUMTEXT NULL,
  `Data_Creazione` TIMESTAMP NOT NULL,
  `Utente_Creatore` INT NOT NULL,
  `Progetto` INT NOT NULL,
  PRIMARY KEY (`ID_Canale`),
  CONSTRAINT `fk_CanaleProgetto_Utente1`
    FOREIGN KEY (`Utente_Creatore`)
    REFERENCES `mydb`.`Utente` (`ID_Utente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CanaleProgetto_Progetto1`
    FOREIGN KEY (`Progetto`)
    REFERENCES `mydb`.`Progetto` (`ID_Progetto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_CanaleProgetto_Utente1_idx` ON `mydb`.`CanaleProgetto` (`Utente_Creatore` ASC) VISIBLE;

CREATE INDEX `fk_CanaleProgetto_Progetto1_idx` ON `mydb`.`CanaleProgetto` (`Progetto` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `mydb`.`Messaggio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Messaggio` (
  `ID_Messaggio` INT NOT NULL AUTO_INCREMENT,
  `Contenuto` MEDIUMTEXT NOT NULL,
  `Timestamp` TIMESTAMP NOT NULL,
  `Canale_Rif` INT NULL,
  `Chat_Rif` INT NULL,
  `Messaggio_Citato` INT NULL,
  `Utente_Mittente` INT NOT NULL,
  PRIMARY KEY (`ID_Messaggio`),
  CONSTRAINT `fk_Messaggio_CanaleProgetto1`
    FOREIGN KEY (`Canale_Rif`)
    REFERENCES `mydb`.`CanaleProgetto` (`ID_Canale`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Messaggio_ChatPrivata1`
    FOREIGN KEY (`Chat_Rif`)
    REFERENCES `mydb`.`ChatPrivata` (`ID_Chat`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Messaggio_Messaggio1`
    FOREIGN KEY (`Messaggio_Citato`)
    REFERENCES `mydb`.`Messaggio` (`ID_Messaggio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Messaggio_Utente1`
    FOREIGN KEY (`Utente_Mittente`)
    REFERENCES `mydb`.`Utente` (`ID_Utente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Messaggio_CanaleProgetto1_idx` ON `mydb`.`Messaggio` (`Canale_Rif` ASC) VISIBLE;

CREATE INDEX `fk_Messaggio_ChatPrivata1_idx` ON `mydb`.`Messaggio` (`Chat_Rif` ASC) VISIBLE;

CREATE INDEX `fk_Messaggio_Messaggio1_idx` ON `mydb`.`Messaggio` (`Messaggio_Citato` ASC) VISIBLE;

CREATE INDEX `fk_Messaggio_Utente1_idx` ON `mydb`.`Messaggio` (`Utente_Mittente` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `mydb`.`ChatPrivata`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ChatPrivata` (
  `ID_Chat` INT NOT NULL AUTO_INCREMENT,
  `Timestamp_Creazione` TIMESTAMP NOT NULL,
  `Utente_Creatore` INT NOT NULL,
  `Utente_Partecipante` INT NOT NULL,
  `Messaggio_Origine_Chat` INT NOT NULL,
  PRIMARY KEY (`ID_Chat`),
  CONSTRAINT `fk_ChatPrivata_Utente1`
    FOREIGN KEY (`Utente_Creatore`)
    REFERENCES `mydb`.`Utente` (`ID_Utente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ChatPrivata_Utente2`
    FOREIGN KEY (`Utente_Partecipante`)
    REFERENCES `mydb`.`Utente` (`ID_Utente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ChatPrivata_Messaggio1`
    FOREIGN KEY (`Messaggio_Origine_Chat`)
    REFERENCES `mydb`.`Messaggio` (`ID_Messaggio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_ChatPrivata_Utente1_idx` ON `mydb`.`ChatPrivata` (`Utente_Creatore` ASC) VISIBLE;

CREATE INDEX `fk_ChatPrivata_Utente2_idx` ON `mydb`.`ChatPrivata` (`Utente_Partecipante` ASC) VISIBLE;

CREATE INDEX `fk_ChatPrivata_Messaggio1_idx` ON `mydb`.`ChatPrivata` (`Messaggio_Origine_Chat` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `mydb`.`PartecipaCanale`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`PartecipaCanale` (
  `ID_Utente` INT NOT NULL,
  `ID_Canale` INT NOT NULL,
  PRIMARY KEY (`ID_Utente`, `ID_Canale`),
  CONSTRAINT `fk_Utente_has_CanaleProgetto_Utente`
    FOREIGN KEY (`ID_Utente`)
    REFERENCES `mydb`.`Utente` (`ID_Utente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Utente_has_CanaleProgetto_CanaleProgetto1`
    FOREIGN KEY (`ID_Canale`)
    REFERENCES `mydb`.`CanaleProgetto` (`ID_Canale`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Utente_has_CanaleProgetto_CanaleProgetto1_idx` ON `mydb`.`PartecipaCanale` (`ID_Canale` ASC) VISIBLE;

CREATE INDEX `fk_Utente_has_CanaleProgetto_Utente_idx` ON `mydb`.`PartecipaCanale` (`ID_Utente` ASC) VISIBLE;

USE `mydb` ;

-- -----------------------------------------------------
-- procedure sp_AM1_AssegnaRuoloCapoProgetto
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_AM1_AssegnaRuoloCapoProgetto` (IN p_ID_Utente INT)
BEGIN
	UPDATE Utente
    SET Ruolo = 'CapoProgetto'
    WHERE ID_Utente = p_ID_Utente;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_AM2_RimuoviRuoloCapoProgetto
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_AM2_RimuoviRuoloCapoProgetto` (IN p_ID_Utente INT)
BEGIN
	UPDATE Utente
    SET Ruolo = 'Dipendente'
    WHERE ID_Utente = p_ID_Utente;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_AM3_AssegnaResponsabilitaProgetto
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_AM3_AssegnaResponsabilitaProgetto` (IN p_ID_Progetto INT, IN p_ID_Utente_Responsabile INT)
BEGIN
	UPDATE Progetto
    SET Utente_Responsabile = p_ID_Utente_Responsabile
    WHERE ID_Progetto = p_ID_Progetto;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_AM4_RiassegnaResponsabilitaProgetto
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_AM4_RiassegnaResponsabilitaProgetto` (IN p_ID_Progetto INT, IN p_ID_Nuovo_Utente_Responsabile INT)
BEGIN
	UPDATE Progetto
    SET Utente_Responsabile = p_ID_Nuovo_Utente_Responsabile
    WHERE ID_Progetto = p_ID_Progetto;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_CP1_CreaCanaleProgetto
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_CP1_CreaCanaleProgetto` (IN p_Nome_Canale VARCHAR(45), IN p_Descrizione_Canale MEDIUMTEXT, IN p_ID_Progetto INT, IN p_ID_Utente_Creatore INT)
BEGIN
	INSERT INTO CanaleProgetto (Nome_Canale, Descrizione_Canale, Data_Creazione, Utente_Creatore, Progetto)
    VALUES (p_Nome_Canale, p_Descrizione_Canale, NOW(), p_ID_Utente_Creatore, p_ID_Progetto);

    INSERT INTO PartecipaCanale (ID_Utente, ID_Canale)
    VALUES (p_ID_Utente_Creatore, LAST_INSERT_ID());
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_CP2_AggiungiUtenteACanale
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_CP2_AggiungiUtenteACanale` (IN p_ID_Canale INT, IN p_ID_Utente INT)
BEGIN
	INSERT INTO PartecipaCanale (ID_Utente, ID_Canale)
    VALUES (p_ID_Utente, p_ID_Canale);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_CP3_RimuoviUtenteDaCanale
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_CP3_RimuoviUtenteDaCanale` (IN p_ID_Canale INT, IN p_ID_Utente INT)
BEGIN
	DELETE FROM PartecipaCanale
    WHERE ID_Canale = p_ID_Canale
      AND ID_Utente = p_ID_Utente;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_CP4_AccediChatPrivateProgetto
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_CP4_AccediChatPrivateProgetto` (IN p_ID_CapoProgetto INT, IN p_ID_Progetto INT)
BEGIN
    -- Implementazione della regola aziendale RA8: "I CapoProgetto hanno la facoltà di visualizzare,
    -- in sola lettura, le ChatPrivate che sono state originate all'interno dei CanaliProgetto
    -- per i quali detengono la Responsabilità."
    -- Questo controllo interno è necessario perché i permessi di accesso standard a livello di database (GRANT)
    -- non consentono di filtrare automaticamente le righe in base alla logica di business del responsabile del progetto.
    -- Assicura che solo il CapoProgetto responsabile di un dato progetto possa accedere alle chat private correlate.
    IF EXISTS (SELECT 1 FROM Utente WHERE ID_Utente = p_ID_CapoProgetto AND Ruolo = 'CapoProgetto')
       AND EXISTS (SELECT 1 FROM Progetto WHERE ID_Progetto = p_ID_Progetto AND Utente_Responsabile = p_ID_CapoProgetto) THEN

        -- Selezione dei dettagli delle Chat Private che soddisfano i criteri di accesso di RA8.
        SELECT
            CP.ID_Chat,
            CP.Timestamp_Creazione,
            MOC.Contenuto AS Messaggio_Originale_Contenuto,
            U_Creatore.Username AS Creatore_Chat_Username,
            U_Partecipante.Username AS Partecipante_Chat_Username,
            Can.Nome_Canale AS Canale_Origine_Nome,
            P.Nome_Progetto AS Progetto_Origine_Nome
        FROM ChatPrivata AS CP
        JOIN Messaggio AS MOC ON CP.Messaggio_Origine_Chat = MOC.ID_Messaggio -- Collega al messaggio originale
        JOIN CanaleProgetto AS Can ON MOC.Canale_Rif = Can.ID_Canale -- Collega al canale del messaggio originale
        JOIN Progetto AS P ON Can.Progetto = P.ID_Progetto -- Collega al progetto del canale
        JOIN Utente AS U_Creatore ON CP.Utente_Creatore = U_Creatore.ID_Utente -- Dettagli del creatore della chat
        JOIN Utente AS U_Partecipante ON CP.Utente_Partecipante = U_Partecipante.ID_Utente -- Dettagli dell'altro partecipante
        WHERE
            P.ID_Progetto = p_ID_Progetto
            AND P.Utente_Responsabile = p_ID_CapoProgetto; -- Filtra per il progetto e il capo progetto responsabile

    ELSE
        -- Se l'utente non è autorizzato o non è il responsabile del progetto specificato,
        -- viene generato un errore SQL per impedire l'accesso non autorizzato e informare l'applicazione.
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Accesso negato: L''utente non è un Capo Progetto responsabile di questo progetto.';
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_UT1a_InviaMessaggioCanaleSemplice
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_UT1a_InviaMessaggioCanaleSemplice` (IN p_ID_Canale INT, IN p_ID_Utente_Mittente INT, IN p_Contenuto MEDIUMTEXT)
BEGIN
 -- Messaggio_Citato e Chat_Rif sono lasciati NULL per un messaggio semplice in canale.
	INSERT INTO Messaggio (Contenuto, Timestamp, Canale_Rif, Chat_Rif, Messaggio_Citato, Utente_Mittente)
    VALUES (p_Contenuto, NOW(), p_ID_Canale, NULL, NULL, p_ID_Utente_Mittente);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_UT1b_InviaMessaggioCanaleConCitazione
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_UT1b_InviaMessaggioCanaleConCitazione` (IN p_ID_Canale INT, IN p_ID_Utente_Mittente INT, IN p_Contenuto MEDIUMTEXT, IN p_ID_Messaggio_Citato INT)
BEGIN
	INSERT INTO Messaggio (Contenuto, Timestamp, Canale_Rif, Chat_Rif, Messaggio_Citato, Utente_Mittente)
    VALUES (p_Contenuto, NOW(), p_ID_Canale, NULL, p_ID_Messaggio_Citato, p_ID_Utente_Mittente);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_UT2_VisualizzaMessaggiCanale
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_UT2_VisualizzaMessaggiCanale` (IN p_ID_Canale INT, IN p_ID_UtenteVisualizzatore INT, IN p_Numero_Pagina INT, IN p_Dimensione_Pagina INT)
BEGIN
	-- Calcola l'offset necessario per la paginazione.
	-- L'offset determina quante righe saltare prima di iniziare a recuperare i risultati.
	-- Esempio: Pagina 1 (offset 0), Pagina 2 (offset = Dimensione_Pagina), ecc.
	DECLARE v_Offset INT;
	SET v_Offset = (p_Numero_Pagina - 1) * p_Dimensione_Pagina;

	-- Query principale per selezionare i messaggi del canale specificato.
	-- Include dettagli del mittente e, se presente, del messaggio citato.
	SELECT
		M.ID_Messaggio,             -- ID del messaggio
		M.Contenuto,                -- Contenuto testuale del messaggio
		M.Timestamp,                -- Data e ora di invio del messaggio
		M.Canale_Rif,               -- ID del canale a cui il messaggio appartiene
		M.Chat_Rif,                 -- ID della chat privata a cui il messaggio appartiene (NULL per i messaggi di canale)
		M.Messaggio_Citato,         -- ID del messaggio citato (NULL se non è una citazione)
		M.Utente_Mittente,          -- ID dell'utente mittente del messaggio

		-- Dettagli dell'utente mittente
		U_Mittente.Username AS Mittente_Username,
		U_Mittente.Nome_Utente AS Mittente_Nome,
		U_Mittente.Cognome_Utente AS Mittente_Cognome,

		-- Dettagli del messaggio citato (se presente).
		-- Viene usata una LEFT JOIN per includere messaggi che non citano altri messaggi.
		MC.Contenuto AS Contenuto_Messaggio_Citato,            -- Contenuto del messaggio citato
		UMC.Username AS Mittente_Citato_Username               -- Username del mittente del messaggio citato
	FROM
		Messaggio AS M
	JOIN
		Utente AS U_Mittente ON M.Utente_Mittente = U_Mittente.ID_Utente
	LEFT JOIN
		Messaggio AS MC ON M.Messaggio_Citato = MC.ID_Messaggio
	LEFT JOIN
		Utente AS UMC ON MC.Utente_Mittente = UMC.ID_Utente
	WHERE
		M.Canale_Rif = p_ID_Canale -- Filtra i messaggi per il canale specificato
	ORDER BY
		M.Timestamp            -- Ordina i messaggi dal più vecchio al più recente per la paginazione sequenziale
	LIMIT p_Dimensione_Pagina      -- Limita il numero di risultati alla dimensione della pagina
	OFFSET v_Offset;               -- Salta le righe delle pagine precedenti
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_UT3_AvviaChatPrivataDaMessaggio
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_UT3_AvviaChatPrivataDaMessaggio` (IN p_ID_Utente_Iniziatore INT, IN p_ID_Messaggio_Origine INT)
BEGIN
	DECLARE v_ID_Mittente_Messaggio_Origine INT;
    DECLARE v_ID_Canale_Rif_Messaggio_Origine INT;

    SELECT Utente_Mittente, Canale_Rif
    INTO v_ID_Mittente_Messaggio_Origine, v_ID_Canale_Rif_Messaggio_Origine
    FROM Messaggio
    WHERE ID_Messaggio = p_ID_Messaggio_Origine
      AND Canale_Rif IS NOT NULL;

    IF v_ID_Mittente_Messaggio_Origine IS NOT NULL AND v_ID_Canale_Rif_Messaggio_Origine IS NOT NULL THEN
        INSERT INTO ChatPrivata (Timestamp_Creazione, Utente_Creatore, Utente_Partecipante, Messaggio_Origine_Chat)
        VALUES (NOW(), p_ID_Utente_Iniziatore, v_ID_Mittente_Messaggio_Origine, p_ID_Messaggio_Origine);
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Impossibile avviare chat: Messaggio di origine non trovato o non proviene da un Canale Progetto.';
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_UT4a_InviaMessaggioChatPrivataSemplice
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_UT4a_InviaMessaggioChatPrivataSemplice` (IN p_ID_Chat INT, IN p_ID_Utente_Mittente INT, IN p_Contenuto MEDIUMTEXT)
BEGIN
	INSERT INTO Messaggio (Contenuto, Timestamp, Canale_Rif, Chat_Rif, Messaggio_Citato, Utente_Mittente)
    VALUES (p_Contenuto, NOW(), NULL, p_ID_Chat, NULL, p_ID_Utente_Mittente);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_UT4b_InviaMessaggioChatPrivataConCitazione
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_UT4b_InviaMessaggioChatPrivataConCitazione` (IN p_ID_Chat INT, IN p_ID_Utente_Mittente INT, IN p_Contenuto MEDIUMTEXT, IN p_ID_Messaggio_Citato INT)
BEGIN
	INSERT INTO Messaggio (Contenuto, Timestamp, Canale_Rif, Chat_Rif, Messaggio_Citato, Utente_Mittente)
    VALUES (p_Contenuto, NOW(), NULL, p_ID_Chat, p_ID_Messaggio_Citato, p_ID_Utente_Mittente);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_UT5_VisualizzaMessaggiChatPrivata
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_UT5_VisualizzaMessaggiChatPrivata` (IN p_ID_Chat INT, IN p_ID_UtenteVisualizzatore INT, IN p_Numero_Pagina INT, IN p_Dimensione_Pagina INT)
BEGIN
	-- Calcola l'offset necessario per la paginazione.
    -- Questo garantisce che vengano recuperate solo le righe pertinenti alla pagina richiesta.
    DECLARE v_Offset INT;
    SET v_Offset = (p_Numero_Pagina - 1) * p_Dimensione_Pagina;

    -- Seleziona i messaggi per la chat privata specificata, con dettagli del mittente e del messaggio citato,
    -- ordinati per timestamp e paginati.
    SELECT
        M.ID_Messaggio,             -- ID del messaggio
        M.Contenuto,                -- Contenuto testuale del messaggio
        M.Timestamp,                -- Data e ora di invio del messaggio
        M.Canale_Rif,               -- ID del canale di riferimento (NULL per i messaggi di chat privata)
        M.Chat_Rif,                 -- ID della chat privata a cui il messaggio appartiene
        M.Messaggio_Citato,         -- ID del messaggio citato (NULL se non è una citazione)
        M.Utente_Mittente,          -- ID dell'utente mittente del messaggio

        -- Dettagli dell'utente mittente
        U_Mittente.Username AS Mittente_Username,
        U_Mittente.Nome_Utente AS Mittente_Nome,
        U_Mittente.Cognome_Utente AS Mittente_Cognome,

        -- Dettagli del messaggio citato (se presente).
        -- Viene usata una LEFT JOIN per includere messaggi che non citano altri messaggi.
        MC.Contenuto AS Contenuto_Messaggio_Citato,            -- Contenuto del messaggio citato
        UMC.Username AS Mittente_Citato_Username               -- Username del mittente del messaggio citato
    FROM
        Messaggio AS M
    JOIN
        Utente AS U_Mittente ON M.Utente_Mittente = U_Mittente.ID_Utente
    LEFT JOIN
        Messaggio AS MC ON M.Messaggio_Citato = MC.ID_Messaggio
    LEFT JOIN
        Utente AS UMC ON MC.Utente_Mittente = UMC.ID_Utente
    WHERE
        M.Chat_Rif = p_ID_Chat      -- Filtra i messaggi per la chat privata specificata
    ORDER BY
        M.Timestamp            -- Ordina i messaggi dal più vecchio al più recente per la paginazione sequenziale
    LIMIT p_Dimensione_Pagina      -- Limita il numero di risultati alla dimensione della pagina
    OFFSET v_Offset;               -- Salta le righe delle pagine precedenti
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_UT6_VisualizzaElencoCanaliPartecipati
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_UT6_VisualizzaElencoCanaliPartecipati` (IN p_ID_Utente_Visualizzatore INT)
BEGIN
	SELECT
        CP.ID_Canale,
        CP.Nome_Canale,
        CP.Descrizione_Canale,
        CP.Data_Creazione,
        P.ID_Progetto,
        P.Nome_Progetto
    FROM
        PartecipaCanale AS PC
    JOIN
        CanaleProgetto AS CP ON PC.ID_Canale = CP.ID_Canale
    JOIN
        Progetto AS P ON CP.Progetto = P.ID_Progetto
    WHERE
        PC.ID_Utente = p_ID_Utente_Visualizzatore;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_UT7_VisualizzaElencoChatPrivatePersonali
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
CREATE PROCEDURE `sp_UT7_VisualizzaElencoChatPrivatePersonali` (IN p_ID_Utente_Visualizzatore INT)
BEGIN
	SELECT
        CP.ID_Chat,
        CP.Timestamp_Creazione,
        CASE
            WHEN CP.Utente_Creatore = p_ID_Utente_Visualizzatore THEN UP.ID_Utente
            ELSE UC.ID_Utente
        END AS Altro_Partecipante_ID,
        CASE
            WHEN CP.Utente_Creatore = p_ID_Utente_Visualizzatore THEN UP.Username
            ELSE UC.Username
        END AS Altro_Partecipante_Username,
        MO.ID_Messaggio AS Messaggio_Origine_ID,
        MO.Contenuto AS Messaggio_Origine_Contenuto,
        C.Nome_Canale AS Canale_Origine_Nome,
        P.Nome_Progetto AS Progetto_Origine_Nome
    FROM
        ChatPrivata AS CP
    JOIN
        Utente AS UC ON CP.Utente_Creatore = UC.ID_Utente
    JOIN
        Utente AS UP ON CP.Utente_Partecipante = UP.ID_Utente
    LEFT JOIN
        Messaggio AS MO ON CP.Messaggio_Origine_Chat = MO.ID_Messaggio
    LEFT JOIN
        CanaleProgetto AS C ON MO.Canale_Rif = C.ID_Canale
    LEFT JOIN
        Progetto AS P ON C.Progetto = P.ID_Progetto
    WHERE
        CP.Utente_Creatore = p_ID_Utente_Visualizzatore OR
        CP.Utente_Partecipante = p_ID_Utente_Visualizzatore;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_LG1_OttieniUtenteDaUsername
-- -----------------------------------------------------

DELIMITER $$
USE `mydb`$$
-- -----------------------------------------------------
-- procedure sp_LG1_OttieniUtenteDaUsername
-- -----------------------------------------------------
-- DESCRIZIONE: Recupera i dati di un utente, incluso l'hash della password,
--              basandosi sul suo username. Questa procedura è il cuore del
--              flusso di login sicuro, delegando la verifica della password
--              all'applicazione Java (che usa BCrypt).
-- -----------------------------------------------------
CREATE PROCEDURE `sp_LG1_OttieniUtenteDaUsername`(IN p_Username VARCHAR(45))
BEGIN
    SELECT 
        ID_Utente, 
        Username, 
        Password_Hash,
        Nome_Utente,
        Cognome_Utente,
        Ruolo
    FROM Utente 
    WHERE BINARY Username = BINARY p_Username;
END$$

DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
USE `mydb`;

DELIMITER $$
USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`trg_Progetto_RemoveExCPAfterResponsibilityRevoked` AFTER UPDATE ON `Progetto` FOR EACH ROW
BEGIN
IF OLD.Utente_Responsabile IS NOT NULL AND NEW.Utente_Responsabile <> OLD.Utente_Responsabile THEN
        DELETE FROM PartecipaCanale
        WHERE ID_Utente = OLD.Utente_Responsabile
          AND ID_Canale IN (SELECT ID_Canale FROM CanaleProgetto WHERE Progetto = OLD.ID_Progetto);
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`trg_Messaggio_CheckXOR_Insert` BEFORE INSERT ON `Messaggio` FOR EACH ROW
BEGIN
IF (NEW.Canale_Rif IS NOT NULL AND NEW.Chat_Rif IS NOT NULL) OR
       (NEW.Canale_Rif IS NULL AND NEW.Chat_Rif IS NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Un messaggio deve appartenere esattamente a un Canale (Canale_Rif) o a una Chat Privata (Chat_Rif), ma non a entrambi o a nessuno dei due.';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`trg_Messaggio_CheckXOR_Update` BEFORE UPDATE ON `Messaggio` FOR EACH ROW
BEGIN
IF (NEW.Canale_Rif IS NOT NULL AND NEW.Chat_Rif IS NOT NULL) OR
       (NEW.Canale_Rif IS NULL AND NEW.Chat_Rif IS NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Un messaggio deve appartenere esattamente a un Canale (Canale_Rif) o a una Chat Privata (Chat_Rif), ma non a entrambi o a nessuno dei due.';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`trg_ChatPrivata_CheckDistinctUsers_Insert` BEFORE INSERT ON `ChatPrivata` FOR EACH ROW
BEGIN
IF NEW.Utente_Creatore = NEW.Utente_Partecipante THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Una Chat Privata deve intercorrere tra due utenti distinti.';
    END IF;
END$$

USE `mydb`$$
CREATE DEFINER = CURRENT_USER TRIGGER `mydb`.`trg_ChatPrivata_CheckDistinctUsers_Update` BEFORE UPDATE ON `ChatPrivata` FOR EACH ROW
BEGIN
IF NEW.Utente_Creatore = NEW.Utente_Partecipante THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Una Chat Privata deve intercorrere tra due utenti distinti.';
    END IF;
END$$


DELIMITER ;
-- Begin attached script 'script'
-- ***********************************************************************************
-- SCIPT: Event_CleanOldMessages_Script (Nome dello script nel Navigator)
-- DESCRIZIONE: Definizione dell'evento programmato per la pulizia dei messaggi più vecchi.
--              Questo evento garantisce il rispetto della regola aziendale sulla persistenza
--              dei dati dei messaggi (non superiori a due anni), ottimizzando lo spazio
--              di archiviazione e le performance nel tempo.
-- ***********************************************************************************

-- Definizione dell'evento evt_CleanOldMessages
DELIMITER $$

CREATE EVENT evt_CleanOldMessages
ON SCHEDULE EVERY 1 DAY       -- L'evento viene eseguito una volta al giorno
STARTS CURRENT_TIMESTAMP      -- Inizia a essere eseguito dal momento della sua creazione
ON COMPLETION PRESERVE        -- L'evento rimane attivo e si ripete indefinitamente
DO
BEGIN
    -- Elimina i messaggi dalla tabella 'Messaggio' che sono più vecchi di 2 anni.
    DELETE FROM Messaggio
    WHERE Timestamp < NOW() - INTERVAL 2 YEAR;
END$$

DELIMITER ;
-- end attached script 'script'

-- ***********************************************************************************
-- SEEDING INIZIALE DEI DATI (POPULATION SCRIPT)
-- ***********************************************************************************
-- Inserimento dell'utente Amministratore di default.
-- Questo garantisce che l'applicazione sia utilizzabile subito dopo l'installazione del database.
-- L'hash corrisponde alla password di default 'superpassword123'.
-- -----------------------------------------------------------------------------------
INSERT INTO `mydb`.`Utente` (`Username`, `Password_Hash`, `Nome_Utente`, `Cognome_Utente`, `Ruolo`) 
VALUES ('admin', '$2a$12$dHocTXhdKgYMaNMNwybG8O4KOu0791d9eZzvnPSKCUygPUzzfxqAW', 'Admin', 'Default', 'Amministratore');

-- Begin attached script 'script1'
-- ***********************************************************************************
-- SCIPT: Users_And_Privileges_Script
-- DESCRIZIONE: Creazione degli utenti di database e assegnazione dei loro privilegi
--              in base ai ruoli applicativi (Amministratore, CapoProgetto, Dipendente).
--              I permessi sono assegnati seguendo il Principio del Minimo Privilegio (PoLP).
-- ***********************************************************************************

-- Eliminazione degli utenti esistenti per un'esecuzione pulita (utile per test)
DROP USER IF EXISTS 'amministratore'@'localhost';
DROP USER IF EXISTS 'capoprogetto'@'localhost';
DROP USER IF EXISTS 'dipendente'@'localhost';

-- ***********************************************************************************
-- RUOLO: AMMINISTRATORE (utente database: 'amministratore')
-- ***********************************************************************************

-- Creazione dell'utente per il ruolo Amministratore
CREATE USER 'amministratore'@'localhost' IDENTIFIED BY 'AdminPass123!'; -- Sostituisci con una password sicura

-- Assegnazione dei privilegi sulle tabelle per l'Amministratore
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.Utente TO 'amministratore'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON mydb.Progetto TO 'amministratore'@'localhost';

-- Assegnazione dei privilegi EXECUTE sulle stored procedure per l'Amministratore
GRANT EXECUTE ON PROCEDURE mydb.sp_AM1_AssegnaRuoloCapoProgetto TO 'amministratore'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_AM2_RimuoviRuoloCapoProgetto TO 'amministratore'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_AM3_AssegnaResponsabilitaProgetto TO 'amministratore'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_AM4_RiassegnaResponsabilitaProgetto TO 'amministratore'@'localhost';
-- NUOVA RIGA: Concedi il permesso per la procedura di login
GRANT EXECUTE ON PROCEDURE mydb.sp_LG1_OttieniUtenteDaUsername TO 'amministratore'@'localhost';

-- ***********************************************************************************
-- RUOLO: CAPO PROGETTO (utente database: 'capoprogetto')
-- ***********************************************************************************

-- Creazione dell'utente per il ruolo Capo Progetto
CREATE USER 'capoprogetto'@'localhost' IDENTIFIED BY 'CapoPass123!'; -- Sostituisci con una password sicura

-- Assegnazione dei privilegi sulle tabelle per il Capo Progetto
GRANT SELECT ON mydb.Utente TO 'capoprogetto'@'localhost';
GRANT SELECT ON mydb.Progetto TO 'capoprogetto'@'localhost';
GRANT SELECT, INSERT, UPDATE ON mydb.CanaleProgetto TO 'capoprogetto'@'localhost';
GRANT SELECT, INSERT, DELETE ON mydb.PartecipaCanale TO 'capoprogetto'@'localhost';
GRANT SELECT, INSERT ON mydb.Messaggio TO 'capoprogetto'@'localhost';
GRANT SELECT, INSERT ON mydb.ChatPrivata TO 'capoprogetto'@'localhost';

-- Assegnazione dei privilegi EXECUTE sulle stored procedure per il Capo Progetto
GRANT EXECUTE ON PROCEDURE mydb.sp_CP1_CreaCanaleProgetto TO 'capoprogetto'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_CP2_AggiungiUtenteACanale TO 'capoprogetto'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_CP3_RimuoviUtenteDaCanale TO 'capoprogetto'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_CP4_AccediChatPrivateProgetto TO 'capoprogetto'@'localhost';
-- NUOVA RIGA: Concedi il permesso per la procedura di login
GRANT EXECUTE ON PROCEDURE mydb.sp_LG1_OttieniUtenteDaUsername TO 'capoprogetto'@'localhost';

-- ***********************************************************************************
-- RUOLO: DIPENDENTE (utente database: 'dipendente')
-- ***********************************************************************************

-- Creazione dell'utente per il ruolo Dipendente
CREATE USER 'dipendente'@'localhost' IDENTIFIED BY 'DipendentePass123!'; -- Sostituisci con una password sicura

-- Assegnazione dei privilegi sulle tabelle per il Dipendente
GRANT SELECT, UPDATE ON mydb.Utente TO 'dipendente'@'localhost';
GRANT SELECT ON mydb.Progetto TO 'dipendente'@'localhost';
GRANT SELECT ON mydb.CanaleProgetto TO 'dipendente'@'localhost';
GRANT SELECT ON mydb.PartecipaCanale TO 'dipendente'@'localhost';
GRANT SELECT, INSERT ON mydb.Messaggio TO 'dipendente'@'localhost';
GRANT SELECT, INSERT ON mydb.ChatPrivata TO 'dipendente'@'localhost';

-- Assegnazione dei privilegi EXECUTE sulle stored procedure per il Dipendente
GRANT EXECUTE ON PROCEDURE mydb.sp_UT1a_InviaMessaggioCanaleSemplice TO 'dipendente'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_UT1b_InviaMessaggioCanaleConCitazione TO 'dipendente'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_UT2_VisualizzaMessaggiCanale TO 'dipendente'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_UT3_AvviaChatPrivataDaMessaggio TO 'dipendente'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_UT4a_InviaMessaggioChatPrivataSemplice TO 'dipendente'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_UT4b_InviaMessaggioChatPrivataConCitazione TO 'dipendente'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_UT5_VisualizzaMessaggiChatPrivata TO 'dipendente'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_UT6_VisualizzaElencoCanaliPartecipati TO 'dipendente'@'localhost';
GRANT EXECUTE ON PROCEDURE mydb.sp_UT7_VisualizzaElencoChatPrivatePersonali TO 'dipendente'@'localhost';
-- NUOVA RIGA: Concedi il permesso per la procedura di login
GRANT EXECUTE ON PROCEDURE mydb.sp_LG1_OttieniUtenteDaUsername TO 'dipendente'@'localhost';


-- Applica le modifiche ai privilegi
FLUSH PRIVILEGES;
-- end attached script 'script1'
