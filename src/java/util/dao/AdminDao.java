/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.dao;

import beans.Duplikat;
import beans.Slicica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.DB;

/**
 *
 * @author mn160304d
 */
public class AdminDao {
    public static List<Duplikat> dohvatiDuplikate() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DB.getInstance().getConnection();
           
            ps = con.prepareStatement("select idSlicice, ime, prezime, K.username from duplikati D, korisnik K where D.username=K.username");
            

            ResultSet rs = ps.executeQuery();

            List<Duplikat> slicice = new ArrayList<>();
            
            while(rs.next()) {
                Duplikat slika = new Duplikat();
                slika.setIdSlicice(rs.getInt("idSlicice"));
                slika.setIme(rs.getString("ime"));
                slika.setPrezime(rs.getString("prezime"));
                slika.setUsername(rs.getString("username"));
                
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
    
     public static int otkupiDuplikat(Duplikat duplikat, String user) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DB.getInstance().getConnection();
           
            ps = con.prepareStatement("delete from duplikati where username=? and idSlicice=? ");
            ps.setInt(2, duplikat.getIdSlicice());
            ps.setString(1, duplikat.getUsername());
            

            int p =ps.executeUpdate();
            return p;
            
        } catch (SQLException ex) {
            System.out.println("Greska u logovanju -->" + ex.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
            DB.getInstance().putConnection(con);
        }
        return 0;
     }
}
