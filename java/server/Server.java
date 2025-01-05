package server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import data.*;
import controllers.*;
import server.*;
import client.*;

import data.*;
import util.*;
import java.util.Optional;

public class Server {
    private static final int PORT = 12345;
    private static final String PASSWORD_FILE = "E:\\Cricket project\\source\\main\\java\\data\\passwords.txt";
    private Map<String, String> credentials = Collections.synchronizedMap(new HashMap<>());  // Credentials map
    private static final String INPUT_FILE_NAME = "E:\\Cricket project\\source\\main\\java\\data\\players.txt";

    private static PlayerDatabase players = new PlayerDatabase();  // Player list


    public PlayerDatabase getPlayers() {
        return players;
    }

    // Load credentials from file
    public Server() throws IOException {
        loadCredentials();
    }

    private void loadCredentials() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    credentials.put(parts[0], parts[1]);  // Load credentials into the map
                }
            }
        }
    }

    // Start the server
    public void start() throws IOException {
        while(true) {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server started on port " + PORT);
                Socket clientSocket = serverSocket.accept();
                SocketWrapper socketWrapper = new SocketWrapper(clientSocket);
                ClientService clientService = new ClientService(players,credentials,socketWrapper);
                Thread t = new Thread(clientService);
                t.start();
            }
        }


    }


    public static void main(String[] args) {
        try {
            players.ReadFromFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            new Server().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}