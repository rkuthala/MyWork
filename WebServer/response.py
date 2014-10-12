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
        
    def setResponseLine(self, code):
        self.response.write("%s %s %s\r\n" % (self.httpVersion, code, self.responseMessage[code]))
        
    def setHeader(self, name, value):
        self.response.write("%s: %s\r\n" % (name, value))
                
    def setContent(self, content):
        self.response.write("\r\n")
        self.response.write(content)

    def toString(self):
        returnVal = self.response.getvalue()
        self.response.close()
        return returnVal
