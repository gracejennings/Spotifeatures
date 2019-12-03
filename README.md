# Spotifeatures

TCP server and client that use the Spotify Web API to display inforamtion about a Spotify accound and give reccomendations for new music from other users. The App is the client and requests authorization to read Spotify account information. If the access is granted, the App sends the access token to the Server. The Server then preforms all of the API calls and saves all of the users top tracks, artists and playlists to the server. 

To authroize Spotifeatures to access Spotify account information, a browser will open after starting the app. Once the user grants access, the browser will be redirected to a URL. This URL must be copied into the terminal app prompt.

```
*** Welcome to Spotifeatures: ***

You will be redirected to an uathorization page to give us
access to your spotify data. When you are redirected to a
URL, copy that URL in your bowser and paste it back into 
this window. Press enter to continue:

Copy your redirect url and paste here:
http://localhost:8080/?code=AQCdERzPdiMi2XljFzBrZcAl5vPtmwGCir_I_gyQEsGZ5kONomWnBXcj-UXAefiCKoO0hyAJ8ik0fpAubIw3OXAxzTBiy74udm026JHiZdde5Z9w2k9DJGBuAHsy9FKRm1F61lP0xcOWwGNyGlC1pZdUleumIY4LSdGmkFgM_54Sz-npq7zu1K7Nz6IrYiWpdFxTB4dzsTaK_4mgjb_BZXLKhEIWep0MgdG9jnmPknh0r_ATszdcaxwrtRjBvXsGfyBYGGcJX3f6l9BRr2TVr8sZaPFKSox4R1EzjzTJf28K5NWmMIBqX87kh_R-u9wTQjI8y6-4ZeF3iWF3PKLu_t1sXJoiIbHBvS0

************
Successful connection!
```

The App has four options once it is authorized:

```
Menu:
________________________________________________________________________
"songs" For top 50 played songs
"artists" For top 50 played artists
"recommend" For a recommended playlist from another user on the server
"quit" To exit application
________________________________________________________________________
Enter menu choice:

```
When the user quits, thier information is still stored on the running server, so that the Server can recommend other user's playlists to current users. 