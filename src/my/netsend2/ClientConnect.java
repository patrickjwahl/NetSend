/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.netsend2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.Timer;
import static my.netsend2.NetSend2.skt;
import java.net.InetAddress;
/**
 *
 * @author Patrick
 */
public class ClientConnect implements Runnable {
    
    @Override
    public void run() {
        while (true) {
        try {
                if (!Thread.interrupted()) {
                skt = new Socket(NetSend2.serverAddress, NetSend2.port);
                } else {
                    return;
                }
                NetSend2.out = new OutputStreamWriter(skt.getOutputStream());
                NetSend2.in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
                break;
            } catch (UnknownHostException e) {
                NetSend2.errLabel.setVisible(true);
                NetSend2.errLabel.setText("The IP address you specified doesn't exist");
                return;
            } catch (IOException e) {
                
            }
        }
        try {
            NetSend2.guestName = NetSend2.nameBox.getText();
            NetSend2.out.write(NetSend2.guestName + NetSend2.newln);
            NetSend2.out.flush();
            NetSend2.in.readLine();
            NetSend2.cancelButton.setVisible(false);
            NetSend2.errLabel.setText("Awaiting response from host...");
            String response = NetSend2.in.readLine();
            if (response.equals("yes")) {
                NetSend2.UITransitionChat();
                NetSend2.hostName = NetSend2.in.readLine();
                NetSend2.roomLabel.setText("You are now chatting with " + NetSend2.hostName + ".");
                NetSend2.clientTime = new Timer(10, NetSend2.clientListener);
                NetSend2.clientTime.start();
            } else {
                NetSend2.errLabel.setVisible(true);
                NetSend2.errLabel.setText("Request Denied");
                NetSend2.in.close();
                NetSend2.out.flush();
                NetSend2.out.close();
                NetSend2.skt.close();
            }
        } catch (IOException e) {
            System.exit(195);
        }
    }
}
