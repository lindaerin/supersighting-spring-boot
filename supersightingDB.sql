DROP DATABASE IF EXISTS supersightingDB;
CREATE DATABASE supersightingDB;

USE supersightingDB;

CREATE TABLE superpower(
	superpowerId INT PRIMARY KEY AUTO_INCREMENT,
    superpowerName VARCHAR(50) NOT NULL
);


CREATE TABLE hero(
	heroId INT PRIMARY KEY AUTO_INCREMENT,
    heroName VARCHAR(50) NOT NULL,
    heroDescription VARCHAR(200),
    superpowerId INT NOT NULL,
    FOREIGN KEY(superpowerId) REFERENCES superpower(superpowerid)
);

CREATE TABLE location(
	locationId INT PRIMARY KEY AUTO_INCREMENT,
    locationName VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    address VARCHAR(200),
    longitude DOUBLE NOT NULL,
    latitude DOUBLE NOT NULL
);

CREATE TABLE sighting(
	sightingId INT PRIMARY KEY AUTO_INCREMENT,
    sightingDate DATETIME NULL,
    heroId INT NOT NULL,
    locationId INT NOT NULL,
    FOREIGN KEY(heroId) REFERENCES hero(heroId),
    FOREIGN KEY(locationId) REFERENCES location(locationId)
);

CREATE TABLE organization(
	organizationId INT PRIMARY KEY AUTO_INCREMENT,
    organizationName VARCHAR(50) NOT NULL,
    organizationDescription VARCHAR(200),
    organizationAddress VARCHAR(100) NOT NULL
);

CREATE TABLE hero_organization(
	heroId INT NOT NULL,
    organizationId INT NOT NULL,
    PRIMARY KEY(heroId, organizationId),
    FOREIGN KEY(heroId) REFERENCES hero(heroId),
    FOREIGN KEY(organizationId) REFERENCES organization(organizationId)
);

INSERT INTO superpower(superpowerName) VALUES('Teleport');
INSERT INTO hero(heroName, heroDescription, superpowerId) VALUES('Wanda', 'Vison', 1);
INSERT INTO organization(organizationName, organizationDescription, organizationAddress) VALUES('Blank HQ', 'Looking for hires', 'Some place anywhere');
INSERT INTO hero_organization(heroId, organizationId) VALUES(1, 1);
