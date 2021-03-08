package www.starcom.com.jualanpraktis.model;

public class ListFavorit {

    private String nama_barang;
    private String harga_barang;
    private String image;

    public ListFavorit(String nama_barang, String harga_barang, String image) {
        this.nama_barang = nama_barang;
        this.harga_barang = harga_barang;
        this.image = image;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getHarga_barang() {
        return harga_barang;
    }

    public void setHarga_barang(String harga_barang) {
        this.harga_barang = harga_barang;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
