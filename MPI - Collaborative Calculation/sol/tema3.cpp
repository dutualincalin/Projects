#include <iostream>
#include <cstring>
#include <vector>
#include <mpi.h>
#include <fstream>
#include "utils.h"

using namespace std;

#define SEND_MASTER 2

int main(int argc, char *argv[]){
	int procs, rank;

	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &procs);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	int vector_dim = atoi(argv[1]);
	int comm01_err = atoi(argv[2]);

	int coordinator, i, j;
	int total_workers = 0;
	int n = -1;

	int *dim_worker, *vector;
	int **topology = (int**)malloc(3 * sizeof(int*));


	if(rank < 3){
		int *cluster;
		int n_cluster;

		// Citirea clusterelor
		string file = "cluster";
		file += std::to_string(rank);
		file.append(".txt");

		ifstream f(file.c_str());
		f >> n;

		// fiecare cluster va avea la inceput numarul de procese worker,
		// urmat de rank-ul coordonatorului
		// si apoi de rank-urile workerilor
		cluster = (int*) malloc((n + 2) * sizeof(int));
		cluster[0] = n;
		cluster[1] = rank;

		for(i = 0; i < n; i++){
			f >> cluster[i + 2];
		}

		f.close();

		// Adaugarea clusterelor in topologie
		topology[rank] = cluster;

		n += 2;


		// Trimiterea clusterelor intre coordonatori
		// pentru cunoasterea completa a topologiei

		// In cazul in care legatura 0-1 e activa
		if(comm01_err == 0){
			if(rank == 0){
				// Faza 1

				//Trimite la 1 cluster-ul propriu
				MPI_Send(&n, 1, MPI_INT, 1, 0, MPI_COMM_WORLD);
				print_message(rank, 1);
			
				MPI_Send(topology[rank], n, MPI_INT, 1, 0, MPI_COMM_WORLD);
				print_message(rank, 1);

				// Primeste de la 2 cluster-ul sau
				MPI_Recv(&n_cluster, 1, MPI_INT, 2, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
				topology[2] = (int *) malloc(n_cluster * sizeof(int));
				MPI_Recv(topology[2], n_cluster, MPI_INT, 2, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);


				// Faza 2
				
				// Trimite la 2 cluster-ul propriu
				MPI_Send(&n, 1, MPI_INT, 2, 0, MPI_COMM_WORLD);
				print_message(rank, 2);
			
				MPI_Send(topology[rank], n, MPI_INT, 2, 0, MPI_COMM_WORLD);
				print_message(rank, 2);

				// Primeste de la 1 cluster-ul sau
				MPI_Recv(&n_cluster, 1, MPI_INT, 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
				topology[1] = (int *) malloc(n_cluster * sizeof(int));
				MPI_Recv(topology[1], n_cluster, MPI_INT, 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

				print_topology(rank, topology);
			}

			else if(rank == 1){
				// Faza 1

				// Primeste de la 0 cluster-ul sau
				MPI_Recv(&n_cluster, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
				topology[0] = (int *) malloc(n_cluster * sizeof(int));
				MPI_Recv(topology[0], n_cluster, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

				// Trimite la 2 cluster-ul propriu
				MPI_Send(&n, 1, MPI_INT, 2, 0, MPI_COMM_WORLD);
				print_message(rank, 2);
			
				MPI_Send(topology[rank], n, MPI_INT, 2, 0, MPI_COMM_WORLD);
				print_message(rank, 2);
				

				// Faza 2

				// Trimite la 0 cluster-ul propriu
				MPI_Send(&n, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
				print_message(rank, 0);
			
				MPI_Send(topology[rank], n, MPI_INT, 0, 0, MPI_COMM_WORLD);
				print_message(rank, 0);

				// Primeste de la 2 cluster-ul sau
				MPI_Recv(&n_cluster, 1, MPI_INT, 2, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
				topology[2] = (int *) malloc(n_cluster * sizeof(int));
				MPI_Recv(topology[2], n_cluster, MPI_INT, 2, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

				print_topology(rank, topology);
			}

			else{
				// Faza 1

				// Trimite la 0 cluster-ul 2
				MPI_Send(&n, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
				print_message(rank, 0);
			
				MPI_Send(cluster, n, MPI_INT, 0, 0, MPI_COMM_WORLD);
				print_message(rank, 0);

				// Primeste de la 1 cluster-ul sau
				MPI_Recv(&n_cluster, 1, MPI_INT, 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
				topology[1] = (int *) malloc(n_cluster * sizeof(int));
				MPI_Recv(topology[1], n_cluster, MPI_INT, 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);


				// Faza 2

				// Trimite cluster-ul 2 la 1
				MPI_Send(&n, 1, MPI_INT, 1, 0, MPI_COMM_WORLD);
				print_message(rank, 1);
			
				MPI_Send(topology[rank], n, MPI_INT, 1, 0, MPI_COMM_WORLD);
				print_message(rank, 1);

				// Primeste de la 0 cluster-ul sau
				MPI_Recv(&n_cluster, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
				topology[0] = (int *) malloc(n_cluster * sizeof(int));
				MPI_Recv(topology[0], n_cluster, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

				print_topology(rank, topology);
			}
		}

		// daca legatura 0-1 este intrerupta
		else{
			if(rank != 2){
				// Se trimite cluster-ul 0 si 1 la coordonatorul 2
				MPI_Send(&n, 1, MPI_INT, SEND_MASTER, 0, MPI_COMM_WORLD);
				print_message(rank, SEND_MASTER);
				
				MPI_Send(cluster, n, MPI_INT, SEND_MASTER, 0, MPI_COMM_WORLD);
				print_message(rank, SEND_MASTER);

				// Se primeste cluster-ul coordonatorului 2
				MPI_Recv(&n, 1, MPI_INT, SEND_MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
				topology[SEND_MASTER] = (int*) malloc(n * sizeof(int));
				MPI_Recv(topology[SEND_MASTER], n, MPI_INT, SEND_MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

				// Se primeste cluster-ul a coordonatorului a carui topologie
				// inca nu este cunoscuta
				MPI_Recv(&n, 1, MPI_INT, SEND_MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
				topology[1 - rank] = (int*) malloc(n * sizeof(int));
				MPI_Recv(topology[1 - rank], n, MPI_INT, SEND_MASTER, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

				// Se printeaza topologia
				print_topology(rank, topology);
			}

			else{
				for(i = 0; i < 2; i++){
					// Se trimite cluster-ul 2 coordonatorilor 0 si 1
					MPI_Send(&n, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
					print_message(rank, i);
					
					MPI_Send(cluster, n, MPI_INT, i, 0, MPI_COMM_WORLD);
					print_message(rank, i);

					// Se preia cluster-ul unui coordonator(0 sau 1)
					MPI_Recv(&n, 1, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
					topology[i] = (int*) malloc(n * sizeof(int));
					MPI_Recv(topology[i], n, MPI_INT, i, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
				}

				// Se printeaza topologia.
				print_topology(rank, topology);

				for(i = 0; i < 2; i++){
					// Se trimite la celalalt
					MPI_Send(&n, 1, MPI_INT, 1 - i, 0, MPI_COMM_WORLD);
					print_message(rank, 1 - i);

					MPI_Send(topology[i], n, MPI_INT, 1 - i, 0, MPI_COMM_WORLD);
					print_message(rank, 1 - i);
				}
			}
		}

		// Se trimite topologia workerilor
		for(i = 0; i < cluster[0]; i++){
			for(j = 0; j < 3; j++){
				n = topology[j][0] + 2;

				MPI_Send(&n, 1, MPI_INT, cluster[i + 2], 0, MPI_COMM_WORLD);
				print_message(rank, cluster[i + 2]);

				MPI_Send(topology[j], topology[j][0] + 2, MPI_INT, cluster[i + 2], 0, MPI_COMM_WORLD);
				print_message(rank, cluster[i + 2]);
			}
		}
	}

	else{
		MPI_Status status;

		// cluster 0
		MPI_Recv(&n, 1, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, &status);
		topology[0] = (int*) malloc(n * sizeof(int));
		MPI_Recv(topology[0], n, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		// cluster 1
		MPI_Recv(&n, 1, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		topology[1] = (int*) malloc(n * sizeof(int));
		MPI_Recv(topology[1], n, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		// cluster 2
		MPI_Recv(&n, 1, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		topology[2] = (int*) malloc(n * sizeof(int));
		MPI_Recv(topology[2], n, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		// Memorarea rank-ului coordonatorului
		coordinator = status.MPI_SOURCE;

		// Afisarea topologiei
		print_topology(rank, topology);
	}

	// Calculele pentru impartirea corecta a vectorului
	for(i = 0; i < 3; i++){
		total_workers += topology[i][0];
	}

	// vector ce retine cate elemente va procesa worker-ul sau intreg-ul cluster
	dim_worker = (int *)malloc((total_workers + 3) * sizeof(int));

	// calcularea nr de elemente pt fiecare worker
	for(i = 0; i < total_workers; i++){
		dim_worker[i + 3] = vector_dim / total_workers;

		// in caz ca nu se imparte exact dimensiunea la nr de workeri
		if((vector_dim % total_workers) - i + 3 > 0){
			dim_worker[i + 3]++;
		}
	}

	// calcularea nr de elemente pt fiecare cluster(nr total de elemente
	// procesate de toti workerii cluster-ului)
	for(i = 0; i < 3; i++){
		dim_worker[i] = 0;

		for(j = 0; j < topology[i][0]; j++){
			dim_worker[i] += dim_worker[topology[i][j + 2]];
		}
	}

	// Realizarea calculelor

	// Crearea vectorului
	if(rank == 0){
		vector = (int *)malloc(vector_dim * sizeof(int));
 
		for(i = 0; i < vector_dim; i++){
			vector[i] = i;
		}
	}

	if(rank < 3){
		if(comm01_err == 0){
			if(rank != 0){
				// Se primeste vectorul
				vector = (int *)malloc(dim_worker[rank] * sizeof(int));
				MPI_Recv(vector, dim_worker[rank], MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
			}

			// Distribuirea bucatii de vector la workerii coordonatorului
			Scatter_logged(vector, dim_worker, topology[rank]);

			if(rank == 0){
				// Trimite vectorul catre coordonatorii 1 si 2
				MPI_Send(&vector[dim_worker[0]], dim_worker[1], MPI_INT, 1, 0, MPI_COMM_WORLD);
				print_message(0, 1);

				MPI_Send(&vector[dim_worker[0] + dim_worker[1]], dim_worker[2], MPI_INT, 2, 0, MPI_COMM_WORLD);
				print_message(0, 2);

			}

			// Impreunarea bucatilor de vector procesate de workerii unui coordonator
			Gather_logged(vector, dim_worker, topology[rank]);

			if(rank == 0){
				// Primeste vectorul de la coordonatorii 1 si 2
				MPI_Recv(&vector[dim_worker[0]], dim_worker[1], MPI_INT, 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
				MPI_Recv(&vector[dim_worker[0] + dim_worker[1]], dim_worker[2], MPI_INT, 2, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
			}

			if(rank != 0){
				// Trimite vectorul catre coordonatorul 0
				MPI_Send(vector, dim_worker[rank], MPI_INT, 0, 0, MPI_COMM_WORLD);
				print_message(rank, 0);
			}	
		}

		else{
			// Cazul cu 0 - 1 picat
			if(rank == 0){
				// Distribuirea bucatii de vector la workerii coordonatorului
				Scatter_logged(vector, dim_worker, topology[rank]);

				// Trimite bucata de vector pentru 1 si 2 catre coordonatorul 2
				MPI_Send(&vector[dim_worker[0]], dim_worker[2] + dim_worker[1], MPI_INT, 2, 0, MPI_COMM_WORLD);
				print_message(0, 2);

				// Impreunarea bucatilor de vector procesate de workerii unui coordonator
				Gather_logged(vector, dim_worker, topology[rank]);

				// Primeste bucata de vector procesata de la 2
				MPI_Recv(&vector[dim_worker[0]], dim_worker[2] + dim_worker[1], MPI_INT, 2, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
			}

			else if(rank == 1){
				// Primeste bucata de vector de la 2
				vector = (int *)malloc(dim_worker[rank] * sizeof(int));
				MPI_Recv(vector, dim_worker[rank], MPI_INT, 2, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

				// Proceseaz-o
				Scatter_logged(vector, dim_worker, topology[rank]);
				Gather_logged(vector, dim_worker, topology[rank]);

				// Trimite bucata procesata inapoi la 2
				MPI_Send(vector, dim_worker[1], MPI_INT, 2, 0, MPI_COMM_WORLD);
				print_message(1, 2);
			}

			else{
				// Primeste bucata de vector pentru 1 si 2 de la 0
				vector = (int *)malloc((dim_worker[rank] + dim_worker[rank - 1]) * sizeof(int));
				MPI_Recv(vector, dim_worker[rank] + dim_worker[rank - 1], MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

				// Proceseaza bucata lui 2
				Scatter_logged(vector, dim_worker, topology[rank]);

				// Trimite lui 1 bucata sa
				MPI_Send(&vector[dim_worker[rank]], dim_worker[rank - 1], MPI_INT, 1, 0, MPI_COMM_WORLD);
				print_message(2, 1);

				// Preia datele procesate de workerii sau
				Gather_logged(vector, dim_worker, topology[rank]);

				// Primeste bucata procesata de 1
				MPI_Recv(&vector[dim_worker[rank]], dim_worker[rank - 1], MPI_INT, 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

				// Trimite ambele bucati procesate lui 0
				MPI_Send(vector, dim_worker[rank] + dim_worker[rank - 1], MPI_INT, 0, 0, MPI_COMM_WORLD);
			}
		}
	}

	else{
		// Workeri
		n = dim_worker[rank];

		// Primeste bucata de vector
		vector = (int *)malloc(n * sizeof(int));
		MPI_Recv(vector, n, MPI_INT, coordinator, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		// Proceseaza elementele
		for(i = 0; i < n; i++){
			vector[i] *= 2;
		}

		// Trimitele coordonatorului
		MPI_Send(vector, n, MPI_INT, coordinator, 0, MPI_COMM_WORLD);
		print_message(rank, coordinator);
	}

	// Dupa terminarea schimburilor de mesaje
	MPI_Barrier(MPI_COMM_WORLD);

	// Se afiseaza rezultatul
	if(rank == 0){
		Print_result(vector_dim, vector);
	}

	// Se da free la elementele alocate dinamic
	for(i = 0; i < 3; i++){
		free(topology[i]);
	}

	free(topology);
	free(dim_worker);
	free(vector);

	// Si se incheie programul
	MPI_Finalize();
}
