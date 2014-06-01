package src.graphs;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import src.graphs.node.Node;




public class Jgraph implements Graph {
	
	private final SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	

	
	public long createNode(Node node){
		try{
			graph.addVertex(node.id);
		}catch(Exception e){
			System.out.println("Exception in Graph2 add " + e);
		}
		return 1;
	}

	
	public void delete(Node node){
		boolean flag = graph.removeVertex(node.id);
		if(flag){
			Iterator<Entry<String, String>> it = node.to.entrySet().iterator();
			while(it.hasNext()){			
				graph.removeEdge(node.id, it.next().getKey() );
			}
		}
	}
	
	public double getShortestPathWeight(String from , String to){
		DijkstraShortestPath<String, DefaultWeightedEdge> shortpath = new DijkstraShortestPath<String, DefaultWeightedEdge>(this.graph, from, to);
		return shortpath.getPathLength();
	}
	
	public List<DefaultWeightedEdge> getShortestPathEdgeList(String from , String to){
		DijkstraShortestPath<String, DefaultWeightedEdge> shortpath = new DijkstraShortestPath<String, DefaultWeightedEdge>(this.graph, from, to);
		return shortpath.getPathEdgeList();
	}
	
	public List<DefaultWeightedEdge> getShortestPathVetices(List<DefaultWeightedEdge> edgeList){
		List<String> list = new ArrayList<String>();
		Iterator<DefaultWeightedEdge> it = edgeList.iterator();
		while(it.hasNext()){
			DefaultWeightedEdge edge =  it.next();
			list.add(graph.getEdgeSource(edge));
			list.add(graph.getEdgeTarget(edge));	
		}
		return edgeList;
		
	}

	@Override
	public List getShortestPathVetices(String from, String to) {
		return getShortestPathVetices(getShortestPathEdgeList(from, to));
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createNodes(List<Node> nodes) {
		for(Node node : nodes){
			createNode(node);
		}
		
	}

	@Override
	public void createEdges(List<Node> nodes) {
		for(Node node : nodes){
			createEdge(node);
		}
		
	}

	@Override
	public void createEdge(Node node) {
		Iterator<Entry<String, String>> it = node.to.entrySet().iterator();
		while(it.hasNext()){			
			Entry<String, String> entry = it.next();
			boolean ff = graph.containsVertex(node.id);
			DefaultWeightedEdge  edge = graph.addEdge(node.id, entry.getKey() );
			graph.setEdgeWeight(edge, Double.valueOf(entry.getValue()));
		}
		
	}


	@Override
	public long deleteNode(Node node) {
		// TODO Auto-generated method stub
		return 0;
	}




}
