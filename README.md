# LMSDataHarvester

Extract data from a Learning Management System (LMS), to a database suitable
for research.  Learning Management Systems, such as
[Moodle](http://www.moodle.org), 

## Dependencies

In order to build the Harvester, the following software packages are required:

* java version 1.8 (Java 8) or newer
* maven version 3 or newer

## Building

To build the Harvester execute the following commands, from the root directory
of this repository:

    mvn clean
    mvn package

The result of the build process is a jar file named _edm-1.0.jar_.  It can be
found in the _target/lib_ subdirectory, along with the jar files for all of its
dependencies.

To build the javadoc's execute the following command:

    mvn javadoc:jar

The result will be a jar file named _edm-1.0-javadoc.jar_ which will be located
in the _target_ directory.

**Note:** Due to a [bug](https://issues.apache.org/jira/browse/MCOMPILER-236) in
the maven compiler plugin, this project must always be cleaned before it is
re-compiled.  Failure to clean the project first will result in the java
compiler terminating with an IllegalStateException.

## Running

Once it is built, the Harvester may be invoked by executing the following
command:

    java -jar target/lib/edm.jar /path/to/processing_file.xml

Before invoking the Harvester, the destination database needs to be initialized,
the database connections parameters need to be set in the profiles, and a
processing file needs to be created.

### Initializing the destination database

Prior to invoking the harvester for the first time, the destination database
needs to be initialized.  The destination database can be initialized by loading
both the course and user schema's into a new database.  The schema's are located
in _src/main/sql_.

### Configuring the database connection parameters

To configure the database connection parameters, the profiles for the source and
destination data-stores need to be created with the proper database connection
information.  Sample profile configurations can be found in the _conf_
directory.

### Creating the processing file

For each run, the Harvester requires a processing file.  The processing file
contains references to the data-stores, the id number of the course to process
and references to the CSV files containing the registration data for all of the
users enrolled in the course.  A sample processing file is provided in
_conf/Harvester.xml_.

## Limitations

There are a few limitations to be aware of when using the Harvester to extract
data:

* The Harvester does not initialize the destination database.

* Extracting grades from the source (Moodle) database is not, currently,
  implemented.

* Enrolments are not extracted from the source (Moodle) database, and need to be
  supplied separately.  Moodle and the Harvester treat enrolments differently.
  The Harvester sees an enrolment as permanent, while Moodle sees an enrolment
  as transient.  As a result, enrolments can not be extracted from the Moodle
  database reliably.

* The Harvester can only process the data from one course per invocation.  This
  is artificial limitation imposed for performance.  A large course will
  a lot of records, which in turn consumes a lot of memory.  Also, a large
  number of records has a significant negative impact of query performance,
  which results in the entire application running very slowly.

## Where to find additional documentation

The Harvester has a complete set of javadoc's, which contains the programming
information as well as some design details, particularly regarding the
extraction of data from the Moodle database.  Some additional documentation for
the Harvester may be found in the Wiki.

## Licence

    Copyright (C) 2014, 2015, 2016 James E. Stark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
