/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontroleri;

import beans.Duplikat;
import beans.Korisnik;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import util.SessionUtils;
import util.dao.AdminDao;

/**
 *
 * @author mn160304d
 */
@ManagedBean
@SessionScoped
@Named(value = "AdminControler")
public class AdminControler implements Serializable{
    List<Duplikat> duplikati;
    Korisnik user;

    public List<Duplikat> getDuplikati() {
        return duplikati;
    }

    public void setDuplikati(List<Duplikat> duplikati) {
        this.duplikati = duplikati;
    }
    
    public AdminControler(){
        HttpSession sesija = SessionUtils.getSession();   
       user = (Korisnik) sesija.getAttribute("korisnik");
        dovuciDuplikate();
    }

    private void dovuciDuplikate() {
        try {
            duplikati=AdminDao.dohvatiDuplikate();
        } catch (SQLException ex) {
            Logger.getLogger(AdminControler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void otkupi(Duplikat duplikat){
        duplikati.remove(duplikat);
        
        try {
            AdminDao.otkupiDuplikat(duplikat, user.getUsername());
        } catch (SQLException ex) {
            Logger.getLogger(AdminControler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
