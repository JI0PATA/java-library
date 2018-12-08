package biblioteka;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.collections.ObservableList;

public class Orders { //класс для того, чтобы хранить, получать записывать всю информцию, связанную с заказами

    private int idd;
    private String login;
    private String dolg;

    Orders() {
        this.idd = 0;
        this.login = "";
        this.dolg = "";
    }

    Orders(int i, String s1, String s2) {
        this.idd = i;
        this.login = s1;
        this.dolg = s2;
    }

    public String getDolg() {
        return this.dolg;
    }

    public void setDolg(String s) {
        this.dolg = s;

    }

    public String getLogin() {
        return this.login;
    }

    public int getIdd() {
        return this.idd;
    }

    public String toStringg() {
        return this.idd + " " + this.login + " " + this.dolg;
    }

    static void rewrite(ObservableList<Orders> mas) {//метод, который перезаписывает текстовый документ с заказами
        try (PrintWriter writer = new PrintWriter(new File(Orders.class.getResource("orders.txt").getPath()))) {
            int f = 0;
            for (Orders ma : mas) {
                if (f != 0) {
                    writer.write("\r\n");
                }
                writer.write(ma.toStringg());
                f = f + 1;
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
