package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business,DefaultWeightedEdge> grafo;
	private Map<String,Business> idMap;
	
	
	public Model() {
		dao = new YelpDao();
	}
	
	public List<String> getAllCity() {
		return dao.getAllCity();
	}
	
	public void creaGrafo(int anno,String citta) {
		this.grafo=new  DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		idMap= new HashMap<>();
		dao.getVertici(idMap,anno,citta);
		
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		
		
		
	}
	
	
	
	
	
}
