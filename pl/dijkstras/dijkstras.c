//OpenMp Version of Dijkstras
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

//Number of vertices
#define MAX_SIZE 1000

//function declarations
int find_minimal_distance(int *vertices, int *cost, int *isPermanent);
int checked(int *vertices, int *cost, int *isPermanent);
void path(int (*graph)[MAX_SIZE], int *vertices, int *cost, int current, int **paths);

int main(int argc, char *argv[])
{
	//variable declarations
	//===============================================================================

	int i, j, k, v1, v2, w, flag = 0; //indicies 
	int graph[MAX_SIZE][MAX_SIZE] = {0}; //To get the vertices and the weigths from the file
	clock_t start, finish;
	double timeToComplete;

	int *vertices, *cost, *isPermanent;;
	int **paths;
	paths = (int **)malloc(sizeof(int*) * MAX_SIZE);

	for(i = 0; i < MAX_SIZE; i++)
	{
		paths[i] = (int *)malloc(sizeof(int) * MAX_SIZE);
	}

	int currentVertice;
	long size = sizeof(int) * MAX_SIZE;

	vertices = (int *)malloc(size);
	cost = (int *)malloc(size);
	isPermanent = (int *)malloc(size);


	//Get the data
	//================================================================================
	//The graph will be read from a file
	FILE *graph_source = fopen("bigGraph", "r");
	FILE *results = fopen("sResults.dat", "w");
	
	//Check if file has contents
	if(graph_source == NULL)
	{
		printf("The file could not be opened.\n");
	}

	//Get all the vertices from the file and add them to the adyecency matrix
	// while(fscanf(graph_source, "%i %i %i",&v1 ,&v2 ,&w) != EOF)
	// {
	// 	graph[v1 - 1][v2 - 1] = w;
	// }

	for(i = 0; i < MAX_SIZE; i++) 
	{
		for(j = 0; j < MAX_SIZE; j++) 
		{
			fscanf(graph_source, "%d", &graph[i][j]);
		}
	}

	//Fill up the temp array
	for(i = 0; i < MAX_SIZE; i++)
	{
		cost[i] = 0;
		isPermanent[i] = 0;
	}
	//================================================================================

	//User can choose which vertice to assign as source
	int source;
	printf("Source: \n");
	scanf("%d", &source);

	vertices[source - 1] = source;
	currentVertice = source;
	cost[source - 1] = 0;
	isPermanent[source - 1] = 1;

	start = clock();
	//Dijkstras
	//=================================================================================
	while(flag == 0)
	{
		//Relaxation step
		for(i = 0; i < MAX_SIZE; i++)
		{
			//Only add the paths with the positive weights
			if(graph[currentVertice - 1][i] != 0)
			{
				//If the vertice has already been visited and the current distance to source is shorter, swap distances.
				if(vertices[i] != 0 && graph[currentVertice - 1][i] + cost[currentVertice - 1] < cost[i])
				{
					cost[i] = graph[currentVertice - 1][i] + cost[currentVertice - 1];
				}
				else if(vertices[i] == 0)
				{
					vertices[i] = i + 1;
					cost[i] = graph[currentVertice - 1][i] + cost[currentVertice - 1];
				}
			}
		}

		currentVertice = find_minimal_distance(vertices, cost, isPermanent);

		isPermanent[currentVertice - 1] = 1;

		flag = checked(vertices, cost, isPermanent);
		path(graph, vertices, cost, currentVertice, paths);
	}
	finish = clock();

	timeToComplete = (double) (finish - start)/CLOCKS_PER_SEC;
	fprintf(results, "Time elapsed for sequential Dijkstras: %f milliseconds\n", timeToComplete);

	//Results
	//===================================================================================
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
	//======================================================================================
	free(vertices);
	free(isPermanent);
	free(cost);

	for(i = 0; i < MAX_SIZE; i++)
	{
		free(paths[i]);
	}
	free(paths);
	fclose(results);
	fclose(graph_source);
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

