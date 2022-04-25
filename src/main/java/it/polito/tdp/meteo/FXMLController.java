/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Model;
import it.polito.tdp.meteo.model.Rilevamento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxMese"
    private ChoiceBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnUmidita"
    private Button btnUmidita; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    private Model model;
    
    public void setModel(Model model) {
    	this.model=model;
    }

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	txtResult.clear();
    	String stmp = "";
    	List<Rilevamento> best = this.model.trovaSequenza(boxMese.getValue());
    	for(Rilevamento r : best) {
    		stmp+=r.getData()+" "+r.getLocalita()+"\n";
    	}
    	txtResult.appendText("Percorso di analisi di miglior costo:\n"+stmp+"Costo: "+this.model.getMigliore());
    	stmp="";
    }

    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Umidità media di Genova: "+this.model.getUmiditaMedia(boxMese.getValue(), "Genova")+"\n");
    	txtResult.appendText("Umidità media di Milano: "+this.model.getUmiditaMedia(boxMese.getValue(), "Milano")+"\n");
    	txtResult.appendText("Umidità media di Torino: "+this.model.getUmiditaMedia(boxMese.getValue(), "Torino")+"\n");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        boxMese.getItems().clear();
        for(int c=1; c<13; c++) {
        	boxMese.getItems().add(c);
    	}
    }
}

