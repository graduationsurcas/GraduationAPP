package extra;

import java.io.Serializable;

/**
 * Created by Gheith on 08/05/2015.
 */
@SuppressWarnings("serial") //with this annotation we are going to hide compiler warning
public class PassInfo implements Serializable {
    private String id;
    private String title;

    public PassInfo(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
