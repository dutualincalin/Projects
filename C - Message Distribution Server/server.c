#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>
#include "helpers.h"

// Functii pentru TCP_client
TCP_client *new_client(char *id, int socket){
	TCP_client *client = malloc(sizeof(TCP_client));
	client->id = strdup(id);
	client->socket = socket;
	client->connected = 1;
	client->offline_msg = 0;
	return client;
}


// Functii pentru lista de clienti
void add_client(Client_List *client_list, TCP_client *client){
	if(client_list->client == NULL){
		client_list->client = client;
		return;
	}

	Client_List *client_node = malloc(sizeof(Client_List));
	client_node->client = client;
	client_node->next = NULL;

	Client_List *aux = client_list;
	
	while(aux->next != NULL){
		aux = aux->next;
	}

	aux->next = client_node;
}

TCP_client *get_client_by_id(Client_List *client_list, char *id){
	Client_List *aux = client_list;

	if(aux->client == NULL){
		return NULL;
	}

	while(aux != NULL){
		if(strcmp(id, aux->client->id) == 0){
			return aux->client;
		}

		aux = aux->next;
	}

	return NULL;
}

TCP_client *get_client_by_socket(Client_List *client_list, int socket){
	Client_List *aux = client_list;
	if(aux->client == NULL){
		return NULL;
	}

	while(aux != NULL){
		if(aux->client->socket == socket){
			return aux->client;
		}

		aux = aux->next;
	}

	return NULL;
}


// Functii pentru Subscriber
void add_subscriber(Topic *topic, TCP_client *client, int SF){
	Subscriber *subscriber = malloc(sizeof(Subscriber));
	subscriber->client = client;
	subscriber->SF = SF;
	subscriber->next = NULL;

	if(topic->subscribers == NULL){
		topic->subscribers = subscriber;
		return;
	}

	Subscriber *aux = topic->subscribers;

	while(aux->next != NULL){
		aux = aux->next;
	}

	aux->next = subscriber;
}

void delete_subscriber(Topic *topic, int socket){
	Subscriber *aux = topic->subscribers;

	if(aux == NULL)
		return;

	if(aux->client->socket == socket){
		topic->subscribers = aux->next;
		free(aux);
		return;
	}

	Subscriber *prev = aux;
	aux = aux->next;

	while(aux != NULL){
		if(aux->client->socket == socket){
			prev->next = aux->next;
			free(aux);
			return;
		}

		prev = aux;
		aux = aux->next;
	}
}

// Functii pentru Topic
Topic *add_topic(Topic *topic_list, char *name){
	if(topic_list->name == NULL){
		topic_list->name = strdup(name);
		return topic_list;
	}

	Topic *topic_node = malloc(sizeof(Topic));
	topic_node->name = strdup(name);
	topic_node->subscribers = NULL;
	topic_node->next = NULL;

	Topic *aux = topic_list;

	while(aux->next != NULL){
		aux = aux->next;
	} 

	aux->next = topic_node;
	return topic_node;
}

Topic *get_topic(Topic *topic_list, char *name){
	Topic *aux = topic_list;
	if(aux->name == NULL)
		return NULL;

	while(aux != NULL){
		if(strcmp(name, aux->name) == 0){
			return aux;
		}

		aux = aux->next;
	}

	return NULL;
}



int main(int argc, char *argv[]) {
	setvbuf(stdout, NULL, _IONBF, BUFSIZ);

	// Declaratii
	unsigned int addrlen;
	int i, j, sent, received, power, type;
	int check, sockfd_udp, sockfd_tcp, client_sock, max_sock = 0, flag = 1;
	char buffer[BUFLEN], *token;
	socklen_t sockaddr_size = sizeof(struct sockaddr);
	size_t TCP_msg_size = sizeof(TCP_msg);
	size_t TCP_sub_msg_size = sizeof(TCP_sub_msg);
	socklen_t UDP_msg_size = sizeof(UDP_msg);

	TCP_client *client;

	Client_List *client_list = malloc(sizeof(Client_List));
	client_list->client = NULL;
	client_list->next = NULL;

	Subscriber *aux;

	Topic *topic, *topic_list;
	topic_list = malloc(sizeof(Topic));
	topic_list-> name = NULL;
	topic_list-> subscribers = NULL;
	topic_list-> next = NULL;

	TCP_sub_msg sub_msg;
	UDP_msg udp_msg;
	TCP_msg tcp_msg;
	tcp_msg.cmd = 0;

	struct sockaddr_in udp_sockaddr, tcp_sockaddr,  client_addr, udp_addr;
	socklen_t client_len = sizeof(client_addr);

	fd_set read_sockets;
	fd_set tmp_sockets;

	FD_ZERO(&read_sockets);
	FD_ZERO(&tmp_sockets);

	// Getting UDP socket
	sockfd_udp = socket(PF_INET, SOCK_DGRAM, 0);
	DIE(sockfd_udp == -1, "No UDP socket for you!\n");

	// Setting sockaddr_in for UDP
	udp_sockaddr.sin_family = AF_INET;
	udp_sockaddr.sin_port = htons(atoi(argv[1]));
	inet_aton("127.0.0.1", &(udp_sockaddr.sin_addr));

	// Bind UDP socket
	check = bind(sockfd_udp, (struct sockaddr *) (&udp_sockaddr), sockaddr_size);
	DIE(check < 0, "UDP bind gone bad ;-;.\n");

	// Getting TCP socket
	sockfd_tcp = socket(AF_INET, SOCK_STREAM, 0);
	DIE(sockfd_tcp == -1, "No UDP socket for you!\n");

	check = setsockopt(sockfd_tcp, IPPROTO_TCP, TCP_NODELAY, (void *)&flag, sizeof(flag));
	DIE(check < 0, "Nagle blessed server socket.\n");

	// Setting sockaddr_in for TCP
	tcp_sockaddr.sin_family = AF_INET;
    tcp_sockaddr.sin_port = htons(atoi(argv[1]));
    tcp_sockaddr.sin_addr.s_addr = INADDR_ANY;

    // Bind TCP socket
    check = bind(sockfd_tcp, (struct sockaddr *) (&udp_sockaddr), sockaddr_size);
    DIE(check < 0, "TCP bind gone bad ;-;.\n");

    // Make TCP socket listen
    check = listen(sockfd_tcp, SOMAXCONN);
    DIE(check < 0, "This socket is deaf..\n");

    FD_SET(0, &read_sockets);
    FD_SET(sockfd_udp, &read_sockets);
    FD_SET(sockfd_tcp, &read_sockets);
    max_sock = sockfd_tcp;

    while(1){
		tmp_sockets = read_sockets; 
    	
    	check = select(max_sock + 1, &tmp_sockets, NULL, NULL, NULL);
		DIE(check < 0, "Selection going mad.\n");

		for (i = 0; i <= max_sock; i++) {
			if (FD_ISSET(i, &tmp_sockets)) {
				memset(buffer, 0, BUFLEN);

				// STDIN
				if (i == 0){
			    	fgets(buffer, BUFLEN - 1, stdin);

					if (strncmp(buffer, "exit", 4) == 0){
						tcp_msg.cmd = 1;

						for(int i = 1 ; i <= max_sock; i++){
							if(i != sockfd_udp && i != sockfd_tcp){
								sent = 0;

								while(sent < TCP_msg_size){
									check = send(i, &tcp_msg + sent, TCP_msg_size - sent, 0);
									
									if(check < 0){
										break;
									}

									sent += check;
								}
								
								close(i);
							}
						}

						close(sockfd_udp);
						close(sockfd_tcp);
						return 0;
					}

					continue;
				}

				// UDP
				if (i == sockfd_udp){
					// TODO
					addrlen = sizeof(struct sockaddr_in);
					
					check = recvfrom(i, &udp_msg, UDP_msg_size, 0, (struct sockaddr*)(&udp_addr), (socklen_t *)&addrlen);
					DIE(check < 0, "Receiver is not receiving what should be received.\n");

					// TODO
					tcp_msg.port = ntohs(udp_addr.sin_port);
					strcpy(tcp_msg.address, inet_ntoa(udp_addr.sin_addr));
					strcpy(tcp_msg.topic_name, udp_msg.topic_name);
					tcp_msg.type = udp_msg.tip_date;

					// TOPIC
					topic = get_topic(topic_list, tcp_msg.topic_name);

					if(topic == NULL){
						topic = add_topic(topic_list, tcp_msg.topic_name);
						continue;
					}

					// INT
					if(tcp_msg.type == 0){
						tcp_msg.integer = (ntohl(*((uint32_t *)&udp_msg.data[1])));

						if(udp_msg.data[0] == 1){
							tcp_msg.integer *= (-1);
						}
					}

					// SHORT_REAL
					if(tcp_msg.type == 1){
						tcp_msg.real_num = (double) (ntohs(*((uint16_t *) udp_msg.data))) / 100;
					}

					// FLOAT
					if(tcp_msg.type == 2){
						tcp_msg.real_num = (double)(ntohl(*((uint32_t *)&udp_msg.data[1])));
						power = udp_msg.data[5];

						for(j = 0; j < power; j++){
							tcp_msg.real_num /= 10;
						}

						if(udp_msg.data[0] == 1){
							tcp_msg.real_num *= (-1);
						}
					}

					// STRING
					if(tcp_msg.type == 3){
						strcpy(tcp_msg.text, udp_msg.data);
					}

					// TOPIC 2
					aux = topic->subscribers;

					while(aux != NULL){
						if(aux->client->connected == 1){
							sent = 0;

							while(sent < sizeof(TCP_msg)){
								check = send(aux->client->socket, &tcp_msg + sent, sizeof(TCP_msg) - sent, 0);
								DIE(check < 0, "Not sending your message!");

								sent += check;
							}
						}

						else{
							if(aux->SF == 1 && aux->client->connected == 0){
								aux->client->offline_msg = 1;

								if(tcp_msg.type == 0){
									sprintf(aux->client->buffer_offline, "%s%s:%d - %s - INT - %d\n", aux->client->buffer_offline, tcp_msg.address, tcp_msg.port, tcp_msg.topic_name, tcp_msg.integer);
								}

								else{
									if(tcp_msg.type == 3){
										sprintf(aux->client->buffer_offline, "%s%s:%d - %s - STRING - %s\n", aux->client->buffer_offline, tcp_msg.address, tcp_msg.port, tcp_msg.topic_name, tcp_msg.text);
									}

									else{ 
										if(tcp_msg.type == 1){ 
											sprintf(aux->client->buffer_offline, "%s%s:%d - %s - SHORT_REAL - %f\n", aux->client->buffer_offline,  tcp_msg.address, tcp_msg.port, tcp_msg.topic_name, tcp_msg.real_num);
										}

										else sprintf(aux->client->buffer_offline, "%s%s:%d - %s - FLOAT - %f\n", aux->client->buffer_offline,  tcp_msg.address, tcp_msg.port, tcp_msg.topic_name, tcp_msg.real_num);
									}
								}
							}
						}

						aux = aux->next;
					}

					continue;
				}
				
				// TCP
				if (i == sockfd_tcp) {
					// Conexiune noua
					client_sock = accept(sockfd_tcp, (struct sockaddr *) &client_addr, &client_len);
					DIE(client_sock < 0, "Kill the TCP outsider!\n");

					check = setsockopt(client_sock, IPPROTO_TCP, TCP_NODELAY, (void *)&flag, sizeof(flag));
					DIE(check < 0, "Nagle blessed subscriber connection.\n");

					check = recv(client_sock, buffer, BUFLEN, 0);
					DIE(check < 0, "Receiver is not receiving what should be received.\n");

					client = get_client_by_id(client_list, buffer);

					// daca clientul a mai fost conectat
					if(client != NULL){
						if(client->connected == 1){
							tcp_msg.cmd = 1;
							sent = 0;

							while(sent < TCP_msg_size){
								check = send(client_sock, &tcp_msg + sent, TCP_msg_size - sent, 0);
								DIE(check < 0, "Not sending your message!");

								sent += check;
							}
							
							close(client_sock);
							printf("Client %s already connected.\n", buffer);
							
							tcp_msg.cmd = 0;
							FD_CLR(client_sock, &read_sockets);

							continue;
						}

						FD_SET(client_sock, &read_sockets);

						printf("New client %s connected from %s:%s.\n", client->id, inet_ntoa(client_addr.sin_addr), argv[1]);
						client->connected = 1;
						client->socket = client_sock;

						if(client->buffer_offline[0] != '\0'){
							tcp_msg.cmd = 2;
							memset(tcp_msg.text, 0, 1500);
							strcpy(tcp_msg.text, client->buffer_offline);

							sent = 0;

							while(sent < TCP_msg_size){
								check = send(client_sock, &tcp_msg + sent, TCP_msg_size - sent, 0);
								DIE(check < 0, "Not sending your message!");

								sent += check;
							}

							memset(client->buffer_offline, 0, 1500);
							tcp_msg.cmd = 0;
							client->offline_msg = 0;
						}

						continue;
					}

					printf("New client %s connected from %s:%s.\n", buffer, inet_ntoa(client_addr.sin_addr), argv[1]);

					if (client_sock > max_sock) { 
						max_sock = client_sock;
					}

					FD_SET(client_sock, &read_sockets);
					add_client(client_list, new_client(buffer, client_sock));
					continue;
				}

				// Clientii TCP trimit ceva
				check = recv(i, &sub_msg, TCP_sub_msg_size, 0);
				DIE(check < 0, "Receiver is not receiving what should be received.\n");

				if (check == 0) {
					// conexiunea s-a inchis
					client = get_client_by_socket(client_list, i);
					close(i);
					printf("Client %s disconnected.\n", client->id);

					client->connected = 0;
					FD_CLR(i, &read_sockets);

					continue;
				}

				else{
					received = check;

					while(check != 0 && received < TCP_sub_msg_size){
						check = recv(i, &sub_msg + received, TCP_sub_msg_size - received, 0);
						DIE(check < 0, "No updates :)");

						received += check;
					}
				}
				
				// subscriptions
				token = strdup(sub_msg.topic_name);
				topic = get_topic(topic_list, token);

				if(strcmp(sub_msg.cmd, "subscribe") == 0){
					if(topic == NULL){
						topic = add_topic(topic_list, token);
					}

					add_subscriber(topic, get_client_by_socket(client_list, i), ntohl(sub_msg.SF));
				}

				if(strcmp(sub_msg.cmd, "unsubscribe") == 0){
					if(topic == NULL){
						continue;
					}

					delete_subscriber(topic, i);
				}

				free(token);
			}
		}
    }
}
