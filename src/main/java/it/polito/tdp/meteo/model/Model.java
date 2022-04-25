package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private MeteoDAO meteoDao;
	private double migliore=10000;
	private List<Rilevamento> sol = new ArrayList<Rilevamento>();
//	private List<Rilevamento> sol;

	public Model() {
		this.meteoDao = new MeteoDAO();
	}

	
	public double getMigliore() {
		return this.migliore;
	}
	// of course you can change the String output with what you think works best
	public double getUmiditaMedia(int mese, String localita) {
		return this.meteoDao.getAvgRilevamentiLocalitaMese(mese, localita);
	}
	
	// of course you can change the String output with what you think works best
	public List<Rilevamento> trovaSequenza(int mese) {
		return this.trovaPercorso(mese, this.meteoDao.getAllCitta());
	}
	
	public List<Rilevamento> trovaPercorso(int mese, List<Citta> lista) {
		List<Citta> parziale = new ArrayList();
		
		MeteoDAO dao = new MeteoDAO();
		for(Citta c : lista) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
		}
		this.cerca(0, parziale, lista);
		return sol;
		
	}
	
	public void cerca(int livello, List<Citta> parziale, List<Citta> lista) {
		if(livello==NUMERO_GIORNI_TOTALI) {
			if(calcolaCosto(parziale)<migliore && controllaPresenzaCitta(parziale)) {
				migliore = calcolaCosto(parziale);
//				sol = new ArrayList<Citta>(parziale);
				sol.clear();
				for(int giorno = 0; giorno<NUMERO_GIORNI_TOTALI; giorno++) {
					sol.add(parziale.get(giorno).getRilevamenti().get(giorno));
				}
			}
//			System.out.println(parziale);
		}else {
			
			if(calcolaCosto(parziale)>migliore)
				return;
			
			for(Citta c : lista) {
				if(aggiuntaValida(c,parziale)) {
					parziale.add(c);
					cerca(livello+1, parziale, lista);
					parziale.remove(parziale.size()-1);				
				}
			}
		}
		
	}

	
	private boolean aggiuntaValida(Citta prova, List<Citta> parziale) {
		
		//verifica giorni massimi
		//contiamo quante volte la città 'prova' era già apparsa nell'attuale lista costruita fin qui
		int conta = 0;
		for (Citta precedente:parziale) {
			if (precedente.equals(prova))
				conta++; 
		}
		if (conta >=NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		// verifica dei giorni minimi
		if (parziale.size()==0) //primo giorno posso inserire qualsiasi città
				return true;
		if (parziale.size()==1 || parziale.size()==2) {
			//siamo al secondo o terzo giorno, non posso cambiare
			//quindi l'aggiunta è valida solo se la città di prova coincide con la sua precedente
			return parziale.get(parziale.size()-1).equals(prova); 
		}
		//nel caso generale, se ho già passato i controlli sopra, non c'è nulla che mi vieta di rimanere nella stessa città
		//quindi per i giorni successivi ai primi tre posso sempre rimanere
		if (parziale.get(parziale.size()-1).equals(prova))
			return true; 
		// se cambio città mi devo assicurare che nei tre giorni precedenti sono rimasto fermo 
		if (parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) 
		&& parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3)))
			return true;
			
		return false;
	}


	private boolean controllaPresenzaCitta(List<Citta> parziale) {
		boolean g=false, t=false, m=false;
		for(Citta c : parziale) {
			if(c.getNome().equals("Genova"))
				g=true;
			else if(c.getNome().equals("Torino"))
				t=true;
			else if(c.getNome().equals("Milano"))
				m=true;
		}
		
		if(g && t && m)
			return true;
		else
			return false;
	}


	public double calcolaCosto(List<Citta> parziale) {
		double costo = 0.0;

			for (int giorno=1; giorno<=parziale.size(); giorno++) {
				Citta c = parziale.get(giorno-1);
				double umid = c.getRilevamenti().get(giorno-1).getUmidita();
				costo+=umid;
			}
			
			for (int giorno=2; giorno<=parziale.size(); giorno++) {
				if(!parziale.get(giorno-1).equals(parziale.get(giorno-2))) {
					costo +=COST;
				}
			}			
		return costo;
	}
}
