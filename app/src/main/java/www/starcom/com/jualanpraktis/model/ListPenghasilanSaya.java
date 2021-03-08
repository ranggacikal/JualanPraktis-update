package www.starcom.com.jualanpraktis.model;

public class ListPenghasilanSaya {

    private String nama_barang;
    private String image_barang;
    private String tanggal;
    private String status;
    private String total;

    public ListPenghasilanSaya(String nama_barang, String image_barang, String tanggal, String status, String total) {
        this.nama_barang = nama_barang;
        this.image_barang = image_barang;
        this.tanggal = tanggal;
        this.status = status;
        this.total = total;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getImage_barang() {
        return image_barang;
    }

    public void setImage_barang(String image_barang) {
        this.image_barang = image_barang;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
