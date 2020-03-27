#!/usr/bin/env python3

from bs4 import BeautifulSoup
import os.path
import sys
import io

# Parses "map_features.html" for OpenStreetMap features and stores them in 
# topic-files inside the assets folder of the app.



# "map_features.html" has to be downloaded manually from this url:
in_url  = "https://wiki.openstreetmap.org/wiki/Map_Features"
in_file = "map_features.html"


# Assets directory for map features topic-files. 
out_dir = "../../app/src/main/assets/map_features"



# Just print out download instructions
def download(url, file):
    if os.path.isfile(file):
        print(file + " exists")
        return True
    else:
        print("You have to manually download \"" + url + "\" to \"" + file + "\"")
        return False



# Parse the map features table and write into out-file
def parse_table(table, ostream):

    e = 0
    for tr in table.find_all('tr'):
            
        i=0
        # 0: key 
        # 1: value 
        # 2: type 
        # 3: comment
        value = ""
        key = ""
        for td in tr.find_all('td'):
            if (i==0):
                key=td.get_text().strip()
            if (i==1):
                value=td.get_text().strip()
            if (i==2):
                e = e + 1

            if (i==3):
                description = td.get_text().strip()
                ostream.write("<p>[<b>"+ key + "</b>=<b>" + value + "</b>]<br>" + description + "</p>\n")
            
            i = i+1    

    return e



# Parse span (one topic) and print a status report
def parse_span(h3, span):
    title = span.string.strip()
    
    out_file = out_dir + "/" + title
       
    ostream = io.open(out_file, mode="w", encoding="utf-8")
    ostream.write("<h1>" + title + "</h1>\n")

    haveD = 0   # Description of topic
    haveT = 0   # Count of key value pairs from table

    for sibling in h3.next_siblings:
        if sibling.name == 'p' and haveD == 0:
            text = sibling.get_text()
            ostream.write("<p>" + text + "</p>\n")
            haveD = 1
                

        elif sibling.name == 'table' and haveT == 0:
            table = sibling
            haveT = parse_table(table, ostream)

        elif sibling.name == 'div' and haveT == 0:
            table = sibling.table
            if table is not None:
                haveT = parse_table(table, ostream)
                
        elif sibling.name == 'h3':
            break
        
        elif haveT > 0 and haveD > 0:
            break    

    print("Generated [" + str(haveD) + "|" + "{:3d}".format(haveT) + "] " + out_file)
 
        

# Parse html file (start with h3 tags)
def parse(soup):
    for h3 in soup.findAll('h3'):
        for span in h3.findAll('span', attrs={"class" : "mw-headline"}):
            parse_span(h3, span)
            
            
            
# Open and return parser
def getSoup(file):
    istream = io.open(in_file, mode="r", encoding="utf-8")
    return BeautifulSoup(istream, "lxml")


# Remove old topic-files in assets directory
def removeFiles(directory):
    files = [ f for f in os.listdir(directory) ]
    for f in files:
        path = os.path.join(directory, f)

        if (os.path.isfile(path) and not os.path.islink(path)):
            print("Remove " + path)
            os.remove(path)

            
# Starting point
if download(in_url, in_file):
    removeFiles(out_dir)
    parse(getSoup(in_file))


