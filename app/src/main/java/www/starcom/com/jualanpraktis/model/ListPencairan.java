package www.starcom.com.jualanpraktis.model;

public class ListPencairan {

    private String tanggal_pencairan;
    private String waktu_pencairan;
    private String total_pencarian;

    public ListPencairan(String tanggal_pencairan, String waktu_pencairan, String total_pencarian) {
        this.tanggal_pencairan = tanggal_pencairan;
        this.waktu_pencairan = waktu_pencairan;
        this.total_pencarian = total_pencarian;
    }

    public String getTanggal_pencairan() {
        return tanggal_pencairan;
    }

    public void setTanggal_pencairan(String tanggal_pencairan) {
        this.tanggal_pencairan = tanggal_pencairan;
    }

    public String getWaktu_pencairan() {
        return waktu_pencairan;
    }

    public void setWaktu_pencairan(String waktu_pencairan) {
        this.waktu_pencairan = waktu_pencairan;
    }

    public String getTotal_pencarian() {
        return total_pencarian;
    }

    public void setTotal_pencarian(String total_pencarian) {
        this.total_pencarian = total_pencarian;
    }
}
