package fr.do_f.rssfeedify.api.json.users;

import java.io.Serializable;

/**
 * Created by do_f on 24/04/16.
 */
public class UpdateUserResponse {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class User implements Serializable {

        private String username;
        private String password;
        private String type;

        public User(String username, String password, String type) {
            this.username = username;
            this.password = password;
            this.type = type;
        }

        public User(String username, String type) {
            this.username = username;
            this.type = type;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
