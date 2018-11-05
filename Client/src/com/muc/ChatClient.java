package com.muc;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ChatClient {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;

    private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();
    private ArrayList<MessageListener> messageListeners = new ArrayList<>();

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("localhost", 8818);

        client.addUserStatusListener(new UserStatusListener() {
            @Override
            public void online(String login) {
                System.out.println("ONLINE: " + login);
            }

            @Override
            public void offline(String login) {
                System.out.println("OFFLINE:" + login);
            }
        });

        client.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(String fromLogin, String msgBody) {
                System.out.println("You got a message from " + fromLogin + " >>> " + msgBody);
            }
        });

        if(!client.connect())
            System.out.println("Connection Failed");
        else {
            System.out.println("Connection Established");
            if(client.login("alex", "alex")){
                System.out.println("User Logged In Successfully");
                client.msg("mike", "Hello There");
            } else {
                System.out.println("Was not able to log in. Try again!");
            }

            //client.logoff();
        }
    }

    public void msg(String sendTo, String msgBody) throws IOException {
        String command = "msg " + sendTo + " " + msgBody + "\n";
        serverOut.write(command.getBytes());
    }

    public boolean login(String login, String password) throws IOException {
        String command = "login " + login + " " + password + "\n";
//        System.out.println(command);
        serverOut.write(command.getBytes());

        String response = bufferedIn.readLine();
        System.out.println(response);

        if(response.equalsIgnoreCase("You are logged in now")){
            //System.out.println("Calling startMessageReader");
            startMessageReader();
            return true;
        } else
            return false;
    }

    public void logoff() throws IOException {
        String command = "logoff\n";
        serverOut.write(command.getBytes());
    }

    private void startMessageReader() {
        //System.out.println("Inside startMessageReader");
        Thread t = new Thread() {
            @Override
            public void run() {
                readMessageLoop();
            }
        };
        t.start();
    }

    private void readMessageLoop() {
        try {
            String line;
            //System.out.println("InsideReadMessageLoop");
            while( (line = bufferedIn.readLine()) != null){
                String[] tokens = StringUtils.split(line);
                if(tokens != null && tokens.length > 0){
                    String command = tokens[0];
                    if("online".equalsIgnoreCase(command)){
                        //System.out.println("Calling handleONLINE");
                        handleOnline(tokens);
                    } else if ("offline".equalsIgnoreCase(command)){
                        //System.out.println("Calling handleOFFLINE");
                        handleOffline(tokens);
                    } else if("msg".equalsIgnoreCase(command)){
                        String[] tokensMsg = StringUtils.split(line, null, 3);
                        handleMessage(tokensMsg);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void handleMessage(String[] tokensMsg) {
        String login = tokensMsg[1];
        String msgBody = tokensMsg[2];

        for(MessageListener listener: messageListeners){
            listener.onMessage(login, msgBody);
        }
    }

    private void handleOffline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener: userStatusListeners){
            listener.offline(login);
        }
    }

    private void handleOnline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener: userStatusListeners){
            listener.online(login);
        }
    }

    public boolean connect() {
        try {
            this.socket = new Socket(serverName,serverPort);
            System.out.println("Client's port is: " + socket.getLocalPort());
            this.serverIn = socket.getInputStream();
            this.serverOut = socket.getOutputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUserStatusListener(UserStatusListener userStatusListener){
        userStatusListeners.add(userStatusListener);
    }

    public void removeUserStatusListener(UserStatusListener userStatusListener){
        userStatusListeners.remove(userStatusListener);
    }

    public void addMessageListener(MessageListener messageListener){
        messageListeners.add(messageListener);
    }

    public void removeMessageListener(MessageListener messageListener){
        messageListeners.remove(messageListener);
    }
}
