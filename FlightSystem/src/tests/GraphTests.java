/**
 * 
 */
package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import src.graphs.Graph;
import src.graphs.MyGraph;
import src.graphs.node.Node;
import utilities.Utils;

/**
 * @author apande
 *
 */
public class GraphTests {
	
	Graph graph = null;
	String pathForInputFile ;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("setup for test");
		Map<String, String> map = System.getenv();
		String env = System.getenv("FlightSystems-Home");
		graph = new MyGraph();
		File file = new File("..\\..\\files\\test.json");	
		if(file == null){
			System.out.println("error in getting test file");
		}
		pathForInputFile = ".\\..\\..\\files\\test.json";
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.println("setup for tearDown");
	}

	@Test
	public void testGraphNotNull() {
		System.out.println("Inside testGraphNotNull ");
		assertNotNull(graph);
		assertNotNull(pathForInputFile);	
	}
	
	@Test
	public void testNumberOfNodes() {
		//Map<String, String> userInputMap = Utils.getDataNodesFile();		
	//	String nodesDataPath = userInputMap.get("DataFile");
		List<Node> newNodesList = Utils.getAllNodesFromJson(pathForInputFile);	
		assertTrue(true);
	}

}
