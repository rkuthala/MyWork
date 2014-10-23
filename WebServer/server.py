from socket import *
from os import path, curdir, sep
import threading

from request import *
from response import *

serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)    
#serverSocket.bind((gethostname(), 8094))
serverSocket.bind(("0.0.0.0", 8093))
serverSocket.listen(5)

class mythread(threading.Thread):

    def __init__(self, socket, addr):
        threading.Thread.__init__(self)
        self.socket = socket
        self.addr = addr

    def run(self):
        self.responseObj = response()
        self.responseCode = 200
        self.mimetype = 'text/plain'
        try:
            self.message = self.socket.recv(4096)
            self.requestObj = request(self.message)
            self.reqParsed = self.requestObj.parse();

            if self.reqParsed == False:
                raise Exception("Server Error")

            self.resource = self.requestObj.getResource()
            if self.resource == "/":
                self.resource = "/index.html"

            self.fileExists = path.isfile(curdir + sep + self.resource)

            if self.fileExists == False:
                raise IOError("File Not Found")

            self.responseCode = 200
            
            if self.resource.endswith(".html"):
                self.mimetype='text/html'
            elif self.resource.endswith(".jpg"):
                self.mimetype='image/jpg'
            elif self.resource.endswith(".png"):
                self.mimetype='image/png'
            elif self.resource.endswith(".gif"):
                self.mimetype='image/gif'
            elif self.resource.endswith(".js"):
                self.mimetype='application/javascript'
            elif self.resource.endswith(".css"):
                self.mimetype='text/css'
            elif self.resource.endswith(".pdf"):
                self.mimetype='application/pdf'
            else:
                self.mimetype='text/plain'
            
            self.filename = curdir + sep + self.resource
            with open(self.filename, "rb") as self.f:
                self.responseContent = self.f.read()
        
        except IOError, e:
            self.responseCode = 404
            self.mimetype = "text/html"
            self.responseContent = "<h1>File Not Found</h1>"

        except Exception,  e:
            self.responseCode = 500
            self.mimetype = "text/html"
            self.responseContent = "<h1>Internal Server Error</h1>"

        except:
            print 'Unhandled exception'

        self.responseObj.setResponseLine(self.responseCode)
        self.responseObj.setHeader("Content-type", self.mimetype) 
        self.responseObj.setContent(self.responseContent)
        self.socket.send(self.responseObj.toString())
        self.socket.close()
        print self.addr, " requested resource '", self.resource, "' response code: ", self.responseCode

print "Server started"
while True:
    connectionSocket, addr = serverSocket.accept()
    print "Serving client (%s, %s)" % addr

    mythread(connectionSocket, addr).start()

serverSocket.close()
