/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kelompok3;

import java.io.File;
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
import kelompok3.RaportAnggota;
/**
 *
 * @author Desktop-Putri
 */
public class RaportAnggota extends javax.swing.JFrame {
    
       // Method simpan untuk menyimpan data
    private void simpan() {
        try {
            // Menghubungkan ke database
            Connection kon = koneksi.koneksiDb();
            Statement st = kon.createStatement();

            // Menyusun query INSERT
            String sql = "INSERT INTO raport_anggota (kode, nama, ttl,sabuk, nilai) VALUES ('"
                         + KodeAnggota.getSelectedItem() + "', '"
                         + NamaAnggota.getText() + "', '"
                         + TTL.getText() + "', '"
                         + Sabuk.getText() + "', '"
                         + Nilai.getText() + "')";

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
    
    public RaportAnggota() 
    {
    initComponents();
    isiKodeAnggota(); 
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
    private void load_data()
    {
        Connection kon=koneksi.koneksiDb();
        Object header[]={"KODE ANGGOTA","NAMA ANGGOTA","TTL","SABUK","NILAI"};
        DefaultTableModel data=new DefaultTableModel(null,header);
        TableAnggota.setModel(data);
        String sql_data="SELECT * FROM raport_anggota";
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
        TTL.setText("");
        Sabuk.setText("");
        Nilai.setText("");
    }
    
        //EDIT (BUTTON)
     private void update() {
    try {
        Connection kon = koneksi.koneksiDb();
        Statement st = kon.createStatement();

        if (kodeTerpilih != null && !kodeTerpilih.isEmpty()) {
            String sql_update = "UPDATE raport_anggota SET "
                    + "nama='" + NamaAnggota.getText() + "', "
                    + "ttl='" + TTL.getText() + "', "
                    + "sabuk='" + Sabuk.getText() + "' "
                     + "nilai='" + Nilai.getText() + "' "
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
        String sql = "DELETE FROM raport_anggota WHERE kode='" + kodeTerpilih + "'";
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
            rs = st.executeQuery("SELECT * FROM raport_anggota");
            
            DefaultTableModel model= new DefaultTableModel();
            model.addColumn("Kode Anggota");
            model.addColumn("Nama Anggota");
            model.addColumn("TTL");
            model.addColumn("Sabuk");
            model.addColumn("Nilai");
            
            
            model.getDataVector().removeAllElements();
            model.fireTableDataChanged();
            model.setRowCount(0);
            
            
            while (rs.next()){
                Object[] data = {
                    
                rs.getString("kode"),
                rs.getString("nama"),
                rs.getString("ttl"),
                rs.getString("sabuk"),
                rs.getString("nilai")
                };
                model.addRow(data);
                TableAnggota.setModel(model);
            }
        }catch (Exception e){
            
        }
    }
    
private void isiKodeAnggota() {
    try {
        Connection conn = koneksi.koneksiDb();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT kode FROM tbl_angg");

        KodeAnggota.removeAllItems(); // Bersihkan dulu
        while (rs.next()) {
            KodeAnggota.addItem(rs.getString("kode"));
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal isi Kode Anggota: " + e.getMessage());
    }

    // ⬇ Tambahkan listener langsung DI SINI
    KodeAnggota.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            try {
                Connection conn = koneksi.koneksiDb();
                String kode = KodeAnggota.getSelectedItem().toString();
                Statement st = conn.createStatement();

                ResultSet rs = st.executeQuery("SELECT nama, ttl, sabuk FROM tbl_angg WHERE kode = '" + kode + "'");

                if (rs.next()) {
                    NamaAnggota.setText(rs.getString("nama"));
                    TTL.setText(rs.getString("ttl")); 
                    Sabuk.setText(rs.getString("sabuk"));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Gagal ambil data anggota: " + e.getMessage());
            }
        }
    });
}  

//INPUT DATA
    private void input_data()
    {
        try
    {
        Connection kon=koneksi.koneksiDb();
        Statement st=kon.createStatement();
        String sql="INSERT INTO raport_anggota VALUES('"+KodeAnggota.getSelectedItem()
                +"','"+NamaAnggota.getText()
                +"','"+TTL.getText()
                  +"','"+Sabuk.getText()
                +"','"+Nilai.getText()
                +"')";
        st.execute(sql);
        JOptionPane.showMessageDialog(null, "Data Supplier Berhasil di Masukan");
    }
    catch(Exception e)
            {
            JOptionPane.showMessageDialog(null, e);
            }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        label = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        NamaAnggota = new javax.swing.JTextField();
        TTL = new javax.swing.JTextField();
        Sabuk = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableAnggota = new javax.swing.JTable();
        Simpan = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        Input = new javax.swing.JButton();
        Batal = new javax.swing.JButton();
        Hapus = new javax.swing.JButton();
        btnhome = new javax.swing.JButton();
        Nilai = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        Cetak = new javax.swing.JButton();
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
        jLabel3.setText("Tempat,tanggal lahir");

        jLabel4.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Sabuk");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("RAPORT ANGGOTA TAEKWONDO ESPA");

        NamaAnggota.setText(" ");

        TTL.setText(" ");

        Sabuk.setText(" ");

        TableAnggota.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode Anggota", "Nama Anggota", "Tempat,tanggal lahir", "Sabuk", "Nilai "
            }
        ));
        TableAnggota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableAnggotaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(TableAnggota);

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

        btnhome.setBackground(new java.awt.Color(255, 255, 255));
        btnhome.setForeground(new java.awt.Color(255, 255, 255));
        btnhome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/home-icon-silhouette (3).png"))); // NOI18N
        btnhome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhomeActionPerformed(evt);
            }
        });

        Nilai.setText(" ");

        jLabel5.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nilai");

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
                .addGap(78, 78, 78)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(NamaAnggota, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                        .addComponent(TTL))
                    .addComponent(KodeAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Sabuk, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Nilai, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(95, 95, 95))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnhome, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(105, 105, 105)
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 947, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(Simpan)
                .addGap(64, 64, 64)
                .addComponent(Edit)
                .addGap(57, 57, 57)
                .addComponent(Input)
                .addGap(55, 55, 55)
                .addComponent(Batal)
                .addGap(74, 74, 74)
                .addComponent(Hapus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(Cetak)
                .addGap(36, 36, 36))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel6))
                    .addComponent(btnhome))
                .addGap(36, 36, 36)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label)
                    .addComponent(jLabel4)
                    .addComponent(Sabuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(KodeAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(NamaAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Nilai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(TTL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Simpan)
                    .addComponent(Edit)
                    .addComponent(Input)
                    .addComponent(Batal)
                    .addComponent(Hapus)
                    .addComponent(Cetak))
                .addGap(50, 50, 50))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void TableAnggotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableAnggotaMouseClicked
        // TODO add your handling code here:
        int row = TableAnggota.getSelectedRow();
        if (row >= 0) {
            // Simpan nilai KODE dari baris yang diklik
            kodeTerpilih = TableAnggota.getValueAt(row, 0).toString();

            KodeAnggota.setSelectedItem(TableAnggota.getValueAt(row, 0).toString());
            NamaAnggota.setText(TableAnggota.getValueAt(row, 1).toString());
            TTL.setText(TableAnggota.getValueAt(row, 2).toString());
            Sabuk.setText(TableAnggota.getValueAt(row, 3).toString());
            Nilai.setText(TableAnggota.getValueAt(row, 4).toString());  // ✅ sampai sini aman
            // TableAnggota.getValueAt(row, 5);  <-- ❌ Ini yang bikin error

            //kunci input kode
            KodeAnggota.setEnabled(false);

            // Aktifkan tombol
            Input.setEnabled(false);
            Simpan.setEnabled(true);
            Edit.setEnabled(true);
            Hapus.setEnabled(true);
            Batal.setEnabled(true);
        }
    }//GEN-LAST:event_TableAnggotaMouseClicked

    private void SimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SimpanActionPerformed
        // TODO add your handling code here:
        try {
            Connection kon = koneksi.koneksiDb();
            Statement st = kon.createStatement();

            // Validasi input tidak boleh kosong
            if (KodeAnggota.getSelectedItem().equals("") ||
                NamaAnggota.getText().equals("") ||
                TTL.getText().equals("") ||
                Sabuk.getText().equals("") ||
                Nilai.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Data Tidak Boleh Kosong!", "Validasi Data", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (isUpdate) {
                // MODE UPDATE
                String sql = "UPDATE raport_anggota SET nama = '" + NamaAnggota.getText() +
                "', ttl = '" + TTL.getText() +
                "', sabuk = '" + Sabuk.getText() +
                "', nilai = '" + Nilai.getText() +
                "' WHERE kode = '" + KodeAnggota.getSelectedItem() + "'";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data Berhasil di Update!", "Update", JOptionPane.INFORMATION_MESSAGE);

            } else {
                // MODE SIMPAN BARU
                String cek = "SELECT * FROM raport_anggota WHERE kode = '" + KodeAnggota.getSelectedItem() + "'";
                ResultSet rs = st.executeQuery(cek);
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Data Sudah Tersimpan di Master!", "Save", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String sql = "INSERT INTO raport_anggota VALUES ('" + KodeAnggota.getSelectedItem() +
                "','" + NamaAnggota.getText() +
                "','" + TTL.getText() +
                "','" + Sabuk.getText() +
                "','" + Nilai.getText() + "')";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data berhasil di Simpan!", "Save", JOptionPane.INFORMATION_MESSAGE);
            }

            // Setelah simpan/update
            load_data();   // Refresh tabel
            Bersih();      // Kosongkan form

            // Aktifkan kembali field input
            KodeAnggota.setEnabled(true);
            NamaAnggota.setEnabled(true);
            TTL.setEnabled(true);
            Sabuk.setEnabled(true);
            Nilai.setEnabled(true);

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
            if (KodeAnggota.getSelectedItem().equals("") ||
                NamaAnggota.getText().equals("") ||
                TTL.getText().equals("") ||
                Sabuk.getText().equals("") ||
                Nilai.getText().equals("")) { //kalo data kosong ada peringatan pake equals
                JOptionPane.showMessageDialog(null,"Harap Masukkan Data Terlebih Dahulu!","Validasi Data",JOptionPane.INFORMATION_MESSAGE);

                return;
            }
            //AKSI SIMPAN DATA
            if (Input.getText().equals("Input")) {

                String cek = "SELECT * FROM raport_anggota WHERE kode = '" + KodeAnggota.getSelectedItem() + "'";
                rs = st.executeQuery(cek);
                if (rs.next()){
                    JOptionPane.showMessageDialog(null,"Kode Supplier Sudah Ada!","Input",JOptionPane.INFORMATION_MESSAGE);

                }else{

                    String sql = "INSERT INTO raport_anggota (kode, nama, ttl, sabuk, nilai) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pst = cn.prepareStatement(sql);
                    pst.setString(1, (String) KodeAnggota.getSelectedItem());
                    pst.setString(2, NamaAnggota.getText());
                    pst.setString(3, TTL.getText());
                    pst.setString(4, Sabuk.getText());
                    pst.setString(5, Nilai.getText());
                    pst.executeUpdate();

                    Bersih();
                }
                // Refresh tabel untuk menampilkan data terbaru
                load_data();

                // Bersihkan form setelah simpan
                Bersih();

                KodeAnggota.setEnabled(true);
                NamaAnggota.setEnabled(true);
                TTL.setEnabled(true);
                Sabuk.setEnabled(true);
                Nilai.setEnabled(true);
                TableAnggota.setEnabled(true);

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
            new RaportAnggota().show();
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
            Sabuk.setEnabled(true);
            Nilai.setEnabled(true);
            TableAnggota.setEnabled(true);

            // 5. Reset tombol
            Input.setEnabled(true);
            Simpan.setEnabled(false);
            Batal.setEnabled(false);
            Edit.setEnabled(false);
            Hapus.setEnabled(false);
        }
    }//GEN-LAST:event_HapusActionPerformed

    private void btnhomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhomeActionPerformed
        // TODO add your handling code here:
        // Pindah ke halaman Menu Admin
        MenuAdmin menu = new MenuAdmin();
        menu.setVisible(true);
        this.dispose(); // Menutup form sekarang

    }//GEN-LAST:event_btnhomeActionPerformed

    private void CetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CetakActionPerformed
 try {
    Connection conn = koneksi.koneksiDb();
    HashMap parameter = new HashMap();

    // Ambil file dari dalam JAR (bukan dari src/)
     java.io.InputStream is = getClass().getResourceAsStream("/Laporan/RaportReport.jasper");
    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(is);

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
            java.util.logging.Logger.getLogger(RaportAnggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RaportAnggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RaportAnggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RaportAnggota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RaportAnggota().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Batal;
    private javax.swing.JButton Cetak;
    private javax.swing.JButton Edit;
    private javax.swing.JButton Hapus;
    private javax.swing.JButton Input;
    private javax.swing.JComboBox<String> KodeAnggota;
    private javax.swing.JTextField NamaAnggota;
    private javax.swing.JTextField Nilai;
    private javax.swing.JTextField Sabuk;
    private javax.swing.JButton Simpan;
    private javax.swing.JTextField TTL;
    private javax.swing.JTable TableAnggota;
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