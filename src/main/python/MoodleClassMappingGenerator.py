#!/usr/bin/python

import csv, sys

csv.register_dialect ('escapedcsv', escapechar='\\', skipinitialspace=True, quoting=csv.QUOTE_NONE)

classmaps = open (sys.argv[1])
for row in csv.reader (classmaps, 'escapedcsv'):
    print '\t<entity name="%s" class="%s" metadata-complete="true">\n\t\t<table name="%s">\n\t</entity>' % (row[0], row[0], row[1])
