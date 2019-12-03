package com.spotifeatures.app;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String HOST_ADDRESS = "127.0.0.1";
    private static final int PORT = 6789;
    private static DataOutputStream outToServer;
    private static BufferedReader serverBufferedReader;
    public static String mClientID = "8e5092808db74a79941ddb15c1e6f220";
    public static String mClientSecret = "f67074f781bf46539e60b49ec43d412c";
    public static User mUser;

    public static void main( String[] args ) throws IOException
    //public void runApp()
    {
        Socket socket = new Socket(HOST_ADDRESS, PORT);
        //Socket so = new Socket
        outToServer = new DataOutputStream(socket.getOutputStream());
        serverBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Scanner in = new Scanner(System.in);

        System.out.println("\n*** Welcome to Spotifeatures: ***"); 
        System.out.println("");
        //System.out.println("Address 1: " + socket.getInetAddress().toString() + socket.getPort());
        System.out.println("You will be redirected to an uathorization page to give us\n" +
                            "access to your spotify data. When you are redirected to a\n" + 
                            "URL, copy that URL in your bowser and paste it back into \n" + 
                            "this window. Press enter to continue:");
        in.nextLine();
        
        
        mUser = new User(mClientID, mClientSecret);
        
        try {
            mUser.authUri();
            System.out.println("Copy your redirect url and paste here:");
            String url = in.nextLine().trim();
            if (!mUser.authUser(url)) {
                System.out.println("Access denied");
            }
            System.out.print("\n***");
            //top(mUser);
            outToServer.writeBytes("JOIN\n");//working
            //System.out.println("Address 2: " + socket.getInetAddress().toString() + socket.getPort());
            String serverResponse = serverBufferedReader.readLine().trim();
            //System.out.println("Server responded with: " + serverResponse);
            System.out.print("***");
            if (serverResponse.equals("ACCESS_TOKEN")){
                outToServer.writeBytes(mUser.getAccessToken() + "\n");
            }
            else {
                System.out.println("ERROR receivec: " + serverResponse);
            }
            serverResponse = serverBufferedReader.readLine();
            System.out.print("***");
            //System.out.println("received " + serverResponse);
            if (serverResponse.trim().equals("REFRESH_TOKEN")){
                outToServer.writeBytes(mUser.getRefreshToken() + "\n");
            }
            else {
                System.out.println("ERROR receivec: " + serverResponse);
            }
            System.out.print("***\n");
            System.out.println("Successful connection!");
            
            menu(in);

            in.close();
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  static void menu(Scanner in) {
        boolean running = true;
        String option;
        while (running) {
            option = getOption(in);
            if (option.equalsIgnoreCase("quit")){
                try {
                    outToServer.writeBytes("quit\n");
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                
                running = false;
            }
            else if (option.equalsIgnoreCase("recommend")) {
                recommend();
            }
            else if (option.equalsIgnoreCase("songs")) {
                songs();
            }
            else if (option.equalsIgnoreCase("artists")) {
                artists();
            }
        }
    }

    public static void recommend() {
        try {
            outToServer.writeBytes("recommend\n");
            String serverResponse = serverBufferedReader.readLine();
            System.out.println("Based on your top artists Spotifeatures recommends: ");
            System.out.println("\t\t" + serverResponse);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void songs() {
        try {
            outToServer.writeBytes("songs\n");
            String serverResponse = serverBufferedReader.readLine();
            System.out.println("Your top songs: ");
            String[] songs = serverResponse.split(",");
            for (String song: songs) {
                System.out.println("\t" + song);
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void artists() {
        try {
            outToServer.writeBytes("artists\n");
            String serverResponse = serverBufferedReader.readLine();
            System.out.println("Your top artists: ");
            String[] artists = serverResponse.split(",");
            for (String art: artists) {
                System.out.println("\t" + art);
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static String getOption(Scanner in) {
        System.out.println("________________________________________________________________________");
        System.out.println("\nMenu:");
        System.out.println("________________________________________________________________________");
        System.out.println("\"songs\" For top 50 played songs");
        System.out.println("\"artists\" For top 50 played artists");
        System.out.println("\"recommend\" For a recommended playlist from another user on the server");
        System.out.println("\"quit\" To exit application");
        System.out.println("________________________________________________________________________");
        System.out.println("Enter menu choice:");
        return in.nextLine().trim();
    }

    public static void top(User usr) {

        try {
            usr.addTopTracks();
            usr.addTopArtists();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void newApi(User usr) {
        try {
            usr.newApi(usr.getAccessToken(), usr.getRefreshToken());
        }
        catch (Exception e ) {
            e.printStackTrace();
        }
    }
}
