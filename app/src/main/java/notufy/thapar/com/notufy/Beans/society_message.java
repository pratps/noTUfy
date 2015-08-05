package notufy.thapar.com.notufy.Beans;

import android.graphics.Bitmap;

/**
 * Created by prat on 2/24/2015.
 */
public class society_message {
    private String heading,info,datetime,event_datetime,image_url,image_path,society_code,society_name,society_color,event_venue,image_size;
    private String society_icon_link,society_icon_path;
    private long message_id,event_timestamp;
    private Bitmap image_bitmap;
    public society_message()
    {
        image_bitmap=null;
        image_path="";
        heading="";
        info="";
        event_venue="";
        datetime="";
        event_datetime="";
        image_url="";
        society_code="";
        society_color="#000000";
        message_id=0;
        image_size="";
        event_timestamp=-1;

    }

    public Bitmap getImage_bitmap() {
        return image_bitmap;
    }

    public String getSociety_icon_path() {
        return society_icon_path;
    }

    public String getSociety_icon_link() {
        return society_icon_link;
    }

    public String getSociety_color() {
        return society_color;
    }

    public String getEvent_venue() {
        return event_venue;
    }

    public String getSociety_name() {
        return society_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getImage_size() {
        return image_size;
    }

    public String getInfo() {
        return info;
    }

    public long getMessage_id() {
        return message_id;
    }

    public long getEvent_timestamp() {
        return event_timestamp;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getEvent_datetime() {
        return event_datetime;
    }

    public String getHeading() {
        return heading;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getSociety_code() {
        return society_code;
    }

    public void setMessage_id(long message_id) {
        this.message_id = message_id;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setSociety_icon_path(String society_icon_path) {
        this.society_icon_path = society_icon_path;
    }

    public void setSociety_icon_link(String society_icon_link) {
        this.society_icon_link = society_icon_link;
    }

    public void setEvent_datetime(String event_datetime) {
        this.event_datetime = event_datetime;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setSociety_code(String society_code) {
        this.society_code = society_code;
    }

    public void setSociety_color(String society_color) {
        this.society_color = society_color;
    }

    public void setSociety_name(String society_name) {
        this.society_name = society_name;
    }

    public void setEvent_venue(String event_venue) {
        this.event_venue = event_venue;
    }

    public void setEvent_timestamp(long event_timestamp) {
        this.event_timestamp = event_timestamp;
    }

    public void setImage_size(String image_size) {

        this.image_size=image_size;
    }

    public void setImage_bitmap(Bitmap image_bitmap) {
        this.image_bitmap = image_bitmap;
    }
}