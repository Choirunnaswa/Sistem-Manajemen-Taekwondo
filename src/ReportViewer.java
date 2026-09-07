import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.util.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;

public class ReportViewer {
    public static void main(String[] args) {
        try {
            // Koneksi ke database (ganti sesuai koneksi kamu)
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kelompok3", "user", "password");

            // Ambil gambar dari folder src/img/
            InputStream logoStream = ReportViewer.class.getResourceAsStream("/img/report.png");

            // Buat parameter dan masukkan stream gambar
            Map<String, Object> param = new HashMap<>();
            param.put("logo", logoStream); // harus sama dengan nama parameter di iReport

            // Compile dan isi report
            JasperReport report = JasperCompileManager.compileReport("src/Laporan/AnggotaReport.jrxml"); // ganti namafile
            JasperPrint print = JasperFillManager.fillReport(report, param, conn);

            // Tampilkan hasilnya
            JasperViewer.viewReport(print, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}