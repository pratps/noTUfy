package notufy.thapar.com.notufy.Beans;

/**
 * Created by prat on 2/24/2015.
 */
public class teacher_message {
    private String sender_user_name,sender_icon,info,datetime,file_name,file_link,file_size,file_path,sender_user_code,sender_user_type;
    private long message_id;

    public int color,file_flag,progress;
    public teacher_message()
    {
        sender_user_name="";
        sender_icon="";
        color=-1;
        info="";
        datetime="";
        file_name="";
        file_link="";
        file_size="";
        sender_user_code="";
        file_path="";
        sender_user_type="";
        message_id=0;
        file_flag=0;
        progress=0;

    }


    public int getFile_flag() {
        return file_flag;
    }

    public int getProgress() {
        return progress;
    }

    public String getFile_path() {
        return file_path;
    }

    public long getMessage_id() {
        return message_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getFile_link() {
        return file_link;
    }

    public String getFile_name() {
        return file_name;
    }

    public String getFile_size() {
        return file_size;
    }

    public String getInfo() {
        return info;
    }

    public String getSender_icon() {
        return sender_icon;
    }

    public String getSender_user_code() {
        return sender_user_code;
    }

    public String getSender_user_name() {
        return sender_user_name;
    }

    public String getSender_user_type() {
        return sender_user_type;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
        if(this.file_name.length()>0)
        this.file_flag=1;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setMessage_id(long message_id) {
        this.message_id = message_id;
    }

    public void setSender_icon(String sender_icon) {
        this.sender_icon = sender_icon;
    }

    public void setSender_user_code(String sender_user_code) {
        this.sender_user_code = sender_user_code;
    }


    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setSender_user_name(String sender_user_name) {
        this.sender_user_name = sender_user_name;
    }

    public void setSender_user_type(String sender_user_type) {
        this.sender_user_type = sender_user_type;
    }
}
