package farma2;

public class LogIn 
{
     private String ime;
     private String password;
     private boolean success = false;

    public LogIn() {}

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public boolean ulogujSe(UsluzneMetode m, Korisnici korisnik) {
       success = m.logujKorisnika(korisnik, ime, password);
       if(success)
       {
           setSuccess(success);
           return true;
       }
       else {
           return false;
       }
    }
    
    /*public boolean ulogujSe(UsluzneMetode m) {
       success = m.izvuciKorisnika(ime, password, success);
       if(success)
       {
           setSuccess(success);
           return true;
       }
       else {
           return false;
       }
    }*/
    
}
