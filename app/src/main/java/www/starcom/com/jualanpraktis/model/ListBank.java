package www.starcom.com.jualanpraktis.model;

public class ListBank {

    private String nama_bank;
    private String gambar;

    public ListBank(String nama_bank, String gambar) {
        this.nama_bank = nama_bank;
        this.gambar = gambar;
    }

    public String getNama_bank() {
        return nama_bank;
    }

    public void setNama_bank(String nama_bank) {
        this.nama_bank = nama_bank;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
