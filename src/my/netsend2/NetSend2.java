/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.netsend2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.awt.event.*;
import java.awt.Font;
import javax.swing.*;
/**
 *
 * @author Patrick
 */
public class NetSend2 extends javax.swing.JFrame {
    public static ServerSocket svr = null;
    static boolean isFocus = false;
    static Socket clnt = null;
    static boolean first = true;
    static Socket skt = null;
    boolean chatting = false;
    static boolean meLast = false;
    static boolean connecting = false;
    static OutputStreamWriter pr = null;
    static BufferedReader br = null;
    static OutputStreamWriter out = null;
    static boolean firstTime = true;
    static BufferedReader in = null;
    static String hostName;
    static String guestName;
    static String newln = System.getProperty("line.separator");
    int answer = 0;
    static String serverAddress = "";
    static boolean isHost;
    static Timer serverTime = null;
    static Timer clientTime = null;
    static int port = 0;
    static ActionListener serverListener;
    static ActionListener clientListener;
    static Thread thread = null;
    WindowFocusListener l;
    
    public static int cancel = 0;
    /**
     * Creates new form userInterface
     */
    public NetSend2() {
        this.l = new WindowFocusListener() {
            @Override
    public void windowLostFocus(WindowEvent e) {
        
            isFocus = false;
        
    }
    @Override
    public void windowGainedFocus(WindowEvent e) {
        
            isFocus = true;
        
    }
        };
    
        clientListener = new ActionListener() {
@Override
public void actionPerformed(ActionEvent evt) {
    try {
        if (in.ready()) {
            String inMessage;
              inMessage = in.readLine();
              if (inMessage.equals("EXIT_CONST")) {
                  clientTime.stop();
                  try {
                      skt.close();
                      in.close();
                      out.flush();
                      out.close();
                      exitButton.setText("Leave Chat Room");
                      roomLabel.setText(hostName + " has left the room.");
                      chatting = false;
                  } catch (IOException e) {
                      System.exit(91);
                  }
                  return;
              }
         if (!isFocus) {
         try {
            File f = new File(System.getProperty("user.dir") + "\\sounds\\s1.wav");
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;
            
            stream = AudioSystem.getAudioInputStream(f);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
         
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
         }
              if (meLast == true || first == true) {
                  chatArea.setText(chatArea.getText() + newln + hostName.toUpperCase() + ":" + newln);
                  meLast = false;
                  first = false;
              }
              chatArea.setText(chatArea.getText() + inMessage + newln);
        }
    } catch (IOException e) {
        System.exit(90);
    }
}

 
};
        serverListener = new ActionListener() {
@Override
public void actionPerformed(ActionEvent evt) {
    try {
        if (br.ready()) {
            String inMessage;
              inMessage = br.readLine();
               if (inMessage.equals("EXIT_CONST")) {
                  serverTime.stop();
                  try {
                      clnt.close();
                      svr.close();
                      br.close();
                      pr.flush();
                      pr.close();
                      exitButton.setText("Leave Chat Room");
                      roomLabel.setText(guestName + " has left the room.");
                      chatting = false;
                  } catch (IOException e) {
                      System.exit(91);
                  }
                  return;
              }
         if (!isFocus) {
         try {
            File f = new File(System.getProperty("user.dir") + "\\sounds\\s1.wav");
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;
            
            stream = AudioSystem.getAudioInputStream(f);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
         
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
         }
              if (meLast == true || first == true) {
                  chatArea.setText(chatArea.getText() + newln + guestName.toUpperCase() + ":" + newln);
                  meLast = false;
                  first = false;
             }
          chatArea.setText(chatArea.getText() + inMessage + newln);
        }
    } catch (IOException e) {
        System.exit(90);
    }
}

  
};
        initComponents();
        errLabel.setVisible(false);
        friendList.setVisible(false);
        cancelButton.setVisible(false);
        addWindowFocusListener(l);
    }

    
 
  
    
    public void disconnect() {
        if (chatting) {
        if (isHost) {
            serverTime.stop();
            try {
                pr.write("EXIT_CONST");
                pr.flush();
                clnt.close();
                svr.close();
                pr.flush();
                pr.close();
                br.close();
                UITransitionLobby();
            } catch (IOException e) {
                System.exit(90);
            }
            
        } else {
                  clientTime.stop();
                  try {
                      out.write("EXIT_CONST");
                      out.flush();
                      skt.close();
                      in.close();
                      out.flush();
                      out.close();
                      UITransitionLobby();
                  } catch (IOException e) {
                      System.exit(92);
                  }
        }
        } else {
            UITransitionLobby();
        }
    }
    public int getPort(String s) {
        String portStr = "654" + s;
        return Integer.parseInt(portStr);
    }
   
    public void connect() throws IOException {
  
        if (nameBox.getText().length() > 18 || nameBox.getText().length() < 1) {
            errLabel.setVisible(true);
            errLabel.setText("Name must be between 1 and 18 characters.");
            return;
        }
        isHost = hostButton.isSelected();
        port = getPort(String.valueOf(channelSelect.getSelectedItem()));
        if (isHost) {
            
            try {
                svr = new ServerSocket(port);
            } catch (IOException e) {
                errLabel.setVisible(true);
                errLabel.setText("All your port are belong to us. You are quit now.");
                return;
            }
            
            cancelButton.setVisible(true);
            errLabel.setVisible(true);
            errLabel.setText("Connecting...");
            
            Thread thr = new Thread(new ServerConnect());
            thr.start();
            
            
            
        } else {
            serverAddress = hostNameBox.getText();
            cancelButton.setVisible(true);
            errLabel.setVisible(true);
            errLabel.setText("Connecting...");
            
            thread = new Thread(new ClientConnect());
            thread.start();
        }
        chatting = true;
    }
    public static void UITransitionChat() {
       
                errLabel.setVisible(false);
                lobby.setVisible(false);
                exitButton.setText("Disconnect");
                chatArea.setText("");
                txtBox.setText("");
                first = true;
                meLast = false;
                firstTime = true;
                txtBox.setText("Enter your message here");
                Font f = new Font("Tahoma", Font.ITALIC, 13);
                txtBox.setFont(f);
                msgButton.setEnabled(false);
    }
    public static void UITransitionLobby() {
        errLabel.setVisible(true);
        errLabel.setText("Disconnected from Partner");
        lobby.setVisible(true);
    }
    
    public void showFriendList() {
        friendList.setVisible(true);
        File friends = new File(System.getProperty("user.dir") + "\\fl.txt");
        DefaultListModel dlm = new DefaultListModel();
        BufferedReader reader;
        try {
            friends.createNewFile();
            reader = new BufferedReader(new FileReader(friends));
            while(reader.ready()) {
                String[] s = reader.readLine().split(" ");
                dlm.addElement(s[0]);
            }
            list.setModel(dlm);
            reader.close();
        } catch (Exception e) {
            System.exit(95);
        }
    }
    public void addFriend() {
        String name = JOptionPane.showInputDialog(friendList, "Enter Friend Name:", "Add Friend", JOptionPane.PLAIN_MESSAGE);
        if (name == null) return;
        if (name.contains(" ")) {
            JOptionPane.showMessageDialog(friendList, "Name cannot contain spaces", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        String address = JOptionPane.showInputDialog(friendList, "Enter Friend IP address:", "Add Friend", JOptionPane.PLAIN_MESSAGE);
        if (address == null) return;
        if (address.contains(" ")) {
            JOptionPane.showMessageDialog(friendList, "IP address cannot contain spaces", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        File friends = new File(System.getProperty("user.dir") + "\\fl.txt");
        PrintWriter writer;
        try {
            writer = new PrintWriter(new FileWriter(friends, true));
            writer.println(name + " " + address);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.exit(295);
        }
        showFriendList();
    }
    public void removeFriend() {
        int nameToRemove = list.getSelectedIndex();
        if (nameToRemove == -1) return;
        String newList = "";
        int counter = 0;
        File friends = new File(System.getProperty("user.dir") + "\\fl.txt");
        PrintWriter writer;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(friends));
            while (reader.ready()) {
                if (counter == nameToRemove) {
                    reader.readLine();
                } else {
                    newList = newList + reader.readLine() + newln;
                }
                counter++;
            }
            reader.close();
            writer = new PrintWriter(new FileWriter(friends));
            writer.print(newList);
            writer.flush();
            writer.close();
            showFriendList();
        } catch (Exception e) {
            System.exit(195);
        }
    }
    public void selectFriend() {
        int selected = list.getSelectedIndex();
        if (selected == -1) return;
        String address = "";
        int counter = 0;
        BufferedReader reader;
        File friends = new File(System.getProperty("user.dir") + "\\fl.txt");
        try {
            reader = new BufferedReader(new FileReader(friends));
            while (counter < selected) {
                reader.readLine();
                counter ++;
            }
            String[] person = reader.readLine().split(" ");
            address = person[1];
        } catch (Exception e) {
            System.exit(6);
        }
        friendList.setVisible(false);
        hostNameBox.setText(address);
    }
    public void sendMessageServer() {
        String message = txtBox.getText();
        if (message.replace(" ", "").equals("")) return; 
        
        try {
            pr.write(message + newln);
            pr.flush();
        } catch (IOException e) {
            roomLabel.setText("There was an error. Please close the program.");
        }
        if (meLast == false || first == true) {
            chatArea.setText(chatArea.getText() + newln + "ME" + ":" + newln);
            meLast = true;
            first = false;
        }
        chatArea.setText(chatArea.getText() + message + newln);
        
    }
    public void sendMessageClient() {
        String message = txtBox.getText();
        if (message.replace(" ", "").equals("")) return; 
        try {
            out.write(message + newln);
            out.flush();
        } catch (IOException e) {
            roomLabel.setText("There was an error. Please close the program.");
        }
        if (meLast == false || first == true) {
            chatArea.setText(chatArea.getText() + newln + "ME" + ":" + newln);
            meLast = true;
            first = false;
        }
        chatArea.setText(chatArea.getText() + message + newln);
        
    }
    
               /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        chatRoom = new javax.swing.JPanel();
        roomLabel = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();
        msgButton = new javax.swing.JButton();
        txtBox = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        lobby = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        connectButton = new javax.swing.JButton();
        quitButton = new javax.swing.JButton();
        hostButton = new javax.swing.JRadioButton();
        guestButton = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        hostLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        hostNameBox = new javax.swing.JTextField();
        nameBox = new javax.swing.JTextField();
        errLabel = new javax.swing.JLabel();
        channelSelect = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        friendButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        friendList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        selectButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("NetSend 2.0");
        setResizable(false);

        roomLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        roomLabel.setText("You are now chatting with");

        exitButton.setText("Disconnect");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        msgButton.setText("Send");
        msgButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                msgButtonActionPerformed(evt);
            }
        });
        msgButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                msgButtonFocusGained(evt);
            }
        });

        txtBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBoxFocusGained(evt);
            }
        });
        txtBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBoxKeyPressed(evt);
            }
        });

        chatArea.setEditable(false);
        chatArea.setColumns(20);
        chatArea.setRows(5);
        chatArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane5.setViewportView(chatArea);

        javax.swing.GroupLayout chatRoomLayout = new javax.swing.GroupLayout(chatRoom);
        chatRoom.setLayout(chatRoomLayout);
        chatRoomLayout.setHorizontalGroup(
            chatRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roomLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(chatRoomLayout.createSequentialGroup()
                .addComponent(txtBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msgButton))
            .addComponent(exitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        chatRoomLayout.setVerticalGroup(
            chatRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatRoomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roomLabel)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(chatRoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(msgButton)
                    .addComponent(txtBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitButton)
                .addContainerGap())
        );

        chatRoom.setBounds(10, 10, 400, 440);
        jLayeredPane1.add(chatRoom, javax.swing.JLayeredPane.DEFAULT_LAYER);

        lobby.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                lobbyComponentHidden(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Agency FB", 1, 72)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("NetSend 2.0");

        jLabel6.setFont(new java.awt.Font("Agency FB", 0, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Created by Patrick Wahl");

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        quitButton.setText("Quit");
        quitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(hostButton);
        hostButton.setSelected(true);
        hostButton.setText("Host");
        hostButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hostButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(guestButton);
        guestButton.setText("Guest");
        guestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guestButtonActionPerformed(evt);
            }
        });

        jLabel7.setText("Determine whether you or your partner will be the host:");

        hostLabel.setText("Enter host IP Address:");
        hostLabel.setEnabled(false);

        jLabel9.setText("Enter your name:");

        hostNameBox.setEnabled(false);
        hostNameBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hostNameBoxKeyPressed(evt);
            }
        });

        nameBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameBoxActionPerformed(evt);
            }
        });
        nameBox.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                nameBoxInputMethodTextChanged(evt);
            }
        });
        nameBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameBoxKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nameBoxKeyTyped(evt);
            }
        });

        errLabel.setText("Error");
        errLabel.setOpaque(true);

        channelSelect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" }));

        jLabel1.setText("Select a channel:");

        jLabel2.setText("Version 1.6");

        friendButton.setText("Friends...");
        friendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                friendButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout lobbyLayout = new javax.swing.GroupLayout(lobby);
        lobby.setLayout(lobbyLayout);
        lobbyLayout.setHorizontalGroup(
            lobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lobbyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(lobbyLayout.createSequentialGroup()
                        .addGroup(lobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(hostButton, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(guestButton, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 85, Short.MAX_VALUE))
                    .addGroup(lobbyLayout.createSequentialGroup()
                        .addGroup(lobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(hostLabel)
                            .addComponent(jLabel9)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(lobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(lobbyLayout.createSequentialGroup()
                                .addComponent(channelSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(hostNameBox)
                            .addComponent(nameBox)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lobbyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(lobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lobbyLayout.createSequentialGroup()
                                .addComponent(friendButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(connectButton))
                            .addComponent(errLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lobbyLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(117, 117, 117)
                                .addComponent(quitButton))
                            .addComponent(cancelButton, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        lobbyLayout.setVerticalGroup(
            lobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lobbyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hostButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(guestButton)
                .addGap(6, 6, 6)
                .addGroup(lobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(channelSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hostLabel)
                    .addComponent(hostNameBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(nameBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(lobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lobbyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(lobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(connectButton)
                            .addComponent(friendButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(errLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addComponent(quitButton)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lobbyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2))))
        );

        lobby.setBounds(0, 0, 430, 470);
        jLayeredPane1.add(lobby, new Integer(3));

        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(list);

        jLabel3.setText("Friends:");

        selectButton.setText("Select Friend");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        addButton.setText("Add Friend");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        jButton3.setText("Close Friend List");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        removeButton.setText("Remove Friend");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout friendListLayout = new javax.swing.GroupLayout(friendList);
        friendList.setLayout(friendListLayout);
        friendListLayout.setHorizontalGroup(
            friendListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(friendListLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(friendListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(friendListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        friendListLayout.setVerticalGroup(
            friendListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(friendListLayout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(friendListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(friendListLayout.createSequentialGroup()
                        .addComponent(selectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(193, Short.MAX_VALUE))
        );

        friendList.setBounds(0, 0, 430, 480);
        jLayeredPane1.add(friendList, new Integer(5));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void guestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guestButtonActionPerformed
        boolean guestSelected = guestButton.isSelected();
            hostLabel.setEnabled(guestSelected);
            hostNameBox.setEnabled(guestSelected);
            
    }//GEN-LAST:event_guestButtonActionPerformed

    private void hostButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hostButtonActionPerformed
        boolean guestSelected = guestButton.isSelected();
            hostLabel.setEnabled(guestSelected);
            hostNameBox.setEnabled(guestSelected);
       
    }//GEN-LAST:event_hostButtonActionPerformed

    private void quitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_quitButtonActionPerformed

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        try { 
            connect();
        } catch (IOException e) {
            errLabel.setVisible(true);
            errLabel.setText("Input-Output Error");
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    private void msgButtonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_msgButtonFocusGained
        txtBox.requestFocus();
    }//GEN-LAST:event_msgButtonFocusGained

    private void msgButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_msgButtonActionPerformed
        if (chatting) {
        if (txtBox.getText().length() > 0) {
        if (isHost) {
            sendMessageServer();
        } else {
            sendMessageClient();
        }
        txtBox.setText("");
        }
        }
    }//GEN-LAST:event_msgButtonActionPerformed

    private void lobbyComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_lobbyComponentHidden
      
    }//GEN-LAST:event_lobbyComponentHidden

    private void txtBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBoxKeyPressed
        if (chatting) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtBox.getText().length() > 0) {
            if (isHost) {
                sendMessageServer();
            } else {
                sendMessageClient();
            }
            txtBox.setText("");
        }
        }
        }
    }//GEN-LAST:event_txtBoxKeyPressed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        disconnect();
    }//GEN-LAST:event_exitButtonActionPerformed

    private void nameBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameBoxKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
            connect();
        } catch (IOException e) {
            errLabel.setVisible(true);
            errLabel.setText("Input-Output Error");
        }
        }
    }//GEN-LAST:event_nameBoxKeyPressed

    private void nameBoxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameBoxKeyTyped
        
    }//GEN-LAST:event_nameBoxKeyTyped

    private void nameBoxInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_nameBoxInputMethodTextChanged

    }//GEN-LAST:event_nameBoxInputMethodTextChanged

    private void nameBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameBoxActionPerformed

    }//GEN-LAST:event_nameBoxActionPerformed

    private void txtBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBoxFocusGained
        if (firstTime) {
            Font newF = new Font("Tahoma", Font.PLAIN, 13);
            txtBox.setFont(newF);
            txtBox.setText("");
            firstTime = false;
            msgButton.setEnabled(true);
        }
    }//GEN-LAST:event_txtBoxFocusGained

    private void friendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_friendButtonActionPerformed
        showFriendList();
    }//GEN-LAST:event_friendButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        friendList.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        addFriend();
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
       removeFriend();
    }//GEN-LAST:event_removeButtonActionPerformed

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        selectFriend();
    }//GEN-LAST:event_selectButtonActionPerformed

    private void hostNameBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hostNameBoxKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
            connect();
        } catch (IOException e) {
            errLabel.setVisible(true);
            errLabel.setText("Input-Output Error");
        }
        }
    }//GEN-LAST:event_hostNameBoxKeyPressed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if (isHost) {
            try {
                svr.close();
                cancelButton.setVisible(false);
                errLabel.setText("Connection Cancelled");
            } catch (IOException ex) {
                
            }
        } else {
            thread.interrupt();
            cancelButton.setVisible(false);
            errLabel.setText("Connection Cancelled");
        }
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NetSend2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NetSend2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NetSend2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NetSend2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NetSend2().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.ButtonGroup buttonGroup1;
    public static javax.swing.JButton cancelButton;
    private javax.swing.JComboBox channelSelect;
    public static javax.swing.JTextArea chatArea;
    private javax.swing.JPanel chatRoom;
    private javax.swing.JButton connectButton;
    public static javax.swing.JLabel errLabel;
    public static javax.swing.JButton exitButton;
    private javax.swing.JButton friendButton;
    private javax.swing.JPanel friendList;
    private javax.swing.JRadioButton guestButton;
    private javax.swing.JRadioButton hostButton;
    private javax.swing.JLabel hostLabel;
    private javax.swing.JTextField hostNameBox;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    public javax.swing.JList list;
    public static javax.swing.JPanel lobby;
    public static javax.swing.JButton msgButton;
    public static javax.swing.JTextField nameBox;
    private javax.swing.JButton quitButton;
    private javax.swing.JButton removeButton;
    public static javax.swing.JLabel roomLabel;
    private javax.swing.JButton selectButton;
    public static javax.swing.JTextField txtBox;
    // End of variables declaration//GEN-END:variables
}


        

