/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.netsend2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import static my.netsend2.NetSend2.pr;

/**
 *
 * @author Patrick
 */
public class ServerConnect implements Runnable {
    
    @Override
    public void run() {
        try {
               NetSend2.clnt = NetSend2.svr.accept();
               NetSend2.cancelButton.setVisible(false);
               pr = new OutputStreamWriter(NetSend2.clnt.getOutputStream());
            NetSend2.br = 
                    new BufferedReader(new InputStreamReader(NetSend2.clnt.getInputStream()));
            NetSend2.hostName = NetSend2.nameBox.getText();
            NetSend2.guestName = NetSend2.br.readLine();
            pr.write("goidjf" + NetSend2.newln);
            pr.flush();
            int ans = JOptionPane.showConfirmDialog(NetSend2.lobby, "Accept " + NetSend2.guestName + "'s request to chat?");
            if (ans == 0) {
                pr.write("yes" + NetSend2.newln);
                pr.flush();
                pr.write(NetSend2.hostName + NetSend2.newln);
                pr.flush();
                NetSend2.UITransitionChat();
                NetSend2.roomLabel.setText("You are now chatting with " + NetSend2.guestName + ".");
                NetSend2.serverTime = new Timer(10, NetSend2.serverListener);
                NetSend2.serverTime.start();
                
            } else {
                pr.write("no" + NetSend2.newln);
                pr.flush();
                pr.close();
                NetSend2.errLabel.setText("Connection Cancelled");
                NetSend2.br.close();
                NetSend2.clnt.close();
                NetSend2.svr.close();
            }
            } catch (SocketException e) {
               
            } catch (IOException j) {
                NetSend2.errLabel.setVisible(true);
                NetSend2.errLabel.setText("I'll be surprised if this error ever shows up.");
               
          
            }
    }
    
}
