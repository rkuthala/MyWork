import re
class request:
    def __init__(self, message):
        self.message = message
        self.method = ""
        self.resource = ""

    def parse(self):
        requestline = re.split('\\r\\n', self.message)[0]
        try:
            req = requestline.split()
            self.method = req[0]
            self.resource = req[1]
        except IndexError, e:
            return False        

        return True
    
    def getMethod(self):
        return self.method

    def getResource(self):
        return self.resource
