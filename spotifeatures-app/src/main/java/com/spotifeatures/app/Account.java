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


class Account {

    public String mUsername;
    public Track[] mTopTracks;
    public Artist[] mTopArtists;


    public Account(String name) {
        mUsername = name;
    }

    // public void setTopTracks(Track[] tracks) {
    //     GetUsersTopTracksRequest req = mApi.getUsersTopTracks().limit(50).build();
    //     Paging<Track> paging = req.execute();
    //     mTopTracks = paging.getItems();
    //     System.out.println("Top Tracks:");
    //     for (Track track: mTopTracks) {
    //         System.out.println(track.getName());
    //     }
    //     System.out.println("\t***");

    // }

    // public void setTopArtists() {
    //     GetUsersTopArtistsRequest req = mApi.getUsersTopArtists().limit(50).build();
    //     Paging<Artist> paging = req.execute();
    //     mTopArtists = paging.getItems();
    //     System.out.println("Top Artists:");
    //     for (Artist artist: mTopArtists) {
    //         System.out.println(artist.getName());
    //     }
    //     System.out.println("\t***");

    // }

}