import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.Style;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.net.*;
import java.nio.Buffer;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Client
 *
 * This is the program the user uses to see and send messages with other clients
 *
 * @author Hudson Reamer, Lucas Mazza, Supriya Dixit, CS 18000
 * @version December 6, 2020
 *
 */
public class Client extends JFrame {
    //connect to server
    static String hostname = "localhost";
    static int portNumber = 6174;
    //initial startup page
    static JButton signIn;
    static JButton signUp;
    static JLabel titleLabel;
    static JTextField userName;
    static JTextField password;
    static JFrame myFrame;
    static JPanel myTopPanel;
    static JPanel myMiddlePanel;
    static JPanel myBottomPanel;
    //frame for editing messages
    static JFrame optionsForConvoLogs;
    static JPanel topPanelForConvoLogs;
    static JPanel middlePanelForConvoLogs;
    static JPanel bottomPanelForConvoLogs;
    static JButton deleteConvoSelected;
    static JTextField editMessage;
    static JTextField newEditedMessage;
    static JButton confirmEditMessage;
    static JTextField deleteMessageField;
    static JButton confirmMessageDeletion;
    static JButton enterOptionsFrameForMessages;
    //signup frame
    static JFrame signUpFrame;
    static JPanel signUpMyTopPanel;
    static JPanel signUpMyMiddlePanel;
    static JPanel signUpMyBottomPanel;
    static JTextField signUpUsername;
    static JTextField signUpPassword;
    static JLabel signUpTopPanelLabel;
    static JButton backButton;
    static JButton confirmSignUp;

    //options board
    static JFrame optionsMenu;
    static JTextField passwordTextChange;
    static JButton confirmPasswordChange;
    static JPanel topPanelOptionsMenu;
    static JButton deleteAccount;
    static JButton backButtonToChat;
    static JPanel middlePanelForOptions;
    static JLabel optionsMenuLabel;
    static JLabel passwordChangeLabel;
    //private static PrintWriter writer = new PrintWriter(socket.getOutputStream());

    //message board
    private static JButton delete;
    private static JButton back;
    private static JTextField deleteMessage;
    private static JButton send;

    private static JTextField composeMessage;
    public static Socket socket;
    //static ArrayList<String> chats = new ArrayList<>();
    static UserAccount currentUser;
    static JPanel chatter;
    static Container content;
    static JTextArea textArea;
    static JPanel top;
    static JPanel bottom;

    //full display, including the chats and the open message
    private static JFrame fullFrame;
    private static JSplitPane splitPane;
    private static JPanel chatButtonFrame;
    private static DefaultListModel<String> model;
    private static JList<String> list;
    private static ArrayList<Conversation> conversations;
    private static JButton newConvo;

    //options panel if the user wants to edit username or password, or delete a conversation
    private static JFrame optionsPane;
    private static JButton options;
    private static JButton changePassword;
    private static JButton deleteConversation;
    private static Client client = new Client();

    private static BufferedReader reader;
    private static PrintWriter writer;
    static String currentFileTitle;

    private static Timer timerList;
    private static Timer chatTimer;
    private static String currentFile;

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        socket = new Socket(hostname, portNumber);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                myFrame = new JFrame("Welcome");
                myFrame.setLayout(new BorderLayout());
                myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                myFrame.setSize(500, 500);
                myFrame.setVisible(true);
                myFrame.getContentPane().setBackground(Color.blue);
                myFrame.setResizable(false);

                signIn = new JButton("Sign in");
                //signIn.setFont(new Font(null, 0, 30));
                signIn.setPreferredSize(new Dimension(150, 40));
                signIn.addActionListener(actionListener);

                signUp = new JButton("Sign up");
                //signUp.setFont(new Font(null, 0, 30));
                signUp.setPreferredSize(new Dimension(150, 40));
                signUp.addActionListener(actionListener);

                password = new JTextField("Password");
                password.setFont(new Font(null, 0, 15));
                password.setMaximumSize(new Dimension(300, 40));
                password.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() > 0) {
                            if (password.getText().equals("Password")) {
                                password.setText(null);
                            }
                        }
                    }
                });

                userName = new JTextField("Username");
                userName.setFont(new Font(null, 0, 15));
                userName.setMaximumSize(new Dimension(300, 40));
                userName.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() > 0) {
                            if (userName.getText().equals("Username"))
                                userName.setText(null);
                        }
                    }

                });

                titleLabel = new JLabel("Please enter your information:");
                titleLabel.setFont(new Font(null, 0, 25));
                myTopPanel = new JPanel();
                myTopPanel.add(titleLabel);

                myBottomPanel = new JPanel();
                myBottomPanel.add(signUp);
                myBottomPanel.add(signIn);
                back = new JButton("Back");
                myMiddlePanel = new JPanel();
                myMiddlePanel.setLayout(new BoxLayout(myMiddlePanel, BoxLayout.Y_AXIS));
                myMiddlePanel.add(userName);
                myMiddlePanel.add(password);
                myMiddlePanel.setBackground(Color.blue);

                myFrame.add(myBottomPanel, BorderLayout.SOUTH);
                myFrame.add(myMiddlePanel, BorderLayout.CENTER);
                myFrame.add(myTopPanel, BorderLayout.NORTH);

                signUpFrame = new JFrame("Sign Up");
                signUpFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                signUpFrame.setSize(500, 500);
                signUpFrame.setResizable(false);

                signUpFrame.setTitle("Sign Up");
                signUpFrame.setLayout(new BorderLayout());
                signUpFrame.getContentPane().setBackground(Color.blue);

                signUpMyTopPanel = new JPanel();
                signUpMyMiddlePanel = new JPanel();
                signUpMyMiddlePanel.setLayout(new BoxLayout(signUpMyMiddlePanel, BoxLayout.Y_AXIS));
                signUpMyMiddlePanel.setBackground(Color.blue);
                signUpMyBottomPanel = new JPanel();

                signUpTopPanelLabel = new JLabel();
                signUpTopPanelLabel.setText("Please enter your information below in order to sign up:");
                signUpTopPanelLabel.setFont(new Font(null, 0, 20));
                signUpMyTopPanel.add(signUpTopPanelLabel);
                signUpTopPanelLabel.setFont(new Font(null, 0, 15));

                signUpUsername = new JTextField("Username:");
                signUpUsername.setFont(new Font(null, 0, 15));
                signUpMyMiddlePanel.add(signUpUsername);
                signUpUsername.setMaximumSize(new Dimension(300, 40));
                signUpUsername.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() > 0) {
                            if (signUpUsername.getText().equals("Username:")) {
                                signUpUsername.setText(null);
                            }
                        }
                    }
                });

                signUpPassword = new JTextField("Password:");
                signUpPassword.setFont(new Font(null, 0, 15));
                signUpMyMiddlePanel.add(signUpPassword);
                signUpPassword.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() > 0) {
                            if (signUpPassword.getText().equals("Password:")) {
                                signUpPassword.setText(null);
                            }
                        }
                    }
                });
                signUpPassword.setMaximumSize(new Dimension(300, 40));

                backButton = new JButton("Back");
                backButton.setSize(new Dimension(signUpMyTopPanel.getWidth(), signUpMyTopPanel.getHeight()));
                backButton.addActionListener(actionListener);
                backButton.setPreferredSize(new Dimension(150, 40));

                confirmSignUp = new JButton("Sign Up");
                confirmSignUp.setSize(new Dimension(signUpMyTopPanel.getWidth(), signUpMyTopPanel.getHeight()));


                confirmSignUp.addActionListener(actionListener);
                signUpMyBottomPanel.add(confirmSignUp);
                confirmSignUp.setPreferredSize(new Dimension(150, 40));
                signUpMyBottomPanel.add(backButton);

                signUpFrame.add(signUpMyMiddlePanel, BorderLayout.CENTER);
                signUpFrame.add(signUpMyBottomPanel, BorderLayout.SOUTH);
                signUpFrame.add(signUpMyTopPanel, BorderLayout.NORTH);
                signUpFrame.setVisible(false);

                conversations = new ArrayList<Conversation>();
                //Daemon textAreaRefresh = new Daemon();
                //textAreaRefresh.setDaemon(true);
                //textAreaRefresh.start();
                fullFrame = new JFrame("Messages");
                fullFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                fullFrame.setSize(500, 500);
                fullFrame.setResizable(false);
                chatButtonFrame = new JPanel();
                chatter = new JPanel();
                textArea = new JTextArea(23, 35);
                textArea.setEditable(false);
                composeMessage = new JTextField(20);
                top = new JPanel();
                bottom = new JPanel();
                enterOptionsFrameForMessages = new JButton("Message Options");
                enterOptionsFrameForMessages.addActionListener(actionListener);
                //delete = new JButton("Delete");
                //deleteMessage = new JTextField("What message would you like to delete?...");
                send = new JButton("Send");
                send.addActionListener(actionListener);
                newConvo = new JButton("+");
                newConvo.addActionListener(actionListener);
                chatButtonFrame.add(newConvo);
                options = new JButton("Account");

                options.addActionListener(actionListener);
                chatButtonFrame.add(options);
                model = new DefaultListModel<String>();
                list = new JList<String>(model);
                JScrollPane listScroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                list.setLayoutOrientation(JList.VERTICAL);
                list.setVisibleRowCount(-1);
                chatButtonFrame.add(list);
                JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                top.add(enterOptionsFrameForMessages);
                //top.add(delete);
                //top.add(deleteMessage);
                bottom.add(send);
                bottom.add(composeMessage);

                chatter.add(top, BorderLayout.NORTH);
                chatter.add(scroll, BorderLayout.CENTER);
                chatter.add(bottom, BorderLayout.SOUTH);

                splitPane = new JSplitPane();
                fullFrame.setSize(700, 500);
                splitPane.setDividerLocation(190);
                splitPane.setDividerSize(20);

                splitPane.setEnabled(false);
                splitPane.setRightComponent(chatter);
                splitPane.setLeftComponent(chatButtonFrame);
                fullFrame.add(splitPane);
                timerList = new Timer(3000, aLList);
                chatTimer = new Timer(2000, aLChat);
                //fullFrame.setVisible(true);

                //constuct message editing frame
                optionsForConvoLogs = new JFrame("Message Options");
                optionsForConvoLogs.setLayout(new BorderLayout());
                optionsForConvoLogs.setVisible(false);
                optionsForConvoLogs.setResizable(false);
                optionsForConvoLogs.setSize(300, 300);

                topPanelForConvoLogs = new JPanel();
                bottomPanelForConvoLogs = new JPanel();
                topPanelForConvoLogs.setBackground(Color.blue);
                optionsForConvoLogs.add(topPanelForConvoLogs, BorderLayout.NORTH);
                middlePanelForConvoLogs = new JPanel();
                middlePanelForConvoLogs.setBackground(Color.blue);
                middlePanelForConvoLogs.setLayout(new BoxLayout(middlePanelForConvoLogs, BoxLayout.Y_AXIS));
                deleteConvoSelected = new JButton("Delete this conversation");
                deleteConvoSelected.addActionListener(actionListener);
                middlePanelForConvoLogs.add(deleteConvoSelected);
                editMessage = new JTextField("Put the text you want to edit here");
                editMessage.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (editMessage.getText().equals("Put the text you want to edit here")) {
                            editMessage.setText("");
                        }
                    }
                });
                newEditedMessage = new JTextField("Put your edited message here");
                newEditedMessage.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (newEditedMessage.getText().equals("Put your edited message here")) {
                            newEditedMessage.setText("");
                        }
                    }
                });
                confirmEditMessage = new JButton("Confirm edit Message");
                confirmEditMessage.addActionListener(actionListener);
                editMessage.setMaximumSize(new Dimension(500, 100));
                newEditedMessage.setMaximumSize(new Dimension(500, 100));
                deleteMessage = new JTextField("Message to delete");
                deleteMessage.setMaximumSize(new Dimension(500, 100));
                deleteMessage.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (deleteMessage.getText().equals("Message to delete")) {
                            deleteMessage.setText("");
                        }
                    }
                });
                deleteMessage.setFont(new Font(null, 0, 15));
                confirmMessageDeletion = new JButton("Confirm Message to delete");
                confirmMessageDeletion.addActionListener(actionListener);
                middlePanelForConvoLogs.add(editMessage);
                middlePanelForConvoLogs.add(newEditedMessage);
                middlePanelForConvoLogs.add(confirmEditMessage);
                bottomPanelForConvoLogs.add(confirmMessageDeletion);
                middlePanelForConvoLogs.add(deleteMessage);

                optionsForConvoLogs.add(middlePanelForConvoLogs, BorderLayout.CENTER);

                optionsForConvoLogs.add(bottomPanelForConvoLogs, BorderLayout.SOUTH);

                //options menu config
                optionsMenu = new JFrame();
                optionsMenu.setVisible(false);
                optionsMenu.setLayout(new BorderLayout());
                optionsMenu.setTitle("Options");
                optionsMenu.setSize(500, 500);
                optionsMenu.setResizable(false);

                topPanelOptionsMenu = new JPanel();
                deleteAccount = new JButton("Delete Account");
                deleteAccount.setMaximumSize(new Dimension(300, 40));
                deleteAccount.addActionListener(actionListener);

                middlePanelForOptions = new JPanel();
                middlePanelForOptions.setBackground(Color.blue);
                middlePanelForOptions.setLayout(new BoxLayout(middlePanelForOptions, BoxLayout.Y_AXIS));


                passwordTextChange = new JTextField("New Password");

                confirmPasswordChange = new JButton("Change Password");
                confirmPasswordChange.setMaximumSize(new Dimension(300, 40));
                confirmPasswordChange.addActionListener(actionListener);
                passwordTextChange.setMaximumSize(new Dimension(300, 40));


                middlePanelForOptions.add(passwordTextChange);
                middlePanelForOptions.add(confirmPasswordChange);
                middlePanelForOptions.add(deleteAccount);


                backButtonToChat = new JButton("Back");
                backButtonToChat.setFont(new Font(null, 0, 15));
                backButtonToChat.addActionListener(actionListener);
                topPanelOptionsMenu.add(backButtonToChat);

                optionsMenuLabel = new JLabel("Account Options");
                optionsMenuLabel.setFont(new Font(null, 0, 15));
                topPanelOptionsMenu.add(optionsMenuLabel);


                optionsMenu.add(topPanelOptionsMenu, BorderLayout.NORTH);
                optionsMenu.add(middlePanelForOptions, BorderLayout.CENTER);
            }
        });
    }

    static ActionListener aLList = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateJList();
        }
    };

    static ActionListener aLChat = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentFileTitle = currentFile;
            textArea.setText(getConversation(currentFileTitle));
        }
    };


    static ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == signIn) {
                try {
                    getValidAccount(userName.getText(), password.getText());
                } catch (Exception g) {
                    g.printStackTrace();
                }
            }
            if (e.getSource() == signUp) {
                myFrame.setVisible(false);
                signUpFrame.setVisible(true);
            }
            if (e.getSource() == backButton) {
                myFrame.setVisible(true);
                signUpFrame.setVisible(false);
            }
            if (e.getSource() == confirmSignUp) {
                createAccount(signUpUsername.getText(), signUpPassword.getText());
            }
            if (e.getSource() == send) {
                sendNewMessage(composeMessage.getText());
            }
            if (e.getSource() == newConvo) {
                createCo();
            }
            if (e.getSource() == options) {
                optionsMenu.setVisible(true);
                fullFrame.setVisible(false);
            }
            if (e.getSource() == backButtonToChat) {
                fullFrame.setVisible(true);
                optionsMenu.setVisible(false);
            }
            if (e.getSource() == enterOptionsFrameForMessages) {
                optionsForConvoLogs.setVisible(true);
            }

            if (e.getSource() == confirmPasswordChange) {
                changePassword(passwordTextChange.getText());
            }

            if (e.getSource() == deleteAccount) {
                deleteAccount(currentUser.getUserName());
            }
            if (e.getSource() == deleteConvoSelected) {
                deleteConvo();
            }
            if (e.getSource() == confirmEditMessage) {
                if (editMessage.getText().equals("Put the text you want to edit here") ||
                        newEditedMessage.getText().equals("Put your edited message here")) {
                    if (editMessage.getText() != null || !editMessage.getText().equals("") ||
                            newEditedMessage.getText() != null || !newEditedMessage.getText().equals("")) {
                        JOptionPane.showMessageDialog(null,
                                "Please enter a value in both fields", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    editMessage(editMessage.getText(), newEditedMessage.getText());
                    editMessage.setText("Put the text you want to edit here");
                    newEditedMessage.setText("Put your edited message here");
                }
            }
            if (e.getSource() == confirmMessageDeletion) {
                if (deleteMessage.getText().equals("Message to delete")) {
                    if (deleteMessage.getText() != null || !deleteMessage.getText().equals("")) {
                        JOptionPane.showMessageDialog(null,
                                "Please enter a value in the field", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    deleteMessage(deleteMessage.getText());
                    deleteMessage.setText("Message to delete");
                }
            }
            MouseListener mouseListener = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() > 0) {
                        String selectedItem = (String) list.getSelectedValue();
                        currentFile = selectedItem;
                        currentFileTitle = selectedItem;
                        System.out.println("currentFileTitle " + currentFileTitle);
                        textArea.setText(getConversation(currentFileTitle));
                        chatTimer.start();
                    }
                }
            };
            list.addMouseListener(mouseListener);
        }
    };

    public static void createAccount(String userName1, String password1) {
        try {
            String sentToServer = "SignUp - " + userName1 + " - " + password1;
            writer.write(sentToServer);
            writer.println();
            writer.flush();

            String response = reader.readLine();

            if (response.equals("Signup Successful")) {
                JOptionPane.showMessageDialog(null, "Your account has " +
                        "been created, return to the login screen.");
            } else {
                JOptionPane.showMessageDialog(null, "The user name you created was taken," +
                        "please try another one.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static void getValidAccount(String username1, String pass) throws UnknownHostException, IOException {
        try {
            String stringReturned = "Login - " + username1 + " - " + pass;
            writer.write(stringReturned);
            writer.println();
            writer.flush();

            String response = reader.readLine();
            if (response.equals("Valid User")) {
                userName.setText("Username");
                password.setText("Password");
                myFrame.setVisible(false);
                currentUser = new UserAccount(username1, pass);
                initJList(username1);
                fullFrame.setVisible(true);
                timerList.start();
            } else {
                JOptionPane.showMessageDialog(null, "Your username or password was incorrect.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException io) {
            JOptionPane.showMessageDialog(null, "Bad connection",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void createCo() {
        int finish = -20;
        ArrayList<String> names = new ArrayList<String>(0);
        String name;
        names.add(currentUser.getUserName());
        String title = "";
        do {
            name = JOptionPane.showInputDialog(null, "Enter the UserAccount",
                    "Create Conversation",
                    JOptionPane.QUESTION_MESSAGE);
            if (check(name)) {
                names.add(name);
                System.out.println(name + " added");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid User", "Create Conversation",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("gets to finish");
            finish = JOptionPane.showConfirmDialog(null, "Would you like add another user?",
                    "Create Conversation", JOptionPane.YES_NO_OPTION);
        } while (finish == JOptionPane.YES_OPTION);

        String line = "";
        for (int i = 0; i < names.size(); i++) {
            if (i != names.size() - 1) {
                line = line + names.get(i) + " - ";
            } else {
                line = line + names.get(i);
            }
        }
        String line2 = "startConversation - " + line;
        System.out.println("line sent to startConversation: " + line2);
        writer.write(line2);
        writer.println();
        writer.flush();

        updateJList(line);

    }

    private static void connectUsers(ArrayList<String> names) {
        // this sends the users that are in a conversation in order to connect them all
        for (int i = 0; i < names.size(); i++) {
            writer.write(names.get(i));
            writer.println();
            writer.flush();
        }
    }

    public static boolean check(String name) { //this will check if the user exists
        boolean checker = false;
        try {
            writer.write("checkValidUser - " + name);
            writer.println();
            writer.flush();
            String response = reader.readLine();
            if (response.equals("User is Valid " + name)) {
                checker = true;
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }

        return checker;
    }

    public static void sendNewMessage(String message) {
        if (message.equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Your message is empty, add a value first to send."
                    , "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            String[] messageWordLimitTest = message.split(" ");
            if (messageWordLimitTest.length > 100) {
                JOptionPane.showMessageDialog(null, "Your message is "
                        + (messageWordLimitTest.length - 100) + "word(s) too long."
                        + "Please shorten it.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                //updateConversation - message - userWhoSent - user - user - user - ..."
                System.out.println("updateConversation" + " - " + message + " - " + currentUser.getUserName() + " - "
                        + currentFileTitle);
                writer.write("updateConversation" + " - " + message + " - " + currentUser.getUserName() + " - "
                        + currentFileTitle); //need end of command with title of all users in chat
                writer.println();
                writer.flush();
            }

            for (int i = 0; i < conversations.size(); i++) {
                System.out.println("conversations get title" + conversations.get(i).getFilename());
                if ((currentFileTitle + ".txt").equals(conversations.get(i).getFilename())) {
                    textArea.setText(getConversation(conversations.get(i).getTitle()));
                }
            }

        }
        composeMessage.setText("");
        // else send message to server
    }

    private static void initJList(String userName1) {
        try {
            writer.write("getAllConversationsInvolved - " + userName1);
            writer.println();
            writer.flush();
            String conversationsNonSplit = reader.readLine();
            System.out.println("allconversations recieved: " + conversationsNonSplit);
            if (conversationsNonSplit != null && !conversationsNonSplit.equals("No conversations in file")) {
                String[] conversationsSplit = conversationsNonSplit.split(", ");
                for (int i = 0; i < conversationsSplit.length; i++) {
                    Conversation n = createConversationObject(conversationsSplit[i], conversationsSplit[i] +
                            ".txt");
                    conversations.add(n);
                    model.addElement(n.getTitle());
                }
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }


        client.repaint();
    }

    public static void updateJList(String conversationTitle) {
        try {
            writer.write("getAllConversationsInvolved - " + currentUser.getUserName());
            writer.println();
            writer.flush();
            String conversationsNonSplit = reader.readLine();
            System.out.println("conversation title sent to update Jlist " + conversationTitle);
            //System.out.println("allconversations recieved: " + conversationsNonSplit);
            if (conversationsNonSplit != null) {
                String[] conversationsSplit = conversationsNonSplit.split(", ");
                //search for conversationTitle
                for (int i = 0; i < conversationsSplit.length; i++) {
                    System.out.println(conversationsSplit[i]);
                    if (conversationTitle.equals(conversationsSplit[i])) {
                        System.out.println("final conversation title " + conversationsSplit[i]);
                        Conversation n = createConversationObject(conversationsSplit[i], conversationsSplit[i]
                                + ".txt");
                        conversations.add(n);
                        model.addElement(n.getTitle());
                    }
                }
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        client.repaint();
    }

    public static void updateJList() {
        try {
            writer.write("getAllConversationsInvolved - " + currentUser.getUserName());
            writer.println();
            writer.flush();
            String conversationsNonSplit = reader.readLine();
            //compare filenames with the what is sent from getAllConversationsInvolved
            String[] split = conversationsNonSplit.split(", ");
            System.out.println(split.length + " ::: " + conversations.size());
            boolean found = false;
            if (!conversationsNonSplit.equals("No conversations in file")) {
                //Here we are checking to see if the split contains what the conversation does not
                //meaning it needs to be added
                if (conversations.size() <= split.length - 1) {
                    for (int i = 0; i < split.length; i++) {
                        for (int j = 0; j < conversations.size(); j++) {
                            if (conversations.get(j).getFilename().equals(split[i] + ".txt")) {
                                found = true;
                            }
                        }
                        if (!found) {
                            Conversation n = createConversationObject(split[i], split[i] + ".txt");
                            conversations.add(n);
                            model.addElement(n.getTitle());
                        }
                        found = false;
                    }
                }

                //removing element if the conversation file contains what the split doesn't meaning it needs
                //to be deleted
                if (conversations.size() > split.length - 1) {
                    for (int i = 0; i < conversations.size(); i++) {
                        String fileName = conversations.get(i).getFilename();
                        fileName = fileName.substring(0, fileName.length() - 4);
                        System.out.println(fileName);
                        if (!Arrays.asList(split).contains(fileName)) {
                            System.out.println("got this far");
                            model.removeElement(conversations.get(i).getTitle());
                            conversations.remove(conversations.get(i));
                        }
                    }
                }
            }

        } catch (IOException ie) {
            ie.printStackTrace();
        }
        client.repaint();

    }


    public static void changePassword(String newPassword) {
        try {
            writer.write("ChangePassword" + " - " + currentUser.getUserName() + " - " + newPassword);
            writer.println();
            writer.flush();
            String response = reader.readLine();

            if (response.equals("Password Changed"))
                JOptionPane.showMessageDialog(null, "Your password has been successfully changed"
                        , "Success", JOptionPane.OK_OPTION);
            else {
                JOptionPane.showMessageDialog(null, "Your password wasn't able to be changed"
                        , "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException io) {
            System.out.println("Password did not change.");
            io.printStackTrace();
        }
    }

    public static void deleteAccount(String userName1) {
        writer.write("DeleteUser" + " - " + currentUser.getUserName());
        writer.println();
        writer.flush();
        fullFrame.setVisible(false);
        optionsMenu.setVisible(false);
        myFrame.setVisible(true);
    }

    public static Conversation createConversationObject(String convoName, String filename) {
        String[] usersInConvo = convoName.split(" - ");
        ArrayList<String> users = new ArrayList<String>();
        for (int i = 0; i < usersInConvo.length; i++) {
            users.add(usersInConvo[i]);
        }
        String convoTitle = "";
        System.out.println(currentUser.getUserName());
        if (users.size() == 2) {
            if (!users.get(0).equals(currentUser.getUserName())) {
                convoTitle = users.get(0);
            } else {
                convoTitle = users.get(1);
            }
        } else {
            for (int i = 0; i < users.size(); i++) {
                if (!users.get(i).equals(currentUser.getUserName())) {
                    convoTitle = convoTitle + users.get(i) + " ";
                }
            }
        }

        System.out.println("conversation title " + convoTitle);
        Conversation n = new Conversation(users, convoTitle, filename);
        return n;
    }

    public static String getConversation(String selectedItem) {
        //sends over command getConvo or whatever
        //special characters where we should put \n since readLine() has a stroke trying to read \n, and then
        //we load that into the text area.
        //+=- new line character
        //conversationName -> userA - userB
        String conversationName = "";
        String loadLine = "";
        try {
            writer.write("allConversations - " + currentUser.getUserName());
            writer.println();
            writer.flush();
            String response = reader.readLine();
            System.out.println("response " + response);
            //Conversation --- userA - userB +=- userA - message +=- Conversation --- userB - userC
            //1) userA - UserB <transcroipt>
            for (int i = 0; i < conversations.size(); i++) {
                if (selectedItem.equals(conversations.get(i).getTitle())) {
                    conversationName = conversations.get(i).getFilename();
                }
            }
            System.out.println("conversation name " + conversationName);
            String[] noTxt = conversationName.split("\\.");
            conversationName = noTxt[0];
            currentFileTitle = conversationName;

            //split by +=-
            //1) Conversation --- name
            //2) user a - akgjkafjg
            //3) end
            String[] allConversationsSplit = response.split(" -=- ");
            //for loop looks for Conversation --- conversationName (String search)
            String search = "Conversation --- " + conversationName;
            System.out.println(search);
            int indexOfConvoBeginning = -1;
            for (int i = 0; i < allConversationsSplit.length; i++) {
                if (allConversationsSplit[i].equals(search)) {
                    indexOfConvoBeginning = i;
                    System.out.println("index of convo beginning " + indexOfConvoBeginning);
                }
            }
            //for loop looks for end after finding the beginning
            int indexOfConvoEnd = -1;
            if (indexOfConvoBeginning != -1) {
                for (int i = indexOfConvoBeginning; i < allConversationsSplit.length; i++) {
                    if (allConversationsSplit[i].equals("end")) {
                        indexOfConvoEnd = i;
                        System.out.println("index of convo end " + i);
                        break;
                    }
                }
            }

            loadLine = "";
            //for loop that loads everything that shows up in the textArea to String loadLine
            if (indexOfConvoBeginning != -1 && indexOfConvoEnd != -1) {
                for (int i = indexOfConvoBeginning + 1; i < indexOfConvoEnd; i++) {
                    loadLine = loadLine + allConversationsSplit[i] + "\n";
                }
            }

            indexOfConvoBeginning = -1;
            indexOfConvoEnd = -1;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadLine;

    }


    public static void deleteConvo() {

        writer.write("deleteConversation" + " - " + currentUser.getUserName() + " - " + currentFileTitle);
        writer.println();
        writer.flush();

    }

    public static void editMessage(String messageToEdit, String newMessage) {
        //editText - conversationTitle - originalText - newText - userEditing

        writer.write("editText" + " - " + messageToEdit + " - " + newMessage + " - " + currentUser.getUserName()
                + " - " + currentFileTitle);
        System.out.println("editText" + " - " + messageToEdit + " - " + newMessage + " - " + currentUser.getUserName()
                + " - " + currentFileTitle);
        writer.println();
        writer.flush();
        try {
            String response = reader.readLine();

            if (response.equals("Message edited")) {
                JOptionPane.showMessageDialog(null, "Message changed successfully!"
                        , "Success", JOptionPane.OK_OPTION);
            } else {
                JOptionPane.showMessageDialog(null,
                        "The message you entered either doesn't exist or you don't have permission to edit it."
                        , "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void deleteMessage(String messageToDelete) {
        writer.write("deleteText" + " - " + messageToDelete + " - " + currentUser.getUserName() + " - " +
                currentFileTitle);
        writer.println();
        writer.flush();

        try {
            String response = reader.readLine();

            if (response.equals("Message deleted")) {
                JOptionPane.showMessageDialog(null, "Message deleted successfully!"
                        , "Success", JOptionPane.OK_OPTION);
            } else {
                JOptionPane.showMessageDialog(null,
                        "The message you entered either doesn't exist or you don't have permission to delete it."
                        , "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }

    }
}