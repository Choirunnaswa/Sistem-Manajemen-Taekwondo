/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kelompok3;

import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kelompok3.koneksi;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import kelompok3.DataPeralatan;

/**
 *
 * @author DESKTOP-Putri
 */
public class DataPeralatan extends javax.swing.JFrame {
    
       // Method simpan untuk menyimpan data
    private void simpan() {
        try {
            // Menghubungkan ke database
            Connection kon = koneksi.koneksiDb();
            Statement st = kon.createStatement();

            // Menyusun query INSERT
            String sql = "INSERT INTO tbl_alat (kode, namaalat, ,jumlah, harga,namasupplier) VALUES ('"
                         + KodePeralatan.getText() + "', '"
                         + NamaPeralatan.getText() + "', '"
                         + Jumlah.getText() + "', '"
                         + Harga.getText() + "', '"
                         + NamaSupplier.getText() + "')";

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
     * Creates new form DataSupp
     */
    public Statement st;
    public ResultSet rs;
    Connection cn = kelompok3.koneksi.koneksiDb();
    
     boolean isUpdate = false;
    
    public DataPeralatan() 
    {
        initComponents();
        load_data();
        TampilData();
        
    Input.setEnabled(true);
    Simpan.setEnabled(false);
    Edit.setEnabled(false);
    Hapus.setEnabled(false);
    Batal.setEnabled(false);
    }
    private void load_data()
    {
        Connection kon=koneksi.koneksiDb();
        Object header[]={"KODEPERALATAN","NAMAPERALATAN","JUMLAH","HARGA","NAMASUPPLIER"};
        DefaultTableModel data=new DefaultTableModel(null,header);
        TablePeralatan.setModel(data);
        String sql_data="SELECT * FROM tbl_alat";
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
        KodePeralatan.setText("");
        NamaPeralatan.setText("");
        Jumlah.setText("");
        Harga.setText("");
        NamaSupplier.setText("");
    }
    
        //EDIT (BUTTON)
     private void update() {
    try {
        Connection kon = koneksi.koneksiDb();
        Statement st = kon.createStatement();

        if (kodeTerpilih != null && !kodeTerpilih.isEmpty()) {
            String sql_update = "UPDATE tbl_alat SET "
                    + "kode='" + KodePeralatan.getText() + "', "
                    + "namaalat='" + NamaPeralatan.getText() + "', "
                    + "jumlah='" + Jumlah.getText() + "' "
                     + "harga='" + Harga.getText() + "' "
                    + "namasupplier='" + NamaSupplier.getText() + "' "
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
        String sql = "DELETE FROM tbl_alat WHERE kode = ?";
        PreparedStatement pst = kon.prepareStatement(sql);
        pst.setString(1, kodeTerpilih);  // Lebih aman dari SQL injection

        int rowsAffected = pst.executeUpdate();  // Berapa baris terhapus?

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");

            load_data();   // Refresh tabel
            Bersih();      // Bersihkan form

            // Aktifkan kembali input
            KodePeralatan.setEnabled(true);
            NamaPeralatan.setEnabled(true);
            Harga.setEnabled(true);
            NamaSupplier.setEnabled(true);
            TablePeralatan.setEnabled(true);

            // Reset tombol
            Input.setEnabled(true);
            Simpan.setEnabled(false);
            Batal.setEnabled(false);
            Edit.setEnabled(false);
            Hapus.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan atau sudah dihapus.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage());
    }
}





    private void TampilData(){
        try{
            st = koneksi.createStatement();
            rs = st.executeQuery("SELECT * FROM tbl_alat");
            
            DefaultTableModel model= new DefaultTableModel();
            model.addColumn("Kode Peralatan");
            model.addColumn("Nama Peralatan");
            model.addColumn("Jumlah");
            model.addColumn("Harga");
            model.addColumn("Nama Supplier");
            
            
            model.getDataVector().removeAllElements();
            model.fireTableDataChanged();
            model.setRowCount(0);
            
            
            while (rs.next()){
                Object[] data = {
                    
                rs.getString("kode"),
                rs.getString("namaalat"),
                rs.getString("jumlah"),
                rs.getString("harga"),
                rs.getString("namasupplier")
                };
                model.addRow(data);
                TablePeralatan.setModel(model);
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
        String sql="INSERT INTO tbl_alat VALUES('"+KodePeralatan.getText()
                +"','"+NamaPeralatan.getText()
                +"','"+Jumlah.getText()
                  +"','"+Harga.getText()
                +"','"+NamaSupplier.getText()
                +"')";
        st.execute(sql);
        JOptionPane.showMessageDialog(null, "Data Supplier Berhasil di Masukan");
    }
    catch(Exception e)
            {
            JOptionPane.showMessageDialog(null, e);
            }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
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
        KodePeralatan = new javax.swing.JTextField();
        NamaPeralatan = new javax.swing.JTextField();
        Jumlah = new javax.swing.JTextField();
        NamaSupplier = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablePeralatan = new javax.swing.JTable();
        Simpan = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        Input = new javax.swing.JButton();
        Batal = new javax.swing.JButton();
        Hapus = new javax.swing.JButton();
        Harga = new javax.swing.JTextField();
        btnhome = new javax.swing.JButton();
        Cetak = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        label.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        label.setForeground(new java.awt.Color(255, 255, 255));
        label.setText("Kode Peralatan");

        jLabel2.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nama Peralatan");

        jLabel3.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Jumlah");

        jLabel4.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Harga");

        jLabel5.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nama Supplier");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("DATA PERALATAN TAEKWONDO ESPA");

        KodePeralatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KodePeralatanActionPerformed(evt);
            }
        });

        NamaPeralatan.setText(" ");

        Jumlah.setText(" ");

        NamaSupplier.setText(" ");
        NamaSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NamaSupplierActionPerformed(evt);
            }
        });

        TablePeralatan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode Peralatan", "Nama Peralatan", "Jumlah", "Harga", "Nama Supplier"
            }
        ));
        TablePeralatan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablePeralatanMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TablePeralatan);

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
        Input.setText("Input");
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

        Harga.setText(" ");
        Harga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HargaActionPerformed(evt);
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

        Cetak.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        Cetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/printer (2).png"))); // NOI18N
        Cetak.setText("Cetak Laporan");
        Cetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CetakActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(Simpan)
                .addGap(50, 50, 50)
                .addComponent(Edit)
                .addGap(63, 63, 63)
                .addComponent(Input)
                .addGap(62, 62, 62)
                .addComponent(Batal)
                .addGap(73, 73, 73)
                .addComponent(Hapus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(Cetak)
                .addGap(40, 40, 40))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnhome, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(120, 120, 120)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 928, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(KodePeralatan)
                            .addComponent(NamaPeralatan, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(Jumlah))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Harga, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(65, 65, 65)
                                .addComponent(NamaSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(78, 78, 78))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jLabel6))
                    .addComponent(btnhome, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label)
                    .addComponent(KodePeralatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(Harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(NamaPeralatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(NamaSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(Jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Simpan)
                    .addComponent(Edit)
                    .addComponent(Input)
                    .addComponent(Batal)
                    .addComponent(Hapus)
                    .addComponent(Cetak))
                .addContainerGap(97, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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

    private void KodePeralatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KodePeralatanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KodePeralatanActionPerformed

    private void TablePeralatanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablePeralatanMouseClicked
                                           
    int baris = TablePeralatan.getSelectedRow();

    if (baris != -1) {
        KodePeralatan.setText(TablePeralatan.getValueAt(baris, 0).toString());
        NamaPeralatan.setText(TablePeralatan.getValueAt(baris, 1).toString());
        Jumlah.setText(TablePeralatan.getValueAt(baris, 2).toString());
        Harga.setText(TablePeralatan.getValueAt(baris, 3).toString());
        NamaSupplier.setText(TablePeralatan.getValueAt(baris, 4).toString());

        // Ini penting:
        isUpdate = true;
        Simpan.setEnabled(true);
        Edit.setEnabled(true);
        Input.setEnabled(false);
        Hapus.setEnabled(true);
        Batal.setEnabled(true);
    }


    }//GEN-LAST:event_TablePeralatanMouseClicked

    private void SimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SimpanActionPerformed
        // TODO add your handling code here:
        try {
            Connection kon = koneksi.koneksiDb();
            Statement st = kon.createStatement();

            // Validasi input tidak boleh kosong
            if (KodePeralatan.getText().equals("") ||
                NamaPeralatan.getText().equals("") ||
                Jumlah.getText().equals("") ||
                Harga.getText().equals("") ||
                NamaSupplier.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Data Tidak Boleh Kosong!", "Validasi Data", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (isUpdate) {
                // MODE UPDATE
                String sql = "UPDATE tbl_alat SET kode = '" + KodePeralatan.getText() +
                "', namaalat = '" + NamaPeralatan.getText() +
                "', jumlah = '" + Jumlah.getText() +
                "', harga = '" + Harga.getText() +
                "', namasupplier = '" + NamaSupplier.getText() +
                "' WHERE kode = '" + KodePeralatan.getText() + "'";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data Berhasil di Update!", "Update", JOptionPane.INFORMATION_MESSAGE);

            } else {
                // MODE SIMPAN BARU
                String cek = "SELECT * FROM tbl_alat WHERE kode = '" + KodePeralatan.getText() + "'";
                ResultSet rs = st.executeQuery(cek);
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Data Sudah Tersimpan di Master!", "Save", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String sql = "INSERT INTO tbl_alat VALUES ('" + KodePeralatan.getText() +
                "','" + NamaPeralatan.getText() +
                "','" + Jumlah.getText() +
                "','" + Harga.getText() +
                "','" + NamaSupplier.getText() + "')";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data berhasil di Simpan!", "Save", JOptionPane.INFORMATION_MESSAGE);
            }

            // Setelah simpan/update
            load_data();   // Refresh tabel
            Bersih();      // Kosongkan form

            // Aktifkan kembali field input
            KodePeralatan.setEnabled(true);
            NamaPeralatan.setEnabled(true);
            Jumlah.setEnabled(true);
            Harga.setEnabled(true);
            NamaSupplier.setEnabled(true);

            // Reset tombol & kolom
            Simpan.setEnabled(false);
            Input.setEnabled(true);
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
        try{
            st = cn.createStatement();
            if (KodePeralatan.getText().equals("") ||
                NamaPeralatan.getText().equals("") ||
                Jumlah.getText().equals("") ||
                Harga.getText().equals("") ||
                NamaSupplier.getText().equals("")) { //kalo data kosong ada peringatan pake equals
                JOptionPane.showMessageDialog(null,"Harap Masukkan Data Terlebih Dahulu!","Validasi Data",JOptionPane.INFORMATION_MESSAGE);

                return;
            }
            //AKSI SIMPAN DATA
            if (Input.getText().equals("Input")) {

                String cek = "SELECT * FROM tbl_alat WHERE kode = '" + KodePeralatan.getText() + "'";
                rs = st.executeQuery(cek);
                if (rs.next()){
                    JOptionPane.showMessageDialog(null,"Kode Supplier Sudah Ada!","Input",JOptionPane.INFORMATION_MESSAGE);

                }else{

                    String sql = "INSERT INTO tbl_alat (kode, namaalat, jumlah, harga, namasupplier) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pst = cn.prepareStatement(sql);
                    pst.setString(1, KodePeralatan.getText());
                    pst.setString(2, NamaPeralatan.getText());
                    pst.setString(3, Jumlah.getText());
                    pst.setString(4, Harga.getText());
                    pst.setString(5, NamaSupplier.getText());
                    pst.executeUpdate();

                    Bersih();
                }
                // Refresh tabel untuk menampilkan data terbaru
                load_data();

                // Bersihkan form setelah simpan
                Bersih();

                KodePeralatan.setEnabled(true);
                NamaPeralatan.setEnabled(true);
                Jumlah.setEnabled(true);
                Harga.setEnabled(true);
                NamaSupplier.setEnabled(true);
                TablePeralatan.setEnabled(true);

            }else{

            }
        } catch (Exception e) {
            e.printStackTrace(); // Menampilkan error di console

        }

        // Atur tombol
        Input.setEnabled(true);      // Nonaktifkan tombol Input
        Simpan.setEnabled(false);      // Aktifkan tombol Simpan

    }//GEN-LAST:event_InputActionPerformed

    private void BatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BatalActionPerformed
        // TODO add your handling code here:
        int keluar;
        keluar = JOptionPane.showOptionDialog(this, "Batal Memilih Data?", "Batal", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,null,null);
        if(keluar==JOptionPane.YES_NO_OPTION)
        {
            new DataPeralatan().show();
            this.dispose();
        }
    }//GEN-LAST:event_BatalActionPerformed

    private void HapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HapusActionPerformed
    int konfirmasi = JOptionPane.showConfirmDialog(this,
        "Apakah Yakin Ingin Menghapus Data Ini?",
        "Konfirmasi Hapus",
        JOptionPane.YES_NO_OPTION);

    if (konfirmasi == JOptionPane.YES_OPTION) {
        int baris = TablePeralatan.getSelectedRow();
        if (baris >= 0) {
            kodeTerpilih = TablePeralatan.getValueAt(baris, 0).toString();
            hapus(); // method hapus yang sudah diperbaiki tadi
        } else {
            JOptionPane.showMessageDialog(this, "Silakan pilih data dari tabel terlebih dahulu.");
        }
    }
    }//GEN-LAST:event_HapusActionPerformed

    private void NamaSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NamaSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NamaSupplierActionPerformed

    private void HargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HargaActionPerformed

    private void btnhomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhomeActionPerformed
        // TODO add your handling code here:
        // Pindah ke halaman Menu Admin
        MenuAdmin menu = new MenuAdmin();
        menu.setVisible(true);
        this.dispose(); // Menutup form sekarang

    }//GEN-LAST:event_btnhomeActionPerformed

    private void CetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CetakActionPerformed
        // TODO add your handling code here:
try {
    Connection conn = koneksi.koneksiDb();
    java.util.HashMap parameter = new java.util.HashMap();

    // Ambil file report dari resources (di dalam JAR, bukan dari src/)
    InputStream is = getClass().getResourceAsStream("/Laporan/PeralatanReport.jasper");
    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);

    @SuppressWarnings("unchecked")
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conn);

    JasperViewer.setDefaultLookAndFeelDecorated(true);
    JasperViewer.viewReport(jasperPrint, false);

} catch (Exception e) {
    e.printStackTrace(); // untuk debug
    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
}

 
    }//GEN-LAST:event_CetakActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DataPeralatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataPeralatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataPeralatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataPeralatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DataPeralatan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Batal;
    private javax.swing.JButton Cetak;
    private javax.swing.JButton Edit;
    private javax.swing.JButton Hapus;
    private javax.swing.JTextField Harga;
    private javax.swing.JButton Input;
    private javax.swing.JTextField Jumlah;
    private javax.swing.JTextField KodePeralatan;
    private javax.swing.JTextField NamaPeralatan;
    private javax.swing.JTextField NamaSupplier;
    private javax.swing.JButton Simpan;
    private javax.swing.JTable TablePeralatan;
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
    // End of variables declaration//GEN-END:variables
}
