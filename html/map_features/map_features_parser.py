from bs4 import BeautifulSoup
import os.path
import sys
import io

out_dir = "../../app/src/main/assets/map_features"


def download(url, name):
    if os.path.isfile(name):
	print(name + " exists") 
    else:
        print("Manually open and save \"" + url + "\" to")
	print ( "\"" + name + "\" from a webbrowser")    


def parse_table(table, out):

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
		out.write("<p>[<b>"+ key + "</b>=<b>" + value + "</b>]<br>" + description + "</p>\n")
	    i = i+1	

    return e



download("https://wiki.openstreetmap.org/wiki/Map_Features", "map_features.html")


fp = io.open("map_features.html", mode="r", encoding="utf-8")
soup = BeautifulSoup(fp, "lxml")

for h3 in soup.findAll('h3'):
        
	for span in h3.findAll('span', attrs={"class" : "mw-headline"}):

		title = span.string.strip()
		out = io.open(out_dir + "/" + title, mode="w", encoding="utf-8")
		out.write("<h1>" + title + "</h1>\n")


		haveD = 0
		haveT = 0

		
		for sibling in h3.next_siblings:
			if sibling.name == 'p' and haveD == 0:
				text = sibling.get_text()
				out.write("<p>" + text + "</p>\n")
				haveD = 1
				

			elif sibling.name == 'table' and haveT == 0:
				table = sibling
    				haveT = parse_table(table, out)

			elif sibling.name == 'div' and haveT == 0:
				table = sibling.table
				if table is not None:
					haveT = parse_table(table, out)
				
			elif sibling.name == 'h3':
				break
		
			elif haveT > 0 and haveD > 0:
				break	


		if haveD > 0:
			print(title + " " + str(haveT))
