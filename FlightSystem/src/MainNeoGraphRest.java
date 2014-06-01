import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import src.graphs.Graph;
import src.graphs.NeoGraph;
import src.graphs.NeoGraphRest;
import src.graphs.node.Node;
import src.jsonutils.JsonNodeReader;
import utilities.Utils;


public class MainNeoGraphRest {

	String nodeSeparator = "-";
	public static void main(String[] args) {
		new MainNeoGraphRest().start();
	}

	private void start() {
		// TODO Auto-generated method stub
		Map<String, String> userInputMap = Utils.getDataNodesFile();		
		String nodesDataPath = userInputMap.get("DataFile");
		List<Node> newNodesList = Utils.getAllNodesFromJson(nodesDataPath);	
		
		Graph graph = new NeoGraphRest();		
		graph.createNodes(newNodesList);
		graph.createEdges(newNodesList);
		
		System.out.println("Ignore above error. It seems some config error from NEO4j DB. Its just warning.");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		List<String> queryList = Utils.getFromAndToList(userInputMap);
		for(String str : queryList){
			String[] strs = str.split("-");
			if(strs.length == 2){
				
				double weight = graph.getShortestPathWeight(strs[0], strs[1]);				
				List<?> edgeList = graph.getShortestPathVetices(strs[0], strs[1]);				
				Iterator<?> it = edgeList.iterator();				
				String shortestPath = "";
				while(it.hasNext()){
					Object edge = it.next();
					shortestPath += edge.toString() + nodeSeparator;					
				}
				if(shortestPath != null && shortestPath.length() > 0  ){
					shortestPath = shortestPath.substring(0,shortestPath.length() - 1);
				}			
			
				
				System.out.println("For fromtoPair = " + strs[0]+"--" + strs[1] +
						"     shortestPath = " + (shortestPath.length() == 0 ? "Path does not exist"  : shortestPath)+ 
						" and its weight = " +weight + (weight > 999 ? "   practically impossible" : ""));
			}
		}
	
		graph.close();		
	}
}
