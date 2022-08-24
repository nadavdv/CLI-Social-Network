#include <iostream>
#include <mutex>
#include <thread>
#include <serverListener.h>


        
    
ServerListener::ServerListener(std::mutex &_mutex, EncoderDecoder *_encoderDecoder,ConnectionHandler *_connectionHandler):  mutex(_mutex),encoderDecoder(_encoderDecoder),connectionHandler(_connectionHandler){}; 
       
void ServerListener::run(){
        while (1){
            std::string encodedMessage;
            if (!connectionHandler->getLine(encodedMessage)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            std::string answer =encoderDecoder->decode(encodedMessage);
            std::cout << "Reply: " << answer  << std::endl << std::endl;
            std::stringstream ss(answer);
            std::string word;
            ss >> word;
            if (word.compare("ERROR") == 0) {
                ss>> word;
                if(word.compare("3") == 0){
                    connectionHandler->stopWait();
                }     
            }


            if (word.compare("ACK") == 0) {
                ss>> word;
                if(word.compare("3") == 0){
                    std::cout << "Exiting...\n" << std::endl;
                    connectionHandler->terminate();
                    connectionHandler->stopWait();
                    break; 
                }     
            }
        }
        
} 