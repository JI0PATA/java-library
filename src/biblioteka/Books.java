package biblioteka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javafx.collections.ObservableList;

public class Books { //класс для того, чтобы хранить, получать записывать всю информцию, связанную с книгами

    private int id;
    private String avtor;
    private String nazvanie;
    private int nalichie;

    Books() {
        this.id = 0;
        this.avtor = "";
        this.nazvanie = "";
        this.nalichie = 0;
    }

    Books(int i, String s1, String s2, int s3) {
        this.id = i;
        this.avtor = s1;
        this.nazvanie = s2;
        this.nalichie = s3;
    }

    public int getId() {
        return this.id;
    }

    public String getAvtor() {
        return this.avtor;
    }

    public String getNazvanie() {
        return this.nazvanie;
    }

    public int getNalichie() {
        return this.nalichie;
    }

    public void setNalichie(int s) {
        this.nalichie = s;

    }

    static ObservableList<Books> getProduct() throws FileNotFoundException, IOException {
        String line;
        String x = "";
        ObservableList<Books> mas = null;
        InputStream books_stream = Books.class.getClass().getResourceAsStream("books.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(books_stream, "utf-8"));
        while ((line = reader.readLine()) != null) {
            String[] s = line.split("&");
            Books books = new Books(Integer.valueOf(s[0]), s[1], s[2], Integer.valueOf(s[3]));
            mas.add(books);
        }
        return mas;
    }

    static String NameById(String i) throws FileNotFoundException {// статический метод, которому передаем артикул данной книги, а получаем ее название и автора
        String line;
        String x = "";
        InputStream books_stream = Books.class.getClass().getResourceAsStream("books.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(books_stream, "utf-8"))) {
            while ((line = reader.readLine()) != null) {
                String[] s = line.split("&");
                if (s[0].equals(i)) {
                    x = s[1] + " " + s[2];
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return x;
    }

    public String toStringg() {
        return this.id + "&" + avtor + "&" + nazvanie + "&" + nalichie;

    }

    static void rewrite(ObservableList<Books> mas) {// метод перезаписывающий текстовый документ "books.txt"
        try (PrintWriter writer = new PrintWriter(new File(Books.class.getResource("books.txt").getPath()))) {
            int f = 0;
            for (Books ma : mas) {
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
