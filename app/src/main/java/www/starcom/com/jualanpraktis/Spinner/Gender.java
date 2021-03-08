package www.starcom.com.jualanpraktis.Spinner;

import androidx.annotation.NonNull;

import java.util.List;

public class Gender {

    private String gender_id;
    private String gender;
    private List<Gender> genderList;

    @Override
    public String toString() {
        return gender;
    }

    public String getGender_id() {
        return gender_id;
    }

    public void setGender_id(String gender_id) {
        this.gender_id = gender_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Gender> getGenderList() {
        return genderList;
    }

    public void setGenderList(List<Gender> genderList) {
        this.genderList = genderList;
    }
}
