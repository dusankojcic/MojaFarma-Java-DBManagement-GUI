package farma2;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class UsluzneMetode {

    private static Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   //private PreparedStatement pstmt;
   private Statement st;
   private ResultSet rs;
   private String izabranaZivotinja = "";
   private String izabranaTabelaZaEditovanje = "";
   private String kolonaZaEditovanje = "";
   private String pol = "";
   private String rasa = "";
   private boolean isMladunac = false;
   
   public void setRasa(String rasa) {
       this.rasa = rasa;
   }
   
   public String getRasa() {
       return this.rasa;
   }
   
   public void setPol(String pol) {
       this.pol = pol;
   }
   
   public String getPol() {
       return this.pol;
   }

   public void setIzabranaZivotinja(String zivotinja) {
        this.izabranaZivotinja = zivotinja;
    }
   
   public String getIzabranaZivotinja() {
       return this.izabranaZivotinja;
   }
   
   public String getIzabranaTabelaZaEditovanje() {
       return this.izabranaTabelaZaEditovanje;
   }
   
   public void setIzabranaTabelaZaEditovanje(String izabranaTabelaZaEditovanje) {
       this.izabranaTabelaZaEditovanje = izabranaTabelaZaEditovanje;
   }
   
   public String getKolonaZaEditovanje() {
       return this.kolonaZaEditovanje;
   }
   
   public void setKolonaZaEditovanje(String kolonaZaEditovanje) {
       this.kolonaZaEditovanje = kolonaZaEditovanje;
   }
   
   public boolean isMladunac() {
       return this.isMladunac;
   }
   
   public void setIsMladunac(boolean isMladunac) {
      this.isMladunac = isMladunac; 
   }
   
   //-------------------------------------PROBAO DA SKRATIM KOD ALI QUERY NECE DA RADI------------------------------------------------//
   
   /*public PreparedStatement prepare() throws SQLException {
        Connection con = DriverManager.getConnection
          ("jdbc:mysql://localhost/farma","root","");
        
        return con.prepareStatement("SELECT * FROM ? WHERE ? = ?");
    }*/
   
   /*public void uradiQueryPremaIzabranojZivotinji() throws SQLException {
        izabranaZivotinja = izabranaZivotinja.substring(0).toLowerCase();
        System.out.println(izabranaZivotinja);
        System.out.println("SELECT * FROM koza WHERE pol = "+pol+"");
        try {
            rs = st.executeQuery("SELECT * FROM '"+izabranaZivotinja+"' WHERE pol = '"+pol+"'");
        } 
        catch (SQLException ex) {
            System.out.println(ex);
        }
        //return rs;
    }*/
   
   //-------------------------------------------------------------------------------------------------//
   
    public UsluzneMetode() throws SQLException {
      st = Konekcija.poveziSe();
    }
    
    //-------------------------------------------------------------------------------------------------//
    
    public ResultSet queryNaOsnovuUlaznihParametara(String brMarkice, String z_brojMarkice, String m_brojMarkice) throws SQLException {
        if(isMladunac == true) {
            if(!"".equals(brMarkice)) {
                return st.executeQuery("SELECT * FROM potomak_koza WHERE brojMarkiceKoze = '"+brMarkice+"'");
            } else if(!"".equals(z_brojMarkice)) {
                return st.executeQuery("SELECT * FROM potomak_koza WHERE z_brojMarkice = '"+z_brojMarkice+"'");
            } else if(!"".equals(m_brojMarkice)) {
                return st.executeQuery("SELECT * FROM potomak_koza WHERE m_brojMarkice = '"+m_brojMarkice+"'");
            } else {
                if(!"".equals(pol) && !"".equals(rasa)) {
                    return st.executeQuery("SELECT * FROM potomak_koza WHERE pol = '"+pol+"' AND rasa = '"+rasa+"'");
                } else if(!"".equals(rasa)) {
                    return st.executeQuery("SELECT * FROM potomak_koza WHERE rasa = '"+rasa+"'");
                } else if(!"".equals(pol)) {
                    return st.executeQuery("SELECT * FROM potomak_koza WHERE pol = '"+pol+"'");
                } else {
                    return st.executeQuery("SELECT * FROM potomak_koza");
                }
            }
        } else {
            if(!"".equals(brMarkice)) {
                return st.executeQuery("SELECT * FROM koza WHERE brojMarkiceKoze = '"+brMarkice+"'");
            } else {
                if(!"".equals(pol) && !"".equals(rasa)) {
                    return st.executeQuery("SELECT * FROM koza WHERE pol = '"+pol+"' AND rasa = '"+rasa+"'");
                } else if(!"".equals(rasa)) {
                    return st.executeQuery("SELECT * FROM koza WHERE rasa = '"+rasa+"'");
                } else if(!"".equals(pol)) {
                    return st.executeQuery("SELECT * FROM koza WHERE pol = '"+pol+"'");
                } else {
                    return st.executeQuery("SELECT * FROM koza");
                }
            }
        }
    }
    
    //-------------------------------------------------------------------------------------------------//
    
    public void generisiNovuZivotinjuZaIspisNakonQueria(ArrayList a) throws SQLException {
    
        String markicaZivotinje = "";
        switch(izabranaZivotinja) {
            case "Koza":
                markicaZivotinje = "brojMarkiceKoze";
            break;
            case "Krava":
                markicaZivotinje = "brojMarkiceKrave";
            break;
            case "Konj":
                markicaZivotinje = "brojMarkiceKonja";
            break;
        }
    
        if(isMladunac() == true) {
            Zivotinje z = new Zivotinje("", "", "", "", "", "");
            z.setBrojMarkice(rs.getString(markicaZivotinje));
            z.setPol(rs.getString("pol"));
            z.setRasa(rs.getString("rasa"));
            z.setDatumRodjenja(rs.getString("datumRodjenja"));
            z.setZ_brojMarkice(rs.getString("z_brojMarkice"));
            z.setM_brojMarkice(rs.getString("m_brojMarkice"));
            a.add(z);
        } else {
            Zivotinje z = new Zivotinje("", "", "", null, "");
            z.setBrojMarkice(rs.getString(markicaZivotinje));
            z.setPol(rs.getString("pol"));
            z.setRasa(rs.getString("rasa"));
            z.setBrojPotomaka(rs.getInt("brojPotomaka"));
            z.setDatumRodjenja(rs.getString("datumRodjenja"));
            a.add(z);
        }
        
    }

    //-------------------------------------------------------------------------------------------------//
    
    public boolean adminUnesiKorisnika(String input_ime, String input_password, String isAdminTextField) throws SQLException {
        String ime = input_ime;
        String password = input_password;
        int isAdmin = Integer.parseInt(isAdminTextField);
        try {
            int num = st.executeUpdate("INSERT INTO korisnici (ime, password, admin)"
                    + " VALUES ('" +ime+ "', '" +password+ "', '"+isAdmin+"')");
            if(num > 0) {
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e) {
            throw e;
        }
    }
    
    public boolean unesiKorisnika(String input_ime, String input_password) throws SQLException  {
        String ime = input_ime;
        String password = input_password;
        try {
        int num = st.executeUpdate("INSERT INTO korisnici (ime,password)"
                + " VALUES ('" +ime+ "', '" +password+ "')");
        if(num > 0) {
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e) {
            throw e;
        }
    }
 
    //-------------------------------------------------------------------------------------------------//
    
    public ArrayList<Korisnici> izvuciSveKorisnike() throws SQLException {
        
        ArrayList<Korisnici> a = new ArrayList();
        
        try {
            rs = st.executeQuery("SELECT * FROM korisnici");
        }
        catch(Exception e) {
            throw e;
        }    
       try {
            while(rs.next()) {
                Korisnici korisnik = new Korisnici(0, "", "", 0);
                korisnik.setId(rs.getInt("korisnik_id"));
                korisnik.setIme(rs.getString("ime"));              
                korisnik.setPassword(rs.getString("password"));
                korisnik.setIsAdmin(rs.getInt("admin"));
                a.add(korisnik);
            }
       } catch (SQLException ex) {
           throw ex;
       }
        
        return a;
    }
    
    public Korisnici izvuciKorisnika(String insertIme,
                                   String insertPassword,
                                   Korisnici korisnik) throws SQLException {
        
        try {
            rs = st.executeQuery("SELECT * FROM korisnici WHERE ime = '"+insertIme+"' AND password = '"+insertPassword+"'");
        }
        catch(Exception e) {
            throw e;
        }    
       try {
            while(rs.next()) {
                korisnik.setId(rs.getInt("korisnik_id"));
                korisnik.setIme(rs.getString("ime"));              
                korisnik.setPassword(rs.getString("password"));
                korisnik.setIsAdmin(rs.getInt("admin"));
            }
       } catch (SQLException ex) {
           throw ex;
       }
        
        return korisnik;
        
    }
    
    public boolean logujKorisnika(Korisnici korisnik, String insertIme, String insertPassword) {
        // Ova provera sprecava null pointer exception ako korisnik ne postoji u bazi
        if(korisnik.getIme() != null || korisnik.getPassword() != null) {
            if(korisnik.getIme().equals(insertIme) && korisnik.getPassword().equals(insertPassword)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    //-------------------------------------------------------------------------------------------------//
    
    public boolean obrisiKorisnika(int insertID) throws SQLException {
        try {
            int num = st.executeUpdate("DELETE FROM korisnici WHERE korisnik_id = '"+insertID+"'");
            if(num > 0) {
                return true;
            } else {
                return false;
            }
        }
        catch(Exception e) {
            throw e;
        }   
    }
    
    //-------------------------------------------------------------------------------------------------//
    
    public boolean updateujKorisnika(String insertIme, String insertPassword, String insertIsAdmin, int insertID) throws SQLException {
        int isAdmin = 0;
        if(!"".equals(insertIme) && !"".equals(insertPassword) && !"".equals(insertIsAdmin)) {
            isAdmin = Integer.parseInt(insertIsAdmin);
            try {
                int num = st.executeUpdate("UPDATE korisnici SET ime = '"+insertIme+"'"
                        + ", password = '"+insertPassword+"', admin = '"+isAdmin+"' WHERE korisnik_id = '"+insertID+"'");
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(insertIme) && !"".equals(insertPassword)){
            try {
                int num = st.executeUpdate("UPDATE korisnici SET ime = '"+insertIme+"'"
                        + ", password = '"+insertPassword+"' WHERE korisnik_id = '"+insertID+"'");
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(insertIme) && !"".equals(insertIsAdmin)) {
            isAdmin = Integer.parseInt(insertIsAdmin);
            try {
                int num = st.executeUpdate("UPDATE korisnici SET ime = '"+insertIme+"'"
                        + ", admin = '"+isAdmin+"' WHERE korisnik_id = '"+insertID+"'");
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(insertPassword) && !"".equals(insertIsAdmin)) {
            isAdmin = Integer.parseInt(insertIsAdmin);
            try {
                int num = st.executeUpdate("UPDATE korisnici SET"
                        + " password = '"+insertPassword+"', admin = '"+isAdmin+"' WHERE korisnik_id = '"+insertID+"'");
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(insertIme)) {
            try {
                int num = st.executeUpdate("UPDATE korisnici SET ime = '"+insertIme+"'"
                        + " WHERE korisnik_id = '"+insertID+"'");
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(insertPassword)) {
            try {
                int num = st.executeUpdate("UPDATE korisnici SET password = '"+insertPassword+"'"
                        + " WHERE korisnik_id = '"+insertID+"'");
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(insertIsAdmin)) {
            isAdmin = Integer.parseInt(insertIsAdmin);
            try {
                int num = st.executeUpdate("UPDATE korisnici SET admin = '"+isAdmin+"'"
                        + " WHERE korisnik_id = '"+insertID+"'");
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else return false;
    }
    
    //-------------------------------------------------------------------------------------------------//
    
    public ArrayList<Zivotinje> izvuciZivotinje(String brMarkice, String z_brojMarkice, String m_brojMarkice) throws SQLException {
        
       ArrayList a = new ArrayList();
       
           switch(izabranaZivotinja) {
            
               case "":
                    try {
                        rs = st.executeQuery("SELECT * FROM zivotinje");
                    } 
                    catch (SQLException ex) {
                        throw ex;
                    }
                    try {
                        while(rs.next()) {
                            Zivotinje z = new Zivotinje();
                            z.setBrojMarkice(rs.getString("brojMarkice"));
                            a.add(z);
                        }
                    } catch(SQLException ex) {
                        throw ex;
                    }
                break;
               
                case "Koza":
                    
                        try {
                            rs = queryNaOsnovuUlaznihParametara(brMarkice, z_brojMarkice, m_brojMarkice);
                        } 
                        catch (SQLException ex) {
                            throw ex;
                        }
                        try {
                            while(rs.next()) {
                                generisiNovuZivotinjuZaIspisNakonQueria(a);
                            }
                        }
                        catch(SQLException ex) {
                             throw ex;
                        }
              
                break;

                case "Krava":
                break;

                case "Konj":
                break;
       
       }
        
       return a;
    }
    
    //-------------------------------------------------------------------------------------------------//
    
    public ArrayList<Zivotinje> izvuciJednuZivotinju(String brMarkice, String z_brojMarkice, String m_brojMarkice) throws SQLException {
        
        ArrayList a = new ArrayList();
        
        switch(izabranaZivotinja) {
            case "Koza":
                
                    try {
                       rs = queryNaOsnovuUlaznihParametara(brMarkice, z_brojMarkice, m_brojMarkice);
                    } 
                    catch (SQLException ex) {
                       throw ex;
                    }
                    try {
                        while(rs.next()) {
                            generisiNovuZivotinjuZaIspisNakonQueria(a);
                        }
                    }
                    catch(SQLException ex) {
                        throw ex;
                    }
            
            break;
            
            case "Krava":
            break;
            
            case "Konj":
            break;
        }
        
        return a;
    }
    
    public boolean unesiZivotinju(String brojMarkice,
                               String pol,
                               String rasa,
                               String brojPotomaka,
                               String datumRodjenja,
                               String zivotinjaZaUnos) throws SQLException {
        
        boolean success = false;
        if("".equals(brojPotomaka)) {
            return false;
        } else {
            int brojPotomakaInt = Integer.parseInt(brojPotomaka);
            switch(zivotinjaZaUnos) {
                case "koza":
                    try {
                        st.executeUpdate("INSERT INTO zivotinje (brojMarkice)" + " VALUES ('"+brojMarkice+"')");
                        return preparedStatementUnesiZivotinju(zivotinjaZaUnos, brojMarkice, pol, rasa, brojPotomakaInt, datumRodjenja);
                    }
                    catch(Exception e) {
                        throw e;
                    } 
                //break;
                case "krava":
                    try {
                        st.executeUpdate("INSERT INTO zivotinje (brojMarkice)" + " VALUES ('"+brojMarkice+"')");
                        return preparedStatementUnesiZivotinju(zivotinjaZaUnos, brojMarkice, pol, rasa, brojPotomakaInt, datumRodjenja);
                    }
                    catch(Exception e) {
                        throw e;
                    } 
                case "konj":
                    try {
                        st.executeUpdate("INSERT INTO zivotinje (brojMarkice)" + " VALUES ('"+brojMarkice+"')");
                        return preparedStatementUnesiZivotinju(zivotinjaZaUnos, brojMarkice, pol, rasa, brojPotomakaInt, datumRodjenja);
                    }
                    catch(Exception e) {
                        throw e;
                    } 
            }
            return success;
        }
    }
    
    public boolean preparedStatementUnesiZivotinju(String zivotinjaZaUnos,
                                            String brojMarkice,
                                            String pol,
                                            String rasa,
                                            int brojPotomaka,
                                            String datumRodjenja) throws SQLException {
        String sql = "";
        switch(zivotinjaZaUnos) {
            case "koza":
                sql = "INSERT INTO koza (brojMarkiceKoze, pol, rasa, brojPotomaka, datumRodjenja)"
                + " VALUES (?,?,?,?,?)";
            break;
            case "krava":
                sql = "INSERT INTO krava (brojMarkiceKoze, pol, rasa, brojPotomaka, datumRodjenja)"
                + " VALUES (?,?,?,?,?)";
            break;
            case "konj":
                sql = "INSERT INTO konj (brojMarkiceKoze, pol, rasa, brojPotomaka, datumRodjenja)"
                + " VALUES (?,?,?,?,?)";
            break;
        }
        
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/farma","root","");
        PreparedStatement prepSt = con.prepareStatement(sql); ) {
            
            prepSt.setString(1, brojMarkice);
            prepSt.setString(2, pol);
            prepSt.setString(3, rasa);
            prepSt.setInt(4, brojPotomaka);
            prepSt.setString(5, datumRodjenja);
            int num = prepSt.executeUpdate();
            if(num > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    //------------------------------------------------------------------------------------------------------------//
    
    public boolean updateujZivotinju(String brojMarkice,
                                    String pol,
                                    String rasa,
                                    String insertBrojPotomaka,
                                    String datumRodjenja,
                                    String zivotinjaZaUpdate,
                                    String brojMarkiceZaUpdate) throws SQLException {
        int brojPotomaka;
        int num;
        if(pol == null) {
            pol = "";
        }
        if(rasa == null) {
            rasa = "";
        }
        st.execute("SET FOREIGN_KEY_CHECKS=0");
        if(!"".equals(brojMarkice) && !"".equals(pol) && !"".equals(rasa) && !"".equals(insertBrojPotomaka) && !"".equals(datumRodjenja)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"'"
                                + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"'"
                                + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"'"
                                + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(pol) && !"".equals(rasa) && !"".equals(insertBrojPotomaka)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(pol) && !"".equals(rasa) && !"".equals(datumRodjenja)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenja+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenja+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenja+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(pol) && !"".equals(insertBrojPotomaka) && !"".equals(datumRodjenja)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(rasa) && !"".equals(insertBrojPotomaka) && !"".equals(datumRodjenja)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(rasa) && !"".equals(pol) && !"".equals(insertBrojPotomaka) && !"".equals(datumRodjenja)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET rasa = '"+rasa+"'"
                            + ", pol = '"+pol+"', brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET rasa = '"+rasa+"'"
                            + ", pol = '"+pol+"', brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET rasa = '"+rasa+"'"
                            + ", pol = '"+pol+"', brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(pol) && !"".equals(rasa)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(pol) && !"".equals(insertBrojPotomaka)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(pol) && !"".equals(datumRodjenja)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(rasa) && !"".equals(insertBrojPotomaka)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(rasa) && !"".equals(datumRodjenja)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkice+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(insertBrojPotomaka) && !"".equals(datumRodjenja)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(rasa) && !"".equals(insertBrojPotomaka)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(rasa) && !"".equals(datumRodjenja)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(insertBrojPotomaka) && !"".equals(datumRodjenja)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET pol = '"+pol+"'"
                            + ", brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET pol = '"+pol+"'"
                            + ", brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET pol = '"+pol+"'"
                            + ", brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(rasa) && !"".equals(insertBrojPotomaka) && !"".equals(datumRodjenja)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET rasa = '"+rasa+"'"
                            + ", brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET rasa = '"+rasa+"'"
                            + ", brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET rasa = '"+rasa+"'"
                            + ", brojPotomaka = '"+brojPotomaka+"', datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(pol)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", pol = '"+pol+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(rasa)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", rasa = '"+rasa+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", rasa = '"+rasa+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", rasa = '"+rasa+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(insertBrojPotomaka)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice) && !"".equals(datumRodjenja)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                        + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "koza":
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                                + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "krava":
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                                + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        break;
                        case "konj":
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                                + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(rasa)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(insertBrojPotomaka)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET pol = '"+pol+"'"
                            + ", brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET pol = '"+pol+"'"
                            + ", brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET pol = '"+pol+"'"
                            + ", brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(datumRodjenja)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET pol = '"+pol+"'"
                            + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET pol = '"+pol+"'"
                            + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET pol = '"+pol+"'"
                            + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(rasa) && !"".equals(insertBrojPotomaka)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET rasa = '"+rasa+"'"
                            + ", brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                         num = st.executeUpdate("UPDATE krava SET rasa = '"+rasa+"'"
                            + ", brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                         num = st.executeUpdate("UPDATE konj SET rasa = '"+rasa+"'"
                            + ", brojPotomaka = '"+brojPotomaka+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(rasa) && !"".equals(datumRodjenja)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET rasa = '"+rasa+"'"
                            + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET rasa = '"+rasa+"'"
                            + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET rasa = '"+rasa+"'"
                            + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(insertBrojPotomaka) && !"".equals(datumRodjenja)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET brojPotomaka = '"+brojPotomaka+"'"
                            + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET brojPotomaka = '"+brojPotomaka+"'"
                            + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET brojPotomaka = '"+brojPotomaka+"'"
                            + ", datumRodjenja = '"+datumRodjenja+"' WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                                + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                        if(num > 0) {
                            num = st.executeUpdate("UPDATE koza SET brojMarkiceKoze = '"+brojMarkice+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                            return num > 0;
                        }
                    break;
                    case "krava":
                        num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                                + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                        if(num > 0) {
                            num = st.executeUpdate("UPDATE krava SET brojMarkiceKrave = '"+brojMarkice+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                            return num > 0;
                        }
                    break;
                    case "konj":
                        num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkice+"'"
                                + " WHERE brojMarkice = '"+brojMarkiceZaUpdate+"'");
                        if(num > 0) {
                            num = st.executeUpdate("UPDATE konj SET brojMarkiceKonja = '"+brojMarkice+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                            return num > 0;
                        }
                    break;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET pol = '"+pol+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET pol = '"+pol+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET pol = '"+pol+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(rasa)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET rasa = '"+rasa+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET rasa = '"+rasa+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET rasa = '"+rasa+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
                
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(insertBrojPotomaka)) {
            brojPotomaka = Integer.parseInt(insertBrojPotomaka);
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET brojPotomaka = '"+brojPotomaka+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET brojPotomaka = '"+brojPotomaka+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET brojPotomaka = '"+brojPotomaka+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(datumRodjenja)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "koza":
                        num = st.executeUpdate("UPDATE koza SET datumRodjenja = '"+datumRodjenja+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "krava":
                        num = st.executeUpdate("UPDATE krava SET datumRodjenja = '"+datumRodjenja+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                    case "konj":
                        num = st.executeUpdate("UPDATE konj SET datumRodjenja = '"+datumRodjenja+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkiceZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        }
        return false;
    }
    
    //------------------------------------------------------------------------------------------------------------//
    
    public boolean unesiMladunca(String zivotinjaZaUnos,
                            String brojMarkice,
                            String pol,
                            String rasa,
                            String z_brojMarkice,
                            String m_brojMarkice,
                            String datumRodjenja) throws SQLException {
        
        boolean success = false;
        switch(zivotinjaZaUnos) {
            case "potomak_koza":
                try {
                    st.executeUpdate("INSERT INTO zivotinje (brojMarkice)" + " VALUES ('"+brojMarkice+"')");
                    return preparedStatementUnesiMladunca(zivotinjaZaUnos, brojMarkice, pol, rasa, z_brojMarkice, m_brojMarkice, datumRodjenja);
                }
                catch(Exception e) {
                    throw e;
                } 
            case "potomak_krava":
                try {
                    st.executeUpdate("INSERT INTO zivotinje (brojMarkice)" + " VALUES ('"+brojMarkice+"')");
                    return preparedStatementUnesiMladunca(zivotinjaZaUnos, brojMarkice, pol, rasa, z_brojMarkice, m_brojMarkice, datumRodjenja);
                }
                catch(Exception e) {
                    throw e;
                } 
            case "potomak_konj":
                try {
                    st.executeUpdate("INSERT INTO zivotinje (brojMarkice)" + " VALUES ('"+brojMarkice+"')");
                    return preparedStatementUnesiMladunca(zivotinjaZaUnos, brojMarkice, pol, rasa, z_brojMarkice, m_brojMarkice, datumRodjenja);
                }
                catch(Exception e) {
                    throw e;
                } 
        }
        return success;
    }
    
    public boolean preparedStatementUnesiMladunca(String zivotinjaZaUnos,
                                            String brojMarkice,
                                            String pol,
                                            String rasa,
                                            String z_brojMarkice,
                                            String m_brojMarkice,
                                            String datumRodjenja) throws SQLException {
        
    String sql = "";
    switch(zivotinjaZaUnos) {
        case "potomak_koza":
            sql = "INSERT INTO potomak_koza (brojMarkiceKoze, pol, rasa, z_brojMarkice, m_brojMarkice, datumRodjenja)"
            + " VALUES (?,?,?,?,?,?)";
        break;
        case "potomak_krava":
            sql = "INSERT INTO potomak_krava (brojMarkiceKrave, pol, rasa, z_brojMarkice, m_brojMarkice, datumRodjenja)"
            + " VALUES (?,?,?,?,?,?)";
        break;
        case "potomak_konj":
            sql = "INSERT INTO potomak_konj (brojMarkiceKonja, pol, rasa, z_brojMarkice, m_brojMarkice, datumRodjenja)"
            + " VALUES (?,?,?,?,?,?)";
        break;
    }

    try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/farma","root","");
    PreparedStatement prepSt = con.prepareStatement(sql); ) {
        prepSt.setString(1, brojMarkice);
        prepSt.setString(2, pol);
        prepSt.setString(3, rasa);
        prepSt.setString(4, z_brojMarkice);
        prepSt.setString(5, m_brojMarkice);
        prepSt.setString(6, datumRodjenja);
        int num = prepSt.executeUpdate();
        if(num > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    //------------------------------------------------------------------------------------------------------------//   
    
    public boolean updateujMladunca(String brojMarkicePotomka,
                                    String pol,
                                    String rasa,
                                    String z_brojMarkice,
                                    String m_brojMarkice,
                                    String datumRodjenjaPotomka,
                                    String zivotinjaZaUpdate,
                                    String brojMarkicePotomkaZaUpdate) throws SQLException {
        
        int num;
        if(pol == null) {
            pol = "";
        }
        if(rasa == null) {
            rasa = "";
        }
        st.execute("SET FOREIGN_KEY_CHECKS=0");
        
        if(!"".equals(brojMarkicePotomka) && !"".equals(pol) && !"".equals(rasa) && !"".equals(z_brojMarkice) && !"".equals(m_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(pol) && !"".equals(rasa) && !"".equals(z_brojMarkice) && !"".equals(m_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(pol) && !"".equals(rasa) && !"".equals(z_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(pol) && !"".equals(rasa) && !"".equals(m_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(pol) && !"".equals(z_brojMarkice) && !"".equals(m_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(rasa) && !"".equals(z_brojMarkice) && !"".equals(m_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(rasa) && !"".equals(z_brojMarkice) && !"".equals(m_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(rasa) && !"".equals(z_brojMarkice) && !"".equals(m_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(rasa) && !"".equals(z_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(rasa) && !"".equals(m_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(rasa) && !"".equals(pol) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', pol = '"+pol+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', pol = '"+pol+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', pol = '"+pol+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(rasa) && !"".equals(pol) && !"".equals(z_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', pol = '"+pol+"', z_brojMarkice = '"+z_brojMarkice+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', pol = '"+pol+"', z_brojMarkice = '"+z_brojMarkice+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', pol = '"+pol+"', z_brojMarkice = '"+z_brojMarkice+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(rasa) && !"".equals(pol) && !"".equals(m_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', pol = '"+pol+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', pol = '"+pol+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', pol = '"+pol+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(pol) && !"".equals(z_brojMarkice) && !"".equals(m_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(rasa) && !"".equals(z_brojMarkice) && !"".equals(m_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(rasa) && !"".equals(z_brojMarkice) && !"".equals(m_brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(rasa) && !"".equals(z_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', datumRodjenjaPotomka = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', datumRodjenjaPotomka = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"', datumRodjenjaPotomka = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(rasa) && !"".equals(m_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenjaPotomka = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenjaPotomka = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', m_brojMarkice = '"+m_brojMarkice+"', datumRodjenjaPotomka = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(pol) && !"".equals(rasa)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', rasa = '"+rasa+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(pol) && !"".equals(z_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', z_brojMarkice = '"+z_brojMarkice+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', z_brojMarkice = '"+z_brojMarkice+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', z_brojMarkice = '"+z_brojMarkice+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(pol) && !"".equals(m_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(pol) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(rasa) && !"".equals(z_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(rasa) && !"".equals(m_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(rasa) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(z_brojMarkice) && !"".equals(m_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(z_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(m_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(rasa) && !"".equals(z_brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', z_brojMarkice = '"+z_brojMarkice+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(rasa) && !"".equals(m_brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(rasa) && !"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(z_brojMarkice) && !"".equals(m_brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(z_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(m_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(rasa) && !"".equals(z_brojMarkice) && !"".equals(m_brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET rasa = '"+rasa+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET rasa = '"+rasa+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET rasa = '"+rasa+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"', m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(rasa) && !"".equals(z_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET rasa = '"+rasa+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET rasa = '"+rasa+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET rasa = '"+rasa+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(rasa) && !"".equals(m_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET rasa = '"+rasa+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET rasa = '"+rasa+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET rasa = '"+rasa+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"', datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(pol)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", pol = '"+pol+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(rasa)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", rasa = '"+rasa+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(z_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", z_brojMarkice = '"+z_brojMarkice+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", z_brojMarkice = '"+z_brojMarkice+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", z_brojMarkice = '"+z_brojMarkice+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(m_brojMarkice)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", m_brojMarkice = '"+m_brojMarkice+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", m_brojMarkice = '"+m_brojMarkice+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", m_brojMarkice = '"+m_brojMarkice+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka) && !"".equals(datumRodjenjaPotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(rasa)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", rasa = '"+rasa+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(z_brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(m_brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol) && !"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(rasa) && !"".equals(z_brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET rasa = '"+rasa+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET rasa = '"+rasa+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET rasa = '"+rasa+"'"
                            + ", z_brojMarkice = '"+z_brojMarkice+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(rasa) && !"".equals(m_brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET rasa = '"+rasa+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET rasa = '"+rasa+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET rasa = '"+rasa+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(rasa) && !"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET rasa = '"+rasa+"'"
                            + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET rasa = '"+rasa+"'"
                            + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET rasa = '"+rasa+"'"
                            + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(z_brojMarkice) && !"".equals(m_brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET z_brojMarkice = '"+z_brojMarkice+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET z_brojMarkice = '"+z_brojMarkice+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET z_brojMarkice = '"+z_brojMarkice+"'"
                            + ", m_brojMarkice = '"+m_brojMarkice+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(z_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET z_brojMarkice = '"+z_brojMarkice+"'"
                            + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET z_brojMarkice = '"+z_brojMarkice+"'"
                            + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET z_brojMarkice = '"+z_brojMarkice+"'"
                            + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(m_brojMarkice) && !"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET m_brojMarkice = '"+m_brojMarkice+"'"
                            + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET m_brojMarkice = '"+m_brojMarkice+"'"
                            + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET m_brojMarkice = '"+m_brojMarkice+"'"
                            + ", datumRodjenja = '"+datumRodjenjaPotomka+"' WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(brojMarkicePotomka)) {
            try {
                num = st.executeUpdate("UPDATE zivotinje SET brojMarkice = '"+brojMarkicePotomka+"'"
                        + " WHERE brojMarkice = '"+brojMarkicePotomkaZaUpdate+"'");
                if(num > 0) {
                    switch(zivotinjaZaUpdate) {
                        case "potomak_koza":
                            num = st.executeUpdate("UPDATE potomak_koza SET brojMarkiceKoze = '"+brojMarkicePotomka+"'"
                                + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_krava":
                            num = st.executeUpdate("UPDATE potomak_krava SET brojMarkiceKrave = '"+brojMarkicePotomka+"'"
                                + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                        case "potomak_konj":
                            num = st.executeUpdate("UPDATE potomak_konj SET brojMarkiceKonja = '"+brojMarkicePotomka+"'"
                                + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        break;
                    }
                }
                return num > 0;
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(pol)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET pol = '"+pol+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET pol = '"+pol+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET pol = '"+pol+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(rasa)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET rasa = '"+rasa+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET rasa = '"+rasa+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET rasa = '"+rasa+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(z_brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET z_brojMarkice = '"+z_brojMarkice+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET z_brojMarkice = '"+z_brojMarkice+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET z_brojMarkice = '"+z_brojMarkice+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(m_brojMarkice)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET m_brojMarkice = '"+m_brojMarkice+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        } else if(!"".equals(datumRodjenjaPotomka)) {
            try {
                switch(zivotinjaZaUpdate) {
                    case "potomak_koza":
                        num = st.executeUpdate("UPDATE potomak_koza SET datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKoze = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_krava":
                        num = st.executeUpdate("UPDATE potomak_krava SET datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKrave = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                    case "potomak_konj":
                        num = st.executeUpdate("UPDATE potomak_konj SET datumRodjenja = '"+datumRodjenjaPotomka+"'"
                            + " WHERE brojMarkiceKonja = '"+brojMarkicePotomkaZaUpdate+"'");
                        return num > 0;
                }
            }
            catch(Exception e) {
                throw e;
            }  
        }
        return false;
    }
    
    //------------------------------------------------------------------------------------------------------------//   
    
    public boolean obrisiZivotinju(String brojMarkice, String zivotinjaZaBrisanje) throws SQLException {
        switch(zivotinjaZaBrisanje) {
            case "koza":
                try {
                    int num = st.executeUpdate("DELETE FROM koza WHERE brojMarkiceKoze = '"+brojMarkice+"'");
                    if(num > 0) {
                        st.executeUpdate("DELETE FROM zivotinje WHERE brojMarkice = '"+brojMarkice+"'");
                        return true;
                    } else {
                        return false;
                    }
                }
                catch(Exception e) {
                    throw e;
                }
            case "krava":
                try {
                    int num = st.executeUpdate("DELETE FROM krava WHERE brojMarkiceKrave = '"+brojMarkice+"'");
                    if(num > 0) {
                        st.executeUpdate("DELETE FROM zivotinje WHERE brojMarkice = '"+brojMarkice+"'");
                        return true;
                    } else {
                        return false;
                    }
                }
                catch(Exception e) {
                    throw e;
                }
            case "konj":
                try {
                    int num = st.executeUpdate("DELETE FROM konj WHERE brojMarkiceKonja = '"+brojMarkice+"'");
                    if(num > 0) {
                        st.executeUpdate("DELETE FROM zivotinje WHERE brojMarkice = '"+brojMarkice+"'");
                        return true;
                    } else {
                        return false;
                    }
                }
                catch(Exception e) {
                    throw e;
                }
        }
        return false;
    }
    
    //------------------------------------------------------------------------------------------------------------//
    
    public boolean obrisiMladunca(String brojMarkice, String zivotinjaZaBrisanje) throws SQLException {
        switch(zivotinjaZaBrisanje) {
            case "potomak_koza":
                try {
                    int num = st.executeUpdate("DELETE FROM potomak_koza WHERE brojMarkiceKoze = '"+brojMarkice+"'");
                    if(num > 0) {
                        st.executeUpdate("DELETE FROM zivotinje WHERE brojMarkice = '"+brojMarkice+"'");
                        return true;
                    } else {
                        return false;
                    }
                }
                catch(Exception e) {
                    throw e;
                }
            case "potomak_krava":
                try {
                    int num = st.executeUpdate("DELETE FROM potomak_krava WHERE brojMarkiceKrave = '"+brojMarkice+"'");
                    if(num > 0) {
                        st.executeUpdate("DELETE FROM zivotinje WHERE brojMarkice = '"+brojMarkice+"'");
                        return true;
                    } else {
                        return false;
                    }
                }
                catch(Exception e) {
                    throw e;
                }
            case "potomak_konj":
                try {
                    int num = st.executeUpdate("DELETE FROM potomak_konj WHERE brojMarkiceKonja = '"+brojMarkice+"'");
                    if(num > 0) {
                        st.executeUpdate("DELETE FROM zivotinje WHERE brojMarkice = '"+brojMarkice+"'");
                        return true;
                    } else {
                        return false;
                    }
                }
                catch(Exception e) {
                    throw e;
                }
        }
        return false;
    }
    
    //------------------------------------------------------------------------------------------------------------//
    
    public void resetForeignKeyChecksAfterUpdate() throws SQLException {
        st.execute("SET FOREIGN_KEY_CHECKS=1");
    }
    
    //------???????????????????????????????????????????????????????????-----//
    
    /*public static void createTable()throws Exception {
       try {
           Connection con = getConnection();
       } 
       catch(Exception e) {
          System.out.println(e);
       }
       finally {
          System.out.println("Function complete");
       }
    }*/
    
    //------???????????????????????????????????????????????????????????-----//
}
            
            
            


