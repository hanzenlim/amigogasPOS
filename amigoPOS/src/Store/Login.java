/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Store;

/**
 *
 * @author Hanzen
 */
public class Login {
    
    private String userName;
    private String password;
    
    public Login(String username, String password)
    {
        this.userName=username;
        this.password=password;
    }
    
    public String getUsername()
    { return userName; }
    
    public String getpassword()
    {
        return password;
    }
}
