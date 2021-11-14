#!/usr/bin/env python3

from os.path import exists

input_file_release = '../../aat-android/build/intermediates/runtime_symbol_list/release/R.txt'
input_file = '../../aat-android/build/intermediates/runtime_symbol_list/debug/R.txt'

output_file = '../../aat-android/src/main/java/ch/bailu/aat/resource/Images.java'

if (exists(input_file_release)):
    input_file = input_file_release



# Using readlines()
in_file = open(input_file, 'r')
out_file = open(output_file, 'w')

lines = in_file.readlines()

out_file.write('/**\n')
out_file.write('    This file was autogenerated by \'aat-lib/util/generate_img.py\'\n')
out_file.write('*/\n')
out_file.write('package ch.bailu.aat.resource;\n\n\n')
out_file.write('import ch.bailu.aat.R;\n\n')
out_file.write('public final class Images {\n')

out_file.write('    public static int get(String name) {\n')
out_file.write('        switch(name) {\n')
 
count = 0
for line in lines:
    count += 1
    words = line.split()
    
    if (len(words) > 3):
        t = words[1]
        
        if words[1] == "drawable":
            name = words[2]
            number = words[3]
       
            print("{}: {}, {}".format(count, name, number))
            
            out_file.write('            case "' + name + '":\n')
            out_file.write('                return R.drawable.' + name + ';\n')

out_file.write('        }\n')
out_file.write('        return 0;\n')
out_file.write('    }\n')

out_file.write('}\n') 

        
   
in_file.close()
out_file.close()