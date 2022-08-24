#include <iostream>
#include <mutex>
#include <thread>
#include <keyboardListener.h>
#include <condition_variable>

        
    
KeyboardListener::KeyboardListener(std::mutex &_mutex, EncoderDecoder *_encoderDecoder,ConnectionHandler *_connectionHandler):  mutex(_mutex),encoderDecoder(_encoderDecoder),connectionHandler(_connectionHandler){}; 
       
void KeyboardListener::run(){

      // constructor locks the mutex while destructor (out of scope) unlocks it
    while(1){
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
		std::string line(buf);
        if (!connectionHandler->sendMessage(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        if(line.compare("LOGOUT")==0){
            // std::unique_lock<std::mutex> lock{mutex};
            // std::condition_variable cond;
            connectionHandler->wait();
            while (connectionHandler->isWaiting()) { // Wait inside loop to handle spurious wakeups etc.
                // cond.wait(lock);
            }
            if(connectionHandler->isTerminated()){
                break;
            }

        }
    }
    
} 
