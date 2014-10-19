A webserver program written in Python.

Run server.py
	1. Port number is hardcoded (8093).
	2. Picks the filesystem from the place server.py is running.
	3. Tries to retrieve index.html file if no resource is mentioned in the url. (i.e., http://<ip>:<port>)
	4. Made server multi threaded so that clients can be served in parallel.
