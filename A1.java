//Graph Theory A1
import java.io.*;
import java.util.*;

public class A1
{
	static final int EXIT_FAILURE = -1;
		
	public static void main(String [] args)
	{
		A1.Graph G = readInGraph();
		performCrossOverAlg(G);
	}
	
	private static void performCrossOverAlg(Graph G)
	{
		HashMap<Integer, Vertex> path = new HashMap<Integer, Vertex>();
		 //u and v vertices adjacent to startVertex
		Vertex adjacentU = null, adjacentV = null, startVertex = null;
		Vertex tempVertex;
		
		if(G == null)
			return;
			
		System.out.println("Crossover alg started");
		
		//Pick a random starting vertex
		int randVertex = (int)(Math.random() * G.getNumVertices()) + 1;
		System.out.println("Random starting vertex = " + randVertex);
		
		startVertex = G.vertices.get(randVertex);
		
		if(startVertex == null){
			System.out.println("ERROR: Vertex " + randVertex + " not found");
			return;
		}
		
		//Add the startVertex to the path
		path.put(startVertex.vertexIndex, startVertex);
		
		//Now find two adjacent vertices to the startVertex
		//and add them to the path if they exist
		if(startVertex.adjacentVertices.size() > 0){
			adjacentU = startVertex.adjacentVertices.remove(0);
			path.put(adjacentU.vertexIndex, adjacentU);
		}
		
		if(startVertex.adjacentVertices.size() > 0){
			adjacentV = startVertex.adjacentVertices.remove(0);
			path.put(adjacentV.vertexIndex, adjacentV);
		}
		
		//Extend both adjacent vertices 
		//until we can't extend them any further
		while(adjacentU != null &&
				adjacentU.adjacentVertices.size() > 0)
		{
			tempVertex = adjacentU.adjacentVertices.remove(0);
			
			if(!path.containsKey(tempVertex.vertexIndex))
			{
				path.put(tempVertex.vertexIndex, tempVertex);
				adjacentU = tempVertex;
			}
		}
		
		while(adjacentV != null &&
				adjacentV.adjacentVertices.size() > 0)
		{
			tempVertex = adjacentV.adjacentVertices.remove(0);
			
			if(!path.containsKey(tempVertex.vertexIndex))
			{
				path.put(tempVertex.vertexIndex, tempVertex);
				adjacentV = tempVertex;
			}
		}
		
		//Print out u and v
		System.out.println("adjacentU = " + adjacentU.vertexIndex);
		System.out.println("adjacentV = " + adjacentV.vertexIndex);
		System.out.println("Total num vertices in path = " + path.size());
		
	}
	
	private static Graph readInGraph()
	{
		int numVerticesOfGraph = 0;
		Scanner sc;
		String fileName , line, graphName = null;
		String [] splitTemp;
		A1 a1 = new A1();
		A1.Graph G = null;
		BufferedReader br;
		
		//Read in the name of the file containing
		//the graph
		System.out.println("Enter the filename");
		
		sc = new Scanner(System.in);
		fileName = sc.next();
		
		if(fileName == null){
			System.exit(EXIT_FAILURE);
		}
					
		System.out.println("filename = " + fileName);
					
		try{
			br = new BufferedReader(new FileReader(fileName));
			
			if((line = br.readLine()) != null){
				//First read the name of the Graph
				graphName = line;
			}
			
			if((line = br.readLine()) != null){
				//Next read the amount of vertices of the Graph
				numVerticesOfGraph = Integer.parseInt(line);
			}
			
			G = a1.new Graph(graphName, numVerticesOfGraph);
			
			while((line = br.readLine()) != null){
				//System.out.println("line = " + line);
				splitTemp = line.split(" ");
				String vertex = splitTemp[0].substring(1);//Remove the first char
				int vertexIndex = 0;
				Vertex mainVertex;
				
				if(!vertex.isEmpty()){
					//The vertex we are at
					vertexIndex = Integer.parseInt(vertex); 
				}
								
				//Check if the Vertex exists in the Graph already
				if(vertexIndex != 0 && 
					G.vertices.containsKey(vertexIndex))
				{
					mainVertex = G.vertices.get(vertexIndex);
				}
				else{
					mainVertex = a1.new Vertex(vertexIndex, splitTemp.length - 1);
				}
				
				//Add the vertex to the graph
				G.vertices.put(vertexIndex, mainVertex);
				
				//Read the list of adjacent vertices
				for(int i = 1; i < splitTemp.length; i++)
				{
					Vertex tempVertex = null;
					int tempIndex = 0;
					
					if(!splitTemp[i].isEmpty()){
						//System.out.println("SPlitVertex = " + splitTemp[i]);
						tempIndex = Integer.parseInt(splitTemp[i]);
					}
					
					//0 signifies the end of the list of vertices
					if(tempIndex != 0){
						//Check if the Vertex exists in the Graph already
						if(G.vertices.containsKey(tempIndex)){
							//System.out.println("G has vertex = " + tempIndex);
							tempVertex = G.vertices.get(tempIndex);
						}
						else{
							tempVertex = a1.new Vertex(tempIndex, 1);
							
							//Add the vertex to the graph
							G.vertices.put(tempIndex, tempVertex);
						}
						
						//Add this vertex as an adjacent vertex to the
						//main vertex and vice versa
						mainVertex.adjacentVertices.add(tempVertex);
						tempVertex.adjacentVertices.add(mainVertex);
					}
				}
				
				//System.out.println("line = " + line);
				//System.out.println("Splitline = " + vertex);
				mainVertex.printAdjacentVertices();
			}			
		}
		catch(Exception e)
		{
			//Opening file failed or other stuff
			e.printStackTrace();
			System.out.println("Exception caught: " + e);
			System.exit(EXIT_FAILURE);
		}
		
		return G;
	}
	
	private class Graph
	{
		private String graphName;
		private int numVertices;
		public HashMap<Integer, Vertex> vertices;
		
		public Graph(String name, int numVert) throws Exception
		{
			if(name == null || numVert < 0)
			{
				System.out.println("Bad parameters passed in for Graph"
					+ " declaration");
				throw new Exception("Invalid parameters for Graph");
			}
			
			graphName = name;
			numVertices = numVert;
			
			//Create the array of vertices for this graph
			vertices = new HashMap<Integer, Vertex>();
		}	
		
		public int getNumVertices()
		{
			return this.numVertices;
		}	
	}
	
	private class Vertex
	{
		public int vertexIndex;
		public ArrayList<Vertex> adjacentVertices;
		
		public Vertex(int index, int numAdjacentVertices)
		{
			vertexIndex = index;
			adjacentVertices = new ArrayList<Vertex>(numAdjacentVertices);
		}
		
		public boolean addAdjacentVertex(Vertex vertex)
		{
			return adjacentVertices.add(vertex);
		}
		
		public void printAdjacentVertices()
		{
			System.out.print("Vertex Index = " + vertexIndex);
			System.out.print(" Adjacent vertices =");
			if(adjacentVertices != null){
				for(int i = 0; i < adjacentVertices.size(); i++)
					System.out.print(" " + adjacentVertices.get(i).vertexIndex);
					
				System.out.println();
			}
			else
			{
				System.out.println("No adjacent vertices");
			}
		}
	}
}


