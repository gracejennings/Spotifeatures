package com.spotifeatures.app;
import java.io.*;
import java.net.*;
import java.util.*;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;

class ServerThread extends Thread {

    private final String mClientID = "8e5092808db74a79941ddb15c1e6f220";
    private final String mClientSecret = "f67074f781bf46539e60b49ec43d412c";

    private final Socket mClientSocket;
    private final BufferedReader clientReader;
    private final DataOutputStream clientWriter;
    private boolean auth;
    private User user;

    public ServerThread(Socket socket, BufferedReader reader, DataOutputStream write) throws IOException {
        this.mClientSocket =  socket;
        this.clientReader = reader;
        //this.clientWriter = write;
        this.clientWriter = new DataOutputStream(mClientSocket.getOutputStream());
        this.auth = false;
    }

    @Override
    public void run() {
        boolean running = true;
        String request;
        System.out.println("Server thread started for user");
        authorize();
        
        try {
            System.out.println("User " + user.getName() + " joined Spotifeatures!");
            user.addTopTracks();
            addTracks();
            user.addTopArtists();
            addArtists();
            user.addPlaylists();
            addPlaylist();
            //Server.printPlaylists();
        }
        catch (Exception e ) {
            e.printStackTrace();
        }

        while(running) {
            
            try {
                request = clientReader.readLine().trim();
                if(request.equalsIgnoreCase("quit")) {
                    running = false;
                }
                else if (request.equalsIgnoreCase("recommend")) {
                    String recID = recommend();
                    if (!recID.equals("-1")) {
                        clientWriter.writeBytes((recID+"\n"));
                    }
                    else {
                        clientWriter.writeBytes("No playlist that has your top artists\n");
                    }
                }
                else if (request.equalsIgnoreCase("songs")) {
                    Track[] tracks = user.getTracks();
                    String response = "";
                    for (Track track: tracks) {
                        response += (track.getName() + " by " + track.getArtists()[0].getName() + ",");
                    }
                    clientWriter.writeBytes((response+"\n"));
                    System.out.println("Sent top songs list");
                    //user.printTopTracks();
                }
                else if (request.equalsIgnoreCase("artists")) {
                    Artist[] artists = user.getArtists();
                    String response = "";
                    for (Artist art: artists) {
                        response += (art.getName() + ",");
                    }
                    clientWriter.writeBytes((response+"\n"));
                    System.out.println("Sent top artists list");
                    //user.printTopArtists();
                }
                else {
                    System.out.print("Option not valid");
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }


        }
    }

    public boolean authorize() {
        try{
            System.out.println("requesting access token...");
            
            clientWriter.writeBytes("ACCESS_TOKEN\n");
            String mAccessToken = clientReader.readLine().trim();
            System.out.println("requesting refresh token...");
            clientWriter.writeBytes("REFRESH_TOKEN\n");
            String mRefreshToken = clientReader.readLine().trim();
            System.out.println("creating new user...");
            user = new User(mClientID, mClientSecret, mAccessToken, mRefreshToken);
            ///Server.users.add(user);
            Server.addUser(user);
            auth = true;
        }
        catch(Exception e) {
            e.printStackTrace();
        } 
        return true;
    }

    public void addTracks() {
        for (Track track: user.getTracks()) {
            Server.addTrack(track);
        }
    }

    public void addArtists() {
        for (Artist artist: user.getArtists()) {
            Server.addArtist(artist);
        }
    }

    public void addPlaylist() {
        for (PlaylistSimplified play: user.getPlaylists()) {
            Server.addPlaylist(play);
        }
    }

    public String recommend() throws SpotifyWebApiException, IOException{
        Set<PlaylistSimplified> currPlaylists = Server.getPlaylists();
        HashMap<PlaylistSimplified, Integer> matched = new HashMap<PlaylistSimplified,Integer>();
        GetPlaylistsTracksRequest req;
        Paging<PlaylistTrack> paging;
        int artistMatches;
        String recPlaylistId = "-1";
        int maxMatches = 0;

        for (PlaylistSimplified playlist: currPlaylists) {
            artistMatches = 0;
            if (!playlist.getOwner().getId().equals(user.getUserID()) 
                && !playlist.getOwner().getDisplayName().toLowerCase().contains("spotify")
                && Server.userIds.contains(playlist.getOwner().getId())) {
                req = user.getApi().getPlaylistsTracks(playlist.getId()).build();
                paging = req.execute();
                PlaylistTrack[] tracks = paging.getItems();
                System.out.println("Testing playlist: " + playlist.getName());
                for(PlaylistTrack t: tracks) {
                    //System.out.println(t.toString());
                    
                    //System.out.println(t.getTrack().getName());

                    try {
                        ArtistSimplified[] arts = t.getTrack().getArtists();//[0];//nullptr exception
                        for (ArtistSimplified art: arts) {
                            for(Artist userArt: user.getArtists()) {
                                if (userArt.getId().equals(art.getId())) {
                                    artistMatches++;
                                }
                            }
                        }   
                    }
                    catch(Exception e) {
                        //e.printStackTrace();
                        System.out.print("Invalid track");
                    }
                    
                }
            }

            if (artistMatches != 0) {
                if (artistMatches > maxMatches) {
                    // if (!playlist.getOwner().getDisplayName().toLowerCase().contains("spotify")) {

                    // }
                    recPlaylistId = playlist.getOwner().getDisplayName() + ": " + playlist.getName();
                }
                matched.put(playlist, artistMatches);
            }
        }
        System.out.println("Recommending...");
        System.out.println(recPlaylistId);
        return recPlaylistId;
    }

}