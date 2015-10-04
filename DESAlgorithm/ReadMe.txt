1. Run build.sh to generate binaries for server, client1, client2, client3, client4, client5, client6 and runTestCases. These binaries will get copied into "output" folder.

2. run server (./server port_number [numberOfRounds]).

3. run clients (./client{1/2/3/4/5/6} hostname port_number [numberOfRounds])

4. The key space (2^64-1) is divided into 7 parts and given to each binary.

5. The input to each instance are passed through files.
	input1.txt  -> supplies initial plain text
	output1.txt -> supplies initial cipher text
	input2.txt  -> supplies plain text to verify after finding key
	output2.txt -> supplies cipher text to verify after finding key
	
6. We used 5 amazon web service(ubuntu) instances and 2 local(ubuntu) instances. Each instance runs 5 seperate threads.

7. public key for amazon instance
	52.27.154.243  - amazon_aws 
	52.24.33.133   - amazon_aws
	52.24.233.44   - amazon_aws
	
	52.27.143.241  - aws_service
	52.25.202.238  - aws_service

8. All instance has the username "ubuntu".

9. We ran server on amazon instance. Once we successfully establish a communication from local machine to cloud instance over tcp, there exist a dedicated pipe between those two instances. From then onwards, bot machines can send/receive messages. Server waits till all 6 clients gets initialized.

10. The runTestCases executable displays the cipher text for given message with 3 different keys.