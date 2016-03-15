dijkstras
=========

An implementation of the Dijkstras algorithms to be used in a graph of integers.

First the implementation will be done purly in C to see the inner working of the Dijkstras algorithm.
A follow up OpenMP and CUDA implementation will be done, with comparisons on performance. 

Note:
=====
- The Graph is being read from a .dat file, each line contains three digits, the vertice that are being connected and the weight of the path.
- An adjecency matrix is being used to store the Graph

Warning: 
=======
This is my first serious programming experience in C and my first experience using parallel programming
using OpenMP and CUDA. 
