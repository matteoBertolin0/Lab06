package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

public class Ricerca {

	private double migliore = 1000000000;
	
	public List<Rilevamento> trovaPercorso(int mese, List<Rilevamento> lista) {
		List<Rilevamento> parziale = new ArrayList();
		this.cerca(0, mese, parziale, lista);
		return null;
		
	}
	
	public void cerca(int livello, int mese, List<Rilevamento> parziale, List<Rilevamento> lista) {
		
		if(livello==15) {
			if(calcolaCosto(parziale)<migliore)
				migliore = calcolaCosto(parziale);
				
			return;
		}
		
		for(Rilevamento r : lista) {
			parziale.add(r);
			this.cerca(livello+1, mese, parziale, lista);
			parziale.remove(parziale.size()-1);
		}
		
	}
	
	public double calcolaCosto(List<Rilevamento> parziale) {
		return -1;
	}
}
