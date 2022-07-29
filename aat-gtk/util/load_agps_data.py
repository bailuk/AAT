#!/usr/bin/python3
#
# load_agps_data
# from https://gist.github.com/alastair-dm/263209b54d01209be28828e555fa6628
#
# proof of concept loading of agps data to quectel modem via AT commands

from datetime import datetime, timezone
import requests
import serial

at_port = '/dev/ttyUSB2' # ttyUSB2 is the command port on mobian - think they renamed via udev on PmOS
agps_url = 'http://xtrapath1.izatcloud.net/xtra.bin'
agps_file = 'RAM:xtra.bin'

def sendCmd(cmdstring):
    print(cmdstring)
    ser.write(f"{cmdstring}\r\n".encode('ascii'))
    res =  []
    while True:
        line = ser.readline()
        if (line == b''):
            break
        else:
            res.append(line)
    print(res)
    return res

def setTime():
    now = datetime.utcnow().replace(tzinfo=timezone.utc).strftime('%Y/%m/%d,%H:%M:%S')
    return sendCmd(f"AT+QGPSXTRATIME=0,\"{now}\"")

# is ModemManager running? Won't work if it's already got the port
print('open serial port')
ser = serial.Serial(at_port, timeout=1)
print('Check modem will talk to us')
res = sendCmd('AT')
# FIXME - expect response OK - really ought to check this and other responses
## is GPS turned on? If so the later stuff won't work so we'll have to turn it off
print('Is GPS turned on?')
res = sendCmd('AT+QGPS?')
print('Turn off GPS')
res = sendCmd('AT+QGPSEND') # this ought to be sent only if the gps was on...
print('Is AGPS enabled?')
res = sendCmd('AT+QGPSXTRA?')
print('Precautionary enabling of AGPS')
res = sendCmd('AT+QGPSXTRA=1')
print('Is valid AGPS data already loaded?')
res = sendCmd('AT+QGPSXTRADATA?')
# FIXME: check the returned stuff
# actually this check seems unnecessary as the returned value is alway the same
# so doesn't help (and doesn't match the docs)
# fetch http://xtrapath[1-3].izatcloud.net/xtra(2).bin and upload
r = requests.get(agps_url)
#FIXME: check r.status_code before doing anything with the data
print('download status: ', r.status_code)
# precautionary delete of the target file - upload will fail if file name in use
print('delete previous AGPS data file')
res = sendCmd(f"AT+QFDEL=\"{agps_file}\"")
print('upload new AGPS data')
res = sendCmd(f"AT+QFUPL=\"{agps_file}\",{len(r.content)}")
# FIXME: check res == CONNECT and don't send if it's not
ser.write(r.content)
res = []
while True:
    line = ser.readline()
    if (line == b''):
        break
    else:
        res.append(line)
print(res)
#FIXME: calculate agps file checksum and check that it matches the response
# we'll assume here that local system time is kept accurate either by NTP or
# phone network time, so we don't need to fetch SNTP time
print('set current UTC time using local system time')
res = setTime()
print('set AGPS data to file we uploaded')
res = sendCmd(f"AT+QGPSXTRADATA=\"{agps_file}\"")
print('what does it day about data validity now?')
res = sendCmd('AT+QGPSXTRADATA?')
print('NOTE: it\'s given us the same response as before, despite having new data uploaded')
print('enable gps')
res = sendCmd('AT+QGPS=1')
print('close serial port')
if ser.is_open:
    ser.close()
