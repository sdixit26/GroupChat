import java.awt.*;
import java.util.ArrayList;
/**
 * UserAccount
 *
 * This class stores the information about the user that is currently using the client
 *
 * @author Supriya Dixit, CS 18000
 * @version December 6, 2020
 *
 */
public class UserAccount {
    private String userName;
    private String password;
    private ArrayList<Conversation> co;
    private Conversation have;

    public UserAccount(String userName, String password) {
        this.co = new ArrayList<Conversation>();
        this.userName = userName;
        this.password = password;
    }
    public String getUserName() {
        return this.userName;
    }
    public ArrayList<Conversation> getConversations() {
        return co;
    }

    public Conversation findConversation(String title) {
        for (int i = 0; i < co.size(); i++) {
            if (co.get(i).getTitle().equals(title)) {
                have = co.get(i);
                break;
            }
        }
        return have;
    }

    public void addCo(Conversation conversation) {
        co.add(conversation);
    }

    public String getPassword() {
        return this.password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}