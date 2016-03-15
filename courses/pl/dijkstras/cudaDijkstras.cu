//Cuda version of dijkstras
#include <stdio.h>
#include <stdlib.h>

//Size of the graph.
//Change to user input later
#define MAX_SIZE 1000

// //Kernel Code Goes Here
__global__ void findNeighbors(int *graph, int source, int *isPermanent, int *vertices, int *cost, int vertexCount)
{
	long index = threadIdx.x + blockIdx.x * blockDim.x;
	if(graph[(source - 1) * vertexCount + index] != 0)
	{
		if(vertices[index] != 0 && (cost[index] > graph[(source - 1) * vertexCount + index] + cost[source - 1]))
		{
			cost[index] = graph[(source - 1) * vertexCount + index] + cost[source - 1];
		}
		else if(vertices[index] == 0)
		{
			vertices[index] = index + 1;
			cost[index] = cost[source - 1] + graph[(source - 1) * vertexCount + index];
		}
	}
}

//function declarations
int find_minimal_distance(int *vertices, int *cost, int *isPermanent);
int checked(int *vertices, int *cost, int *isPermanent);
void path(int (*graph)[MAX_SIZE], int *vertices, int *cost, int current, int **paths);

//Host Code
int main(int argc, char *argv[])
{
	//variable declarations
	//===============================================================================
	

	int i, k, j, v1, v2, w, flag = 0; //indicies 
	int tempGraph[MAX_SIZE][MAX_SIZE] = {0};
	float timeResult;

	int *graph, *vertices, *cost, *isPermanent;;
	int *d_vertices, *d_cost, *d_isPermanent, *d_graph;
	int **paths;
	paths = (int **)malloc(sizeof(int*) * MAX_SIZE);

	for(i = 0; i < MAX_SIZE; i++)
	{
		paths[i] = (int *)malloc(sizeof(int) * MAX_SIZE);
	}
	int currentVertice;
	long size = sizeof(int) * MAX_SIZE;

	graph = (int *)malloc(sizeof(int) * MAX_SIZE * MAX_SIZE);
	vertices = (int *)malloc(size);
	cost = (int *)malloc(size);
	isPermanent = (int *)malloc(size);

	cudaMalloc((void **) &d_graph, sizeof(int) * MAX_SIZE * MAX_SIZE);
	cudaMalloc((void **) &d_vertices, size);
	cudaMalloc((void **) &d_cost, size);
	cudaMalloc((void **) &d_isPermanent, size);

	//Initialize the timers to be used in the cuda part.
	cudaEvent_t startTime, stopTime;
	cudaEventCreate(&startTime);
	cudaEventCreate(&stopTime);


	//Get the data
	//================================================================================
	//The graph will be read from a file
	FILE *graph_source = fopen("bigGraph", "r");
	FILE *results = fopen("cResults.dat", "w");

	//Check if file has contents
	if(graph_source == NULL)
	{
		printf("The file could not be opened.\n");
	}

	//Get all the vertices from the file and add them to the adyecency matrix
	// while(fscanf(graph_source, "%i %i %i",&v1 ,&v2 ,&w) != EOF)
	// {
	// 	tempGraph[v1 - 1][v2 - 1] = w;
	// }

	for(i = 0; i < MAX_SIZE; i++) 
	{
		for(j = 0; j < MAX_SIZE; j++) 
		{
			fscanf(graph_source, "%d", &tempGraph[i][j]);
		}
	}

	//Fill up the temp array
	for(i = 0; i < MAX_SIZE; i++)
	{
		cost[i] = 0;
		isPermanent[i] = 0;
		for(k = 0; k < MAX_SIZE; k++)
		{
			graph[i * MAX_SIZE + k] = tempGraph[i][k]; 
		}
	}
	//================================================================================

	//User can choose which vertice to assign as source
	int source;
	printf("Source: \n");
	scanf("%d", &source);

	currentVertice = source;
	cost[source - 1] = 0;
	isPermanent[source - 1] = 1;
	vertices[source - 1] = source;

	
	cudaMemcpy(d_graph, graph, sizeof(int) * MAX_SIZE * MAX_SIZE, cudaMemcpyHostToDevice);
	
	//Start timer
	cudaEventRecord(startTime, 0);

	//Calculate Shortest Path
	//==================================================================================
	while(flag == 0)
	{
		//Copy the arrays to the device;
		cudaMemcpy(d_vertices, vertices, size, cudaMemcpyHostToDevice);
		cudaMemcpy(d_cost, cost, size, cudaMemcpyHostToDevice);
		cudaMemcpy(d_isPermanent, isPermanent, size, cudaMemcpyHostToDevice);

		findNeighbors<<<1,MAX_SIZE>>>(d_graph, currentVertice, d_isPermanent, d_vertices, d_cost, MAX_SIZE);

		//Copy the arrays back from the device, this is the time consuming part
		cudaMemcpy(vertices, d_vertices, size, cudaMemcpyDeviceToHost);
		cudaMemcpy(cost, d_cost, size, cudaMemcpyDeviceToHost);
		cudaMemcpy(isPermanent, d_isPermanent, size, cudaMemcpyDeviceToHost);

		currentVertice = find_minimal_distance(vertices, cost, isPermanent);

		isPermanent[currentVertice - 1] = 1;

		flag = checked(vertices, cost, isPermanent);
		path(tempGraph, vertices, cost, currentVertice, paths);
	}
	//Results
	//======================================================================================

	//End timer
	cudaEventRecord(stopTime, 0);

	cudaEventSynchronize(startTime);
	cudaEventSynchronize(stopTime);

	cudaEventElapsedTime(&timeResult, startTime, stopTime);
	fprintf(results, "Time elapsed for CUDA Dijkstras: %f milliseconds\n", timeResult);

	for(i = 0; i < MAX_SIZE; i++)
	{
		fprintf(results, "Vertice: %d Distance: %d Path: ", vertices[i], cost[i]);
		for(k = 0; k < MAX_SIZE; k++)
		{
			if(paths[i][k] != 0)
				fprintf(results, "%d ", paths[i][k]);
		}
		fprintf(results,"\n");
	}

	//Sanitize
	//==================================================================================
	//Close the file
	free(vertices);
	free(isPermanent);
	free(cost);
	free(graph);

	for(i = 0; i < MAX_SIZE; i++)
	{
		free(paths[i]);
	}
	free(paths);
	fclose(results);
	fclose(graph_source);

	cudaFree(d_graph);
	cudaFree(d_cost);
	cudaFree(d_isPermanent);
	cudaFree(d_vertices);

}
int find_minimal_distance(int *vertices, int *cost, int *isPermanent)
{
	int k, j;

	int tempVertice;
	//Find the first vertice that is not permanent and hold it in temp
		for(k = 0; k < MAX_SIZE; k++)
		{
			if(isPermanent[k] == 0 && cost[k] > 0)
			{
				tempVertice = vertices[k];
				break;
			}
		}

		//Find the smallest amongsts the temporary distances. 
		for(j = 0; j < MAX_SIZE; j++)
		{
			
			if(isPermanent[j] == 0 && (cost[j] < cost[tempVertice - 1] && cost[j] != 0))
			{

				tempVertice = vertices[j];
			}
		}
		return tempVertice;
}

void path(int (*graph)[MAX_SIZE], int *vertices, int *cost, int current, int **paths)
{
	//variables used
	int w, c = 0, origin = current - 1;
	//If the vertex connects to nowhere, don't check for the path.
	if(cost[current - 1] == 0)
	{
		return;
	}
	//The first position is occupied by the currentVertice itself
	while(cost[current - 1]!= 0)
	{
		for(w = 0; w < MAX_SIZE; w++)
		{
			if(graph[current - 1][w] != 0)
			{
				//If the minimal distance of the current node minus the the weight of one of it's connected paths
				//is equal to the minimal distance of the vertice t the other side of the path
				//That is the correct path.
				if(cost[current - 1] - graph[current - 1][w] == cost[w])
				{
					paths[origin][c] = current;;
					current = vertices[w];
					c++;
					break;
				}
			}
		}
	}
	paths[origin][c] = current;
}

int checked(int *vertices, int *cost, int *isPermanent)
{
	int b;
	for(b = 0; b < MAX_SIZE; b++)
	{
		//If there is at least one that is not permanent, break
		if(isPermanent[b] == 0)
		{
			break;
		}
	}
	if(b == MAX_SIZE)
	{
		return 1;
	}
	else
	{
		return 0;
	}
}


