package uts_aplikasipenerimaansiswabaru;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class AdminApp extends JFrame {

    private ArrayList<Siswa> daftarSiswa202 = new ArrayList<>();
    private DefaultTableModel tableModel202;
    private JTextField tfNama202, tfNilai202, tfJurusan202, tfSearch202;
    private JLabel labelTotal202;

    public AdminApp() {
        setTitle("Aplikasi Penerimaan Siswa Baru");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ================= Panel Input (Atas) =================
        JPanel panelInput202 = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInput202.setBorder(BorderFactory.createTitledBorder("Form Pendaftaran"));

        tfNama202 = new JTextField();
        tfNilai202 = new JTextField();
        tfJurusan202 = new JTextField();

        panelInput202.add(new JLabel("Nama:"));
        panelInput202.add(tfNama202);
        panelInput202.add(new JLabel("Nilai:"));
        panelInput202.add(tfNilai202);
        panelInput202.add(new JLabel("Jurusan:"));
        panelInput202.add(tfJurusan202);

        JButton btnTambah202 = new JButton("Tambah Siswa");
        labelTotal202 = new JLabel("Total Siswa: 0");

        panelInput202.add(btnTambah202);
        panelInput202.add(labelTotal202);

        add(panelInput202, BorderLayout.NORTH);

        // ================= Panel Tabel (Tengah) =================
        tableModel202 = new DefaultTableModel();
        tableModel202.addColumn("No");
        tableModel202.addColumn("Nama");
        tableModel202.addColumn("Nilai");
        tableModel202.addColumn("Jurusan");
        tableModel202.addColumn("Status");

        JTable table202 = new JTable(tableModel202);
        table202.setAutoCreateRowSorter(true);
        JScrollPane scrollPane202 = new JScrollPane(table202);
        scrollPane202.setBorder(BorderFactory.createTitledBorder("Data Siswa"));
        add(scrollPane202, BorderLayout.CENTER);

        // ================= Panel Aksi (Bawah) =================
        JPanel panelTombol202 = new JPanel(new GridLayout(3, 2, 5, 5));
        panelTombol202.setBorder(BorderFactory.createTitledBorder("Aksi"));

        JButton btnSimpan202 = new JButton("Simpan ke File");
        JButton btnLoad202 = new JButton("Load dari File");
        JButton btnUrutkan202 = new JButton("Urutkan Nilai");

        tfSearch202 = new JTextField();
        JButton btnCari202 = new JButton("Cari Nama");

        panelTombol202.add(btnSimpan202);
        panelTombol202.add(btnLoad202);
        panelTombol202.add(btnUrutkan202);
        panelTombol202.add(tfSearch202);
        panelTombol202.add(btnCari202);

        add(panelTombol202, BorderLayout.SOUTH);

        // ================= Aksi Tombol =================
        btnTambah202.addActionListener(e -> tambahSiswa());
        btnSimpan202.addActionListener(e -> simpanFile());
        btnLoad202.addActionListener(e -> bacaFile());
        btnUrutkan202.addActionListener(e -> urutkanSiswa());
        btnCari202.addActionListener(e -> cariSiswa());
    }

    private void tambahSiswa() {
        try {
            String nama202 = tfNama202.getText().trim();
            int nilai202 = Integer.parseInt(tfNilai202.getText().trim());
            String jurusan202 = tfJurusan202.getText().trim();

            if (nama202.isEmpty() || jurusan202.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama dan Jurusan harus diisi.");
                return;
            }
            
            // ... update tabel dan reset input ...
            // dan komponen lainnnya....
            
            Siswa s202 = new Siswa(nama202, nilai202, jurusan202);
            daftarSiswa202.add(s202);
            
            String status202 = nilai202 >= 75 ? "Diterima" : "Ditolak";
            tableModel202.addRow(new Object[]{
                daftarSiswa202.size(),
                s202.getNama(),
                s202.getNilai(),
                s202.getJurusan(),
                status202
            });

            labelTotal202.setText("Total Siswa: " + hitungRekursif(0));

            tfNama202.setText("");
            tfNilai202.setText("");
            tfJurusan202.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nilai harus berupa angka.");
        }
    }

    private void simpanFile() {
        try (PrintWriter pw202 = new PrintWriter("siswa.txt")) {
            for (Siswa s202 : daftarSiswa202) {
                pw202.println(s202.getNama() + ";" + s202.getNilai() + ";" + s202.getJurusan());
            }
            JOptionPane.showMessageDialog(this, "Berhasil disimpan ke file.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Mana file nya?.");
            e.printStackTrace();
        }
    }

    private void bacaFile() {
        daftarSiswa202.clear();
        tableModel202.setRowCount(0);
        
        try (BufferedReader br202 = new BufferedReader(new FileReader("siswa.txt"))) {
            String line202;
            int no202 = 1;
            while ((line202 = br202.readLine()) != null) {
                String[] data202 = line202.split(";");
                if (data202.length == 3) {
                    String nama202 = data202[0];
                    int nilai202 = Integer.parseInt(data202[1]);
                    String jurusan202 = data202[2];
                    Siswa s202 = new Siswa(nama202, nilai202, jurusan202);
                    daftarSiswa202.add(s202);
                    
                    String status202 = nilai202 >= 75 ? "Diterima" : "Ditolak";
                    tableModel202.addRow(new Object[]{
                        no202++,
                        s202.getNama(),
                        s202.getNilai(),
                        s202.getJurusan(),
                        status202
                    });
                }
            }
            labelTotal202.setText("Total Siswa: " + hitungRekursif(0));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "File tidak ada yang disimpan.");
            e.printStackTrace();
        }
    }

    private void urutkanSiswa() {
        daftarSiswa202.sort(Comparator.comparingInt(Siswa::getNilai).reversed());
        refreshTabel();
        JOptionPane.showMessageDialog(this, "Data telah diurutkan berdasarkan nilai");
    }

    private void cariSiswa() {
        String keyword202 = tfSearch202.getText().trim().toLowerCase();
        boolean ketemu202 = false;
        
        tableModel202.setRowCount(0);
        
        int no202 = 1;
        for (Siswa s202 : daftarSiswa202) {
            if (s202.getNama().toLowerCase().contains(keyword202)) {
                String status202 = s202.getNilai() >= 75 ? "Diterima" : "Ditolak";
                tableModel202.addRow(new Object[]{
                    no202++,
                    s202.getNama(),
                    s202.getNilai(),
                    s202.getJurusan(),
                    status202
                });
                ketemu202 = true;
            }
        }
        
        if (!ketemu202) {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan.");
            refreshTabel();
        }
    }

    private void refreshTabel() {
        tableModel202.setRowCount(0);
        int no202 = 1;
        for (Siswa s202 : daftarSiswa202) {
            String status202 = s202.getNilai() >= 75 ? "Diterima" : "Ditolak";
            tableModel202.addRow(new Object[]{
                no202++,
                s202.getNama(),
                s202.getNilai(),
                s202.getJurusan(),
                status202
            });
        }
    }

    private int hitungRekursif(int index202) {
        if (index202 >= daftarSiswa202.size()) return 0;
        return 1 + hitungRekursif(index202 + 1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminApp().setVisible(true);
        });
    }
}