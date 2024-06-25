#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/tcp.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include "helpers.h"

int main(int argc, char *argv[]){

	setvbuf(stdout, NULL, _IONBF, BUFSIZ);

	int i, sockfd, check, sent, received;
	struct sockaddr_in server_addr;
	char buffer[BUFLEN], *token;
	size_t TCP_msg_size = sizeof(TCP_msg);
	size_t TCP_sub_msg_size = sizeof(TCP_sub_msg);
	
	TCP_sub_msg sub_msg;
	TCP_msg tcp_msg;

	// Alocare socket
	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	DIE(sockfd < 0, "socket");

	i = 1;
	check = setsockopt(sockfd, IPPROTO_TCP, TCP_NODELAY, (void *)&i, sizeof(i));
	DIE(check < 0, "Nagle blessed subscriber socket.");
	// Setare structura sockaddr_in
	server_addr.sin_family = AF_INET;
	server_addr.sin_port = htons(atoi(argv[3]));
	inet_aton(argv[2], &server_addr.sin_addr);

	// Conectarea la server
	check = connect(sockfd, (struct sockaddr*) &server_addr, sizeof(server_addr));
	DIE(check < 0, "Connection access you don't have..");

	check = send(sockfd, argv[1], sizeof(argv[1]), 0);
	DIE(check < 0, "No id sending...");

	fd_set read_fds;
	fd_set tmp_fds;
	int fdmax;

	FD_ZERO(&read_fds);
	FD_ZERO(&tmp_fds);

	FD_SET(0, &read_fds);
	FD_SET(sockfd, &read_fds);
	fdmax = sockfd;

	while (1) {
		tmp_fds = read_fds;

		check = select(fdmax + 1, &tmp_fds, NULL, NULL, NULL);
		DIE(check < 0, "No select");

		for (i = 0; i <= fdmax; i++) {
			if (FD_ISSET(i, &tmp_fds)) {
				memset(buffer, 0, BUFLEN);
				
				if(i == 0){
	  				// se citeste de la tastatura
					fgets(buffer, BUFLEN - 1, stdin);

					if (strncmp(buffer, "exit", 4) == 0){
						close(sockfd);
						return 0;
					}

					token = strtok(buffer, " \n");
					if(token == NULL)
						continue;
					
					strcpy(sub_msg.cmd, token);

					token = strtok(NULL, " \n");
					if(token == NULL)
						continue;

					strcpy(sub_msg.topic_name, token);

					if(strcmp(sub_msg.cmd, "subscribe") == 0){
						token = strtok(NULL, " \n");
						if(token == NULL){
							continue;
						}

						sub_msg.SF = htonl(atoi(token));
					}

					else sub_msg.SF = -1;

					sent = 0;

					while(sent < TCP_sub_msg_size){
						check = send(sockfd, &sub_msg + sent, TCP_sub_msg_size - sent, 0);
						DIE(check < 0, "Not sending your message!");

						sent += check;
					}

					if(strcmp(sub_msg.cmd, "subscribe") == 0){
						printf("Subscribed to topic.\n");
						continue;
					}

					if(strcmp(sub_msg.cmd, "unsubscribe") == 0){
						printf("Unsubscribed from topic.\n");
					}
				}

				else{
					// se primeste de la server
					received = 0;

					while(check != 0 && received < TCP_msg_size){
						check = recv(sockfd, &tcp_msg + received, TCP_msg_size - received, 0);
						DIE(check < 0, "No updates :)");

						received += check;
					}

					if(tcp_msg.cmd == 2){
						printf("%s", tcp_msg.text);
						continue;
					}

					if(tcp_msg.cmd == 1){
						close(sockfd);
						return 0;
					}

					if(tcp_msg.type == 0){
						printf("%s:%d - %s - INT - %d\n", tcp_msg.address, tcp_msg.port, tcp_msg.topic_name, tcp_msg.integer);
						continue;
					}

					if(tcp_msg.type == 3){
						printf("%s:%d - %s - STRING - %s\n", tcp_msg.address, tcp_msg.port, tcp_msg.topic_name, tcp_msg.text);
						continue;
					}

					if(tcp_msg.type == 1){
						printf("%s:%d - %s - SHORT_REAL - %f\n", tcp_msg.address, tcp_msg.port, tcp_msg.topic_name, tcp_msg.real_num);
						continue;
					}

					if(tcp_msg.type == 2){
						printf("%s:%d - %s - FLOAT - %f\n", tcp_msg.address, tcp_msg.port, tcp_msg.topic_name, tcp_msg.real_num);
						continue;
					}
				}
			}
		}
	}
}
