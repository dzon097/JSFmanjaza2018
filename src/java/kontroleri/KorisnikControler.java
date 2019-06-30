/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontroleri;

import beans.Korisnik;
import beans.Slicica;
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
import util.dao.KlijentDao;

/**
 *
 * @author mn160304d
 */
@ManagedBean
@SessionScoped
@Named(value = "KorisnikControler")
public class KorisnikControler implements Serializable{
    List<Slicica> slicice =null;
    Korisnik user;
    String poruka;

    public Korisnik getUser() {
        return user;
    }

    public void setUser(Korisnik user) {
        this.user = user;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public List<Slicica> getSlicice() {
        return slicice;
    }

    public void setSlicice(List<Slicica> slicice) {
        this.slicice = slicice;
    }
    
    public KorisnikControler(){
       HttpSession sesija = SessionUtils.getSession();   
       user = (Korisnik) sesija.getAttribute("korisnik");
       dohvatiSlicice();
    }

    private void dohvatiSlicice() {
        try {
            slicice=KlijentDao.dohvatiSlicice(user.getUsername());
        } catch (SQLException ex) {
            Logger.getLogger(KorisnikControler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    List<Slicica> kesica;
    
    public String otvoriKesicu(){
        try {
            boolean otvorio = KlijentDao.otvorioKesicu(user.getUsername());
            
            if(otvorio){
                int kesicaBr[]= new int[5];
                otvori(kesicaBr);
                
                kesica = KlijentDao.dohvatiKesicu(kesicaBr);
                KlijentDao.statuisiSlicica(kesica, user.getUsername());
               slicice=KlijentDao.dohvatiSlicice(user.getUsername());
                
                return "kesica";
            }
        } catch (SQLException ex) {
            Logger.getLogger(KorisnikControler.class.getName()).log(Level.SEVERE, null, ex);
        }
        poruka="Otvorili ste maks.broj kesica za danas";
        return "korisnik";
    }

    public List<Slicica> getKesica() {
        return kesica;
    }

    public void setKesica(List<Slicica> kesica) {
        this.kesica = kesica;
    }

    private void otvori(int[] kesica) { 
       for(int i=0; i<5; i++){
           kesica[i]=(int) (Math.random()*50+1);
       }
        
    }
    
    
}
