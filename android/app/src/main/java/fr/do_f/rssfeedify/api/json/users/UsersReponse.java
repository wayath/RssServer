package fr.do_f.rssfeedify.api.json.users;

import java.io.Serializable;
import java.util.List;

/**
 * Created by do_f on 23/04/16.
 */
public class UsersReponse {

    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public static class User implements Serializable {
        private int     id;
        private String  username;
        private String  type;
        private int     color;

        public User(String username, String type) {
            this.username = username;
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

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

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
