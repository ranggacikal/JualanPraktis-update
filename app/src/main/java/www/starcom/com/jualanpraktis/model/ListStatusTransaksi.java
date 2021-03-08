package www.starcom.com.jualanpraktis.model;

public class ListStatusTransaksi {

    private String id;
    private String tanggal;
    private String nama_barang;
    private String variasi;
    private String harga_produk;
    private String harga_jual;
    private String keuntungan;
    private String status_pesanan;
    private String image;

    public ListStatusTransaksi(String id, String tanggal, String nama_barang, String variasi, String harga_produk, String harga_jual, String keuntungan, String status_pesanan, String image) {
        this.id = id;
        this.tanggal = tanggal;
        this.nama_barang = nama_barang;
        this.variasi = variasi;
        this.harga_produk = harga_produk;
        this.harga_jual = harga_jual;
        this.keuntungan = keuntungan;
        this.status_pesanan = status_pesanan;
        this.image = image;
    }

    public ListStatusTransaksi(String id, String tanggal, String nama_barang, String variasi, String harga_produk, String harga_jual, String keuntungan, String status_pesanan) {
        this.id = id;
        this.tanggal = tanggal;
        this.nama_barang = nama_barang;
        this.variasi = variasi;
        this.harga_produk = harga_produk;
        this.harga_jual = harga_jual;
        this.keuntungan = keuntungan;
        this.status_pesanan = status_pesanan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getVariasi() {
        return variasi;
    }

    public void setVariasi(String variasi) {
        this.variasi = variasi;
    }

    public String getHarga_produk() {
        return harga_produk;
    }

    public void setHarga_produk(String harga_produk) {
        this.harga_produk = harga_produk;
    }

    public String getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(String harga_jual) {
        this.harga_jual = harga_jual;
    }

    public String getKeuntungan() {
        return keuntungan;
    }

    public void setKeuntungan(String keuntungan) {
        this.keuntungan = keuntungan;
    }

    public String getStatus_pesanan() {
        return status_pesanan;
    }

    public void setStatus_pesanan(String status_pesanan) {
        this.status_pesanan = status_pesanan;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
