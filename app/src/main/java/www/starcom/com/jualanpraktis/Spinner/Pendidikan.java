package www.starcom.com.jualanpraktis.Spinner;

import java.util.List;

public class Pendidikan {

    private String pendidikan_id;
    private String pendidikan;
    List<Pendidikan> pendidikanList;

    @Override
    public String toString() {
        return pendidikan;
    }

    public String getPendidikan_id() {
        return pendidikan_id;
    }

    public void setPendidikan_id(String pendidikan_id) {
        this.pendidikan_id = pendidikan_id;
    }

    public String getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan = pendidikan;
    }

    public List<Pendidikan> getPendidikanList() {
        return pendidikanList;
    }

    public void setPendidikanList(List<Pendidikan> pendidikanList) {
        this.pendidikanList = pendidikanList;
    }
}
