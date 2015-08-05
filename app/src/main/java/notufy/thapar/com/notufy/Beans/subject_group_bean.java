package notufy.thapar.com.notufy.Beans;

/**
 * Created by prat on 2/27/2015.
 */
public class subject_group_bean {

    private String subject_code,group,course,subject_group_code,year,LTP;
    private int flag;

    public subject_group_bean()
    {
        subject_code="";
        course="";
        subject_group_code="";
        year="";
        group="";
        LTP="";
        flag=0;
    }


    public String getLTP() {
        return LTP;
    }

    public int getFlag() { return flag;}

    public String getGroup() {
        return  group;
    }

    public String getCourse() {
        return course;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public String getSubject_group_code() {
        return subject_group_code;
    }

    public String getYear() {
        return year;
    }

    public void setFlag(int flag) {this.flag = flag;}

    public void setCourse(String course) {
        this.course = course;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    public void setSubject_group_code(String subject_group_code) {
        this.subject_group_code = subject_group_code;
    }

    public void setLTP(String LTP) {
        this.LTP = LTP;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setGroup(String group) { this.group = group; }

}
