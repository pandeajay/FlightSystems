package src.graphs;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.server.rest.web.RestfulGraphDatabase;





public class NeoGraphRest implements Graph{
	RestGraphDatabase graphDb = null;
	//GraphDatabaseService graphDb = null;
	static Map<String, String > nodeIAndNeoId = new HashMap<String, String>();
	
	
	
	public NeoGraphRest(){
		try{
			graphDb = new RestGraphDatabase("http://localhost:7474/db/data");
	
		}catch(Exception e){
			System.out.println("Exception "+ e ) ;
		}
	}

	public static void main(String[] args){
		NeoGraphRest neoGraph = new NeoGraphRest();
		
		List<Map<String,String>> toListWithWeight = new ArrayList<Map<String,String>> ();
		Map<String, String> map = new HashMap<String, String>();
		map.put("2", "2");
		map.put("3","3");
		map.put("4", "100");
		toListWithWeight.add(map);			
		src.graphs.node.Node node1 = new src.graphs.node.Node("1", toListWithWeight);
		map.clear();
		toListWithWeight.clear();
		
		long id = neoGraph.createNode(node1);
		Node newNode = neoGraph.getNode(id);
		System.out.println("NeoGraph newNode " + newNode);
		
	}


	public Node getNode(long id) {
		Node node = null;
		try (Transaction tx = graphDb.beginTx()) {
			node = graphDb.getNodeById(id);
			tx.success();
		} catch (Exception e) {
			System.out.println("Exception in NeoGraph createNode " + e);
		}
		return node;

	}


	@Override
	public long createNode(src.graphs.node.Node node) {
		Node myNode = null;
		myNode = graphDb.createNode();
		myNode.setProperty("id", node.id);
		myNode.setProperty("to", node.to.toString());
		NeoGraphRest.nodeIAndNeoId.put(node.id, "" + myNode.getId());
		return myNode.getId();

	}

	@Override
	public void createEdge(src.graphs.node.Node node) {

			long fromId = Long.parseLong(NeoGraphRest.nodeIAndNeoId.get(node.id));
			Node fromNode = graphDb.getNodeById(fromId);			
			Iterator<Entry<String, String>> it = node.to.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, String> entry = it.next();
				long toId = Long.parseLong(NeoGraphRest.nodeIAndNeoId.get(entry.getKey()));
				Node toNode = graphDb.getNodeById(toId);
				Relationship edge = fromNode.createRelationshipTo(toNode, RelTypes.TravellingTo);
				double weight = 0.0;
				try{
					String tt = (String) edge.getProperty("weight");
					
					if(tt != null && tt.length() > 0){
						weight += Double.parseDouble(tt); 
					}
				}catch(Exception e){
					
				}
				weight += Double.parseDouble(entry.getValue());
				edge.setProperty("weight", ""+weight);	
				
			}	

		
	}

	@Override
	public void delete(src.graphs.node.Node node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getShortestPathWeight(String from, String to) {
		
		double minWeight = 0.0 ;
		
		//try (Transaction tx = graphDb.beginTx()) {
			long fromId = Long.parseLong(NeoGraphRest.nodeIAndNeoId.get(from));
			long toId = Long.parseLong(NeoGraphRest.nodeIAndNeoId.get(to));
			
			Node fromNode = graphDb.getNodeById(fromId);			
			Node toNode = graphDb.getNodeById(toId);			
			


			PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra(
				    PathExpanders.forTypeAndDirection( RelTypes.TravellingTo, Direction.OUTGOING ), "weight" );
			
			
			
			Iterable<WeightedPath> paths = finder.findAllPaths(fromNode, toNode);
			Iterator<WeightedPath> it = paths.iterator();
			while(it.hasNext()){
				WeightedPath  path = it.next();	
				minWeight = path.weight();			
			}
		//}
		return minWeight;
	}


	
	String getNodeIdFromNeoNodeId(long neoId){
		Iterator<Entry<String, String>> it = NeoGraphRest.nodeIAndNeoId.entrySet().iterator();
		
		while(it.hasNext()){
			Entry<String, String> entry = it.next();
			if(neoId == Long.parseLong(entry.getValue())){
				return entry.getKey();
			}
		}		
		
		return null;
		
	}

	@Override
	public List getShortestPathVetices(String from, String to) {
		List<String> list = new ArrayList<String>();
		
	//	try (Transaction tx = graphDb.beginTx()) {
			long fromId = Long.parseLong(NeoGraphRest.nodeIAndNeoId.get(from));
			long toId = Long.parseLong(NeoGraphRest.nodeIAndNeoId.get(to));
			
			Node fromNode = graphDb.getNodeById(fromId);			
			Node toNode = graphDb.getNodeById(toId);			
			
			
			PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra(
				    PathExpanders.forTypeAndDirection( RelTypes.TravellingTo, Direction.OUTGOING ), "weight" );
			
			
			
			Iterable<WeightedPath> paths = finder.findAllPaths(fromNode, toNode);
			Iterator<WeightedPath> it = paths.iterator();
			while(it.hasNext()){
				Path path = it.next();				
				Iterable<Node> nodes = path.nodes();
				Iterator<Node> it2 = nodes.iterator();
				while(it2.hasNext()){
					Node tempNode = it2.next();					
					list.add(getNodeIdFromNeoNodeId(tempNode.getId()));
				}
			}			
		//	tx.success();
		//} catch (Exception e) {
		//	System.out.println("Exceptio in NeoGraph getShortestPathVetices " + e);
		//}
		return list;

	}
	
	private static enum RelTypes implements RelationshipType
	{
	    TravellingTo
	}

	@Override
	public void close() {
			graphDb.shutdown();
	}

	@Override
	public void createNodes(List<src.graphs.node.Node> nodes) {
		for(src.graphs.node.Node node : nodes){
			createNode(node);
		}	
	}

	@Override
	public void createEdges(List<src.graphs.node.Node> nodes) {
		for(src.graphs.node.Node node : nodes){
			createEdge(node);
		}	
		
	}

	@Override
	public long deleteNode(src.graphs.node.Node node) {
		return 0;
	}
}
