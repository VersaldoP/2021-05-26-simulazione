package it.polito.tdp.yelp.model;

import java.util.ArrayList;
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
	private List<Adiacenza> archi;
	private Business bMigliore;
	private int migliore;
	
	
	public Model() {
		dao = new YelpDao();
	}
	
	public List<String> getAllCity() {
		return dao.getAllCity();
	}
	
	public void creaGrafo(int anno,String citta) {
		this.grafo=new  DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		dao.getVertici(idMap,anno,citta);
		
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		archi = new ArrayList<>();
		dao.getArchi(idMap,anno,citta,archi);
		for(Adiacenza a : archi) {
			if(a.getPeso()>0) {
				Graphs.addEdge(this.grafo, a.getB2(), a.getB1(), a.getPeso());
			}
			else {
				Graphs.addEdge(this.grafo, a.getB1(), a.getB2(), -a.getPeso());
			}
		}
		
		
	}
	public int nVertici() {
		// TODO Auto-generated method stub
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		// TODO Auto-generated method stub
		return this.grafo.edgeSet().size();
	}
	
	public Business LocaleMigliore() {
		int peso =0;

		for(Business b: idMap.values()) {
			int entranti =0;
			int uscenti =0;
			for(DefaultWeightedEdge e: this.grafo.incomingEdgesOf(b)) {
				entranti+=this.grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(b)) {
				uscenti+=this.grafo.getEdgeWeight(e);
			}
			peso=entranti-uscenti;
			if(peso>migliore) {
				migliore = peso;
				bMigliore = b;
				
			}
		}
		return bMigliore;
	}
	
	
	
}
