/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kelompok3;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.UnitValue;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import java.io.FileOutputStream;

import java.text.Format;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.SQLException;
import kelompok3.MenuAdmin;

// PDF Writer dan Document
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;

// Elemen Layout
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;

// Properti Layout
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.borders.Border;

// Gambar
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.property.HorizontalAlignment;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

// File output
import java.io.FileOutputStream;
import java.io.IOException;

//number format
import java.text.NumberFormat;
import java.util.Locale;
import javax.imageio.ImageIO;

/**
 *
 * @author Desktop-Putri
 */
public class NaikTingkatt extends javax.swing.JFrame {

    private final Connection conn;
               // Method simpan untuk menyimpan data
    private void simpan() throws SQLException {
        try {
            // Menghubungkan ke database
            Connection kon = koneksi.koneksiDb();
            Statement st = kon.createStatement();

            // Menyusun query INSERT
            String sql = "INSERT INTO tingkat (kode, nama, tgl,jumlah,sabuk) VALUES ('"
                         + KodeAnggota.getSelectedItem() + "', '"
                         + NamaAnggota.getText() + "', '"
                         + Tgl.getText() + "', '"
                         + Jumlah.getText() + "', '"
                         + sabuk.getSelectedItem().toString()  + "')";

            
            // Menjalankan query
            st.executeUpdate(sql);

            // Menampilkan pesan berhasil
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");

                        // Memanggil load_data untuk refresh tabel
            load_data();

            // Bersihkan form setelah simpan
            Bersih();

        } catch (Exception e) {
            // Jika terjadi error, tampilkan pesan error
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data: " + e.getMessage());
        }
    }

        String kodeTerpilih = null;
    /**
     * Creates new form NaikTingkatt
     */
    public Statement st;
    public ResultSet rs;
    Connection cn = kelompok3.koneksi.koneksiDb();
    
     boolean isUpdate = false; 
  
    /** Creates new form NaikTingkatt */
    public NaikTingkatt() {
        initComponents();
        isiKodeAnggota();
        conn = koneksi.koneksiDb();
         loadSabuk();
        load_data();
        TampilData();
    
    Input.setEnabled(false);
    Simpan.setEnabled(true);
    Edit.setEnabled(false);
    Hapus.setEnabled(false);
    Batal.setEnabled(false);
    
        // Tambahkan ini di dalam constructor
    KodeAnggota.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            try {
                Connection conn = koneksi.koneksiDb();
                String kode = KodeAnggota.getSelectedItem().toString();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT nama FROM tbl_angg WHERE kode = '" + kode + "'");
                if (rs.next()) {
                    NamaAnggota.setText(rs.getString("nama"));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Gagal ambil nama anggota: " + e.getMessage());
            }
        }
    });
    
    }
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NaikTingkatt().setVisible(true);
            }
        });
    }
private void loadSabuk() {
    System.out.println("loadSabuk() called");
    try {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        sabuk.setModel(model); // buang semua item lama

        String sql = "SELECT DISTINCT nama_sabuk FROM sabuk";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            model.addElement(rs.getString("nama_sabuk"));
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal load sabuk: " + e.getMessage());
    }
}






     // Method untuk menyimpan JPanel ke dalam file PNG
public void savePanelAsImage(JPanel panel, String filePath) {
    // Set ukuran minimal agar panel tidak 0x0
    panel.setSize(panel.getPreferredSize());

    BufferedImage image = new BufferedImage(
        panel.getWidth(), 
        panel.getHeight(), 
        BufferedImage.TYPE_INT_ARGB
    );
    Graphics2D g2 = image.createGraphics();
    panel.paint(g2);
    g2.dispose();

    try {
        ImageIO.write(image, "png", new File(filePath));
        System.out.println("Struk berhasil disimpan di: " + filePath);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}

   private void load_data()
    {
        Connection kon=koneksi.koneksiDb();
        Object header[]={"KODE ","NAMA ","TGL","JUMLAH","SABUK"};
        DefaultTableModel data=new DefaultTableModel(null,header);
        TableTingkat.setModel(data);
        String sql_data="SELECT * FROM tingkat";
        try
        {
            Statement st=kon.createStatement();
            ResultSet rs=st.executeQuery(sql_data);
            while (rs.next())
            {
                String d1=rs.getString(1);
                String d2=rs.getString(2);
                String d3=rs.getString(3);
                String d4=rs.getString(4);
                String d5=rs.getString(5);
                
                String d[]={d1,d2,d3,d4,d5};
                data.addRow(d);
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
   //INPUT DATA
    
    
        private void Bersih(){
        KodeAnggota.setSelectedItem("");
        NamaAnggota.setText("");
        Tgl.setText("");
        Jumlah.setText("");
        sabuk.setSelectedIndex(0);
    }
    
        //EDIT (BUTTON)
     private void update() {
    try {
        Connection kon = koneksi.koneksiDb();
        Statement st = kon.createStatement();

        if (kodeTerpilih != null && !kodeTerpilih.isEmpty()) {
            String sql_update = "UPDATE tingkat SET "
               + "nama='" + NamaAnggota.getText() + "', "
               + "tgl='" + Tgl.getText() + "', "
               + "jumlah='" + Jumlah.getText() + "', "
               + "sabuk='" + sabuk.getSelectedItem().toString() + "' "
               + "WHERE kode='" + kodeTerpilih + "'";

            System.out.println("kodeTerpilih = " + kodeTerpilih);
            System.out.println("SQL: " + sql_update);

            st.executeUpdate(sql_update);
            JOptionPane.showMessageDialog(null, "Data Berhasil Diupdate", "Update", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih untuk diubah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

private void hapus() {
    try {
        Connection kon = koneksi.koneksiDb();
        String sql = "DELETE FROM tingkat WHERE kode='" + kodeTerpilih + "'";
        PreparedStatement pst = kon.prepareStatement(sql);
        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage());
    }
}



    private void TampilData(){
        try{
            st = koneksi.createStatement();
            rs = st.executeQuery("SELECT * FROM tingkat");
            
            DefaultTableModel model= new DefaultTableModel();
            model.addColumn("Kode Anggota");
            model.addColumn("Nama Anggota");
            model.addColumn("Tgl");
            model.addColumn("Jumlah");
            model.addColumn("sabuk");
            
            
            model.getDataVector().removeAllElements();
            model.fireTableDataChanged();
            model.setRowCount(0);
            
            
            while (rs.next()){
                Object[] data = {
                    
                rs.getString("kode"),
                rs.getString("nama"),
                rs.getString("tgl"),
                rs.getString("jumlah"),
                rs.getString("sabuk")
                };
                model.addRow(data);
                TableTingkat.setModel(model);
            }
        }catch (Exception e){
            
        }
    }
    
    //INPUT DATA
    private void input_data()
    {
        try
    {
        Connection kon=koneksi.koneksiDb();
        Statement st=kon.createStatement();
        String sql="INSERT INTO tingkat VALUES('"+KodeAnggota.getSelectedItem()
                +"','"+NamaAnggota.getText()
                +"','"+Tgl.getText()
                +"','"+Jumlah.getText()
                + sabuk.getSelectedItem().toString() + "')";
              
        st.execute(sql);
        JOptionPane.showMessageDialog(null, "Data tingkat Berhasil di Masukan");
    }
    catch(Exception e)
            {
            JOptionPane.showMessageDialog(null, e);
            }
    }
    

 private void isiKodeAnggota() {
    try {
        Connection conn = koneksi.koneksiDb(); 
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT kode FROM tbl_angg"); // Ambil semua kode dari tabel anggota

        KodeAnggota.removeAllItems(); // 

        while (rs.next()) {
            KodeAnggota.addItem(rs.getString("kode")); // Tambahkan item satu per satu ke combobox
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal isi Kode Anggota: " + e.getMessage());
    }
}
 
// Method untuk membuat dan menampilkan struk serta menyimpannya
    
private void cetakStruk() {
    // Panel utama struk
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Padding

    // Font
    Font headerFont = new Font("Monospaced", Font.BOLD, 16);
    Font subHeaderFont = new Font("Monospaced", Font.PLAIN, 12);
    Font bodyFont = new Font("Monospaced", Font.PLAIN, 14);

    // Kop / Header Klub
    JLabel namaClub = new JLabel("TAEKWONDO ESPA TEAM");
    JLabel alamat = new JLabel("Jl. Bangau Raya No.192-174, Depok Jaya, Kec. Pancoran Mas");
    JLabel kontak = new JLabel("HP:  0813-1916-7787");
    
    namaClub.setFont(headerFont);
    namaClub.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    alamat.setFont(subHeaderFont);
    alamat.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    kontak.setFont(subHeaderFont);
    kontak.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    panel.add(namaClub);
    panel.add(alamat);
    panel.add(kontak);
    panel.add(Box.createVerticalStrut(15)); // spasi

    // Judul Struk
    JLabel judul = new JLabel("====== STRUK PEMBAYARAN KENAIKAN TINGKAT ======");
    judul.setFont(headerFont);
    judul.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    panel.add(judul);
    panel.add(Box.createVerticalStrut(10)); // spasi

// Isi Struk
    JLabel kode = new JLabel("Kode Anggota  : " + KodeAnggota.getSelectedItem());
    JLabel nama = new JLabel("Nama          : " + NamaAnggota.getText());
    JLabel tanggal = new JLabel("Tanggal Bayar : " + Tgl.getText());
    JLabel jumlah = new JLabel("Jumlah        : Rp " + Jumlah.getText());
    JLabel Sabuk = new JLabel("sabuk    : " + sabuk.getSelectedItem());
    JLabel garisAkhir = new JLabel("================================");

    for (JLabel label : new JLabel[]{ kode, nama ,tanggal, jumlah, Sabuk, garisAkhir}) {
        label.setFont(bodyFont);
        label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
    }
    // Tampilkan struk
    JOptionPane.showMessageDialog(this, panel, "Struk Pembayaran", JOptionPane.PLAIN_MESSAGE);

    // Simpan sebagai gambar (pastikan kamu sudah punya fungsi ini)
    String userHome = System.getProperty("user.home");
    String filePath = userHome + "/Documents/struk_pembayaran.png";
    savePanelAsImage(panel, filePath);
}
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        label = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        NamaAnggota = new javax.swing.JTextField();
        Tgl = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableTingkat = new javax.swing.JTable();
        Simpan = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        Input = new javax.swing.JButton();
        Batal = new javax.swing.JButton();
        Hapus = new javax.swing.JButton();
        Jumlah = new javax.swing.JTextField();
        btnhome = new javax.swing.JButton();
        sabuk = new javax.swing.JComboBox<>();
        KodeAnggota = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        label.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        label.setForeground(new java.awt.Color(255, 255, 255));
        label.setText("Kode Anggota");

        jLabel2.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nama Anggota");

        jLabel3.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tanggal Bayar");

        jLabel4.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Jumlah");

        jLabel5.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Sabuk");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 3, 30)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("PEMBAYARAN KENAIKAN TINGKAT TAEKWONDO ESPA");

        NamaAnggota.setText(" ");

        Tgl.setText(" ");

        TableTingkat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode Anggota", "Nama Anggota", "Tanggal Bayar", "Jumlah", "Sabuk"
            }
        ));
        TableTingkat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableTingkatMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TableTingkat);

        Simpan.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        Simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/diskette (1).png"))); // NOI18N
        Simpan.setText("Simpan");
        Simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SimpanActionPerformed(evt);
            }
        });

        Edit.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        Edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pencil (1).png"))); // NOI18N
        Edit.setText("Edit");
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });

        Input.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        Input.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/download (4) (1).png"))); // NOI18N
        Input.setText("Cetak Struk");
        Input.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputActionPerformed(evt);
            }
        });

        Batal.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        Batal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cancel (1).png"))); // NOI18N
        Batal.setText("Batal");
        Batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BatalActionPerformed(evt);
            }
        });

        Hapus.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        Hapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trash-can (1).png"))); // NOI18N
        Hapus.setText("Hapus");
        Hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HapusActionPerformed(evt);
            }
        });

        Jumlah.setText(" ");
        Jumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JumlahActionPerformed(evt);
            }
        });

        btnhome.setBackground(new java.awt.Color(204, 204, 204));
        btnhome.setForeground(new java.awt.Color(204, 204, 204));
        btnhome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/home-icon-silhouette (3).png"))); // NOI18N
        btnhome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhomeActionPerformed(evt);
            }
        });

        sabuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sabukActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnhome, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(NamaAnggota, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                                        .addComponent(Tgl))
                                    .addComponent(KodeAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addGap(65, 65, 65)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sabuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(Simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75)
                                .addComponent(Edit)
                                .addGap(66, 66, 66)
                                .addComponent(Input)
                                .addGap(29, 29, 29)
                                .addComponent(Batal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(jLabel6)
                        .addContainerGap(40, Short.MAX_VALUE))))
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnhome)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel6)))
                .addGap(39, 39, 39)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label)
                    .addComponent(jLabel4)
                    .addComponent(Jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(KodeAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(NamaAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(sabuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(Tgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Simpan)
                    .addComponent(Edit)
                    .addComponent(Input)
                    .addComponent(Batal)
                    .addComponent(Hapus))
                .addContainerGap(64, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void TableTingkatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableTingkatMouseClicked

        int baris = TableTingkat.getSelectedRow();

        if (baris != -1) {
            KodeAnggota.setSelectedItem(TableTingkat.getValueAt(baris, 0).toString());
            NamaAnggota.setText(TableTingkat.getValueAt(baris, 1).toString());
            Tgl.setText(TableTingkat.getValueAt(baris, 2).toString());
            Jumlah.setText(TableTingkat.getValueAt(baris, 3).toString());
            sabuk.setSelectedItem(TableTingkat.getValueAt(baris, 4).toString()); // ✅ Diubah

            // ✅ Set nilai kodeTerpilih
            kodeTerpilih = TableTingkat.getValueAt(baris, 0).toString();

            isUpdate = true;
            Simpan.setEnabled(true);
            Edit.setEnabled(true);
            Input.setEnabled(true);
            Hapus.setEnabled(true);
            Batal.setEnabled(true);
        }
    }//GEN-LAST:event_TableTingkatMouseClicked

    private void SimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SimpanActionPerformed
        // TODO add your handling code here:
        try {
            Connection kon = koneksi.koneksiDb();
            Statement st = kon.createStatement();

            // Validasi input tidak boleh kosong
            if (KodeAnggota.getSelectedItem().equals("") ||
                NamaAnggota.getText().equals("") ||
                Tgl.getText().equals("") ||
                Jumlah.getText().equals("") ||
                sabuk.getSelectedItem() == null || sabuk.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Data Tidak Boleh Kosong!", "Validasi Data", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (isUpdate) {
                // MODE UPDATE
                String sql = "UPDATE tingkat SET nama = '" + NamaAnggota.getText() +
                "', tgl = '" + Tgl.getText() +
                "', jumlah = '" + Jumlah.getText() +
                "', Sabuk = '" + sabuk.getSelectedItem() +
                "' WHERE kode = '" + KodeAnggota.getSelectedItem() + "'";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data Berhasil di Update!", "Update", JOptionPane.INFORMATION_MESSAGE);

            } else {
                // MODE SIMPAN BARU
                String cek = "SELECT * FROM tingkat WHERE kode = '" + KodeAnggota.getSelectedItem() + "'";
                ResultSet rs = st.executeQuery(cek);
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Data Sudah Tersimpan di Master!", "Save", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String sql = "INSERT INTO tingkat VALUES ('" + KodeAnggota.getSelectedItem() +
                "','" + NamaAnggota.getText() +
                "','" + Tgl.getText() +
                "','" + Jumlah.getText() +
                "','" + sabuk.getSelectedItem() + "')";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data berhasil di Simpan!", "Save", JOptionPane.INFORMATION_MESSAGE);
            }

            // Setelah simpan/update
            load_data();   // Refresh tabel
            Bersih();      // Kosongkan form

            // Aktifkan kembali field input
            KodeAnggota.setEnabled(true);
            NamaAnggota.setEnabled(true);
            Tgl.setEnabled(true);
            Jumlah.setEnabled(true);
            sabuk.setEnabled(true);

            // Reset tombol & kolom
            Simpan.setEnabled(true);
            Input.setEnabled(false);
            Edit.setEnabled(false);
            Hapus.setEnabled(false);
            Batal.setEnabled(false);

            isUpdate = false; // kembali ke mode simpan

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
        }
    }//GEN-LAST:event_SimpanActionPerformed

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed
        // TODO add your handling code here:
        isUpdate = true; // aktifkan mode update
        Simpan.setEnabled(true); // aktifkan tombol simpan
        Input.setEnabled(false); // nonaktifkan tombol input

        JOptionPane.showMessageDialog(null, "Klik Tombol Simpan untuk Update Data!", "Edit Mode", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_EditActionPerformed

    private void InputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputActionPerformed
        // TODO add your handling code here:
        cetakStruk();
    }//GEN-LAST:event_InputActionPerformed

    private void BatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BatalActionPerformed
        // TODO add your handling code here:
        int keluar;
        keluar = JOptionPane.showOptionDialog(this, "Batal Memilih Data?", "Batal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,null,null);
        if(keluar==JOptionPane.YES_NO_OPTION)
        {
            new NaikTingkatt().show();
            this.dispose();
        }
    }//GEN-LAST:event_BatalActionPerformed

    private void HapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HapusActionPerformed
        // TODO add your handling code here:
        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Apakah Yakin Ingin Menghapus Data Ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            hapus();       // 1. Hapus data dari database
            load_data();   // 2. Refresh tabel
            Bersih();      // 3. Bersihkan form

            // 4. Aktifkan kembali input
            KodeAnggota.setEnabled(true);
            NamaAnggota.setEnabled(true);
            Tgl.setEnabled(true);
            Jumlah.setEnabled(true);
            sabuk.setEnabled(true);
            TableTingkat.setEnabled(true);

            // 5. Reset tombol
            Input.setEnabled(false);
            Simpan.setEnabled(true);
            Batal.setEnabled(false);
            Edit.setEnabled(false);
            Hapus.setEnabled(false);
        }
    }//GEN-LAST:event_HapusActionPerformed

    private void JumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JumlahActionPerformed

    private void btnhomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhomeActionPerformed
        // TODO add your handling code here:
        // Pindah ke halaman Menu Admin
        MenuAnggota menu = new MenuAnggota();
        menu.setVisible(true);
        this.dispose(); // Menutup form sekarang
    }//GEN-LAST:event_btnhomeActionPerformed

    private void sabukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sabukActionPerformed
        // TODO add your handling code here:
        try {
         String sql = "SELECT * FROM sabuk";
           PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String namaSabuk = rs.getString("nama_sabuk");
                sabuk.addItem(namaSabuk);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal load sabuk: " + e.getMessage());
        }
    }//GEN-LAST:event_sabukActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Batal;
    private javax.swing.JButton Edit;
    private javax.swing.JButton Hapus;
    private javax.swing.JButton Input;
    private javax.swing.JTextField Jumlah;
    private javax.swing.JComboBox<String> KodeAnggota;
    private javax.swing.JTextField NamaAnggota;
    private javax.swing.JButton Simpan;
    private javax.swing.JTable TableTingkat;
    private javax.swing.JTextField Tgl;
    private javax.swing.JButton btnhome;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel label;
    private javax.swing.JComboBox<String> sabuk;
    // End of variables declaration//GEN-END:variables

}