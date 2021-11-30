import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
/**
 * ServerThread
 *
 * This is a class that is threaded and receives input from the client and than sends data to the
 * client based on that input
 *
 * @author Hudson Reamer, Steve Rong, CS 18000
 * @version December 6, 2020
 *
 */
public class ServerThread extends Thread {
    private Socket socket;

    // When relaunching server we should open up the files to get
    // the usernames that are active rn, with an arraylist that encompases it.
    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<String> passwords = new ArrayList<String>();
    public final String userFile = "users.txt";
    public final String conversationsFile = "conversations.txt";

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        /*Here will go the implementation for each Client  */

        //try catch statement for the input and output streams and connection
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream())) {

            String command = "";
            while (true) {
                /*Inside this for loop it will essentially listen to
                 * commands from the client than send out any thing that is needed*/
                command = reader.readLine();

                //if nothing is read than we restart the loop and check again
                if (command == null) {
                    System.out.println("command = null");
                    break;
                }
                System.out.println("Received: " + command);
                String[] arguements = command.split(" - ");

                //Login - Username - Password
                if (arguements[0].equals("Login")) {
                    //Checking to see if its a valid login
                    String stringToClient = isValidLogin(arguements[1], arguements[2]);

                    //sending to client
                    writer.write(stringToClient);
                    writer.println();
                    writer.flush(); // Ensure data is sent to the client.
                    System.out.println("Client was sent data: " + stringToClient);
                } else if (arguements[0].equals("SignUp")) {
                    //SignUp - Username - Password
                    if (addUser(arguements[1], arguements[2])) {
                        System.out.println(arguements[1] + " was added to users.txt");

                        writer.write("Signup Successful");
                        writer.println();
                        writer.flush(); // Ensure data is sent to the client.
                    } else {
                        System.out.println("User already exists");

                        writer.write("Signup Unsuccessful");
                        writer.println();
                        writer.flush(); // Ensure data is sent to the client.
                    }
                } else if (arguements[0].equals("DeleteUser")) {
                    //DeleteUser - username
                    File f = new File(userFile);
                    FileReader fr = new FileReader(f);
                    BufferedReader br = new BufferedReader(fr);

                    /*Finding all the usernames besides the one we want deleted and adding
                     * it to a varibale*/
                    String line = br.readLine();
                    String withoutUser = "";
                    while (line != null) {
                        String[] parts = line.split(" - ");
                        if (!parts[0].equals(arguements[1])) {
                            withoutUser += line + ", ";
                        }
                        line = br.readLine();
                    }
                    fr.close();
                    br.close();

                    /*Now we take that variable and just write it back to the file
                     * because we are not in append mode in our fos we just overwrite the
                     * whole file */
                    String[] toFile = withoutUser.split(", ");
                    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)))) {
                        for (String user : toFile) {
                            pw.println(user);
                        }
                    }
                } else if (arguements[0].equals("checkValidUser")) {
                    //checkValidUser - user
                    File f = new File(userFile);
                    FileReader fr = new FileReader(f);
                    BufferedReader br = new BufferedReader(fr);


                    boolean found = false;
                    String line = br.readLine();
                    while (line != null) {
                        String[] parts = line.split(" - ");

                        if (arguements[1].equals(parts[0])) {
                            writer.write("User is Valid " + parts[0]);
                            System.out.println("User is Valid " + parts[0]);
                            writer.println();
                            writer.flush(); // Ensure data is sent to the client.
                            found = true;
                            break;
                        }

                        line = br.readLine();
                    }

                    if (found == false) {
                        writer.write("User is Invalid");
                        writer.println();
                        writer.flush(); // Ensure data is sent to the client.
                    }

                    fr.close();
                    br.close();
                } else if (arguements[0].equals("getAllConversationsInvolved")) {
                    //getAllConversationsInvolved - user
                    File f = new File(conversationsFile);
                    String toClient = "";

                    if (f.length() == 0) {
                        System.out.println("Client was sent data: No conversations in file");
                        writer.write("No conversations in file");
                        writer.println();
                        writer.flush(); //ensure the client gets the data
                    } else {
                        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                            //this is the line in conversation file formated like     user - user.txt
                            String line = br.readLine();
                            while (line != null) {
                                //spliting the .txt file name
                                String[] parts = line.split("\\.");
                                //splitting the names part
                                String[] names = parts[0].split(" - ");

                                //looping through each name against the other names
                                for (int i = 0; i < names.length; i++) {
                                    //if the name of the user is in the file name than we look to see if its deleted
                                    if (names[i].equals(arguements[1])) {
                                        //checking to see if the user has deleted the conversation
                                        File f2 = new File(line);
                                        FileReader fr2 = new FileReader(f2);
                                        BufferedReader br2 = new BufferedReader(fr2);

                                        //grabbing first line of conversation file
                                        String usersNotDeleted = br2.readLine();

                                        //if the line contains the username we add
                                        if (usersNotDeleted.contains(arguements[1])) {
                                            toClient += parts[0] + ", ";
                                        }

                                        fr2.close();
                                        br2.close();
                                    }

                                }
                                line = br.readLine();
                            }
                        }
                        System.out.println("Client was sent data: " + toClient);
                        writer.write(toClient);
                        writer.println();
                        writer.flush(); //ensuring it sends to the client
                    }
                } else if (arguements[0].equals("startConversation")) {
                    //startConversation - user - user - user - ...
                    String fileName = "";
                    for (int i = 1; i < arguements.length; i++) {
                        if (i != arguements.length - 1) {
                            fileName += arguements[i] + " - ";
                        } else {
                            fileName += arguements[i];
                        }
                    }
                    String users = fileName;
                    fileName += ".txt";

                    //creates the file
                    File f = new File(fileName);
                    if (!f.exists()) {
                        f.createNewFile();
                    }

                    //printing the users who have not deleted which is all of them currently
                    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
                        pw.println(users);
                    }

                    //adding the new file to the list of conversations
                    File f2 = new File(conversationsFile);
                    try (PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter(f2, true)))) {
                        pw2.println(fileName);
                    }
                } else if (arguements[0].equals("ChangePassword")) {
                    //ChangePassword - user - newPassword
                    //opening user file to read from
                    File f = new File(userFile);
                    FileReader fr = new FileReader(f);
                    BufferedReader br = new BufferedReader(fr);

                    String totalFile = "";

                    //Read the line and if it equals the username add it to totalConversation
                    //with the new password. else just add the line with the user that doesnt equal
                    //what the client said
                    String line = br.readLine();
                    while (line != null) {
                        String[] parts = line.split(" - ");

                        if (parts[0].equals(arguements[1])) {
                            totalFile += parts[0] + " - " + arguements[2] + "\n";
                        } else {
                            totalFile += line + "\n";
                        }

                        line = br.readLine();
                    }

                    //closing the streams
                    fr.close();
                    br.close();

                    //writing to the userfile. Using just a print cause everyline we add a
                    //newline character to the end of the line so no need to use println
                    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)))) {
                        pw.print(totalFile);
                    }

                    writer.write("Password Changed");
                    writer.println();
                    writer.flush(); // Ensure data is sent to the client.
                } else if (arguements[0].equals("updateConversation")) {
                    //updateConversation - message - userWhoSent - user - user - user - ..."
                    String fileName = "";
                    for (int i = 3; i < arguements.length; i++) {
                        if (i != arguements.length - 1) {
                            fileName += arguements[i] + " - ";
                        } else {
                            fileName += arguements[i];
                        }
                    }
                    fileName += ".txt";

                    File f = new File(fileName);

                    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
                        //User who sent - message - deleted by user - ...
                        pw.println(arguements[2] + " - " + arguements[1]);
                    }
                } else if (arguements[0].equals("allConversations")) {
                    //allConversations - user
                    ArrayList<String> conversationList = new ArrayList<String>();
                    String user = arguements[1];
                    String allConversations = "";

                    File f = new File(conversationsFile);
                    FileReader fr = new FileReader(f);
                    BufferedReader br = new BufferedReader(fr);

                    String conversation = br.readLine();
                    while (conversation != null) {
                        //marker
                        String[] parts = conversation.split("\\.");

                        String[] noDot = parts[0].split(" - ");

                        for (int i = 0; i < noDot.length; i++) {
                            if (noDot[i].equals(user)) {
                                conversationList.add(parts[0]);
                            }
                        }

                        conversation = br.readLine();
                    }

                    fr.close();
                    br.close();

                    for (String convoName : conversationList) {
                        String name = convoName + ".txt";
                        File f1 = new File(name);
                        FileReader fr1 = new FileReader(f1);
                        BufferedReader br1 = new BufferedReader(fr1);
                        boolean streamsClosed = false;

                        String transcript = br1.readLine();

                        //if user deleted the conversation than we dont add the conversation to the list
                        if (transcript.contains(arguements[1])) {
                            allConversations += "Conversation --- " + convoName + " -=- ";

                            transcript = br1.readLine();
                            while (transcript != null) {
                                allConversations += transcript + " -=- ";

                                transcript = br1.readLine();
                            }
                            allConversations += "end -=- ";

                            fr1.close();
                            br1.close();
                            streamsClosed = true;
                        }

                        if (!streamsClosed) {
                            fr1.close();
                            br1.close();
                        }
                    }

                    System.out.println("Client was sent data: " + allConversations);
                    writer.write(allConversations);
                    writer.println();
                    writer.flush(); // Ensure data is sent to the client.
                } else if (arguements[0].equals("deleteText")) {
                    //deleteText - originalText - userEditing - conversationTitle
                    String fileName = "";
                    for (int i = 3; i < arguements.length; i++) {
                        if (i < arguements.length - 1) {
                            fileName += arguements[i] + " - ";
                        } else {
                            fileName += arguements[i];
                        }
                    }

                    File f = new File(fileName + ".txt");
                    FileReader fr = new FileReader(f);
                    BufferedReader br = new BufferedReader(fr);

                    String allConversation = "";
                    boolean deleted = false;

                    String line = br.readLine();
                    while (line != null) {
                        String[] parts = line.split(" - ");

                        if (parts[0].equals(arguements[2]) && parts.length > 1 && parts[1].equals(arguements[1])) {
                            deleted = true;
                        } else {
                            allConversation += line + " -=- ";
                        }

                        line = br.readLine();
                    }

                    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)))) {
                        String[] parts2 = allConversation.split(" -=- ");

                        for (int i = 0; i < parts2.length; i++) {
                            pw.println(parts2[i]);
                        }
                    }

                    if (deleted == true) {
                        writer.write("Message deleted");
                        writer.println();
                        writer.flush(); // Ensure data is sent to the client.
                    } else if (deleted == false) {
                        writer.write("Message was not deleted");
                        writer.println();
                        writer.flush(); // Ensure data is sent to the client.
                    }
                    fr.close();
                    br.close();
                } else if (arguements[0].equals("editText")) {
                    //editText - originalText - newText - userEditing - conversationTitle
                    String fileName = "";
                    for (int i = 4; i < arguements.length; i++) {
                        if (i < arguements.length - 1) {
                            fileName += arguements[i] + " - ";
                        } else {
                            fileName += arguements[i];
                        }
                    }

                    File f = new File(fileName + ".txt");
                    FileReader fr = new FileReader(f);
                    BufferedReader br = new BufferedReader(fr);

                    String allConversation = "";
                    boolean edited = false;

                    String line = br.readLine();
                    while (line != null) {
                        String[] parts = line.split(" - ");

                        if (parts[0].equals(arguements[3]) && parts[1].equals(arguements[1])) {
                            allConversation += arguements[3] + " - " + arguements[2] + " -=- ";
                            edited = true;
                        } else {
                            allConversation += line + " -=- ";
                        }


                        line = br.readLine();
                    }

                    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)))) {
                        String[] parts2 = allConversation.split(" -=- ");

                        for (int i = 0; i < parts2.length; i++) {
                            pw.println(parts2[i]);
                        }
                    }

                    if (edited == true) {
                        writer.write("Message edited");
                        writer.println();
                        writer.flush(); // Ensure data is sent to the client.
                    } else if (edited == false) {
                        writer.write("Message not edited");
                        writer.println();
                        writer.flush(); // Ensure data is sent to the client.
                    }

                    fr.close();
                    br.close();
                } else if (arguements[0].equals("deleteConversation")) {
                    //deleteConversation - userWhoIsDeleting - conversation name
                    String fileName = "";
                    for (int i = 2; i < arguements.length; i++) {
                        if (i < arguements.length - 1) {
                            fileName += arguements[i] + " - ";
                        } else {
                            fileName += arguements[i];
                        }
                    }

                    File f = new File(fileName + ".txt");
                    FileReader fr = new FileReader(f);
                    BufferedReader br = new BufferedReader(fr);

                    String allConversations = "";

                    String line = br.readLine();
                    String[] users = line.split(" - ");

                    for (int i = 0; i < users.length; i++) {
                        if (!users[i].equals(arguements[1])) {
                            allConversations += users[i] + " - ";
                        }
                    }
                    allConversations = allConversations.substring(0, allConversations.length() - 3);

                    allConversations += " -=- ";

                    line = br.readLine();
                    while (line != null) {
                        allConversations += line + " -=- ";

                        line = br.readLine();
                    }

                    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)))) {
                        String[] parts2 = allConversations.split(" -=- ");
                        for (int i = 0; i < parts2.length; i++) {
                            pw.println(parts2[i]);
                        }
                    }
                    fr.close();
                    br.close();
                }
            }

        } catch (IOException e) {
            System.out.println("Connection error closing thread");
            e.printStackTrace();
        }
    }

    public String isValidLogin(String userName, String password) throws IOException {
        File f = new File(userFile);
        FileReader fos = new FileReader(f);
        BufferedReader br = new BufferedReader(fos);

        String user = br.readLine();
        while (user != null) {
            String[] userInfo = user.split(" - ");
            if (userInfo[0].equals(userName) && userInfo[1].equals(password)) {
                fos.close();
                br.close();
                return "Valid User";
            }


            user = br.readLine();
        }
        fos.close();
        br.close();
        return "Invalid User";
    }

    public boolean addUser(String userName, String password) throws IOException {
        File f = new File(userFile);

        FileReader fos = new FileReader(f);
        BufferedReader br = new BufferedReader(fos);

        String user = br.readLine();
        while (user != null) {
            String[] userInfo = user.split(" - ");
            if (userInfo[0].equals(userName)) {
                fos.close();
                br.close();
                return false;
            }
            user = br.readLine();
        }
        fos.close();
        br.close();

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
            pw.println(userName + " - " + password);
        } catch (IOException e) {
            System.out.println("File couldn't write");
        }
        return true;
    }
}
