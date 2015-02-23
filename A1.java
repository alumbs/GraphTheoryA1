//Graph Theory A1
import java.io.*;
import java.util.*;

public class A1
{
	static final int EXIT_FAILURE = -1;
		
	public static void main(String [] args)
	{
		Graph G = readInGraph();
		performCrossOverAlg(G);
	}
	
	private static void performCrossOverAlg(Graph G)
	{
		HashMap<Integer, Integer> path = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> visitedVertices =
			new HashMap<Integer, Integer>();
		int pathIndex = G.numVertices;
		int uIndex = pathIndex, vIndex = pathIndex;
		boolean crossoverFound = true;
		
		 //u and v vertices adjacent to startVertex
		Vertex adjacentU = null, adjacentV = null, startVertex = null;
		Vertex tempVertex, uVertex, vVertex;
		
		if(G == null)
			return;
			
		System.out.println("Crossover alg started");
		
		//Pick a random starting vertex
		//int randVertex = (int)(Math.random() * G.getNumVertices()) + 1;
		int randVertex = 9;
		System.out.println("Random starting vertex = " + randVertex);
		
		startVertex = G.vertices.get(randVertex);
		
		if(startVertex == null){
			System.out.println("ERROR: Vertex " + randVertex + " not found");
			return;
		}
		
		//Add the startVertex to the path
		//Add it to position pathIndex incase it was the last vertex
		// in the list
		path.put(pathIndex, startVertex.vertexIndex);
		
		//Now find two adjacent vertices to the startVertex
		//and add them to the path if they exist
		if(startVertex.adjacentVertices.size() > 0){
			adjacentU = startVertex.adjacentVertices.get(0);
			
			//Decerement the uIndex for the next vertex to be 
			//added to the left of vertex U
			uIndex--;
			
			//Vertex U should be to the left 
			path.put(uIndex, adjacentU.vertexIndex);
		}
		
		if(startVertex.adjacentVertices.size() > 1){
			adjacentV = startVertex.adjacentVertices.get(1);
			
			//Increment the vIndex for the next vertex to be 
			//added to the right of vertex V
			vIndex++;
			
			//Vertex V should be to the right 
			path.put(vIndex, adjacentV.vertexIndex);
		}
		
		//Extend both adjacent vertices 
		//until we can't extend them any further
		int count = 0;
		while(adjacentU != null &&
				adjacentU.adjacentVertices.size() > 0
				&& count < adjacentU.adjacentVertices.size())
		{
			tempVertex = adjacentU.adjacentVertices.get(count);
			
			if(!path.containsValue(tempVertex.vertexIndex))
			{
				//print("Vertex="+tempVertex.vertexIndex + " count=" + count);
				uIndex--;
				path.put(uIndex, tempVertex.vertexIndex);
				adjacentU = tempVertex;
				count = 0;
			}
			else
			{
				count++;
			}
		}
		
		count = 0;
		while(adjacentV != null &&
				adjacentV.adjacentVertices.size() > 0 &&
				count < adjacentV.adjacentVertices.size())
		{
			tempVertex = adjacentV.adjacentVertices.get(count);
			
			if(!path.containsValue(tempVertex.vertexIndex))
			{
				print("Vertex V added to path= "+ tempVertex.vertexIndex +
					" At index: " + (vIndex+1) + " for vertex " + adjacentV.vertexIndex);
				vIndex++;
				path.put(vIndex, tempVertex.vertexIndex);
				adjacentV = tempVertex;
				count = 0;
			}
			else
			{
				count++;
			}
		}
		
		//Print out u and v
		System.out.println("adjacentU = " + adjacentU.vertexIndex);
		System.out.println("adjacentV = " + adjacentV.vertexIndex);
		System.out.println("Total num vertices in path = " + path.size());
		System.out.println("Path contains " + path.values());
		
		//Main loop
		while(crossoverFound)
		{
			print("Crossover started");
			int vertexXIndex=0, vertexWIndex=0, vertexYIndex=0;
			boolean pathExtended = false;
			
			//adjacentU
			for(Vertex tempW : adjacentU.adjacentVertices)
			{
				print("For vertices adjacent to " + adjacentU.vertexIndex +
					" AdjVertex = " + tempW.vertexIndex);
					
				//Get the prev vertex to w on the path
				Vertex tempX = null;
				for(Integer key : path.keySet())
				{
					if(path.get(key) == tempW.vertexIndex){
						vertexWIndex = key;
						vertexXIndex = key-1;
						tempX = G.vertices.get(path.get(vertexXIndex));
						break; //stop this inner for-loop
					}
				}
				
				print("Found prev vertex " + tempX.vertexIndex);
				
				//check all vertices adjacent to X not in the path
				if(tempX != null)
				for(Vertex tempZ : tempX.adjacentVertices)
				{
					print("Adj vert = " +tempZ.vertexIndex+" for vert "+tempX.vertexIndex);
					if(!path.containsValue(tempZ.vertexIndex))
					{
						System.out.println("Path Extension found: Vertex"+
							"= " + vertexXIndex +   + adjacentU.vertexIndex);
						//extend the path
						HashMap<Integer, Integer> newPath = 
							new HashMap<Integer, Integer>();
						int newUIndex = uIndex;
						
						//Add Z-X-U-W-V to the new path
						newPath.put(newUIndex, tempZ.vertexIndex);
						newUIndex++;
						newPath.put(newUIndex, tempX.vertexIndex);
						newUIndex++;
						
						//Add all vertices from vertex X to vertex U
						vertexXIndex--;						
						while(vertexXIndex >= uIndex)
						{
							newPath.put(newUIndex, path.get(vertexXIndex));
							newUIndex++;
							vertexXIndex--;
						}
						
						//Add all vertices from vertex W to vertex V
						while(vertexWIndex <= vIndex)
						{
							newPath.put(newUIndex, path.get(vertexWIndex));
							newUIndex++;
							vertexWIndex++;
						}
						
						//vIndex has changed due to the extension
						vIndex = newUIndex - 1;
						
						pathExtended = true;
						path = newPath;
						break;
					}
				}
				
				if(pathExtended)
					break;
			}
			
			//if the path was extended, break out and start again
			if(pathExtended)
				break;
				
			print("Path not extended, try AdjV");
			
			//adjacentV
			for(Vertex tempW : adjacentV.adjacentVertices)
			{
				print("For vertices adjacent to " + adjacentV.vertexIndex +
					" AdjVertex = " + tempW.vertexIndex);
					
				//Get the next vertex to w on the path
				Vertex tempY = null;
				for(Integer key : path.keySet())
				{
					if(path.get(key) == tempW.vertexIndex){
						vertexWIndex = key;
						vertexYIndex = key+1;
						tempY = G.vertices.get(path.get(vertexYIndex));
						break; //stop this inner for-loop
					}
				}
				
				print("Found next vertex " + tempY.vertexIndex);
				
				//check all vertices adjacent to Y not in the path
				if(tempY != null)
				for(Vertex tempZ : tempY.adjacentVertices)
				{
					print("Adj vert = " +tempZ.vertexIndex+" for vert "+tempY.vertexIndex);
					if(!path.containsValue(tempZ.vertexIndex))
					{
						print("Path extension found");
						//extend the path
						HashMap<Integer, Integer> newPath = 
							new HashMap<Integer, Integer>();
						int newUIndex = uIndex;
						
						//Add Z-Y-V-W-U to the new path
						newPath.put(newUIndex, tempZ.vertexIndex);
						newUIndex++;
						newPath.put(newUIndex, tempY.vertexIndex);
						newUIndex++;
						
						//Add all vertices from vertex Y to vertex V
						vertexYIndex++;						
						while(vertexYIndex <= vIndex)
						{
							newPath.put(newUIndex, path.get(vertexYIndex));
							newUIndex++;
							vertexYIndex++;
						}
						
						//Add all vertices from vertex W to vertex U
						while(vertexWIndex >= uIndex)
						{
							newPath.put(newUIndex, path.get(vertexWIndex));
							newUIndex++;
							vertexWIndex--;
						}
						
						//vIndex has changed due to the extension
						vIndex = newUIndex - 1;
						
						pathExtended = true;
						path = newPath;
						break;
					}
				}
				
				//if the path was extended, break out and start again
				if(pathExtended)
					break;
			}
			
			//if the path was extended, break out and start again
			if(pathExtended)
				break;
				
			print("Path not extended, try looking for crossover");
			
			//Else look for a crossover of order 1
			//if found, extend P
			uVertex = G.vertices.get(path.get(uIndex));
			for(Vertex yVertex : uVertex.adjacentVertices)
			{
				print("For vertices adjacent to " + uVertex.vertexIndex +
					" AdjVertex = " + yVertex.vertexIndex);
					
				//Get the prev vertex to adjVertices on the path
				Vertex prevX = null;
				int yIndex = 0, prevXIndex =0;
				for(Integer key : path.keySet())
				{
					if(path.get(key) == yVertex.vertexIndex){
						yIndex = key;
						prevXIndex = key-1;
						prevX = G.vertices.get(path.get(prevXIndex));
						break; //stop this inner for-loop
					}
				}
				
				//Check if the prev vertex is adjacent to vertex V
				int tempVVertex = path.get(vIndex);
				
				//If yes then we have a crossover
				if(prevX.containsAdjacentVertex(tempVVertex))
				{
					print("One vertex adj to U is also adj to V= "+prevX.vertexIndex);
					//Go through all vertices on the path
					//and see if it is adjacent to any vertices 
					//not on the path - if yes, extend the Path
					for(int pIndex = uIndex; pIndex <= vIndex; pIndex++)
					{
						Vertex pVertex = G.vertices.get(path.get(pIndex));
						
						for(Vertex tempZ : pVertex.adjacentVertices)
						{
							//If no, then that means the current vertex
							//is adjacent to something not on the path
							//So extend the path
							if(!path.containsValue(tempZ.vertexIndex))
							{
								//extend the path - BUT we have 2 cases
								//1: pt p is to the left of crossover
								//2: pt p is to the right of crossover
								if(pIndex < prevXIndex)
								{
									//Do Z-P-X-V-Y-U-(P-1)
									HashMap<Integer, Integer> newPath = 
										new HashMap<Integer, Integer>();
									int newUIndex = uIndex;
									
									//Add Z to the new path
									newPath.put(newUIndex, tempZ.vertexIndex);
									newUIndex++;
									
									//Add all vertices from vertex P
									// to vertex X
									int tempPIndex = pIndex;						
									while(tempPIndex <= prevXIndex)
									{
										newPath.put(newUIndex, path.get(tempPIndex));
										newUIndex++;
										tempPIndex++;
									}
									
									//Add all vertices from vertex V 
									//to vertex Y
									int tempVIndex = vIndex;
									while(tempVIndex >= yIndex)
									{
										newPath.put(newUIndex, path.get(tempVIndex));
										newUIndex++;
										tempVIndex--;
									}
									
									//Add U - (P-1) vertices
									int tempUIndex = uIndex;
									while(tempUIndex <= pIndex-1)
									{
										newPath.put(newUIndex, path.get(tempUIndex));
										newUIndex++;
										tempUIndex++;
									}
									
									//vIndex has changed due to the extension
									vIndex = newUIndex - 1;
									
									pathExtended = true;
									path = newPath;
									break;
								}
								else //pIndex is > prevXIndex
								{
									//Do Z-P-Y-U-X-V-(P+1)
									HashMap<Integer, Integer> newPath = 
										new HashMap<Integer, Integer>();
									int newUIndex = uIndex;
									
									//Add Z and P to the new path
									newPath.put(newUIndex, tempZ.vertexIndex);
									newUIndex++;
																		
									//Add P to Y 
									int tempPIndex = pIndex;						
									while(tempPIndex >= yIndex)
									{
										newPath.put(newUIndex, path.get(tempPIndex));
										newUIndex++;
										tempPIndex--;
									}
									
									//Add U to X
									int tempUIndex = uIndex;
									while(tempUIndex <= prevXIndex)
									{
										newPath.put(newUIndex, path.get(tempUIndex));
										newUIndex++;
										tempUIndex++;
									}
									
									//Add V to (P+1)
									int tempVIndex = vIndex;
									while(tempVIndex >= pIndex + 1)
									{
										newPath.put(newUIndex, path.get(tempVIndex));
										newUIndex++;
										tempVIndex--;
									}
									
									//vIndex has changed due to the extension
									vIndex = newUIndex - 1;
									
									pathExtended = true;
									path = newPath;
									break;
								}
							}
							
							//if the path was extended, break out and start again
							if(pathExtended)
								break;
						}
						
						//if the path was extended, break out and start again
						if(pathExtended)
							break;
					}
				}
				
				//if the path was extended, break out and start again
				if(pathExtended)
					break;
			}
			
			//Finally if no extension was performed,
			//quit the loop
			if(!pathExtended)
				crossoverFound = false;
		}		
		
		System.out.println("After Extension:: Total num vertices" + 
							" in path = " + path.size());
		System.out.println("Path contains " + path.values());
	}
	
	public static void print(String s)
	{
		System.out.println(s);
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
		
		public boolean containsAdjacentVertex(int wantedVertexIndex)
		{
			boolean hasVertex = false;
			if(adjacentVertices != null)
			{
				for(Vertex v : adjacentVertices)
				{
					if(v.vertexIndex == wantedVertexIndex)
					{
						hasVertex = true;
						break;
					}
				}
			}
			
			return hasVertex;
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


