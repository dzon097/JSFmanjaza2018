/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.dao;

import beans.Korisnik;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.DB;

/**
 *
 * @author mn160304d
 */
public class LoginDao {

    public static Korisnik dohvatiKorisnika(String user, String pass) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DB.getInstance().getConnection();
            if (con == null) {
                System.out.println("*** Nije moguće konektovati se na bazu podataka. ***");
                return null;
            }
            ps = con.prepareStatement("SELECT * FROM korisnik WHERE username = ? AND password = ?");
            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");

                Korisnik kor = new Korisnik();
                kor.setUsername(user);
                kor.setTip(rs.getInt("tipKorisnika"));
                kor.setIme(ime);
                kor.setPrezime(prezime);
                return kor;
            }
        } catch (SQLException ex) {
            System.out.println("Greska u logovanju -->" + ex.getMessage());
            return null;
        } finally {
            if (ps != null) {
                ps.close();
            }
            DB.getInstance().putConnection(con);
        }
        return null;
    }
}
