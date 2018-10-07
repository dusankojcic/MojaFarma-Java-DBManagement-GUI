
package farma2;

public class Korisnici 
{
    private int  id;
    private String ime;
    //private String prezime;
    private String password;
    private int isAdmin;

    public Korisnici() {}
    
    public Korisnici(int id, String ime, String password, int isAdmin) {
        this.id = id;
        this.ime = ime;
        //this.prezime = prezime;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    /*public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public int isAdmin() {
        return this.isAdmin;
    }
    
    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
    
        @Override
    public String toString() {
        return "Korisnici{"+ "id= " + id + ", ime= " + ime + ", password=" + password + ", admin="+isAdmin+'}';
    }





}