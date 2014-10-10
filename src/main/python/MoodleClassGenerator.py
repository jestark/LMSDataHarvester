#!/usr/bin/python

import csv, sys, string

def loadTemplate (filename):
    templatefile = open (filename)
    template = string.Template (templatefile.read ())
    templatefile.close ()

    return template

def processActivity (params):
    result = dict ()

    result["Package"] = "moodle"
    result["ActivityClass"] = params[0]
    result["ActivityBaseClass"] = params[1]
    result["ActivityBaseParameters"] = ""
    result["HashBase"] = params[2]
    result["HashMult"] = params[3]

    return result

def processActivityOneParam (params):
    result = processActivity (params)
    result["ActivityBaseParameters"] = "<%s>" % (params[4])

    return result

def processActivityTwoParams (params):
    result = processActivity (params)
    result["ActivityBaseParameters"] = "<%s, %s>" % (params[4], params[5])

    return result

def loadClassDefinitions (filename):
    csv.register_dialect ('escapedcsv', escapechar='\\', skipinitialspace=True, quoting=csv.QUOTE_NONE)
    result = {"GenericNamedActivity": dict (), "GenericGroupedActivity": dict (), "GenericGroupedActivityGroup": dict (), "GenericGroupedActivityMember": dict ()}

    classdefs = open (filename)
    for row in csv.reader (classdefs, 'escapedcsv'):
        if (row[1] == "GenericNamedActivity"):
                result["GenericNamedActivity"][row[0]] = processActivity (row)
        elif (row[1] == "GenericGroupedActivity"):
            result["GenericGroupedActivity"][row[0]] = processActivityOneParam (row)
        elif (row[1] == "GenericGroupedActivityGroup"):
            result["GenericGroupedActivityGroup"][row[0]] = processActivityTwoParams (row)
            result["GenericGroupedActivityGroup"][row[0]]["LogClass"] = row[6]
        elif (row[1] == "GenericGroupedActivityMember"):
            result["GenericGroupedActivityMember"][row[0]] = processActivityOneParam (row)
            result["GenericGroupedActivityMember"][row[0]]["LogClass"] = row[5]
        else:
            print "Invalid Base Class: %s" % (row[1])
            exit (1)
    
    classdefs.close ()

    return result

def writeCode (filename, filedata):
    codefile = open ("%s.java" % (filename), "w+")
    codefile.write (filedata)
    codefile.close ()

activitytemplate = loadTemplate ("templates/java/Activity.java")
logtemplate = loadTemplate ("templates/java/Log.java")

classdefs = loadClassDefinitions (sys.argv[1])

for base in sorted (classdefs):
    for activity in sorted (classdefs[base]):
        writeCode (classdefs[base][activity]["ActivityClass"], activitytemplate.substitute (classdefs[base][activity]))

        if ("LogClass" in classdefs[base][activity]):
            writeCode (classdefs[base][activity]["LogClass"], logtemplate.substitute (classdefs[base][activity]))

