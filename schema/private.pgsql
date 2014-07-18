-- Schema for the private database.  Documentation for the schema is on the 
-- wiki.

-- Identifying information for students in the public database.
CREATE TABLE IF NOT EXISTS STUDENTS (
	ID INTEGER PRIMARY KEY,
	STUDENTID INTEGER UNIQUE NOT NULL,
	LOGINID VARCHAR[8] NOT NULL
);
