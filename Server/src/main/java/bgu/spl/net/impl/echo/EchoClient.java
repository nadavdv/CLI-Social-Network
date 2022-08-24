package bgu.spl.net.impl.echo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import bgu.spl.net.api.bidi.Messages.Register;

public class EchoClient {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            args = new String[]{"127.0.0.1", "hello"};
        }

        /* if (args.length < 2) {
            System.out.println("you must supply two arguments: host, message");
            System.exit(1);
        } */
        

        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
        try (Socket sock = new Socket(args[0], 2090);
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                DataOutputStream dOut = new DataOutputStream(sock.getOutputStream()) ){

            System.out.println("sending message to server");
            Register r = new Register("username", "password", "birthday");
            byte[] b = r.encode(r);
            
            dOut.write(b);
            
            // out.newLine();
            // dout.flush();

            System.out.println("awaiting response");
            String line = in.readLine();
            System.out.println("message from server: " + line);
        }
    }
}
