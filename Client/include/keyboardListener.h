#include <iostream>
#include <mutex>
#include <thread>
#include <encoderDecoder.h>
#include <connectionHandler.h>


class KeyboardListener{
    private:
        EncoderDecoder * encoderDecoder;
        ConnectionHandler * connectionHandler;
        std::mutex &mutex;
    public:
        KeyboardListener(std::mutex &_mutex, EncoderDecoder *_encoderDecoder,ConnectionHandler * _connectionHandler); 
       
        void run();
};