#include <iostream>
#include <mutex>
#include <thread>
#include <encoderDecoder.h>
#include <connectionHandler.h>


class ServerListener{
    private:
        std::mutex &mutex;
        EncoderDecoder * encoderDecoder;
        ConnectionHandler * connectionHandler;
    public:
    
        ServerListener(std::mutex &_mutex, EncoderDecoder *encoderDecoder,ConnectionHandler * connectionHandler); 
       
        void run();
};