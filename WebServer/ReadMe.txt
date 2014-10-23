A webserver program written in Python.

server.py
	1. Port number is hardcoded (8093).
	2. Picks the filesystem from the place server.py is running.
	3. Tries to retrieve index.html file if no resource is mentioned in the url. (i.e., http://<ip>:<port>)
	4. Made server multi threaded so that clients can be served in parallel.

request.py
	Class containing the implementation to parse an HTTP request.

response.py
	Implementation to form an HTTP response message.

HTTPGetClient.py (usage: HTTPGetClient.py <server-ip/host> <server-port> <resource>)
	A sample client that establishes a communication with server and send an HTTP GET request for the requested resource. 
	If resource is not mentioned in the command, the client will request for '/'