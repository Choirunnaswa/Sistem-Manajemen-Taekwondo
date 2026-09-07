package kelompok3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Desktop-Putri
 */
public class koneksi {

    private static Connection conn;

    public static Connection koneksiDb() {
        if (conn == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost/kel3", "root", "");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Koneksi Gagal: " + e.getMessage());
            }
        }
        return conn;
    }

    // Method connect agar sesuai dengan pemanggilan di bprintActionPerformed
    public Connection connect() {
        return koneksiDb();
    }

    public static Statement createStatement() {
        try {
            Connection conn = koneksiDb();
            return conn.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return null;
        }
    }
}
