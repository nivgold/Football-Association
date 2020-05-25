use footballassociationdb;

-- drop all tables if exists
SET FOREIGN_KEY_CHECKS = 0;
drop table if exists address;
drop table if exists association_agent;
drop table if exists coach;
drop table if exists coach_in_team;
drop table if exists complaint;
drop table if exists event;
drop table if exists field;
drop table if exists game;
drop table if exists league;
drop table if exists log;
drop table if exists member;
drop table if exists player;
drop table if exists player_in_team;
drop table if exists policy;
drop table if exists rankingPolicy;
drop table if exists referee;
drop table if exists refereesInSIL;
drop table if exists season;
drop table if exists seasonInLeague;
drop table if exists side_referee_in_game;
drop table if exists team;
drop table if exists team_manager;
drop table if exists team_owner;
drop table if exists teamsInSIL;
SET FOREIGN_KEY_CHECKS = 1;

-- create Complaint table
CREATE TABLE complaint(
    complaintID int NOT NULL AUTO_INCREMENT,
    complainterID int NOT NULL,
    complaint_data varchar (255),
    complaint_answer varchar (255),
    was_answered boolean DEFAULT 0,
    primary key (complaintID)
);

-- create Address table
CREATE TABLE address(
    addressID int NOT NULL AUTO_INCREMENT,
    country varchar (255),
    state varchar (255),
    city varchar (255),
    postalCode varchar (255),
    primary key (addressID)
);

-- create Member table
CREATE TABLE member(
    memberID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username varchar (255) UNIQUE NOT NULL,
    passwordHash varchar (255) NOT NULL,
    name varchar (255) NOT NULL,
    email varchar (255),
    -- FOREIGN KEYS
    addressID int NOT NULL,
    coachID int DEFAULT NULL,
    teamOwnerID int DEFAULT NULL,
    teamManagerID int DEFAULT NULL,
    playerID int DEFAULT NULL,
    refereeID int DEFAULT NULL ,
    associationAgentID int DEFAULT NULL,
    -- CONSTRAINTS
    CONSTRAINT `fk_address` FOREIGN KEY (`addressID`)
    REFERENCES `address` (`addressID`)
);

-- adding foreign key to complaint table
ALTER TABLE complaint
    ADD CONSTRAINT `fk_member` FOREIGN KEY (`complainterID`)
    REFERENCES `member` (`memberID`);

-- create Coach table
CREATE TABLE coach(
    coachID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    qualification varchar (255),
    -- FOREIGN KEYS
    memberID int NOT NULL,
    -- CONSTRAINTS
    CONSTRAINT  `fk_member_coach` FOREIGN KEY (`memberID`)
    REFERENCES `member` (`memberID`)
);

-- create AssociationAgent table
CREATE TABLE association_agent(
    associationAgentID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    -- FOREIGN KEYS
    memberID int NOT NULL,
    -- CONSTRAINTS
    CONSTRAINT `fk_member_association_member` FOREIGN KEY (`memberID`)
    REFERENCES `member` (`memberID`)
);

-- create Player table
CREATE TABLE player(
    playerID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    birth_date date,
    -- FOREIGN KEYS
    memberID int NOT NULL,
    -- CONSTRAINTS
    CONSTRAINT `fk_member_player` FOREIGN KEY (`memberID`)
    REFERENCES `member` (`memberID`)
);

-- create Referee table
CREATE TABLE referee(
    refereeID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    qualification varchar (255),
    -- FOREIGN KEYS
    memberID int NOT NULL,
    -- CONSTRAINTS
    CONSTRAINT `fk_member_referee` FOREIGN KEY (`memberID`)
    REFERENCES `member` (`memberID`)
);

-- create TeamManager table
CREATE TABLE team_manager(
    teamManagerID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    -- FOREIGN KEYS
    teamID int,
    appointerID int,
    memberID int NOT NULL,
    -- CONSTRAINTS
    CONSTRAINT `fk_member_team_manager` FOREIGN KEY (`memberID`)
    REFERENCES `member` (`memberID`)
);

-- create TeamOwner table
CREATE TABLE team_owner(
    teamOwnerID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    -- FOREIGN KEYS
    teamID int DEFAULT NULL ,
    appointerID int DEFAULT NULL,
    memberID int NOT NULL,
    -- CONSTRAINTS
    CONSTRAINT `fk_appointer_team_owner` FOREIGN KEY (`appointerID`)
    REFERENCES `team_owner` (`teamOwnerID`),
    CONSTRAINT `fk_member_team_owner` FOREIGN KEY (`memberID`)
    REFERENCES `member` (`memberID`)
);

-- adding foreign key to TeamManager table
ALTER TABLE team_manager
    ADD CONSTRAINT `fk_appointer_team_manager` FOREIGN KEY (`appointerID`)
    REFERENCES `team_owner` (`teamOwnerID`);

-- adding roles foreign keys to Member table
ALTER TABLE member
    ADD CONSTRAINT `fk_coach_member` FOREIGN KEY (`coachID`)
    REFERENCES `coach` (`coachID`),
    ADD CONSTRAINT `fk_player_member` FOREIGN KEY (`playerID`)
    REFERENCES `player` (`playerID`),
    ADD CONSTRAINT `fk_referee_member` FOREIGN KEY (`refereeID`)
    REFERENCES `referee` (`refereeID`),
    ADD CONSTRAINT `fk_teamManager_member` FOREIGN KEY (`teamManagerID`)
    REFERENCES `team_manager` (`teamManagerID`),
    ADD CONSTRAINT `fk_teamOwner_member` FOREIGN KEY (`teamOwnerID`)
    REFERENCES `team_owner` (`teamOwnerID`),
    ADD CONSTRAINT `fk_associationAgent_member` FOREIGN KEY (`associationAgentID`)
    REFERENCES `association_agent` (`associationAgentID`);

-- create Team table
CREATE TABLE team(
    teamID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    teamName varchar (255) UNIQUE NOT NULL,
    teamStatus BOOLEAN DEFAULT TRUE,
    -- FOREIGN KEYS
    fieldID int UNIQUE
);

-- adding TeamOwner team foreign key
ALTER TABLE team_owner
    ADD CONSTRAINT `fk_team_team_owner` FOREIGN KEY (`teamID`)
    REFERENCES `team` (`teamID`);

-- adding TeamManager team foreign key
ALTER TABLE team_manager
    ADD CONSTRAINT `fk_team_team_manager` FOREIGN KEY (`teamID`)
    REFERENCES `team` (`teamID`);

-- create CoachInTeam table
CREATE TABLE coach_in_team(
    -- FOREIGN KEYS
    coachID int NOT NULL,
    teamID int NOT NULL,
    -- CONSTRAINTS
    CONSTRAINT `fk_coach_coach_in_team` FOREIGN KEY (`coachID`)
    REFERENCES `coach` (`coachID`),
    CONSTRAINT `fk_team_coach_in_team` FOREIGN KEY (`teamID`)
    REFERENCES `team` (`teamID`),
    PRIMARY KEY (coachID, teamID)
);

-- create PlayerInTeam table
CREATE TABLE player_in_team(
    -- FOREIGN KEYS
    playerID int NOT NULL,
    teamID int NOT NULL,
    -- CONSTRAINTS
    CONSTRAINT `fk_player_player_in_team` FOREIGN KEY (`playerID`)
    REFERENCES `player` (`playerID`),
    CONSTRAINT `fk_team_player_in_team` FOREIGN KEY (`teamID`)
    REFERENCES `team` (`teamID`),
    PRIMARY KEY (playerID, teamID)
);

-- create Field table
CREATE TABLE field(
    fieldID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    -- FOREIGN KEYS
    teamID int UNIQUE,
    addressID int NOT NULL,
    -- CONSTRAINTS
    CONSTRAINT `fk_team_field` FOREIGN KEY (`teamID`)
    REFERENCES `team` (`teamID`),
    CONSTRAINT `fk_address_field` FOREIGN KEY (`addressID`)
    REFERENCES `address` (`addressID`)
);

CREATE TABLE league(
    leagueID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    league_name varchar(255) NOT NULL unique
);


CREATE TABLE season(
    seasonYear int NOT NULL PRIMARY KEY unique
);

CREATE TABLE seasonInLeague(
    -- FOREIGN KEYS
    seasonYear int NOT NULL,
    leagueID int NOT NULL,
    PolicyID int NOT NULL,
    -- CONSTRAINT
    CONSTRAINT `fk_league_seasonInLeague` FOREIGN KEY (`leagueID`)
    REFERENCES `league` (`leagueID`),
    CONSTRAINT `fk_season_seasonInLeague` FOREIGN KEY (`seasonYear`)
    REFERENCES `season` (`seasonYear`),
    -- PRIMARY KEY
    PRIMARY KEY (leagueID, seasonYear)
);

-- create Game table
CREATE TABLE game(
    gameID int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    `date` DATETIME NOT NULL ,
    host_team_score int DEFAULT 0,
    guest_team_score int DEFAULT 0,
    game_status int DEFAULT 0,
    report varchar(255) DEFAULT NULL,
    -- FOREIGN KEYS
    host_teamID int NOT NULL ,
    guest_teamID int NOT NULL ,
    main_refereeID int NOT NULL ,
    seasonYear int NOT NULL,
    leagueID int NOT NULL,
    fieldID int NOT NULL ,
    -- CONSTRAINTS
    CONSTRAINT `fk_host_team_game` FOREIGN KEY (`host_teamID`)
    REFERENCES `team` (`teamID`),
    CONSTRAINT `fk_guest_team_game` FOREIGN KEY (`guest_teamID`)
    REFERENCES `team` (`teamID`),
    CONSTRAINT `fk_referee_game` FOREIGN KEY (`main_refereeID`)
    REFERENCES `referee` (`refereeID`),
    CONSTRAINT `fk_field_game` FOREIGN KEY (`fieldID`)
    REFERENCES `field` (`fieldID`),
    CONSTRAINT `fk_seasonInLeague_game` FOREIGN KEY (`leagueID`, `seasonYear`)
    REFERENCES `seasonInLeague` (`leagueID`, `seasonYear`)
);

-- create SideRefereeInGame table
CREATE TABLE side_referee_in_game(
    -- FOREIGN KEYS
    gameID int NOT NULL ,
    side_referee_id int NOT NULL ,
    -- CONSTRAINTS
    CONSTRAINT `fk_game_side_referee_in_game` FOREIGN KEY (`gameID`)
    REFERENCES `game` (`gameID`),
    CONSTRAINT `fk_side_referee_id_side_referee_in_game` FOREIGN KEY (`side_referee_id`)
    REFERENCES `referee` (`refereeID`),
    PRIMARY KEY (gameID, side_referee_id)
);

CREATE TABLE event(
    `date` DATE NOT NULL ,
    game_minute INT NOT NULL ,
    description varchar(255) ,
    event_type INT NOT NULL ,
    -- FOREIGN KEYS
    playerID int NOT NULL ,
    gameID int NOT NULL ,
    -- CONSTRAINTS
    CONSTRAINT `fk_player_event` FOREIGN KEY (`playerID`)
    REFERENCES `player` (`playerID`),
    CONSTRAINT `fk_game_event` FOREIGN KEY (`gameID`)
    REFERENCES `game` (`gameID`),
    PRIMARY KEY (playerID, gameID)
);

CREATE TABLE rankingPolicy(
    rankingPolicyID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    win int NOT NULL,
    goals int NOT NULL,
    draw int NOT NULL,
    yellowCards int NOT NULL,
    redCards int NOT NULL,
    -- FOREIGN KEYS
    policyID int NOT NULL
);

CREATE TABLE policy(
    policyID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    gameSettingPolicy tinyint,
    -- FOREIGN KEYS
    rankingPolicyID int,
    seasonYear int,
    leagueID int,
    -- CONSTRAINTS
    CONSTRAINT `fk_rankingPolicy_policy` FOREIGN KEY (`rankingPolicyID`)
    REFERENCES `rankingPolicy` (`rankingPolicyID`),
    CONSTRAINT `fk_seasonInLeague_policy` FOREIGN KEY (`leagueID`,`seasonYear`)
    REFERENCES `seasonInLeague` (`leagueID`,`seasonYear`)
);

ALTER TABLE seasonInLeague
    ADD CONSTRAINT `fk_policy_seasonInLeague` FOREIGN KEY (`PolicyID`)
    REFERENCES `policy` (`policyID`);

ALTER TABLE rankingPolicy
    ADD CONSTRAINT `fk_policy_rankingPolicy` FOREIGN KEY (`policyID`)
    REFERENCES `policy` (`policyID`);

CREATE TABLE refereesInSIL(
    -- FOREIGN KEY
    seasonYear int NOT NULL,
    leagueID int NOT NULL,
    refereeID int NOT NULL,
    -- CONSTRAINT
    CONSTRAINT `fk_seasonInLeague_refereesInSIL` FOREIGN KEY (`leagueID`, `seasonYear`)
    REFERENCES `seasonInLeague` (`leagueID`,`seasonYear`),
    CONSTRAINT `fk_referee_refereesInSIL` FOREIGN KEY (`refereeID`)
    REFERENCES `referee` (`refereeID`),
    -- PRIMARY KEY
    PRIMARY KEY (leagueID, seasonYear, refereeID)
);

CREATE TABLE teamsInSIL(
    -- FOREIGN KEY
    seasonYear int NOT NULL,
    leagueID int NOT NULL,
    teamID int NOT NULL,
    -- CONSTRAINT
    CONSTRAINT `fk_seasonInLeague_teamsInSIL` FOREIGN KEY (`leagueID`, `seasonYear`)
    REFERENCES `seasonInLeague` (`leagueID`,`seasonYear`),
    CONSTRAINT `fk_team_refereesInSIL` FOREIGN KEY (`teamID`)
    REFERENCES `team` (`teamID`),
    -- PRIMARY KEY
    PRIMARY KEY (`leagueID`, `seasonYear`, teamID)
);

CREATE TABLE log(
  logID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  data varchar(255)
);

INSERT INTO `address` (`city`,`country`,`postalCode`, `state`)
    VALUES ('Netanya','Israel','472948239','NONE');

INSERT INTO `member` (`username`,`passwordHash`,`name`, `email`, `addressID`)
    VALUES ('teamOwner_Koren','833df42ad4b881262b26fe02df0f6cef26b27266','Koren Ishlach', 'korenish@post.bgu.ac.il', 1);

INSERT INTO `team_owner` (`memberID`)
    VALUES (1);

UPDATE member
    SET teamOwnerID = 1
    WHERE memberID = 1;

INSERT INTO `address` (`city`,`country`,`postalCode`, `state`)
    VALUES ('Nesher','Israel','472948676','NONE');

INSERT INTO `member` (`username`,`passwordHash`,`name`, `email`, `addressID`)
    VALUES ('teamOwner_Niv','f213c58a5a9f87db49e3ab9ddfee3332075d4ced','Niv GoldShlager', 'nivgold@post.bgu.ac.il', 2);

INSERT INTO `address` (`city`,`country`,`postalCode`, `state`)
    VALUES ('Nesher','Israel','472940000','NONE');

INSERT INTO field (addressID)
    VALUES (3);

INSERT INTO team (teamName, fieldID)
    VALUES ('Niv Team', 1);

INSERT INTO `team_owner` (`memberID`, teamID)
    VALUES (2, 1);

UPDATE member
    SET teamOwnerID = 2
    WHERE memberID = 2;

INSERT INTO `address` (`city`,`country`,`postalCode`, `state`)
    VALUES ('Netanya','Israel','983457348','NONE');

INSERT INTO `member` (`username`,`passwordHash`,`name`, `email`, `addressID`)
    VALUES ('teamOwner_Tal','a96c9ea7833b5a8698f9bfc042fbfab1ea90040a','Tal Frimerman', 'talfrim@post.bgu.ac.il', 4);

INSERT INTO `address` (`city`,`country`,`postalCode`, `state`)
    VALUES ('Netanya','Israel','983450000','NONE');

INSERT INTO field (addressID)
    VALUES (5);

INSERT INTO team (teamName, fieldID)
    VALUES ('Tal Team', 2);

INSERT INTO `team_owner` (`memberID`, teamID)
    VALUES (3, 2);

UPDATE field
    SET field.teamID = 1
    WHERE fieldID = 1;

UPDATE field
SET field.teamID = 2
WHERE fieldID = 2;

UPDATE member
    SET teamOwnerID = 3
    WHERE memberID = 3;

INSERT INTO `address` (`city`,`country`,`postalCode`, `state`)
    VALUES ('Heaven','Israel','983457348','NONE');

INSERT INTO `member` (`username`,`passwordHash`,`name`, `email`, `addressID`)
    VALUES ('assocAgent yarin','c424197946a55fcc400a20811aeaeeda3d72d71d','Yarin Nahum', 'yarina@post.bgu.ac.il', 6);

INSERT INTO association_agent (memberID)
    VALUES (4);

INSERT INTO league (league_name)
    VALUES ('Base League');

INSERT INTO season (seasonYear)
    values (2020);

INSERT INTO policy (policyID)
    VALUES (1);

INSERT INTO seasonInLeague (seasonYear, leagueID, PolicyID)
    VALUES (2020, 1, 1);

UPDATE policy
    SET seasonYear = 2020 AND leagueID = 1
    WHERE policyID = 1;

INSERT INTO `address` (`city`,`country`,`postalCode`, `state`)
    VALUES ('Holon','Israel','384765534','NONE');

INSERT INTO `member` (`username`,`passwordHash`,`name`, `email`, `addressID`)
    VALUES ('main Referee asaf','1608f3ee993f91f37818451c218b393896f214dd','asaf zosman', 'asafzos@post.bgu.ac.il', 7);

INSERT INTO referee (qualification, memberID)
    VALUES ('qualified main', 5);

INSERT INTO refereesInSIL (seasonYear, leagueID, refereeID)
    VALUES (2020, 1, 1);

INSERT INTO game (date, host_teamID, guest_teamID, main_refereeID, seasonYear, leagueID, fieldID)
    VALUES ('2020-08-20', 1, 2, 1, 2020, 1, 1);

INSERT INTO `member` (`username`,`passwordHash`,`name`, `email`, `addressID`)
    VALUES ('side Referee asaf1','63ee39facef3e951eddbfad442b8624a811563e0','asaf1 zosman', 'asafzos1@post.bgu.ac.il', 7);

INSERT INTO `member` (`username`,`passwordHash`,`name`, `email`, `addressID`)
    VALUES ('side Referee asaf2','5f18c71e0494f65f47f52d0984644110b85a7af2','asaf2 zosman', 'asafzos2@post.bgu.ac.il', 7);

INSERT INTO referee (qualification, memberID)
    VALUES ('qualified side1', 6);

INSERT INTO referee (qualification, memberID)
    VALUES ('qualified side', 7);

INSERT INTO refereesInSIL (seasonYear, leagueID, refereeID)
    VALUES (2020, 1, 2);

INSERT INTO refereesInSIL (seasonYear, leagueID, refereeID)
    VALUES (2020, 1, 3);

INSERT INTO teamsInSIL (seasonYear, leagueID, teamID)
    VALUES (2020, 1, 1);

INSERT INTO teamsInSIL (seasonYear, leagueID, teamID)
    VALUES (2020, 1, 2);