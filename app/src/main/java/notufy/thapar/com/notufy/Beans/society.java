package notufy.thapar.com.notufy.Beans;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import notufy.thapar.com.notufy.R;

/**
 * Created by prat on 2/25/2015.
 */
public class society {
    private String society_name,society_code,society_info,society_color,society_icon_link,society_icon_path;
    private Bitmap icon_bitmap;
    int flag;
    public society()
    {
        flag=0;
        society_name="";
        society_icon_link="";
        society_icon_path="";
        society_code="";
        society_info="";
        society_color="#000000";
        icon_bitmap= BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.placeholder);
    }

    public int getFlag() {
        return flag;
    }

    public String getSociety_name() {
        return society_name;
    }

    public String getSociety_icon_link() {
        return society_icon_link;
    }

    public String getSociety_icon_path() {
        return society_icon_path;
    }

    public String getSociety_code() {
        return society_code;
    }

    public String getSociety_info() {
        return society_info;
    }

    public String getSociety_color() {
        return society_color;
    }

    public Bitmap getIcon_bitmap() {
        return icon_bitmap;
    }

    public void setSociety_name(String society_name) {
        this.society_name = society_name;
    }

    public void setSociety_icon_link(String society_icon_link) {
        this.society_icon_link = society_icon_link;
    }

    public void setSociety_icon_path(String society_icon_path) {
        this.society_icon_path = society_icon_path;
    }

    public void setIcon_bitmap(Bitmap icon_bitmap) {
        this.icon_bitmap = icon_bitmap;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setSociety_code(String society_code) {
        this.society_code = society_code;
    }

    public void setSociety_info(String society_info) {
        this.society_info = society_info;
    }

    public void setSociety_color(String society_color) {
        this.society_color = society_color;
    }
}
