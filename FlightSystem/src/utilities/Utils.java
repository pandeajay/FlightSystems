package utilities;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import src.graphs.node.Node;
import src.jsonutils.JsonNodeReader;

public class Utils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static List<Node> getAllNodesFromJson(String josnFile) {
		List<String> nodesDataList = JsonNodeReader.readData(josnFile);
		Iterator<String> newIt = nodesDataList.iterator();
		Map<String, Node> nodesMap = new HashMap<String, Node>();
		while (newIt.hasNext()) {
			String str = newIt.next();
			String[] strs = str.split(";");
			Node newNode = Utils.createNode(strs);
			if (nodesMap.containsKey(newNode.id)) {
				Node tempnode = nodesMap.get(newNode.id);
				Map<String, String> map = tempnode.to;
				Map<String, String> tempMap = newNode.to;
				map.putAll(tempMap);
			} else {
				nodesMap.put(newNode.id, newNode);
			}

		}

		Iterator<Entry<String, Node>> mapIt = nodesMap.entrySet().iterator();

		List<Node> newNodesList = new ArrayList<Node>();

		while (mapIt.hasNext()) {
			Entry<String, Node> entry = mapIt.next();
			newNodesList.add(entry.getValue());
		}
		return newNodesList;

	}

	static Node createNode(String[] strs) {
		List<Map<String, String>> toListWithWeight = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		if(strs.length > 1 ){
			map.put(strs[1], strs[2]);
		}
		toListWithWeight.add(map);
		Node newNode = new Node(strs[0], toListWithWeight);
		return newNode;
	}
	
	public static Map<String, String> getDataNodesFile(){
		BufferedReader br = null;
		Map<String, String> userFeedMap = new HashMap<String, String>();
		userFeedMap.put("queriesList",";");
		try { 
			String sCurrentLine; 
			br = new BufferedReader(new FileReader("..\\..\\files\\user-inputs.txt")); 
			while ((sCurrentLine = br.readLine()) != null) {
				
				if(sCurrentLine.contains("DataFile=")){
					userFeedMap.put("DataFile", sCurrentLine.substring("DataFile===".length()-2).trim());
				}else if (sCurrentLine.contains("FromNode---ToNode=")){
					String str = sCurrentLine.substring("FromNode---ToNode=".length());
					String [] strs = str.split("---");
					if(strs.length == 2){
						String list = userFeedMap.get("queriesList");
						list = list + strs[0].trim() + "-" + strs[1].trim() + ";";
						userFeedMap.put("queriesList",list);
					}				
				}
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return userFeedMap;
	}
	
	public static List<String> getFromAndToList(Map<String,String> map){
		List<String> frmToList = new ArrayList<String>();		
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, String> entry = it.next();
			if(! entry.getKey().contains("DataFile")){
				String str = entry.getValue();
				String[] strs = str.split(";");
				for(String temp : strs){
					if(temp != null && temp.length() > 0)
					frmToList.add(temp);
				}
				
			}
		}
		return frmToList;
		
	}

}
