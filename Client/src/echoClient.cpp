#include <stdlib.h>
#include <connectionHandler.h>
#include <encoderDecoder.h>
#include <sstream> 
#include <thread>
#include <mutex>
#include <serverListener.h>
#include <keyboardListener.h>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
	EncoderDecoder encoderDecoder = EncoderDecoder();
    
    std::mutex mutex;
    KeyboardListener keyboardTask(mutex,&encoderDecoder,&connectionHandler);


    ServerListener serverTask(mutex,&encoderDecoder,&connectionHandler);

 

    std::thread keyboardListenerThread(&KeyboardListener::run, &keyboardTask);
    std::thread serverListenerThread(&ServerListener::run, &serverTask);


     serverListenerThread.join();
    keyboardListenerThread.join();


	// From here we will see the rest of the ehco client implementation:
    // while (1) {
    //     const short bufsize = 1024;
    //     char buf[bufsize];
    //     std::cin.getline(buf, bufsize);
	// 	std::string line(buf);
    //     std::vector<char> encodedLine = encoderDecoder.encode(line);
	// 	int len=encodedLine.size();
    //    //std::cout << encodedLine<<"\n";
        
    //     char buff [encodedLine.size()];
    //     for (int i = 0; i < len; i++)
    //     {
    //         buff[i] = encodedLine[i];
    //     }
        
    //     if (!connectionHandler.sendBytes (buff,len)) {
    //         std::cout << "Disconnected. Exiting...\n" << std::endl;
    //         break;
    //     }
	// 	// connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
    //     std::cout << "Sent " << len+1 << " bytes to server" << std::endl;

 
    //     // We can use one of three options to read data from the server:
    //     // 1. Read a fixed number of characters
    //     // 2. Read a line (up to the newline character using the getline() buffered reader
    //     // 3. Read up to the null character
    //     std::string encodedMessage;
    //     // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
    //     // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
    //     if (!connectionHandler.getLine(encodedMessage)) {
    //         std::cout << "Disconnected. Exiting...\n" << std::endl;
    //         break;
    //     }
    //     std::string answer =encoderDecoder.decode(encodedMessage);
	// 	//len=answer.length();
	// 	// A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
	// 	// we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
    //     //answer.resize(len-1);
    //     std::cout << "Reply: " << answer  << std::endl << std::endl;
    //     std::stringstream ss(answer);
    //     std::string word;
    //     ss >> word;
    //     if (word.compare("ACK") == 0) {
    //         ss>> word;
    //         if(word.compare("3") == 0){
    //             std::cout << "Exiting...\n" << std::endl;
    //             break; 
    //         }
                
    //     }
    // }
    return 0;
}
