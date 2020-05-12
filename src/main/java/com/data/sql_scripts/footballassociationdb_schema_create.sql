use footballassociationdb;

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
    coachID int,
    teamOwnerID int,
    teamManagerID int,
    playerID int,
    refereeID int,
    associationAgentID int,
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
    teamID int,
    appointerID int,
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
    teamStatus TINYINT (1) DEFAULT 1,
    -- FOREIGN KEYS
    fieldID int
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
    teamID int,
    addressID int NOT NULL,
    -- CONSTRAINTS
    CONSTRAINT `fk_team_field` FOREIGN KEY (`teamID`)
    REFERENCES `team` (`teamID`),
    CONSTRAINT `fk_address_field` FOREIGN KEY (`addressID`)
    REFERENCES `address` (`addressID`)
);

-- create Game table
-- TODO add foreign keys to season, league
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
    seasonID int NOT NULL ,
    main_refereeID int NOT NULL ,
    leagueID int NOT NULL ,
    fieldID int NOT NULL ,
    -- CONSTRAINTS
    CONSTRAINT `fk_host_team_game` FOREIGN KEY (`host_teamID`)
                 REFERENCES `team` (`teamID`),
    CONSTRAINT `fk_guest_team_game` FOREIGN KEY (`guest_teamID`)
                 REFERENCES `team` (`teamID`),
    CONSTRAINT `fk_referee_game` FOREIGN KEY (`main_refereeID`)
                 REFERENCES `referee` (`refereeID`),
    CONSTRAINT `fk_field_game` FOREIGN KEY (`fieldID`)
                 REFERENCES `field` (`fieldID`)
);

-- create SizeRefereeInGame table
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

CREATE TABLE league(
    leagueID int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    league_name varchar(255) NOT NULL
);


CREATE TABLE season(
  seasonID int NOT NULL PRIMARY KEY
);

CREATE TABLE rankingPolicy(
    rankingPolicyID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    win int NOT NULL,
    goals int NOT NULL,
    draw int NOT NULL,
    yellowCards int NOT NULL,
    redCards int NOT NULL,
    -- FOREIGN KEYS
    policyKey int NOT NULL
);

CREATE TABLE policy(
    gameSettingPolicy tinyint NOT NULL,
    -- FOREIGN KEYS
    leagueID int NOT NULL,
    seasonID int NOT NULL,
    rankingPolicyID int NOT NULL,
    -- CONSTRAINTS
    CONSTRAINT `fk_rankingPolicy_policy` FOREIGN KEY (`rankingPolicyID`)
    REFERENCES `rankingPolicy` (`rankingPolicyID`),
    CONSTRAINT `fk_league_policy` FOREIGN KEY (`leagueID`)
    REFERENCES `league` (`leagueID`),
    CONSTRAINT `fk_season_policy` FOREIGN KEY (`seasonID`)
    REFERENCES `season` (`seasonID`),
    -- PRIMARY KEY
    PRIMARY KEY (leagueID, seasonID)
);

ALTER TABLE rankingPolicy
    ADD CONSTRAINT `fk_policy_rankingPolicy` FOREIGN KEY (`policyKey`)
    REFERENCES `policy` (`leagueID`, `seasonID`);