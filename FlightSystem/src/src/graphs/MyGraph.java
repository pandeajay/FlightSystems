package src.graphs;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import src.graphs.node.Node;

public class MyGraph implements Graph{

	List<Node> graph = new ArrayList<Node>();
	String nodeSeparator = "-";
	String pathSeparator = "::";
	Map<String, String> weightPath = new HashMap<String, String>();
	String minWeightStr = "minWeight=";
	String minPathsStr = ";paths=";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void add(Node node) {
		try {
			this.graph.add(node);
			
		} catch (Exception e) {
			System.out.println("Exception while adding in the Graph. Reason ::"
					+ e);
		}
		
	}

	public void delete(Node node) {
		try {
			this.graph.remove(node);
			
		} catch (Exception e) {
			System.out
					.println("Exception while deleteing in the Graph. Reason ::"
							+ e);
		}
	
	}

	public List<Node> getGraph() {
		return this.graph;
	}

	Node getNode(String nodeName) {
		Iterator<Node> it = this.graph.iterator();
		while (it.hasNext()) {
			Node node = it.next();
			if (node.id.equalsIgnoreCase(nodeName)) {
				return node;
			}
		}
		return null;

	}
	
	List<String> getIdsFromNode (String nodeId){
		List<String> listFromNode = new ArrayList<String>();
		Node node = getNode(nodeId);
		Iterator<Entry<String, String>>  it = node.to.entrySet().iterator();
		while(it.hasNext()){
			String str = it.next().getKey();
			listFromNode.add(str);
		}
				
		return listFromNode;		
	}
	
	
	public List<String> getAllPaths(String startNodeId){
		List<String> tempList = new ArrayList<String>();
		Node startNode = getNode(startNodeId);
		if (startNode.to.entrySet().size() == 0){
			return tempList;
		}
		Iterator<Entry<String, String>> it = startNode.to.entrySet().iterator();
		while(it.hasNext()){			
			tempList.add(it.next().getKey());			
		}
		Iterator<String> it2 = tempList.iterator();
		while(it2.hasNext()){
			tempList.addAll(getAllPaths(it2.next()));
		}		
		return tempList;
		
		
	}
	
	public Vector<String> findPath (String from){
		Vector<String> paths = new Vector<String>();		
		try{			
			//try building possibilities from the existing nodes
			Node startNode = getNode(from);			
			Iterator<Entry<String, String>> it = startNode.to.entrySet().iterator();
			
			while(it.hasNext()){
				Entry<String, String> entry = it.next();
				paths.add(from + nodeSeparator + entry.getKey() );
			}		
			
			int depth = 0;
			for(int i = 0 ; i < paths.size()  ; i++ ) {
				String str = paths.get(i);
				String[] path  = str.split(nodeSeparator);
				String lastNodeStr = path[path.length - 1];
				Node lastNode = getNode(lastNodeStr);
				Iterator<Entry<String, String>> it2 = lastNode.to.entrySet().iterator();
				
				while(it2.hasNext()){
					depth++;
					if(depth > 100){
						break;
					}					
					Entry<String, String> entry = it2.next();
					paths.add(str + nodeSeparator + entry.getKey() );
				}					
			}		
		
		}catch(Exception e){
			System.out.println("findShortestPath exception " + e);
		}
		return paths;
		
	}

/*	public List<String> findShortestPath (String from, String to){
		List<String> shortestPaths = new ArrayList<String>();
		try{
			
			//try building possibilities from the existing nodes
			Node startNode = getNode(from);
			Node endNode = getNode(to);
			
			List<String> possiblePaths = new ArrayList<String>();
			Iterator<String> it = getIdsFromNode(from).iterator();
			
			return getAllPaths(from);
		
		
		}catch(Exception e){
			
		}
		
		return shortestPaths;
		
	} */

	@Override
	public long createNode(Node node) {
		try {
			this.graph.add(node);			
		} catch (Exception e) {
			System.out.println("Exception while adding in the Graph. Reason ::" + e);
		}
		return 1;
		
	}



	@Override
	public double getShortestPathWeight(String from, String to) {
		
		//don't recalculate. Have this intelligence build in the system
		if(weightPath.containsKey(from + nodeSeparator + to	)){
			String str = weightPath.get(from + nodeSeparator + to	);
			String[] strs = str.split(";");
			double weight = 0.0;
			for(int i = 0 ; i < strs.length ; i++){
				String temp = strs[i];
				if(temp.contains(minWeightStr)){
					weight = Double.parseDouble(temp.substring(minWeightStr.length()));
					break;
				}			
			}			
			return weight;
		}
		
		List<Object> paths = getPathVertices(from, to);
		Map<String, String> tempWeightPath = new HashMap<String, String>();
		
		int depth = 0 ; 
		Iterator<Object> it = paths.iterator();
		while(it.hasNext() && depth < 100){
			depth++;
			double weight = 0.0;
			String path = (String) it.next();
			String[] pathNodes = path.split(nodeSeparator);
			for(int i = 0 ; i <= pathNodes.length - 2; i++){
				Node prevNode = getNode(pathNodes[i]);
				Node nextNode = getNode(pathNodes[i+1]);
				weight = weight +  Double.parseDouble(prevNode.to.get(nextNode.id));				
			}
			tempWeightPath.put(path, ""+weight);
		}
		
		Iterator<Entry<String, String>> it2 = tempWeightPath.entrySet().iterator();
		String minPath = "";
		double minWeight = 0.0; 
		while(it2.hasNext()){
			Entry<String, String> entry = it2.next();
			if(minPath.length() == 0){
				minPath = entry.getKey();
				minWeight = Double.parseDouble(entry.getValue());
			}else{
				if(minWeight > Double.parseDouble(entry.getValue())){
					minPath = entry.getKey();
					minWeight = Double.parseDouble(entry.getValue());					
				}				
			}
		}	
		weightPath.put(from + nodeSeparator + to, minWeightStr + minWeight + minPathsStr + minPath);
		
		return minWeight;
	}


	
	public List<Object> getPathVertices(String from, String to) {
		List<Object> list = new ArrayList<Object>();
		Vector<String> paths = findPath(from);
		Iterator<String> it = paths.iterator();
		int depth = 0;
		while(it.hasNext() && depth < 100){
			depth++;
			String path = it.next();
			if(path.contains(nodeSeparator + to )){
				list.add(path);		
			}
		}		
		return list;		
	}
	
	@Override
	public List<String> getShortestPathVetices(String from, String to) {
		getShortestPathWeight(from, to);		
		List<String> list = new ArrayList<String>();	
		String paths = weightPath.get(from + nodeSeparator + to);
		paths = paths.substring(  paths.indexOf(";")+ minPathsStr.length());
		list.add(paths);		

		return list;		
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
		
	}

	@Override
	public long deleteNode(Node node) {
		// TODO Auto-generated method stub
		return 0;
	} 
}
