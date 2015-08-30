#!/usr/bin/python

import sys, string, xml.etree.ElementTree

def loadTemplate (filename):
    templatefile = open (filename)
    template = string.Template (templatefile.read ())
    templatefile.close ()

    return template

def writeCode (filename, filedata):
    codefile = open ("%s.java" % (filename), "w+")
    codefile.write (filedata)
    codefile.close ()

def processNode (node, data, template):
    params = data.copy ()
    entry = ""

    if (node.tag == "source"):
        params ["ActivitySource"] = node.attrib ["name"]

        for child in node:
            processNode (child, params, template)

    else:
        params ["HashBase"] = node.attrib ["base"]
        params ["HashMult"] = node.attrib ["mult"]

        if (node.tag == "activity"):
            entry = "Activity"

            params ["ClassName"] = node.attrib ["name"]
            params ["ActivityType"] = node.attrib ["type"]

            for child in node:
                tag = processNode (child, params, template)

        elif (node.tag == "subactivity"):
            entry = "SubActivity"

            params ["ParentClass"] = params ["ClassName"]
            params ["ClassName"] = node.attrib ["name"]

            for child in node:
                tag = processNode (child, params, template)

        elif (node.tag == "log"):
            entry = "Log"

            params ["SubActivityClass"] = params ["ClassName"]
            params ["ClassName"] = node.attrib ["name"]

        else:
            print "Unknown tag: %s" % (node.tag)

        writeCode ("moodle/%s" % (params ["ClassName"]), (template [entry]).substitute (params));

    return node.tag


template = dict ()
template ["Activity"] = loadTemplate ("templates/java/Activity.java")
template ["SubActivity"] = loadTemplate ("templates/java/SubActivity.java")
template ["Log"] = loadTemplate ("templates/java/Log.java");

config = xml.etree.ElementTree.parse (sys.argv[1]);

processNode (config.getroot (), dict (), template)
