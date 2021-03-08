package www.starcom.com.jualanpraktis.Spinner;

import java.util.List;

public class StatusPerkawinan {

    private String status_id;
    private String status;
    private List<StatusPerkawinan> statusPerkawinanList;

    @Override
    public String toString() {
        return status;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StatusPerkawinan> getStatusPerkawinanList() {
        return statusPerkawinanList;
    }

    public void setStatusPerkawinanList(List<StatusPerkawinan> statusPerkawinanList) {
        this.statusPerkawinanList = statusPerkawinanList;
    }
}
