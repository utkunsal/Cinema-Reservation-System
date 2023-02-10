import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

public class User {
    private String name, password;
    private boolean clubMember, admin;
    private static HashMap<String,User> users = new HashMap<>(); // contains all users
    private static User loggedInUser = null;


    public User(String name, String hashedPassword, String clubMember, String admin){
        this.name=name;
        this.clubMember = Boolean.parseBoolean(clubMember);
        this.admin = Boolean.parseBoolean(admin);
        this.password = hashedPassword;
        users.put(name,this); // adds to user hashmap
    }

    // to hash the passwords
    public static String hashPassword(String pw){
        byte[] bytesOfPassword = pw.getBytes(StandardCharsets.UTF_8);
        byte[] md5Digest = new byte[0];
        try {
            md5Digest = MessageDigest.getInstance("MD5").digest(bytesOfPassword);
        } catch (NoSuchAlgorithmException ignored){}
        return Base64.getEncoder().encodeToString(md5Digest);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isClubMember() {
        return clubMember;
    }

    public boolean isAdmin() {
        return admin;
    }

    public static HashMap<String,User> getUsers() {
        return users;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        User.loggedInUser = loggedInUser;
    }

    public void changeAdminStatus(){
        admin = !admin;
    }

    public void changeClubMemberStatus(){
        clubMember = !clubMember;
    }

    @Override
    public String toString() {
        return name;
    }
}
