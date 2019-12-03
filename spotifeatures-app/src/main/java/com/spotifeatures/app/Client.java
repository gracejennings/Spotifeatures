package com.spotifeatures.app;
import java.io.*;
import java.net.*;
import java.util.*;

import com.wrapper.spotify.SpotifyApi;

public class Client {

  //private static final String HOST_ADDRESS = "ec2-18-223-102-135.us-east-2.compute.amazonaws.com";
  private static final String HOST_ADDRESS = "127.0.0.1.";
  private static final int PORT = 6789;

  public static void main(String[] args) throws IOException {
    Socket socket = new Socket(HOST_ADDRESS, PORT);
    DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
    BufferedReader serverBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    System.out.println("Welcome to Spotifeatures:");
    BufferedReader userBufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String userInput = userBufferedReader.readLine();
    outToServer.writeBytes(userInput + '\n');

    String responseFromServer = serverBufferedReader.readLine();
    System.out.println("Response from server: " + responseFromServer);

    socket.close();
  }
}