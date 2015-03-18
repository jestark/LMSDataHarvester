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
        params ["Builder"] = node.attrib ["builder"]
        
        if (node.tag == "activity"):
            entry = "Named"

            params ["ClassName"] = node.attrib ["name"]
            params ["ActivityType"] = node.attrib ["type"]

            for child in node:
                tag = processNode (child, params, template)

                if (tag == "subactivity"):
                    entry = "Group"

        elif (node.tag == "subactivity"):
            entry = "Member"

            params ["ParentClass"] = params ["ClassName"]
            params ["ClassName"] = node.attrib ["name"]

            for child in node:
                tag = processNode (child, params, template)

                if (tag == "subactivity"):
                    entry = "SubGroup"

        elif (node.tag == "log"):
            entry = "Log"

            params ["ActivityClass"] = params ["ClassName"]
            params ["ClassName"] = node.attrib ["name"]

        else:
            print "Unknown tag: %s" % (node.tag)

        writeCode ("moodle/%s" % (params ["ClassName"]), (template [entry]).substitute (params));

    return node.tag


template = dict ()
template ["Named"] = loadTemplate ("templates/java/NamedActivity.java")
template ["Group"] = loadTemplate ("templates/java/NamedActivityGroup.java")
template ["Member"] = loadTemplate ("templates/java/SubActivity.java")
template ["SubGroup"] = loadTemplate ("templates/java/SubActivityGroup.java")
template ["Log"] = loadTemplate ("templates/java/Log.java");

config = xml.etree.ElementTree.parse (sys.argv[1]);

processNode (config.getroot (), dict (), template)
