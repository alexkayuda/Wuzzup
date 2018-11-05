package com.muc;


import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        final int port = 8818;
        Server server = new Server(port);
        server.start();
    }


}
