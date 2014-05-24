/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deliquescence.Network;

import Deliquescence.Config;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Josh
 */
public class Server {

    public void test() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(Config.getInt("LAN_PORT"));
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            serverSocket.accept();
        } catch (Exception e) {

        }
    }
}
