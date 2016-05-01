package fr.do_f.rssfeedify.api.json.users;

import java.io.Serializable;

/**
 * Created by do_f on 26/04/16.
 */
public class GetUserReponse implements Serializable{

    private String username;
    private String type;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
