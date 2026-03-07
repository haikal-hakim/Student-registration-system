package uts_aplikasipenerimaansiswabaru;

public class Siswa {

    private String nama202;
    private int nilai202;
    private String jurusan202;

    public Siswa(String nama, int nilai, String jurusan) {
        this.nama202 = nama;
        this.nilai202 = nilai;
        this.jurusan202 = jurusan;
    }

    public String getNama() {
        return nama202;
    }

    public int getNilai() {
        return nilai202;
    }

    public String getJurusan() {
        return jurusan202;
    }

    @Override
    public String toString() {
        return "Nama: " + nama202 + ", Nilai: " + nilai202 + ", Jurusan: " + jurusan202;
    }
}