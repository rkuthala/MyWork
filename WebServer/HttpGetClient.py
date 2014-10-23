import sys
import socket

resource = "/"
if(len(sys.argv) < 3):
    print "usage: HttpGetClient.py <host> <port> <filename>"
    exit(0)

if( len(sys.argv) > 3 ):
    resource = sys.argv[3]

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((sys.argv[1], int(sys.argv[2])))
s.send("GET " + resource + " HTTP/1.1\r\n")
data = s.recv(4096)
s.close()
print
print data
