package farma2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static jdk.nashorn.internal.objects.NativeString.trim;
//TODO EXPORT TO EXCEL
/*import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;*/

public class Start extends Application
{
    public LogIn log;
    public UsluzneMetode m;
    public Button adminBtn;
    public Korisnici korisnik;
    public Zivotinje z;
    BorderPane adminLayout;
    public TableView<Zivotinje> table;
    public TableView<Zivotinje> tableMladunci;
    public TableView<Korisnici> tableKorisnici;
    public Listener listener = new Listener();
    public ArrayList<Zivotinje> sveZivotinje = new ArrayList();
    public ArrayList<Korisnici> sviKorisnici = new ArrayList();
    public ArrayList<Zivotinje> jednaZivotinja = new ArrayList();
    
    public void mojQuery(BorderPane layout)
    {
        if(m.isMladunac() == true) {
            layout.setCenter(tableMladunci);
        } else {
            layout.setCenter(table);
        }
    }
    
    public ObservableList<Korisnici> getKorisnici() throws SQLException {
        ObservableList<Korisnici> data;
        sviKorisnici = m.izvuciSveKorisnike();
        data = FXCollections.observableArrayList(sviKorisnici);
        return data;
    }
    
    public ObservableList<Zivotinje> getZivotinje(String brMarkice, String z_brojMarkice, String m_brojMarkice) throws SQLException {
        
        ObservableList<Zivotinje> data;
        if("".equals(brMarkice) && "".equals(z_brojMarkice) && "".equals(m_brojMarkice)) {
            sveZivotinje = m.izvuciZivotinje(brMarkice, z_brojMarkice, m_brojMarkice);
            data = FXCollections.observableArrayList(sveZivotinje);
            //System.out.println(data);
            return data;
        } else {
            jednaZivotinja = m.izvuciJednuZivotinju(brMarkice, z_brojMarkice, m_brojMarkice);
            data = FXCollections.observableArrayList(jednaZivotinja);
            //System.out.println(data);
            return data;
        }
        
    }
    
    public void setupLoginWindow(Stage primaryStage, Scene mainScene, Stage LogInStage, GridPane grid, Scene logScene) {
        //LogIn Window
        Button registerBtn = new Button("Register");
        Button loginBtn = new Button("Log In");
        loginBtn.setDefaultButton(true);
        
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
                
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(registerBtn);
        hbBtn.getChildren().add(loginBtn);
        grid.add(hbBtn, 1, 4);
        
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Username:");
        grid.add(userName, 0, 1);

        TextField usernameTextField = new TextField();
        grid.add(usernameTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);
        //grid.setGridLinesVisible(true); //da vidis linije
        PasswordField passwordTextField = new PasswordField();
        grid.add(passwordTextField, 1, 2);
        
        LogInStage.setScene(logScene);
        LogInStage.show();
        
        //-----------------------------------------POZIV NA FUNKCIJU ZA REGISTRACIJU----------------------------------------------//
        
        register(registerBtn, usernameTextField, passwordTextField, grid);
        
        //-------------------------------------POZIV NA FUNKCIJU ZA LOGIN-----------------------------------------------//
        
        login(loginBtn, usernameTextField, passwordTextField, LogInStage, primaryStage, grid);
        
        //LogIn Window END
    }
    
    public void register(Button registerBtn,
                      TextField usernameTextField,
                      TextField passwordTextField,
                      GridPane grid) {
        
        registerBtn.setOnAction( e -> {
           String ime = usernameTextField.getText();
           String password = passwordTextField.getText();
            try {
                if(m.unesiKorisnika(ime, password)) {
                    Label uspesnaRegistracija = new Label("Registracija je uspesna.");
                    uspesnaRegistracija.setTextFill(Color.GREEN);
                    grid.add(uspesnaRegistracija, 1, 5);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                Label neuspesnaRegistracija = new Label("Registracija nije uspela.");
                neuspesnaRegistracija.setTextFill(Color.RED);
                grid.add(neuspesnaRegistracija, 1, 5);
            }
        });
    }
    
    public void login(Button loginBtn,
                      TextField usernameTextField,
                      TextField passwordTextField,
                      Stage LogInStage,
                      Stage primaryStage,
                      GridPane grid) {
        
        loginBtn.setOnAction((ActionEvent e) -> {
            String ime = trim(usernameTextField.getText());
            String pass = passwordTextField.getText();
            log.setIme(ime);
            log.setPassword(pass);
            try {
                korisnik = m.izvuciKorisnika(ime, pass, korisnik);
            } catch (SQLException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Provera da li da se iscrta adminBtn na osnovu privilegija accounta
            if(korisnik.isAdmin() == 1) {
                adminBtn.setManaged(true);
            } else {
                adminBtn.setManaged(false);
            }
            if(log.ulogujSe(m, korisnik)) {
                //Reset login polja nakon login-a da budu prazna nakon logout-a
                usernameTextField.clear();
                passwordTextField.clear();
                LogInStage.close();
                primaryStage.show();
            } else {
                Label logGreska = new Label("Pogresan Username ili Password.");
                logGreska.setTextFill(Color.RED);
                grid.add(logGreska, 1, 5);
              }
        });
    }
    
    public void setupAdminWindow(VBox leftMenu, Stage primaryStage, Scene mainScene, Stage adminStage) {
        //Generisem novi VBox, BorderPane i Scene kako bih zamenio celu scenu
        //na primaryStage-u bez brisanja elemenata sa prosle scene
        adminLayout = new BorderPane();
        Scene adminScene = new Scene(adminLayout, 1024, 768);
        VBox adminLeftMenu = new VBox();
        
        VBox generisaniTextFieldovi = new VBox(8);
        generisaniTextFieldovi.setSpacing(8);
        generisaniTextFieldovi.setPadding(new Insets (10,25,10,0));
        
        TextField novaVrednostKolone = new TextField();
        novaVrednostKolone.setPromptText("Unesi novu vrednost");
        
        TextField updateTextField = new TextField();
        
        Button nazad = new Button("Nazad");
        nazad.setOnAction( e -> {
            primaryStage.setScene(mainScene);
            adminStage.close();
            primaryStage.show();
        });
        
        ComboBox sqlFunkcija = new ComboBox();
        sqlFunkcija.setPromptText("Izaberi funkciju");
        sqlFunkcija.getItems().addAll("insert", "update", "delete");
        
        ComboBox tabelaZaEditovanje = new ComboBox();
        tabelaZaEditovanje.setPromptText("Izaberi tabelu");
        
        ComboBox kolonaZaEditovanje = new ComboBox();
        kolonaZaEditovanje.setPromptText("Izaberi kolonu");
        
        ComboBox polComboBox = new ComboBox();
        polComboBox.setPromptText("Izaberi pol");
        polComboBox.getItems().addAll("Muzjak", "Zenka");
        
        ComboBox rasaComboBox = new ComboBox();
        rasaComboBox.setPromptText("Izaberi rasu");
        
        ComboBox korisnikID = new ComboBox();
        korisnikID.setPromptText("Izaberi ID");
        
        ComboBox brojMarkiceZivotinjaIMladunaca = new ComboBox();
        korisnikID.setPromptText("Izaberi ID");
        
        // TextFieldovi za korisnika
        TextField imeTextField = new TextField();
        imeTextField.setPromptText("Unesi ime");
        TextField passwordTextField = new TextField();
        passwordTextField.setPromptText("Unesi password");
        TextField isAdminTextField = new TextField();
        isAdminTextField.setPromptText("Unesi 1 za admin");
        
        // TextFieldovi za zivotinje
        TextField brojMarkiceTextField = new TextField();
        brojMarkiceTextField.setPromptText("Unesi broj markice");
        TextField brojPotomakaTextField = new TextField();
        brojPotomakaTextField.setPromptText("Unesi broj potomaka");
        TextField datumRodjenjaTextField = new TextField();
        datumRodjenjaTextField.setPromptText("d.m.gggg.");
        
        // TextFieldovi za mladunce zivotinja
        TextField brojMarkicePotomkaTextField = new TextField();
        brojMarkicePotomkaTextField.setPromptText("Unesi broj markice");
        TextField z_brojMarkiceTextField = new TextField();
        z_brojMarkiceTextField.setPromptText("Unesi z_brojMarkice");
        TextField m_brojMarkiceTextField = new TextField();
        m_brojMarkiceTextField.setPromptText("Unesi m_brojMarkice");
        TextField datumRodjenjaPotomkaTextField = new TextField();
        datumRodjenjaPotomkaTextField.setPromptText("d.m.gggg.");
        
        Label adminQuerySuccess = new Label("Query je uspesno izvrsen.");
        adminQuerySuccess.setTextFill(Color.GREEN);
        Label adminQueryFail = new Label("Query nije uspesno izvrsen.");
        adminQueryFail.setTextFill(Color.RED);
        
        Button izvrsiQuery = new Button("Izvrsi query");
        izvrsiQuery.setDefaultButton(true);
        adminIzvrsiQuery(izvrsiQuery,
                        sqlFunkcija,
                        tabelaZaEditovanje,
                        kolonaZaEditovanje,
                        polComboBox,
                        rasaComboBox,
                        imeTextField,
                        passwordTextField,
                        isAdminTextField,
                        brojMarkiceTextField,
                        brojPotomakaTextField,
                        datumRodjenjaTextField,
                        brojMarkicePotomkaTextField,
                        z_brojMarkiceTextField,
                        m_brojMarkiceTextField,
                        datumRodjenjaPotomkaTextField,
                        adminQuerySuccess,
                        adminQueryFail,
                        adminLeftMenu,
                        updateTextField,
                        korisnikID,
                        brojMarkiceZivotinjaIMladunaca);
        
        Button adminLeftMenuReset = new Button("Reset");
        adminLeftMenuReset.setOnAction( e -> {
        adminLeftMenu.getChildren().removeAll(adminLeftMenu.getChildren());
        adminLeftMenu.getChildren().addAll(nazad,
                                       sqlFunkcija,
                                       tabelaZaEditovanje);
        sqlFunkcija.setValue(null);
        tabelaZaEditovanje.setValue(null);
        polComboBox.setValue(null);
        rasaComboBox.setValue(null);
        imeTextField.clear();
        passwordTextField.clear();
        isAdminTextField.clear();
        brojMarkiceTextField.clear();
        brojPotomakaTextField.clear();
        datumRodjenjaTextField.clear();
        brojMarkicePotomkaTextField.clear();
        z_brojMarkiceTextField.clear();
        m_brojMarkiceTextField.clear();
        datumRodjenjaPotomkaTextField.clear();
        });
        
        HBox adminSceneButtons = new HBox(30);
        adminSceneButtons.getChildren().addAll(izvrsiQuery, adminLeftMenuReset);
        
        tabelaZaEditovanje.getItems().addAll("korisnici",
                                             "koza",
                                             "krava",
                                             "konj",
                                             "potomak_koza",
                                             "potomak_krava",
                                             "potomak_konj");
        
        adminLeftMenu.setSpacing(8);
        adminLeftMenu.setPadding(new Insets (25,25,25,25));
        adminLeftMenu.getChildren().addAll(nazad,
                                           sqlFunkcija,
                                           tabelaZaEditovanje);
        adminLayout.setLeft(adminLeftMenu);
        adminStage.setScene(adminScene);
        
        sqlFunkcija.valueProperty().addListener((observable, oldItem, newItem) -> {
            // Resetovanje prikazanih elemenata nakon promene sql funkcije
            if(oldItem == "update" && oldItem != newItem) {
                if(adminLeftMenu.getChildren().contains(brojMarkiceZivotinjaIMladunaca)) {
                    adminLeftMenu.getChildren().remove(brojMarkiceZivotinjaIMladunaca);
                }
                if(adminLeftMenu.getChildren().contains(korisnikID)) {
                    adminLeftMenu.getChildren().remove(korisnikID);
                }
                kolonaZaEditovanje.setValue(null);
                generisaniTextFieldovi.getChildren().removeAll(generisaniTextFieldovi.getChildren());
                adminLeftMenu.getChildren().removeAll(kolonaZaEditovanje, generisaniTextFieldovi, adminSceneButtons);
                if(adminLeftMenu.getChildren().contains(adminQuerySuccess)) {
                    adminLeftMenu.getChildren().remove(adminQuerySuccess);
                } else if(adminLeftMenu.getChildren().contains(adminQueryFail)) {
                    adminLeftMenu.getChildren().remove(adminQueryFail);
                }
                tabelaZaEditovanje.setValue(null);
                brisanjeTabelaPriPromeniSQLFunkcije();
            } else if(oldItem == "insert" && oldItem != newItem) {
                generisaniTextFieldovi.getChildren().removeAll(generisaniTextFieldovi.getChildren());
                adminLeftMenu.getChildren().removeAll(generisaniTextFieldovi, adminSceneButtons);
                if(adminLeftMenu.getChildren().contains(adminQuerySuccess)) {
                    adminLeftMenu.getChildren().remove(adminQuerySuccess);
                } else if(adminLeftMenu.getChildren().contains(adminQueryFail)) {
                    adminLeftMenu.getChildren().remove(adminQueryFail);
                }
                tabelaZaEditovanje.setValue(null);
                brisanjeTabelaPriPromeniSQLFunkcije();
            } else if(oldItem == "delete" && oldItem != newItem) {
                generisaniTextFieldovi.getChildren().removeAll(generisaniTextFieldovi.getChildren());
                adminLeftMenu.getChildren().removeAll(updateTextField, adminSceneButtons);
                if(adminLeftMenu.getChildren().contains(adminQuerySuccess)) {
                    adminLeftMenu.getChildren().remove(adminQuerySuccess);
                } else if(adminLeftMenu.getChildren().contains(adminQueryFail)) {
                    adminLeftMenu.getChildren().remove(adminQueryFail);
                }
                tabelaZaEditovanje.setValue(null);
                brisanjeTabelaPriPromeniSQLFunkcije();
            }
        });
        
        tabelaZaEditovanje.valueProperty().addListener((observable, oldItem, newItem) -> {
            if(null != (String)sqlFunkcija.getValue()) {
                rasaComboBox.getItems().clear();
                if(adminLeftMenu.getChildren().contains(updateTextField)) {
                    adminLeftMenu.getChildren().remove(updateTextField);
                }
                if(adminLeftMenu.getChildren().contains(korisnikID)) {
                    adminLeftMenu.getChildren().remove(korisnikID);
                }
                switch ((String)sqlFunkcija.getValue()) {
                    case "insert":
                        // Odstranjivanje elemenata kako bi mogli ponovo da se unesu ukoliko je izabrana druga tabela
                        if(!generisaniTextFieldovi.getChildren().isEmpty()) {
                            generisaniTextFieldovi.getChildren().removeAll(generisaniTextFieldovi.getChildren());
                        }
                        if(generisaniTextFieldovi.getChildren().isEmpty()) {
                            adminLeftMenu.getChildren().removeAll(generisaniTextFieldovi, adminSceneButtons);
                            if(adminLeftMenu.getChildren().contains(adminQuerySuccess)) {
                                adminLeftMenu.getChildren().remove(adminQuerySuccess);
                            } else if(adminLeftMenu.getChildren().contains(adminQueryFail)) {
                                adminLeftMenu.getChildren().remove(adminQueryFail);
                            }
                        }
                        if(tabelaZaEditovanje.getValue() != null) {
                            switch((String)newItem) {
                                case "koza":
                                case "potomak_koza":
                                    rasaComboBox.getItems().addAll(
                                            "Alpina",
                                            "Patuljasta");
                                    break;
                                case "krava":
                                case "potomak_krava":
                                    rasaComboBox.getItems().addAll(
                                            "GreasyMilk",
                                            "PhatTits"
                                    );
                                    break;
                                case "konj":
                                case "potomak_konj":
                                    rasaComboBox.getItems().addAll(
                                            "Lipicaner",
                                            "Englski Galoper",
                                            "Pink Unicorn Pony"
                                    );
                                    break;
                            }
                        }
                        
                        String textFieldGeneratorString;
                        // Generalizovanje generisanja text fieldova za tabele koje su istog sadrzaja
                        if(newItem != null) {
                            loadTableOnTableSelected(tabelaZaEditovanje);
                            switch ((String)newItem) {
                                case "potomak_koza":
                                case "potomak_krava":
                                case "potomak_konj":
                                    adminLayout.setCenter(tableMladunci);
                                    textFieldGeneratorString = "potomak";
                                    break;
                                case "koza":
                                case "krava":
                                case "konj":
                                    adminLayout.setCenter(table);
                                    textFieldGeneratorString = "zivotinja";
                                    break;
                                default:
                                    try {
                                        tableKorisnici.setItems(getKorisnici());
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    adminLayout.setCenter(tableKorisnici);
                                    textFieldGeneratorString = "korisnici";
                                    break;
                            }
                            
                            switch(textFieldGeneratorString) {
                                case "korisnici":
                                    generisaniTextFieldovi.getChildren().addAll(imeTextField, passwordTextField, isAdminTextField);
                                    break;
                                case "zivotinja":
                                    generisaniTextFieldovi.getChildren().addAll(brojMarkiceTextField,
                                            polComboBox,
                                            rasaComboBox,
                                            brojPotomakaTextField,
                                            datumRodjenjaTextField);
                                    break;
                                case "potomak":
                                    generisaniTextFieldovi.getChildren().addAll(brojMarkicePotomkaTextField,
                                            polComboBox,
                                            rasaComboBox,
                                            z_brojMarkiceTextField,
                                            m_brojMarkiceTextField,
                                            datumRodjenjaPotomkaTextField);
                                    break;
                            }
                            adminLeftMenu.getChildren().addAll(generisaniTextFieldovi, adminSceneButtons);
                        }
                    break;
                    case "update":
                        if(tabelaZaEditovanje.getValue() != null) {
                            switch((String)newItem) {
                                case "koza":
                                case "potomak_koza":
                                    rasaComboBox.getItems().addAll(
                                            "Alpina",
                                            "Patuljasta");
                                    break;
                                case "krava":
                                case "potomak_krava":
                                    rasaComboBox.getItems().addAll(
                                            "GreasyMilk",
                                            "PhatTits"
                                    );
                                    break;
                                case "konj":
                                case "potomak_konj":
                                    rasaComboBox.getItems().addAll(
                                            "Lipicaner",
                                            "Englski Galoper",
                                            "Pink Unicorn Pony"
                                    );
                                    break;
                            }
                            loadTableOnTableSelected(tabelaZaEditovanje);
                        if(!generisaniTextFieldovi.getChildren().isEmpty()) {
                            generisaniTextFieldovi.getChildren().removeAll(generisaniTextFieldovi.getChildren());
                        }
                        if(generisaniTextFieldovi.getChildren().isEmpty()) {
                            adminLeftMenu.getChildren().removeAll(generisaniTextFieldovi, adminSceneButtons);
                            if(adminLeftMenu.getChildren().contains(adminQuerySuccess)) {
                                adminLeftMenu.getChildren().remove(adminQuerySuccess);
                            } else if(adminLeftMenu.getChildren().contains(adminQueryFail)) {
                                adminLeftMenu.getChildren().remove(adminQueryFail);
                            }
                        }
                        
                        switch((String)tabelaZaEditovanje.getValue()) {
                                case "potomak_koza":
                                case "potomak_krava":
                                case "potomak_konj":
                                    if(!brojMarkiceZivotinjaIMladunaca.getItems().isEmpty()) {
                                        brojMarkiceZivotinjaIMladunaca.getItems().clear();
                                    }
                                    try {
                                        m.setIsMladunac(true);
                                        sveZivotinje = m.izvuciZivotinje("","","");
                                        for(Zivotinje zivotinjaItem : sveZivotinje) {
                                            brojMarkiceZivotinjaIMladunaca.getItems().add(zivotinjaItem.getBrojMarkice());
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    if(adminLeftMenu.getChildren().contains(korisnikID)) {
                                        adminLeftMenu.getChildren().remove(korisnikID);
                                    }
                                    if(adminLeftMenu.getChildren().contains(brojMarkiceZivotinjaIMladunaca)) {
                                        adminLeftMenu.getChildren().remove(brojMarkiceZivotinjaIMladunaca);
                                    }
                                    brojMarkiceZivotinjaIMladunaca.setPromptText("Izaberi broj markice.");
                                    adminLeftMenu.getChildren().add(brojMarkiceZivotinjaIMladunaca);
                                    generisaniTextFieldovi.getChildren().addAll(brojMarkicePotomkaTextField,
                                                                                polComboBox,
                                                                                rasaComboBox,
                                                                                z_brojMarkiceTextField,
                                                                                m_brojMarkiceTextField,
                                                                                datumRodjenjaPotomkaTextField);
                                    adminLeftMenu.getChildren().addAll(generisaniTextFieldovi, adminSceneButtons);
                                    adminLayout.setCenter(tableMladunci);
                                    break;
                                case "koza":
                                case "krava":
                                case "konj":
                                    if(!brojMarkiceZivotinjaIMladunaca.getItems().isEmpty()) {
                                        brojMarkiceZivotinjaIMladunaca.getItems().clear();
                                    }
                                    try {
                                        String zivotinjaString = (String)tabelaZaEditovanje.getValue();
                                        String prvoSlovo = zivotinjaString.substring(0, 1).toUpperCase();
                                        String capitalized = prvoSlovo + zivotinjaString.substring(1);
                                        m.setIzabranaZivotinja(capitalized);
                                        sveZivotinje = m.izvuciZivotinje("","","");
                                        for(Zivotinje zivotinjaItem : sveZivotinje) {
                                            brojMarkiceZivotinjaIMladunaca.getItems().add(zivotinjaItem.getBrojMarkice());
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    if(adminLeftMenu.getChildren().contains(korisnikID)) {
                                        adminLeftMenu.getChildren().remove(korisnikID);
                                    }
                                    if(adminLeftMenu.getChildren().contains(brojMarkiceZivotinjaIMladunaca)) {
                                        adminLeftMenu.getChildren().remove(brojMarkiceZivotinjaIMladunaca);
                                    }
                                    brojMarkiceZivotinjaIMladunaca.setPromptText("Izaberi broj markice.");
                                    adminLeftMenu.getChildren().add(brojMarkiceZivotinjaIMladunaca);
                                    generisaniTextFieldovi.getChildren().addAll(brojMarkiceTextField,
                                                                                polComboBox,
                                                                                rasaComboBox,
                                                                                brojPotomakaTextField,
                                                                                datumRodjenjaTextField);
                                    adminLeftMenu.getChildren().addAll(generisaniTextFieldovi, adminSceneButtons);
                                    adminLayout.setCenter(table);
                                    break;
                                case "korisnici":
                                    try {
                                        if(adminLeftMenu.getChildren().contains(brojMarkiceZivotinjaIMladunaca)) {
                                            adminLeftMenu.getChildren().remove(brojMarkiceZivotinjaIMladunaca);
                                        }
                                        try {
                                            korisnikID.getItems().clear();
                                            sviKorisnici = m.izvuciSveKorisnike();
                                            for(Korisnici korisnikItem : sviKorisnici) {
                                                korisnikID.getItems().add(korisnikItem.getId());
                                            }
                                        } catch (SQLException ex) {
                                            Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        adminLeftMenu.getChildren().add(korisnikID);
                                        generisaniTextFieldovi.getChildren().addAll(imeTextField,
                                                                                    passwordTextField,
                                                                                    isAdminTextField);
                                        adminLeftMenu.getChildren().addAll(generisaniTextFieldovi, adminSceneButtons);
                                        tableKorisnici.setItems(getKorisnici());
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    adminLayout.setCenter(tableKorisnici);
                                    break;
                            }
                        }
                    break;
                    case "delete":
                        if(tabelaZaEditovanje.getValue() != null) {
                            if(adminLeftMenu.getChildren().contains(brojMarkiceZivotinjaIMladunaca)) {
                                adminLeftMenu.getChildren().remove(brojMarkiceZivotinjaIMladunaca);
                            }
                            if(newItem != null && !adminLeftMenu.getChildren().contains(updateTextField)) {
                                adminLeftMenu.getChildren().removeAll(updateTextField, adminSceneButtons);
                            }
                            
                            loadTableOnTableSelected(tabelaZaEditovanje);
                            switch((String)tabelaZaEditovanje.getValue()) {
                                case "potomak_koza":
                                case "potomak_krava":
                                case "potomak_konj":
                                    updateTextField.setPromptText("Unesi broj markice.");
                                    adminLeftMenu.getChildren().add(updateTextField);
                                    if(newItem != null && !adminLeftMenu.getChildren().contains(adminSceneButtons)) {
                                        adminLeftMenu.getChildren().addAll(adminSceneButtons);
                                    }
                                    adminLayout.setCenter(tableMladunci);
                                    break;
                                case "koza":
                                case "krava":
                                case "konj":
                                    updateTextField.setPromptText("Unesi broj markice.");
                                    adminLeftMenu.getChildren().addAll(updateTextField);
                                    if(newItem != null && !adminLeftMenu.getChildren().contains(adminSceneButtons)) {
                                        adminLeftMenu.getChildren().add(adminSceneButtons);
                                    }
                                    adminLayout.setCenter(table);
                                    break;
                                case "korisnici":
                                    try {
                                        if(korisnikID.getItems().isEmpty()) {
                                            try {
                                                sviKorisnici = m.izvuciSveKorisnike();
                                                for(Korisnici korisnikItem : sviKorisnici) {
                                                    korisnikID.getItems().add(korisnikItem.getId());
                                                }
                                            } catch (SQLException ex) {
                                                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                        adminLeftMenu.getChildren().addAll(korisnikID);
                                        if(newItem != null && !adminLeftMenu.getChildren().contains(adminSceneButtons)) {
                                            adminLeftMenu.getChildren().add(adminSceneButtons);
                                        }
                                        tableKorisnici.setItems(getKorisnici());
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    adminLayout.setCenter(tableKorisnici);
                                break;
                            }
                        }
                    break;
                    default:
                    break;
                }
            }
        });
        
        kolonaZaEditovanje.valueProperty().addListener((observable, oldItem, newItem) -> {
            generisaniTextFieldovi.getChildren().removeAll(generisaniTextFieldovi.getChildren());
            adminLeftMenu.getChildren().removeAll(generisaniTextFieldovi, adminSceneButtons);
            if(adminLeftMenu.getChildren().contains(adminQuerySuccess)) {
                adminLeftMenu.getChildren().remove(adminQuerySuccess);
            } else if(adminLeftMenu.getChildren().contains(adminQueryFail)) {
                adminLeftMenu.getChildren().remove(adminQueryFail);
            }
        });
        
        // TABELA ZA EDITOVANJE COMBO BOX I FUNKCIONALNOST SA DEFINICIJOM KOLONA ZA EDITOVANJE COMBO BOX
        tabelaZaEditovanje.setOnAction( e -> {
            // Provera zbog zastite od null exception-a nakon reseta elemenata
            if(tabelaZaEditovanje.getValue() != null) {
                m.setIzabranaTabelaZaEditovanje((String)tabelaZaEditovanje.getValue());
                kolonaZaEditovanje.getItems().clear();
                // Generisanje elemenata za kolone na osnovu izabrane tabele
                switch(m.getIzabranaTabelaZaEditovanje()) {
                    case "korisnici":
                        kolonaZaEditovanje.getItems().addAll("ime",
                                                            "prezime",
                                                            "admin");
                        
                            kolonaZaEditovanje.setOnAction( a -> {
                            m.setKolonaZaEditovanje((String)kolonaZaEditovanje.getValue());
                        });
                    break;
                    
                    case "koza":
                        kolonaZaEditovanje.getItems().addAll("brojMarkice",
                                                            "pol",
                                                            "rasa",
                                                            "brojPotomaka",
                                                            "datumRodjenja");
                        
                            kolonaZaEditovanje.setOnAction( a -> {
                            m.setKolonaZaEditovanje((String)kolonaZaEditovanje.getValue());
                        });
                    break;
                    
                    case "potomak_koza":
                        kolonaZaEditovanje.getItems().addAll("brojMarkice",
                                                            "pol",
                                                            "rasa",
                                                            "z_brojMarkice",
                                                            "m_brojMarkice",
                                                            "datumRodjenja");
                        
                            kolonaZaEditovanje.setOnAction( a -> {
                            m.setKolonaZaEditovanje((String)kolonaZaEditovanje.getValue());
                        });
                    break;
                }
            }
        });
    }
    
    public void brisanjeTabelaPriPromeniSQLFunkcije() {
        if(adminLayout.getChildren().contains(table)) {
            adminLayout.getChildren().remove(table);
        } else  if(adminLayout.getChildren().contains(tableMladunci)){
            adminLayout.getChildren().remove(tableMladunci);
        } else {
            adminLayout.getChildren().remove(tableKorisnici);
        }
    }
    
    public void loadTableOnTableSelected(ComboBox tabelaZaEditovanje) {
        switch((String)tabelaZaEditovanje.getValue()) {
            case "koza":
                try {
                    m.setIzabranaZivotinja("Koza");
                    m.setIsMladunac(false);
                    table.setItems(getZivotinje("", "", ""));
                } catch (SQLException ex) {
                    Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                }
            break;

            case "krava":
                try {
                    m.setIzabranaZivotinja("Krava");
                    m.setIsMladunac(false);
                    table.setItems(getZivotinje("", "", ""));
                } catch (SQLException ex) {
                    Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                }
            break;

            case "konj":
                try {
                    m.setIzabranaZivotinja("Konj");
                    m.setIsMladunac(false);
                    table.setItems(getZivotinje("", "", ""));
                } catch (SQLException ex) {
                    Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                }
            break;

            case "potomak_koza":
                try {
                    m.setIzabranaZivotinja("Koza");
                    m.setIsMladunac(true);
                    tableMladunci.setItems(getZivotinje("", "", ""));
                } catch (SQLException ex) {
                    Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                }
            break;    

            case "potomak_krava":
                try {
                    m.setIzabranaZivotinja("Krava");
                    m.setIsMladunac(true);
                    tableMladunci.setItems(getZivotinje("", "", ""));
                } catch (SQLException ex) {
                    Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                }
            break;   

            case "potomak_konj":
                try {
                    m.setIzabranaZivotinja("Konj");
                    m.setIsMladunac(true);
                    tableMladunci.setItems(getZivotinje("", "", ""));
                } catch (SQLException ex) {
                    Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                }
            break;    
        }
    }
    
    public void adminIzvrsiQuery(Button izvrsiQuery,
                                ComboBox sqlFunkcija,
                                ComboBox tabelaZaEditovanje,
                                ComboBox kolonaZaEditovanje,
                                ComboBox polComboBox,
                                ComboBox rasaComboBox,
                                TextField imeTextField,
                                TextField passwordTextField,
                                TextField isAdminTextField,
                                TextField brojMarkiceTextField,
                                TextField brojPotomakaTextField,
                                TextField datumRodjenjaTextField,
                                TextField brojMarkicePotomkaTextField,
                                TextField z_brojMarkiceTextField,
                                TextField m_brojMarkiceTextField,
                                TextField datumRodjenjaPotomkaTextField,
                                Label adminQuerySuccess,
                                Label adminQueryFail,
                                VBox adminLeftMenu,
                                TextField updateTextField,
                                ComboBox korisnikID,
                                ComboBox brojMarkiceZivotinjaIMladunaca) {
        
        izvrsiQuery.setOnAction( e -> {
            if(adminLeftMenu.getChildren().contains(adminQuerySuccess)) {
                adminLeftMenu.getChildren().remove(adminQuerySuccess);
            } else if(adminLeftMenu.getChildren().contains(adminQueryFail)) {
                adminLeftMenu.getChildren().remove(adminQueryFail);
            }
            switch((String)sqlFunkcija.getValue()) {
                case "insert":
                    String zivotinjaZaUnos = (String)tabelaZaEditovanje.getValue();
                    String pol = (String)polComboBox.getValue();
                    String rasa = (String)rasaComboBox.getValue();
                    switch((String)tabelaZaEditovanje.getValue()) {
                        case "korisnici":
                            try {
                                if(m.adminUnesiKorisnika(imeTextField.getText(),
                                                        passwordTextField.getText(),
                                                        isAdminTextField.getText())) {
                                    adminLeftMenu.getChildren().add(adminQuerySuccess);
                                    imeTextField.clear();
                                    passwordTextField.clear();
                                    isAdminTextField.clear();
                                    tableKorisnici.setItems(getKorisnici());
                                } else {
                                    adminLeftMenu.getChildren().add(adminQueryFail);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        break;
                        
                        case "koza":
                        case "krava":
                        case "konj":
                            try {
                                if(m.unesiZivotinju(brojMarkiceTextField.getText(),
                                                    pol,
                                                    rasa,
                                                    brojPotomakaTextField.getText(),
                                                    datumRodjenjaTextField.getText(),
                                                    zivotinjaZaUnos)) {
                                    adminLeftMenu.getChildren().add(adminQuerySuccess);
                                    brojMarkiceTextField.clear();
                                    polComboBox.setValue(null);
                                    rasaComboBox.setValue(null);
                                    brojPotomakaTextField.clear();
                                    datumRodjenjaTextField.clear();
                                    table.setItems(getZivotinje("", "", ""));
                                } else {
                                    adminLeftMenu.getChildren().add(adminQueryFail);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        break;
                        
                        case "potomak_koza":
                        case "potomak_krava":
                        case "potomak_konj":
                            try {
                                if(m.unesiMladunca(zivotinjaZaUnos,
                                                    brojMarkicePotomkaTextField.getText(),
                                                    pol,
                                                    rasa,
                                                    z_brojMarkiceTextField.getText(),
                                                    m_brojMarkiceTextField.getText(),
                                                    datumRodjenjaPotomkaTextField.getText())) {
                                    adminLeftMenu.getChildren().add(adminQuerySuccess);
                                    brojMarkicePotomkaTextField.clear();
                                    polComboBox.setValue(null);
                                    rasaComboBox.setValue(null);
                                    z_brojMarkiceTextField.clear();
                                    m_brojMarkiceTextField.clear();
                                    datumRodjenjaPotomkaTextField.clear();
                                    tableMladunci.setItems(getZivotinje("", "", ""));
                                } else {
                                    adminLeftMenu.getChildren().add(adminQueryFail);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        break;    
                    }
                break;
                
                case "update":
                    pol = (String)polComboBox.getValue();
                    rasa = (String)rasaComboBox.getValue();
                    switch((String)tabelaZaEditovanje.getValue()) {
                        case "korisnici":
                            try {
                                if(m.updateujKorisnika(imeTextField.getText(),
                                                        passwordTextField.getText(),
                                                        isAdminTextField.getText(),
                                                        (int)korisnikID.getValue())) {
                                    m.resetForeignKeyChecksAfterUpdate();
                                    adminLeftMenu.getChildren().add(adminQuerySuccess);
                                    imeTextField.clear();
                                    passwordTextField.clear();
                                    isAdminTextField.clear();
                                    if(korisnikID.getItems().isEmpty()) {
                                        try {
                                            sviKorisnici = m.izvuciSveKorisnike();
                                            for(Korisnici korisnikItem : sviKorisnici) {
                                                korisnikID.getItems().add(korisnikItem.getId());
                                            }
                                        } catch (SQLException ex) {
                                            Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    tableKorisnici.setItems(getKorisnici());
                                } else {
                                    adminLeftMenu.getChildren().add(adminQueryFail);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        break;
                        case "koza":
                        case "krava":
                        case "konj":
                            try {
                                if(m.updateujZivotinju(brojMarkiceTextField.getText(),
                                                    pol,
                                                    rasa,
                                                    brojPotomakaTextField.getText(),
                                                    datumRodjenjaTextField.getText(),
                                                    (String)tabelaZaEditovanje.getValue(),
                                                    (String)brojMarkiceZivotinjaIMladunaca.getValue())) {
                                    m.resetForeignKeyChecksAfterUpdate();
                                    adminLeftMenu.getChildren().add(adminQuerySuccess);
                                    brojMarkiceTextField.clear();
                                    polComboBox.setValue(null);
                                    rasaComboBox.setValue(null);
                                    brojPotomakaTextField.clear();
                                    datumRodjenjaTextField.clear();
                                    if(!brojMarkiceZivotinjaIMladunaca.getItems().isEmpty()) {
                                        brojMarkiceZivotinjaIMladunaca.getItems().clear();
                                    }
                                    try {
                                        String zivotinjaString = (String)tabelaZaEditovanje.getValue();
                                        String prvoSlovo = zivotinjaString.substring(0, 1).toUpperCase();
                                        String capitalized = prvoSlovo + zivotinjaString.substring(1);
                                        m.setIzabranaZivotinja(capitalized);
                                        sveZivotinje = m.izvuciZivotinje("","","");
                                        for(Zivotinje zivotinjaItem : sveZivotinje) {
                                            brojMarkiceZivotinjaIMladunaca.getItems().add(zivotinjaItem.getBrojMarkice());
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    table.setItems(getZivotinje("","",""));
                                } else {
                                    adminLeftMenu.getChildren().add(adminQueryFail);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        break;
                        case "potomak_koza":
                        case "potomak_krava":
                        case "potomak_konj":
                            try {
                                if(m.updateujMladunca(brojMarkicePotomkaTextField.getText(),
                                                    pol,
                                                    rasa,
                                                    z_brojMarkiceTextField.getText(),
                                                    m_brojMarkiceTextField.getText(),
                                                    datumRodjenjaPotomkaTextField.getText(),
                                                    (String)tabelaZaEditovanje.getValue(),
                                                    (String)brojMarkiceZivotinjaIMladunaca.getValue())) {
                                    m.resetForeignKeyChecksAfterUpdate();
                                    adminLeftMenu.getChildren().add(adminQuerySuccess);
                                    brojMarkicePotomkaTextField.clear();
                                    polComboBox.setValue(null);
                                    rasaComboBox.setValue(null);
                                    z_brojMarkiceTextField.clear();
                                    m_brojMarkiceTextField.clear();
                                    datumRodjenjaPotomkaTextField.clear();
                                     if(!brojMarkiceZivotinjaIMladunaca.getItems().isEmpty()) {
                                        brojMarkiceZivotinjaIMladunaca.getItems().clear();
                                    }
                                    try {
                                        m.setIsMladunac(true);
                                        sveZivotinje = m.izvuciZivotinje("","","");
                                        for(Zivotinje zivotinjaItem : sveZivotinje) {
                                            brojMarkiceZivotinjaIMladunaca.getItems().add(zivotinjaItem.getBrojMarkice());
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    tableMladunci.setItems(getZivotinje("","",""));
                                } else {
                                    adminLeftMenu.getChildren().add(adminQueryFail);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        break;
                    }
                break;
                
                case "delete":
                    switch((String)tabelaZaEditovanje.getValue()) {
                        case "korisnici":
                            try {
                                if(m.obrisiKorisnika((int)korisnikID.getValue())) {
                                    adminLeftMenu.getChildren().add(adminQuerySuccess);
                                    //updateTextField.clear();
                                    tableKorisnici.setItems(getKorisnici());
                                } else {
                                    adminLeftMenu.getChildren().add(adminQueryFail);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        break;
                        case "koza":
                        case "krava":
                        case "konj":
                            try {
                                if(m.obrisiZivotinju(updateTextField.getText(),
                                                    (String)tabelaZaEditovanje.getValue())) {
                                    adminLeftMenu.getChildren().add(adminQuerySuccess);
                                    updateTextField.clear();
                                    table.setItems(getZivotinje(updateTextField.getText(),"",""));
                                } else {
                                    adminLeftMenu.getChildren().add(adminQueryFail);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        break;
                        case "potomak_koza":
                        case "potomak_krava":
                        case "potomak_konj":
                            try {
                                if(m.obrisiMladunca(updateTextField.getText(),
                                                    (String)tabelaZaEditovanje.getValue())) {
                                    adminLeftMenu.getChildren().add(adminQuerySuccess);
                                    updateTextField.clear();
                                    tableMladunci.setItems(getZivotinje(updateTextField.getText(),"",""));
                                } else {
                                    adminLeftMenu.getChildren().add(adminQueryFail);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        break;
                    }
                break;
            }
        });
    }
    
    public void setupMainMenuWindow(BorderPane layout, VBox leftMenu, Stage primaryStage, Stage adminStage, Stage LogInStage, Scene logScene) throws FileNotFoundException, IOException {
        //MAIN MENU START
        
        HBox logOutAndAdminButtonHBox = new HBox();
        logOutAndAdminButtonHBox.setSpacing(105);
        //logOutAndAdminButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        logOutAndAdminButtonHBox.setPadding(new Insets (0,0,25,0));
        
        Button logOut = new Button("Log out");
        logOut.setOnAction( e -> {
            LogInStage.setScene(logScene);
            primaryStage.close();
            LogInStage.show();
        });
        
        ComboBox rasaComboBox = new ComboBox<>();
        rasaComboBox.setPromptText("Izaber rasu");
        ComboBox vrstaZivotinjeComboBox = new ComboBox<>();
        CheckBox mladunciCheckBox = new CheckBox("Mladunci");
        mladunciCheckBox.allowIndeterminateProperty();
        mladunciCheckBox.setSelected(false);
        vrstaZivotinjeComboBox.setPromptText("Izaberi zivotinju");
        vrstaZivotinjeComboBox.getItems().addAll(
            "Koza",
            "Krava",
            "Konj"
            );
        
        //-------------------------------------------------------------------------------------------------//
        
        // VRSTA ZIVOTINJE COMBO BOX I FUNKCIONALNOST SA DEFINICIJOM RASA COMBO BOX
        vrstaZivotinjeComboBox.setOnAction( e -> {
            // Provera zbog zastite od null exception-a nakon reseta elemenata
            if(vrstaZivotinjeComboBox.getValue() != null) {
                m.setIzabranaZivotinja((String)vrstaZivotinjeComboBox.getValue());
                rasaComboBox.getItems().clear();
                // Generisanje elemenata za rase na osnovu vrste zivotinje
                switch(m.getIzabranaZivotinja()) {
                    case "Koza":
                        rasaComboBox.getItems().addAll(
                            "Alpina",
                            "Patuljasta"
                            );
                            rasaComboBox.setOnAction( a -> {
                            m.setRasa((String)rasaComboBox.getValue());
                        });
                    break;

                    case "Krava":
                        rasaComboBox.getItems().addAll(
                            "GreasyMilk",
                            "PhatTits"
                            );
                            rasaComboBox.setOnAction( i -> {
                            m.setRasa((String)rasaComboBox.getValue());
                        });
                    break;

                    case "Konj":
                        rasaComboBox.getItems().addAll(
                            "Lipicaner",
                            "Englski Galoper",
                            "Pink Unicorn Pony"
                            );
                            rasaComboBox.setOnAction( o -> {
                            m.setRasa((String)rasaComboBox.getValue());
                        });
                    break;
                }
            }
        });
        
        //-------------------------------------------------------------------------------------------------//
        
        // POL COMBO BOX I FUNKCIONALNOST
        ComboBox polComboBox = new ComboBox<>();
        polComboBox.getItems().addAll(
            "Muzjak",
            "Zenka"
            );
        polComboBox.setPromptText("Izaberi pol");
        polComboBox.setOnAction( e -> {
            String pol = (String)polComboBox.getValue();
            m.setPol(pol);
        });
        
        //-------------------------------------------------------------------------------------------------//
        
        //Text field
        TextField brMarkiceTextField = new TextField();
        brMarkiceTextField.setPromptText("Unesi broj Markice");
        
        Label izaberiZivotinju = new Label("Izaberi vrstu zivotinje");
        izaberiZivotinju.setTextFill(Color.RED);
        
        TextField z_brojMarkiceTextField = new TextField();
        z_brojMarkiceTextField.setPromptText("Unesi broj markice majke");
        
        TextField m_brojMarkiceTextField = new TextField();
        m_brojMarkiceTextField.setPromptText("Unesi broj markice oca");
        
        //------------------------------------POZIV NA FUNKCIJU ZA PRIKAZ ADMIN STRANICE------------------------------------------//
        
        adminBtn = new Button("Admin");
        adminBtn.setOnAction( e -> {
            
            // Brisanje tabele sa MainMenu scene kako bi se mogla prikazati na
            // admin sceni i promena scene
            primaryStage.close();
            layout.getChildren().remove(table);
            //adminLayout.setCenter(table);
            adminStage.show();
            
        });
        
        //--------------------------------------QUERY BUTTON I POZIV NA FUNKCIJU ZA QUERY------------------------------------------------//
        
        Button queryBtn = new Button("Pretrazi");
        queryBtn.setDefaultButton(true);
        
        startQuery(queryBtn,
                   leftMenu,
                   brMarkiceTextField,
                   z_brojMarkiceTextField,
                   m_brojMarkiceTextField,
                   izaberiZivotinju,
                   layout);
       
        //--------------------------------------RESET BUTTON I POZIV NA FUNKCIJU ZA RESET ELEMENATA----------------------------------------------//
        
        Button resetBtn = new Button("Reset");
        
        resetAllElements(resetBtn,
                        vrstaZivotinjeComboBox,
                        mladunciCheckBox,
                        rasaComboBox,
                        polComboBox,
                        brMarkiceTextField,
                        z_brojMarkiceTextField,
                        m_brojMarkiceTextField,
                        layout,
                        leftMenu);
        
        //-------------------------------------------------------------------------------------------------//
        
        HBox vrstaZivotinjeIMladunciCheckBox = new HBox(20);
        vrstaZivotinjeIMladunciCheckBox.getChildren().add(vrstaZivotinjeComboBox);
        vrstaZivotinjeIMladunciCheckBox.getChildren().add(mladunciCheckBox);
        
        HBox queryResetBtns = new HBox(115);
        queryResetBtns.getChildren().add(queryBtn);
        queryResetBtns.getChildren().add(resetBtn);
        
        //------------------------------------CHECKBOX FUNKCIONALNOST MENJA SADRZAJ ZA PRETRAGU-----------------------------------------------//
        
        promeniSadrzajZaPretragu(mladunciCheckBox,
                                leftMenu,
                                polComboBox,
                                rasaComboBox,
                                brMarkiceTextField,
                                z_brojMarkiceTextField,
                                m_brojMarkiceTextField,
                                queryResetBtns);
        
        //-------------------------------------------------------------------------------------------------//
        
        // SASTAVLJANJE ELEMENATA
        leftMenu.setSpacing(8);
        leftMenu.setPadding(new Insets (25,25,25,25));
        logOutAndAdminButtonHBox.getChildren().addAll(logOut, adminBtn);
        leftMenu.getChildren().addAll(logOutAndAdminButtonHBox, vrstaZivotinjeIMladunciCheckBox, polComboBox, rasaComboBox, brMarkiceTextField, queryResetBtns);
        
        //MAIN MENU END
    }
    
    public void startQuery(Button queryBtn,
                           VBox leftMenu,
                           TextField brMarkiceTextField,
                           TextField z_brojMarkiceTextField,
                           TextField m_brojMarkiceTextField,
                           Label izaberiZivotinju,
                           BorderPane layout) {
        
        queryBtn.setOnAction( (ActionEvent e) -> {
            // Provera za slucaj kada nije izabrana zivotinja a upisana je markica
            // Tada i samo tada ispisuje samo markice svih mogucih zivotinja
            if("".equals(m.getIzabranaZivotinja()) && !"".equals(brMarkiceTextField.getText())) {
                leftMenu.getChildren().add(izaberiZivotinju);
            } else {
                try {
                    leftMenu.getChildren().remove(izaberiZivotinju);
                    if(m.isMladunac() == true) {
                        tableMladunci.setItems(getZivotinje(brMarkiceTextField.getText(), z_brojMarkiceTextField.getText(), m_brojMarkiceTextField.getText()));
                    } else {
                        table.setItems(getZivotinje(brMarkiceTextField.getText(), z_brojMarkiceTextField.getText(), m_brojMarkiceTextField.getText()));
                    }
            } catch (SQLException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
            }
                mojQuery(layout);
            }
        });
    }
    
    public void resetAllElements(Button resetBtn,
                                ComboBox vrstaZivotinjeComboBox,
                                CheckBox mladunciCheckBox,
                                ComboBox rasaComboBox,
                                ComboBox polComboBox,
                                TextField brMarkiceTextField,
                                TextField z_brojMarkiceTextField,
                                TextField m_brojMarkiceTextField,
                                BorderPane layout,
                                VBox leftMenu) {
        
        resetBtn.setOnAction( e -> {
            vrstaZivotinjeComboBox.setValue(null);
            mladunciCheckBox.setSelected(false);
            rasaComboBox.getItems().removeAll(rasaComboBox.getItems());
            polComboBox.setValue(null);
            m.setIzabranaZivotinja("");
            m.setRasa("");
            m.setPol("");
            brMarkiceTextField.setText("");
            z_brojMarkiceTextField.setText("");
            m_brojMarkiceTextField.setText("");
            table.getItems().clear();
            tableMladunci.getItems().clear();
            layout.setCenter(table);
            leftMenu.getChildren().removeAll(z_brojMarkiceTextField, m_brojMarkiceTextField);
        });
    }
    
    public void promeniSadrzajZaPretragu(CheckBox mladunciCheckBox,
                                        VBox leftMenu,
                                        ComboBox polComboBox,
                                        ComboBox rasaComboBox,
                                        TextField brMarkiceTextField,
                                        TextField z_brojMarkiceTextField,
                                        TextField m_brojMarkiceTextField,
                                        HBox queryResetBtns) {
        mladunciCheckBox.setOnAction( e -> {
            leftMenu.getChildren().removeAll(polComboBox, rasaComboBox, brMarkiceTextField, z_brojMarkiceTextField, m_brojMarkiceTextField, queryResetBtns);
            if(!mladunciCheckBox.isSelected()) {
                m.setIsMladunac(false);
                leftMenu.getChildren().addAll(polComboBox, rasaComboBox, brMarkiceTextField, queryResetBtns);
            } else {
                m.setIsMladunac(true);
                leftMenu.getChildren().addAll(polComboBox, rasaComboBox, brMarkiceTextField, z_brojMarkiceTextField, m_brojMarkiceTextField, queryResetBtns);
            }
        });
    }
    
    public void tableSetup() {
        //TABLE VIEW
        
        //korisnikov id
        TableColumn<Korisnici, String>id = new TableColumn("Id");
        id.setMinWidth(100);
        id.setCellValueFactory(new PropertyValueFactory("id"));
        
        //korisnikovo ime
        TableColumn<Korisnici, String>ime = new TableColumn("Ime");
        ime.setMinWidth(100);
        ime.setCellValueFactory(new PropertyValueFactory("ime"));
        
        //korisnikov password
        TableColumn<Korisnici, String>password = new TableColumn("Password");
        password.setMinWidth(100);
        password.setCellValueFactory(new PropertyValueFactory("password"));
        
        //korisnikov admin status
        TableColumn<Korisnici, String>isAdmin = new TableColumn("Admin");
        isAdmin.setMinWidth(100);
        isAdmin.setCellValueFactory(new PropertyValueFactory("admin"));
        
        //broj markice
        TableColumn<Zivotinje,String>brojMarkice = new TableColumn("Broj Markice");
        brojMarkice.setMinWidth(100);
        brojMarkice.setCellValueFactory(new PropertyValueFactory("brojMarkice"));
        
        //broj markice mladunca
        TableColumn<Zivotinje,String>brojMarkiceMladunca = new TableColumn("Broj Markice");
        brojMarkiceMladunca.setMinWidth(100);
        brojMarkiceMladunca.setCellValueFactory(new PropertyValueFactory("brojMarkice"));

        //pol
        TableColumn<Zivotinje,String>pol = new TableColumn("Pol");
        pol.setMinWidth(100);
        pol.setCellValueFactory(new PropertyValueFactory<>("pol"));
        
        //pol mladunca
        TableColumn<Zivotinje,String>polMladunca = new TableColumn("Pol");
        polMladunca.setMinWidth(100);
        polMladunca.setCellValueFactory(new PropertyValueFactory<>("pol"));

        //rasa
        TableColumn<Zivotinje,String>rasa = new TableColumn("Rasa");
        rasa.setMinWidth(100);
        rasa.setCellValueFactory(new PropertyValueFactory<>("rasa"));
        
        //rasa mladunca
        TableColumn<Zivotinje,String>rasaMladunca = new TableColumn("Rasa");
        rasaMladunca.setMinWidth(100);
        rasaMladunca.setCellValueFactory(new PropertyValueFactory<>("rasa"));

        //br potomaka
        TableColumn<Zivotinje,String>brojPotomaka = new TableColumn("Broj Potomaka");
        brojPotomaka.setMinWidth(100);
        brojPotomaka.setCellValueFactory(new PropertyValueFactory<>(("brojPotomaka")));

        //datum rodjenja
        TableColumn<Zivotinje,String>datumRodjenja = new TableColumn("Datum Rodjenja");
        datumRodjenja.setMinWidth(100);
        datumRodjenja.setCellValueFactory(new PropertyValueFactory<>("datumRodjenja"));
        
        //datum rodjenja mladunca
        TableColumn<Zivotinje,String>datumRodjenjaMladunca = new TableColumn("Datum Rodjenja");
        datumRodjenjaMladunca.setMinWidth(100);
        datumRodjenjaMladunca.setCellValueFactory(new PropertyValueFactory<>("datumRodjenja"));

        // z_brojMarkice
        TableColumn<Zivotinje,String>z_brojMarkice = new TableColumn("Z_brojMarkice");
        z_brojMarkice.setMinWidth(100);
        z_brojMarkice.setCellValueFactory(new PropertyValueFactory<>("z_brojMarkice"));

        // m_brojMarkice
        TableColumn<Zivotinje,String>m_brojMarkice = new TableColumn("M_brojMarkice");
        m_brojMarkice.setMinWidth(100);
        m_brojMarkice.setCellValueFactory(new PropertyValueFactory<>("m_brojMarkice"));

        table = new TableView<>();
        table.getColumns().addAll(brojMarkice, pol, rasa, brojPotomaka, datumRodjenja);
        
        tableMladunci = new TableView<>();
        tableMladunci.getColumns().addAll(brojMarkiceMladunca, polMladunca, rasaMladunca, datumRodjenjaMladunca, z_brojMarkice, m_brojMarkice);
        
        tableKorisnici = new TableView<>();
        tableKorisnici.getColumns().addAll(id, ime, password, isAdmin);
        
        //TABLE VIEW END
    }
    
    @Override
    public void start(Stage primaryStage) throws SQLException, IOException {
        
        this.m = new UsluzneMetode();
        this.log = new LogIn();
        this.korisnik = new Korisnici();
                
        primaryStage.setTitle("Moja farma"); 
        Stage adminStage = new Stage();
        adminStage.setTitle("Admin");
        BorderPane layout = new BorderPane();
        Scene mainScene = new Scene(layout,1024,768);
        VBox leftMenu = new VBox();
        Stage LogInStage = new Stage();
        LogInStage.setTitle("LogInWindow");
        GridPane grid = new GridPane();
        Scene logScene = new Scene(grid,640,480);
        
        tableSetup();
        setupLoginWindow(primaryStage, mainScene, LogInStage, grid, logScene);
        setupAdminWindow(leftMenu, primaryStage, mainScene, adminStage);
        setupMainMenuWindow(layout, leftMenu, primaryStage, adminStage, LogInStage, logScene);
        
        layout.setLeft(leftMenu);
        layout.setCenter(table);
        primaryStage.setScene(mainScene);
        
    }
    
}