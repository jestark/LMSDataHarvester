-- Schema for the private database.  Documentation for the schema is on the
-- wiki.

-- Identifying information for students in the public database.
CREATE TABLE IF NOT EXISTS USER (
	IDNUMBER INTEGER PRIMARY KEY,
	LOGINID VARCHAR[8] NOT NULL,
	GIVENNAME TEXT NOT NULL,
	SURNAME TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS USERENROLLMENTS (
	IDNUMBER INTEGER NOT NULL REFERENCES USER(IDNUMBER) DELETE RESTRICT ON UPDATE CASCADE,
	ENROLMENTID INTEGER NOT NULL UNIQUE,
	PRIMARY KEY (IDNUMBER, ENROLMENTID)
);
