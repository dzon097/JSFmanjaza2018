package kontroleri;

import beans.Korisnik;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.servlet.http.HttpSession;
import util.SessionUtils;
import util.dao.LoginDao;


/**
 *
 * @author Drazen
 */
@SessionScoped
@Named("loginController")
public class LoginController implements Serializable {

    private String user;
    private String pass;
    private Korisnik korisnik=null;
    private String poruka = "";

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

   

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String login() throws SQLException {
            korisnik = LoginDao.dohvatiKorisnika(user, pass);
          
            HttpSession sesija = SessionUtils.getSession();   
            sesija.setAttribute("korisnik", korisnik); //stavlja objekat Korisnik u sesiju
           
  
            
            if(korisnik!=null){
                if(korisnik.getTip()==1)
                    return "administrator";
                if(korisnik.getTip()==0)
                    return "korisnik";
            }

         else 
            poruka = "Došlo je do greške u radu sa bazom. Pokušajte kasnije.";
            return "index";
     }

    public String logout() {
        HttpSession sesija = SessionUtils.getSession();
        sesija.invalidate();
        return "index";
    }
}
