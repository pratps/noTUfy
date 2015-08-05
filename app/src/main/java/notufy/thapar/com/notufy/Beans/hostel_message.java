package notufy.thapar.com.notufy.Beans;

/**
 * Created by prat on 3/25/2015.
 */
public class hostel_message {
    private String heading,info,datetime;
    private long message_id;
    public hostel_message()
    {
        heading="";
        info="";
        datetime="";
        message_id=0;
    }

    public long getMessage_id() {
        return message_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getHeading() {
        return heading;
    }

    public String getInfo() {
        return info;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setMessage_id(long message_id) {
        this.message_id = message_id;
    }
}
