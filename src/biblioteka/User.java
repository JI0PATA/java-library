package biblioteka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class User {//класс для того, чтобы хранить, получать записывать всю информцию, связанную с пользователями

    static String login;
    private String parol;

    User(String s1, String s2) {
        this.login = s1;
        this.parol = s2;
    }

    User() {
        this.login = "";
        this.parol = "";
    }

    public String getLogin() {
        return this.login;
    }

    public boolean enter() throws IOException { //проверка на входе верный логин и пароль или нет
        boolean f = false;
        String line;
        InputStream users = getClass().getResourceAsStream("user.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(users, "utf-8"))) {
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                if ((login.equals(data[0])) & (parol.equals(data[1]))) {
                    f = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public boolean whois() {//проверка кем является пользователь клиентом или библотекарем 
        String chek = "";
        boolean f = false;
        String line;
        InputStream users = getClass().getResourceAsStream("user.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(users, "utf-8"))) {
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                if ((login.equals(data[0])) & (parol.equals(data[1]))) {
                    chek = data[2];
                    if (chek.equals("user")) {
                        f = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;

    }

}
