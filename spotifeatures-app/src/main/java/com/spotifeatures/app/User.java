package com.spotifeatures.app;
import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.util.*;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.model_objects.specification.Playlist;


class User {

    public boolean authorized;
    private String mClientID;
    private String mClientSecret;
    private URI mRedirectUri;
    private URL mRedirectUrl;
    private SpotifyApi mApi;
    private URI mUri;
    private URL mUrl;
    private String mCode = "";
    private AuthorizationCodeUriRequest authCodeUriRequest;
    private AuthorizationCodeRequest authCodeRequest;
    private Track[] mTopTracks;
    private Artist[] mTopArtists;
    private PlaylistSimplified[] mPlaylistSimplified;


    public User(String id, String secret) {
        authorized = false;
        mClientID = id;
        mClientSecret = secret;
        mRedirectUri = SpotifyHttpManager.makeUri("http://localhost:8080");
        mApi = new SpotifyApi.Builder()
            .setClientId(mClientID).setClientSecret(mClientSecret).setRedirectUri(mRedirectUri).build();
        
    }

    public User(String id, String secret, String access, String refresh) {
        mClientID = id;
        mClientSecret = secret;
        mRedirectUri = SpotifyHttpManager.makeUri("http://localhost:8080");
        mApi = new SpotifyApi.Builder().setClientId(mClientID).setClientSecret(mClientSecret)
        .setAccessToken(access).setRefreshToken(refresh).build();
        authorized = true;
    }

    public String getName() throws SpotifyWebApiException, IOException {
        return mApi.getCurrentUsersProfile().build().execute().getDisplayName();
    }

    public String getUserID() throws SpotifyWebApiException, IOException {
        return mApi.getCurrentUsersProfile().build().execute().getId();
    }

    public SpotifyApi getApi() {
        return mApi;
    }

    public void authUri() throws IOException {
        authCodeUriRequest = mApi.authorizationCodeUri()
            .scope("user-top-read,user-read-recently-played,user-library-read,"+
                "user-follow-read,playlist-read-private,playlist-modify-private").build();
        URI mAuthUri = authCodeUriRequest.execute();
        Desktop mDesktop = Desktop.getDesktop();
        mDesktop.browse(mAuthUri);
    }

    public boolean authUser(String url) throws SpotifyWebApiException, IOException {
        mRedirectUrl = new URL(url);
        //System.out.println(mRedirectUrl.getQuery());
        if (mRedirectUrl.getQuery().split("=")[0].equals("code")) {
            mCode = mRedirectUrl.getQuery().split("=")[1];
            authCodeRequest = mApi.authorizationCode(mCode).build();
            final AuthorizationCodeCredentials authCodeCredentials = authCodeRequest.execute();
            mApi.setAccessToken(authCodeCredentials.getAccessToken());
            mApi.setRefreshToken(authCodeCredentials.getRefreshToken());
        }
        else {
            return false;
        }
        return true;
    }

    public String getAccessToken() {
        return mApi.getAccessToken();
    }

    public String getRefreshToken() {
        return mApi.getRefreshToken();
    }

    public void newApi(String access, String refresh) throws SpotifyWebApiException, IOException {
        SpotifyApi newApi = new SpotifyApi.Builder().setClientId(mClientID).setClientSecret(mClientSecret)
        .setAccessToken(access).setRefreshToken(refresh).build();
        GetUsersTopTracksRequest req = newApi.getUsersTopTracks().limit(50).build();
        Paging<Track> paging = req.execute();
        mTopTracks = paging.getItems();
        System.out.println("Top Tracks with New API:");
        for (Track track: mTopTracks) {
            System.out.println(track.getName());
        }
        System.out.println("\t***");
    }

    public void addTopTracks() throws SpotifyWebApiException, IOException {
        GetUsersTopTracksRequest req = mApi.getUsersTopTracks().limit(50).build();
        Paging<Track> paging = req.execute();
        mTopTracks = paging.getItems();

    }

    public void printTopTracks() {
        for (Track track: mTopTracks) {
            System.out.println(track.getName());
        }
        System.out.println("\t***");
    }

    public void addTopArtists() throws SpotifyWebApiException, IOException {
        GetUsersTopArtistsRequest req = mApi.getUsersTopArtists().limit(50).build();
        Paging<Artist> paging = req.execute();
        mTopArtists = paging.getItems();
        
    }

    public void printTopArtists() {
        System.out.println("Top Artists:");
        for (Artist artist: mTopArtists) {
            System.out.println(artist.getName());
        }
        System.out.println("\t***");
    }

    public Track[] getTracks() {
        return mTopTracks;
    }

    public Artist[] getArtists() {
        return mTopArtists;
    }

    public void addPlaylists() throws SpotifyWebApiException, IOException{
        GetListOfCurrentUsersPlaylistsRequest req = 
            mApi.getListOfCurrentUsersPlaylists().limit(10).build();
        Paging<PlaylistSimplified> paging = req.execute();
        mPlaylistSimplified = paging.getItems();
        // for (PlaylistSimplified playlist: mPlaylistSimplified){
        //     playlist.getId();
        // }
    }

    public PlaylistSimplified[] getPlaylists() {
        return mPlaylistSimplified;
    }

}