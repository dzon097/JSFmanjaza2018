/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.dao;

import beans.Korisnik;
import beans.Slicica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DB;

/**
 *
 * @author mn160304d
 */
public class KlijentDao {
     public static List<Slicica> dohvatiSlicice(String user) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DB.getInstance().getConnection();
           
            ps = con.prepareStatement("select S.idSlicice, naziv, zemlja, tip from moj_album A, slicica S where username = ? and S.idSlicice = A.idSlicice order by tip");
            ps.setString(1, user);

            ResultSet rs = ps.executeQuery();

            List<Slicica> slicice = new ArrayList<>();
            
            while(rs.next()) {
                Slicica slika = new Slicica();
                slika.setBroj(rs.getInt("idSlicice"));
                slika.setNaziv(rs.getString("naziv"));
                slika.setTip(rs.getString("tip"));
                slika.setZemlja(rs.getString("zemlja"));
                
                slicice.add(slika);
            }
                return slicice;
            
        } catch (SQLException ex) {
            System.out.println("Greska u logovanju -->" + ex.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
            DB.getInstance().putConnection(con);
        }
        return null;
     }
     public static boolean otvorioKesicu(String user) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DB.getInstance().getConnection();
           
            ps = con.prepareStatement("select count(*) as otvorio from otvorio_kesicu where datum=current_date() and username=? ");
            ps.setString(1, user);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                int otvorio = rs.getInt("otvorio");
                if(otvorio==3) return false; //Otvorio max;
            }
            
            //Otvori novu
            
            ps = con.prepareStatement("insert into otvorio_kesicu (username, datum) value( ? , current_date())");
            ps.setString(1, user);
            
            int ret = ps.executeUpdate();
            if(ret!=0) return true;
            
        } catch (SQLException ex) {
            System.out.println("Greska u logovanju -->" + ex.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
            DB.getInstance().putConnection(con);
        }
        return false;
     }

    public static List<Slicica> dohvatiKesicu(int[] kesicaBr) throws SQLException {
        List<Slicica> kesica = new ArrayList<>();
        
        Connection con = null;
        PreparedStatement ps = null;
         con = DB.getInstance().getConnection();
         
         try {
             for(int br : kesicaBr){
             ps = con.prepareStatement("select * from slicica where idSlicice=? ");
             ps.setInt(1, br);
             
             ResultSet rs = ps.executeQuery();
             if(rs.next()){
                 Slicica slika = new Slicica();
                 slika.setBroj(br);
                 slika.setNaziv(rs.getString("naziv"));
                 slika.setTip(rs.getString("tip"));
                 slika.setZemlja(rs.getString("zemlja"));
                 
                 kesica.add(slika); 
                 
                 
             }
             else
                     System.err.println("Greska u br slicice");
             }
             return kesica;
             
         } catch (SQLException ex) {
             Logger.getLogger(KlijentDao.class.getName()).log(Level.SEVERE, null, ex);
         } finally {
            if (ps != null) {
                ps.close();
            }
            DB.getInstance().putConnection(con);
        }
         
         return null;
    }

    public static void statuisiSlicica(List<Slicica> kesica, String user) throws SQLException {
        Connection con = null;
        PreparedStatement psDuplikatDodaj=null, psPostoji=null, psZalepi=null ;
        con = DB.getInstance().getConnection();
         
         try {
            psDuplikatDodaj = con.prepareStatement("insert into duplikati (username, idSlicice) value (?, ?)");
            psDuplikatDodaj.setString(1, user);
            
            psPostoji = con.prepareStatement("select * from moj_album where idSlicice=? and username=?");
            psPostoji.setString(2, user);
            psZalepi = con.prepareStatement("insert into moj_album (username, idSlicice) value (?, ?)");
            psZalepi.setString(1, user);
                 
            ResultSet rs=null;
            
            for(Slicica slicica : kesica){
                
               psPostoji.setInt(1, slicica.getBroj());
               rs=psPostoji.executeQuery();
               
               if(rs.next()){ //Postoji vec
                   slicica.setStatus("duplikat");
                   psDuplikatDodaj.setInt(2, slicica.getBroj());
                   psDuplikatDodaj.executeUpdate();
               }
               else{
                   slicica.setStatus("zalepljena");
                   psZalepi.setInt(2, slicica.getBroj());
                   psZalepi.executeUpdate();
               }
            }   
             
        } catch (SQLException ex) {
             Logger.getLogger(KlijentDao.class.getName()).log(Level.SEVERE, null, ex);
         } finally {
           psDuplikatDodaj.close(); psPostoji.close(); psZalepi.close();
            DB.getInstance().putConnection(con);
        }
         
    }
    
}
