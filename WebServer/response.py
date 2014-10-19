import StringIO
class response:
    httpVersion = "HTTP/1.1"
    responseMessage = {
        200: 'OK',
        404: 'Not Found',
        405: 'Method Not Allowed',
        500: 'Internal Server Error',
        }
        
    def __init__(self):
        self.response = StringIO.StringIO()
        self.headers = {}
        
    def setResponseLine(self, code):
        self.code = code;
        
    def setHeader(self, name, value):
        self.headers[name] = value
                
    def setContent(self, content):
        self.content = content

    def toString(self):
        self.response.write("%s %s %s\r\n" % (self.httpVersion, self.code, self.responseMessage[self.code]))

        self.keys = self.headers.keys()
        for self.key in self.keys:
            self.response.write("%s: %s\r\n" % (self.key, self.headers[self.key]))

        self.response.write("\r\n")
        self.response.write(self.content)
        returnVal = self.response.getvalue()
        self.response.close()
        return returnVal
