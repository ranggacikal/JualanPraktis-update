package www.starcom.com.jualanpraktis.Spinner;

import java.util.List;

public class Penghasilan {

    private String penghasilan_id;
    private String penghasilan;
    List<Penghasilan> penghasilanList;

    @Override
    public String toString() {
        return penghasilan;
    }

    public String getPenghasilan_id() {
        return penghasilan_id;
    }

    public void setPenghasilan_id(String penghasilan_id) {
        this.penghasilan_id = penghasilan_id;
    }

    public String getPenghasilan() {
        return penghasilan;
    }

    public void setPenghasilan(String penghasilan) {
        this.penghasilan = penghasilan;
    }

    public List<Penghasilan> getPenghasilanList() {
        return penghasilanList;
    }

    public void setPenghasilanList(List<Penghasilan> penghasilanList) {
        this.penghasilanList = penghasilanList;
    }
}
