#include <string>
#include <sstream> 
#include <map>
#include <encoderDecoder.h>
#include <vector>
#include <iostream>
EncoderDecoder::EncoderDecoder(){}

using std::string;
std::vector<char> EncoderDecoder::encode(std::string line){
    std::stringstream ss(line);
    std::string word;
    ss>> word;
    Opcode opcode =  stringActionToOpcode.at(word);
    std::vector<char> encodedMessage;
    OcodeToBytes(opcode,encodedMessage);
    switch (opcode)
    {
        case REGISTER:
            registerEncoding(line,encodedMessage);
            break;
        case LOGIN:
            loginEncoding(line,encodedMessage);
            break;
        case LOGOUT:
            break;
        case FOLLOW:
            followEncoding(line,encodedMessage);
            break;
        case POST:
            postEncoding(line,encodedMessage);
            break;
        case PM:
            pmEncoding(line,encodedMessage);
            break;
        case LOGSTAT:
            break;
        case STAT:
            statEncoding(line,encodedMessage);
            break;
        case BLOCK:
            blockEncoding(line,encodedMessage);
            break;
        default:
            break;
    }
    encodedMessage.insert(encodedMessage.end(),';');
    return encodedMessage;
}

void EncoderDecoder::OcodeToBytes( Opcode opcode,std::vector<char> &encodedMessage){
    char bytesArr[2];
    shortToBytes(static_cast<short>(opcode),bytesArr);
    encodedMessage.push_back(bytesArr[0]);
    encodedMessage.push_back(bytesArr[1]);   
}

void EncoderDecoder::registerEncoding(std::string line, std::vector<char> &encodedMessage){
    line = line.substr(9);
    for (size_t i = 0; i < line.size(); i++){   
        if(line[i]== ' ')
            encodedMessage.insert(encodedMessage.end(),'\0');
        else
            encodedMessage.insert(encodedMessage.end(),line[i]);
    }
    encodedMessage.insert(encodedMessage.end(),'\0');
    
}

void EncoderDecoder::loginEncoding(std::string line, std::vector<char> &encodedMessage){
    line = line.substr(6);
    for (size_t i = 0; i < line.size(); i++){   
        if(line[i]== ' ')
            encodedMessage.insert(encodedMessage.end(),'\0');
        else
            encodedMessage.insert(encodedMessage.end(),line[i]);
    }
    encodedMessage.insert(encodedMessage.end(),'\0');
    
}
void EncoderDecoder::followEncoding(std::string line, std::vector<char> &encodedMessage){
    line = line.substr(7);
    for (size_t i = 0; i < line.size(); i++){   
        if(line[i] !=' ')
            encodedMessage.insert(encodedMessage.end(),line[i]);
    }
    encodedMessage.insert(encodedMessage.end(),'\0');
}

void EncoderDecoder::postEncoding(std::string line, std::vector<char> &encodedMessage){
    line = line.substr(5);
    for (size_t i = 0; i < line.size(); i++){   
        encodedMessage.insert(encodedMessage.end(),line[i]);
    }
    encodedMessage.insert(encodedMessage.end(),'\0');
}

void EncoderDecoder::pmEncoding(std::string line, std::vector<char> &encodedMessage){
    line = line.substr(3);
    for (size_t i = 0; i < line.size(); i++){   
        if(line[i]== ' ')
            encodedMessage.insert(encodedMessage.end(),'\0');
        else
            encodedMessage.insert(encodedMessage.end(),line[i]);
    }
    encodedMessage.insert(encodedMessage.end(),'\0');
}

void EncoderDecoder::blockEncoding(std::string line, std::vector<char> &encodedMessage){
    line = line.substr(6);
    for (size_t i = 0; i < line.size(); i++){   
        encodedMessage.insert(encodedMessage.end(),line[i]);
    }
    encodedMessage.insert(encodedMessage.end(),'\0');
}



void EncoderDecoder::statEncoding(std::string line, std::vector<char> &encodedMessage){
    line = line.substr(5);
    for (size_t i = 0; i < line.size(); i++){   
        encodedMessage.insert(encodedMessage.end(),line[i]);
    }
    encodedMessage.insert(encodedMessage.end(),'\0');
}


std::string EncoderDecoder::decode(std::string encodedMessage){
    char bytesArr[] = {encodedMessage[0],encodedMessage[1]};
    switch (static_cast<int>(bytesToShort(bytesArr))){
    case 9 /* constant-expression */:
        return decodeNotification(encodedMessage);
        break;
    case 10:
        return decodeAck(encodedMessage);
        break;
    case 11:
        return decodeError(encodedMessage);
        break;    
    default:
        return NULL;
        break;
    }
}
std::string EncoderDecoder::decodeNotification(std::string encodedMessage){
    std::string decodedMessage = "NOTIFICATION ";
    char type = encodedMessage[2];//PM || Public
    if(type == '0')//PM
        decodedMessage += "PM ";
    else//Public 
        decodedMessage += "Public ";
    for(size_t i= 3; i < encodedMessage.size()-1;i++){
        if(encodedMessage[i]=='\0')
            decodedMessage += " ";
        else    
            decodedMessage += encodedMessage[i];
    }
    return decodedMessage;
}
std::string EncoderDecoder::decodeAck(std::string encodedMessage){
    std::string decodedMessage = "ACK ";
    char bytesArr[] = {encodedMessage[2],encodedMessage[3]};
    int messageOpcodeValue =static_cast<int>(bytesToShort(bytesArr));
    decodedMessage +=  std::to_string(messageOpcodeValue)+" ";
    switch (messageOpcodeValue)
    {
        case 4:
            {
                for(size_t i= 4; i < encodedMessage.size()-1;i++){
                    if(encodedMessage[i]=='\0')
                        decodedMessage += " ";
                    else    
                        decodedMessage += encodedMessage[i];
                }
                break;
            }
        case 7: case 8:
            {
                char ageBytes[] = {encodedMessage[4],encodedMessage[5]};
                char numPostsBytes[] = {encodedMessage[6],encodedMessage[7]};
                char numFollowersBytes[] = {encodedMessage[8],encodedMessage[9]};
                char numFollowingBytes[] = {encodedMessage[10],encodedMessage[11]};
                int age =static_cast<int>(bytesToShort(ageBytes));
                int numPosts =static_cast<int>(bytesToShort(numPostsBytes));
                int numFollowers =static_cast<int>(bytesToShort(numFollowersBytes));
                int numFollowing =static_cast<int>(bytesToShort(numFollowingBytes));
                decodedMessage += "Age: "+std::to_string(age)+" ";
                decodedMessage += "Num of Posts: "+std::to_string(numPosts)+" ";
                decodedMessage += "Num of Followers: "+std::to_string(numFollowers)+" ";
                decodedMessage += "Num of Followings: "+std::to_string(numFollowing);
                break;
            }
            
        default:
            break;
    }
    
    return decodedMessage;
}
std::string EncoderDecoder::decodeError(std::string encodedMessage){
    std::string decodedMessage = "ERROR ";
    char bytesArr[] = {encodedMessage[2],encodedMessage[3]};
    int messageOpcodeValue =static_cast<int>(bytesToShort(bytesArr));
    decodedMessage +=  std::to_string(messageOpcodeValue);
    return decodedMessage;
}
