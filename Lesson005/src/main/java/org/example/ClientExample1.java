package org.example;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientExample1 {
    public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            Socket client = new Socket(address, 1300);

            System.out.println(client.getInetAddress());
            System.out.println(client.getLocalPort());

            InputStream inStream = client.getInputStream();
            OutputStream outStream = client.getOutputStream();
            DataInputStream dataInputStream = new DataInputStream(inStream);
            PrintStream printStream = new PrintStream(outStream);

            printStream.println("Привет!");
            System.out.println(dataInputStream.readLine());
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
