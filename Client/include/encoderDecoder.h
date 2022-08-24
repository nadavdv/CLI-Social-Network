#ifndef ENCODER_DECODER__
#define ENCODER_DECODER__

#include <string>
#include <sstream> 
#include <map>
#include <vector>



enum Opcode{
    REGISTER = 1,
    LOGIN = 2,
    LOGOUT = 3,
    FOLLOW = 4,
    POST = 5,
    PM = 6,
    LOGSTAT = 7,
    STAT = 8,
    NOTIFICATION = 9,
    ACK = 10,
    ERROR = 11,
    BLOCK = 12
};




class EncoderDecoder{
    private:
        std::map<std::string, Opcode> stringActionToOpcode{
            {"REGISTER",REGISTER},
            {"LOGIN",LOGIN},
            {"LOGOUT",LOGOUT},
            {"FOLLOW",FOLLOW} ,
            {"POST",POST} ,
            {"PM",PM},
            {"LOGSTAT",LOGSTAT},
            {"STAT",STAT},
            {"NOTIFICATION",NOTIFICATION},
            {"ACK",ACK},
            {"ERROR",ERROR} ,
            {"BLOCK",BLOCK} 
        };
        

    public:
        EncoderDecoder();


        short bytesToShort(char* bytesArr){
            short result = (short)((bytesArr[0] & 0xff) << 8);
            result += (short)(bytesArr[1] & 0xff);
            return result;
        }

        void shortToBytes(short num, char* bytesArr){
            bytesArr[0] = ((num >> 8) & 0xFF);
            bytesArr[1] = (num & 0xFF);
        }
        
        std::string decodeAck(char bytes[]){
            std::string line(bytes);  /* code */
            return line.substr(4,sizeof(line));
        }
        
        
        std::vector<char> encode(std::string line);

        void OcodeToBytes(Opcode opcode,std::vector<char> &encodedMessage);

        void registerEncoding(std::string line, std::vector<char> &encodedMessage);

        void loginEncoding(std::string line, std::vector<char> &encodedMessage);

        void followEncoding(std::string line, std::vector<char> &encodedMessage);

        void postEncoding(std::string line, std::vector<char> &encodedMessage);

        void pmEncoding(std::string line, std::vector<char> &encodedMessage);

        void statEncoding(std::string line, std::vector<char> &encodedMessage);

        void blockEncoding(std::string line, std::vector<char> &encodedMessage);



        std::string decode(std::string encodedMessage);

        std::string decodeNotification(std::string encodedMessage);

        std::string decodeAck(std::string encodedMessage);

        std::string decodeError(std::string encodedMessage);
};
#endif
