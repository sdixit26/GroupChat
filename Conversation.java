import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.net.*;
/**
 * Conversation
 *
 * This is a class that stores the data for each conversation
 *
 * @author Steve Rong, Supriya Dixit, CS 18000
 * @version December 6, 2020
 *
 */
public class Conversation {
    private ArrayList<String> users;
    private String title;
    private String filename;

    public Conversation(ArrayList<String> users, String title, String filename) {
        this.users = users;
        this.title = title;
        this.filename = filename;

    }

    public String getFilename() { return this.filename; }

    public ArrayList<String> getUsers() {
        return users;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
