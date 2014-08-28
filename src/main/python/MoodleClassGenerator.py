#!/usr/bin/python

import csv, sys

javacode = '''package ca.uoguelph.socs.icc.moodleapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class %s extends %s
{
	protected %s ()
	{
		super ();
	}

	@Override
	public int hashCode ()
	{
		final int base = %s;
		final int mult = %s;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
'''

classdefs = open (sys.argv[1])

csv.register_dialect ('escapedcsv', escapechar='\\', skipinitialspace=True, quoting=csv.QUOTE_NONE)

for row in csv.reader (classdefs, 'escapedcsv'):
    codefile = open ("%s.java" % row[0], "w+")
    codefile.write (javacode % (row[0], row[1], row[0], row[2], row[3]))
    codefile.close ()

classdefs.close ()
