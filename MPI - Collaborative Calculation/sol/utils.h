#ifndef UTILS_H
#define UTILS_H

// Functie pentru afisarea unui mesaj trimis
void print_message(int sender, int receiver){
	printf("M(%d,%d)\n", sender, receiver);
}

// Functie pentru afisarea topologiei
void print_topology(int rank, int **topology){
	int i, j, n_workers;

	printf("%d ->", rank);

	for(i = 0; i < 3; i++){
		n_workers = topology[i][0];
		printf(" %d:", topology[i][1]);

		for(j = 0; j < n_workers - 1; j++){
			printf("%d,", topology[i][j + 2]);
		}

		printf("%d", topology[i][n_workers + 1]);
	}

	printf("\n");
}

// Functie pentru distribuirea vectorului in bucati workerilor
void Scatter_logged(int *vector, int *dim_worker, int *coord_topology){
	int i, j = 0;

	int source = coord_topology[1];

	for(i = 0; i < coord_topology[0]; i++){
		int destination = coord_topology[i + 2];

		MPI_Send(&vector[j], dim_worker[destination], MPI_INT, destination, 0, MPI_COMM_WORLD);
		print_message(source, destination);

		j += dim_worker[destination];
	}
}

// Functie pentru impreunarea bucatilor workerilor intr-o singura bucata
void Gather_logged(int *vector, int *dim_worker, int *coord_topology){
	int i, j = 0;

	for(i = 0; i < coord_topology[0]; i++){
		int source = coord_topology[i + 2];

		MPI_Recv(&vector[j], dim_worker[source], MPI_INT, source, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		j += dim_worker[source];
	}
}

// Functie pentru printarea rezultatului final
void Print_result(int n, int *vector){
	printf("Rezultat:");

	for(int i = 0; i < n; i++){
		printf(" %d", vector[i]);
	}

	printf("\n");
}

#endif