package www.starcom.com.jualanpraktis.Spinner;

import java.util.List;

public class Pekerjaan {

    private String pekerjaan_id;
    private String pekerjaan;
    List<Pekerjaan> pekerjaanList;

    @Override
    public String toString() {
        return pekerjaan;
    }

    public String getPekerjaan_id() {
        return pekerjaan_id;
    }

    public void setPekerjaan_id(String pekerjaan_id) {
        this.pekerjaan_id = pekerjaan_id;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    public List<Pekerjaan> getPekerjaanList() {
        return pekerjaanList;
    }

    public void setPekerjaanList(List<Pekerjaan> pekerjaanList) {
        this.pekerjaanList = pekerjaanList;
    }
}
