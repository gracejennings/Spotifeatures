package com.spotifeatures.app;
import java.io.*;
import java.net.*;
import java.util.*;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;

class Server {

    private final String mClientID = "8e5092808db74a79941ddb15c1e6f220";
    private final String mClientSecret = "f67074f781bf46539e60b49ec43d412c";

    private final boolean running = true;
    private static ServerSocket mServerSocket;
    public Socket mClientSocket;
    private boolean auth;
    
    public static Set<User> users;
    public static Set<String> userIds;
    public static HashMap<Track, Integer> tracks;
    public static HashMap<Artist, Integer> artists;
    public static Set<PlaylistSimplified> playlists;


    public Server(int mPort) throws IOException {
        mServerSocket = new ServerSocket(mPort);
        users = new HashSet<User>();
        userIds = new HashSet<String>();
        tracks = new HashMap<Track, Integer>();
        artists = new HashMap<Artist,Integer>();
        playlists = new HashSet<PlaylistSimplified>();
        auth = false;
    }

    public void listen() throws IOException {
        boolean quit = false;
        while (running) {
          waitForConnection();
    
          BufferedReader clientReader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
          DataOutputStream clientWriter = new DataOutputStream(mClientSocket.getOutputStream());
    
          String clientMessage = clientReader.readLine();
          System.out.println("  received message: " + clientMessage);
    
          if (clientMessage.equals("JOIN")) {
            ServerThread mThread = new ServerThread(mClientSocket, clientReader, clientWriter);
            mThread.run();
          }
    
          String response = "You sent me " + clientMessage.length() + " characters.\n";
          clientWriter.writeBytes(response);
    
          System.out.println("");
        }
      }
    
    private void waitForConnection() throws IOException {
        System.out.println("waiting for a connection");
        mClientSocket = mServerSocket.accept();
        System.out.println("connected!");
        InetAddress clientAddress = mClientSocket.getInetAddress();
        System.out.println("  client at: " + clientAddress.toString() + ":" + mClientSocket.getPort());
    }

    public static void addUser(User usr) {
          users.add(usr);
          try {
            userIds.add(usr.getUserID());
          }
          catch (Exception e) {
            e.printStackTrace();
          }
    }

      /**
       * Adds a track to the server set of tracks and updates count is already preset
       * 
       * @param track   to add
       * @return        true for already present, false for first add
       */
    public static boolean addTrack(Track track) {
          int oldCount;
          if(tracks.containsKey(track)) {
              oldCount = tracks.get(track);
              tracks.replace(track, oldCount, oldCount++);
              return true;
          }
          else {
              tracks.put(track, 1);
              return false;
          }
    }

      /**
       * Adds a artist to the server set of artists and updates count is already preset
       * 
       * @param artist   to add
       * @return        true for already present, false for first add
       */
    public static boolean addArtist(Artist artist) {
        int oldCount;
        if(artists.containsKey(artist)) {
            oldCount = artists.get(artist);
            artists.replace(artist, oldCount, oldCount++);
            return true;
        }
        else {
            artists.put(artist, 1);
            return false;
        }
    }

    /**
       * Adds a playlist to the server set of playlists and updates count is already preset
       * 
       * @param playlist   to add
       * @return        true for already present, false for first add
       */
    public static boolean addPlaylist(PlaylistSimplified playlist) {
        if(playlists.contains(playlist)) {
            return true;
        }
        else {
            playlists.add(playlist);
            return false;
        }
    }

    public static void printPlaylists() {
        for (PlaylistSimplified playlist: playlists) {
            System.out.println(playlist.getOwner().getDisplayName() + ": " + playlist.getName());
        }
    }

    public static Set<PlaylistSimplified> getPlaylists() {
        return playlists;
    }

}