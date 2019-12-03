package com.spotifeatures.app;

import java.io.*;

public class ServerDriver {
  private static final int SERVER_PORT = 6789;

  public static void main(String argv[]) throws IOException {
    Server tcpServer = new Server(SERVER_PORT);
    tcpServer.listen();
  }

}
