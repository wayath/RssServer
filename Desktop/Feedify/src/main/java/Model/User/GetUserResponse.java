/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.User;

/**
 *
 * @author Bastien
 */
public class GetUserResponse {
    private String username;
    private String type;
    
    public String getType() {
        return type;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void setUsername(String userName) {
        this.username = userName;
    }
}
