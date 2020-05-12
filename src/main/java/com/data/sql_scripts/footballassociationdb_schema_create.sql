use footballassociationdb;

-- create Complaint table
CREATE TABLE complaint(
complaintID int NOT NULL AUTO_INCREMENT,
complainterID int NOT NULL,
complaint_data varchar (255),
complaint_answer varchar (255),
was_answerd boolean DEFAULT 0,
primary key (complaintID)
);

-- create Address table
CREATE TABLE address(
addressID int NOT NULL AUTO_INCREMENT,
country varchar (255),
state varchar (255),
city varchar (255),
postalcode varchar (255),
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
CONSTRAINT  `fk_member` FOREIGN KEY (`memberID`)
REFERENCES `member` (`memberID`)
);

-- create AssociationAgent table
CREATE TABLE association_agent(
associationAgentID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
-- FOREIGN KEYS
memberID int NOT NULL,
-- CONSTRAINTS
CONSTRAINT `fk_member` FOREIGN KEY (`memberID`)
REFERENCES `member` (`memberID`)
);

-- create Player table
CREATE TABLE player(
playerID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
birth_date date,
-- FOREIGN KEYS
memberID int NOT NULL,
-- CONSTRAINTS
CONSTRAINT `fk_member` FOREIGN KEY (`memberID`)
REFERENCES `member` (`memberID`)
);

-- create Referee table
CREATE TABLE referee(
refereeID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
qualification varchar (255),
-- FOREIGN KEYS
memberID int NOT NULL,
-- CONSTRAINTS
CONSTRAINT `fk_member` FOREIGN KEY (`memberID`)
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
CONSTRAINT `fk_member` FOREIGN KEY (`memberID`)
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
CONSTRAINT `fk_appointer` FOREIGN KEY (`appointerID`)
REFERENCES `team_owner` (`teamOwnerID`),
CONSTRAINT `fk_member` FOREIGN KEY (`memberID`)
REFERENCES `member` (`memberID`)
);

-- adding foreign key to TeamManager table
ALTER TABLE team_manager
ADD CONSTRAINT `fk_appointer` FOREIGN KEY (`appointerID`)
REFERENCES `team_owner` (`teamOwnerID`);

-- adding roles foreign keys to Member table
ALTER TABLE member
ADD CONSTRAINT `fk_coach` FOREIGN KEY (`coachID`)
REFERENCES `coach` (`coachID`),
ADD CONSTRAINT `fk_player` FOREIGN KEY (`playerID`)
REFERENCES `player` (`playerID`),
ADD CONSTRAINT `fk_referee` FOREIGN KEY (`refereeID`)
REFERENCES `referee` (`refereeID`),
ADD CONSTRAINT `fk_teamManager` FOREIGN KEY (`teamManagerID`)
REFERENCES `team_manager` (`teamManagerID`),
ADD CONSTRAINT `fk_teamOwner` FOREIGN KEY (`teamOwnerID`)
REFERENCES `team_owner` (`teamOwnerID`),
ADD CONSTRAINT `fk_associationAgent` FOREIGN KEY (`associationAgentID`)
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
ADD CONSTRAINT `fk_team` FOREIGN KEY (`teamID`)
REFERENCES `team` (`teamID`);

-- adding TeamManager team foreign key
ALTER TABLE team_manager
ADD CONSTRAINT `fk_team` FOREIGN KEY (`teamID`)
REFERENCES `team` (`teamID`);

-- create CoachInTeam table
CREATE TABLE coach_in_team(
-- FOREIGN KEYS
coachID int NOT NULL,
teamID int NOT NULL,
-- CONSTRAINTS
CONSTRAINT `fk_coach` FOREIGN KEY (`coachID`)
REFERENCES `coach` (`coachID`),
CONSTRAINT `fk_team` FOREIGN KEY (`teamID`)
REFERENCES `team` (`teamID`),
PRIMARY KEY (coachID, teamID)
);

-- create PlayerInTeam table
CREATE TABLE player_in_team(
-- FOREIGN KEYS
playerID int NOT NULL,
teamID int NOT NULL,
-- CONSTRAINTS
CONSTRAINT `fk_player` FOREIGN KEY (`playerID`)
REFERENCES `player` (`playerID`),
CONSTRAINT `fk_team` FOREIGN KEY (`teamID`)
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
CONSTRAINT `fk_team` FOREIGN KEY (`teamID`)
REFERENCES `team` (`teamID`),
CONSTRAINT `fk_address` FOREIGN KEY (`addressID`)
REFERENCES `address` (`addressID`)
);
