
package farma2;

public class Zivotinje
{
    private String brojMarkice;
    private String pol;
    private String rasa;
    private Integer brojPotomaka;
    private String datumRodjenja;
    private String z_brojMarkice;
    private String m_brojMarkice;

    public Zivotinje() {
    }

    //Izvuci Zivotinju
    public Zivotinje(String brojMarkice, String pol, String rasa, Integer brojPotomaka, String datumRodjenja) {
        this.brojMarkice = brojMarkice;
        this.pol = pol;
        this.rasa = rasa;
        this.brojPotomaka = brojPotomaka;
        this.datumRodjenja = datumRodjenja;
    }

    //Izvuci Potomka Zivotinje
    public Zivotinje(String brojMarkice, String pol, String rasa, String datumRodjenja, String z_brojMarkice, String m_brojMarkice) {
        this.brojMarkice = brojMarkice;
        this.pol = pol;
        this.rasa = rasa;
        this.datumRodjenja = datumRodjenja;
        this.z_brojMarkice = z_brojMarkice;
        this.m_brojMarkice = m_brojMarkice;
    }

    public String getBrojMarkice() {
        return brojMarkice;
    }

    public void setBrojMarkice(String brojMarkice) {
        this.brojMarkice = brojMarkice;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getRasa() {
        return rasa;
    }

    public void setRasa(String rasa) {
        this.rasa = rasa;
    }

    public Integer getBrojPotomaka() {
        return brojPotomaka;
    }

    public void setBrojPotomaka(Integer brojPotomaka) {
        this.brojPotomaka = brojPotomaka;
    }

    public String getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(String datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getZ_brojMarkice() {
        return z_brojMarkice;
    }

    public void setZ_brojMarkice(String z_brojMarkice) {
        this.z_brojMarkice = z_brojMarkice;
    }

    public String getM_brojMarkice() {
        return m_brojMarkice;
    }

    public void setM_brojMarkice(String m_brojMarkice) {
        this.m_brojMarkice = m_brojMarkice;
    }
    
    @Override
    public String toString() {
        return "Zivotinje{" + "brojMarkice=" + brojMarkice + ", pol=" + pol + ""
                + ", rasa=" + rasa + ", brojPotomaka=" + brojPotomaka + 
                ", datumRodjenja=" + datumRodjenja + ", z_brojMarkice="
                + z_brojMarkice + ", m_brojMarkice=" + m_brojMarkice + '}';
    }
    
    
}
