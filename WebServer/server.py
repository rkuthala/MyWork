from socket import *
from os import path, curdir, sep

from request import *
from response import *

serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)    
#serverSocket.bind((gethostname(), 8094))
serverSocket.bind(("0.0.0.0", 8093))
serverSocket.listen(5)

while True: 
    connectionSocket, addr = serverSocket.accept()
    responseObj = response()
    responseCode = 200
    mimetype = 'text/plain'
    try: 
        message = connectionSocket.recv(4096)
        requestObj = request(message)
        reqParsed = requestObj.parse();

        if reqParsed == False:
            raise Exception("Server Error")

        resource = requestObj.getResource()
        if resource == "/":
            resource = "/index.html"
           
        fileExists = path.isfile(curdir + sep + resource)

        if fileExists == False:
            raise IOError("File Not Found")

        responseCode = 200    
        
        if resource.endswith(".html"):
            mimetype='text/html'
        elif resource.endswith(".jpg"):
            mimetype='image/jpg'
        elif resource.endswith(".png"):
            mimetype='image/png'
        elif resource.endswith(".gif"):
            mimetype='image/gif'
        elif resource.endswith(".js"):
            mimetype='application/javascript'
        elif resource.endswith(".css"):
            mimetype='text/css'
        elif resource.endswith(".pdf"):
            mimetype='application/pdf'
        else:
            mimetype='text/plain'
            
        filename = curdir + sep + resource
        with open(filename, "rb") as f:
            responseContent = f.read()
         
    except IOError, e:
        responseCode = 404
        mimetype = "text/html"
        responseContent = "<h1>File Not Found</h1>"
            
    except Exception,  e:
        responseCode = 500
        mimetype = "text/html"
        responseContent = "<h1>Internal Server Error</h1>"
        
    except:
        print 'Un handled exception'

    responseObj.setResponseLine(responseCode)
    responseObj.setHeader("Content-type", mimetype) 
    responseObj.setContent(responseContent)
    connectionSocket.send(responseObj.toString())
    connectionSocket.close()

serverSocket.close()
