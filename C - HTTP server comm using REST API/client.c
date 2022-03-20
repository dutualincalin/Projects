#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include "parson.h"
#include "helpers.h"
#include "requests.h"

#define IP_PORT 8080
#define IP_SERVER "34.118.48.238"
#define JSON "application/json"
#define REGISTER "/api/v1/tema/auth/register"
#define LOGIN "/api/v1/tema/auth/login"
#define ACCESS "/api/v1/tema/library/access"
#define BOOKS "/api/v1/tema/library/books"
#define LOGOUT "/api/v1/tema/auth/logout"

int main(){
	int sockfd, id, page_count;

    char buffer[BUFLEN], username[BUFLEN], password[BUFLEN], err[BUFLEN];
    char author[BUFLEN], publisher[BUFLEN], genre[BUFLEN], title[BUFLEN];
    char server[20], the_book[30];

    char *data, *message, *response, *str, *str2;
	char *JWT = malloc(BUFLEN);
    char *cookie = malloc(BUFLEN);

    // Crearea host-ului	
    sprintf(server, "%s:%d", IP_SERVER, IP_PORT);

    while(scanf("%s", buffer)){
    	
    	// exit
    	if(strcmp(buffer,"exit") == 0){
    		printf("Client out.\n");
    		return 0;
    	}

    	// deschiderea conexiunii cu server-ul
		sockfd = open_connection(IP_SERVER, IP_PORT, AF_INET, SOCK_STREAM, 0);
	    if(sockfd < 0){
	    	error("Socket not available. It has other missions to accomplish.");
	    }

	    // register
		if(strcmp(buffer, "register") == 0){
			// Citire acreditari
			printf("username=");
			scanf("%s", username);
			printf("password=");
			scanf("%s", password);
			
			// Creare JSON
			JSON_Value *value = json_value_init_object();
			JSON_Object *obj = json_value_get_object(value);
			json_object_set_string(obj, "username", username);
			json_object_set_string(obj, "password", password);
			data = json_serialize_to_string_pretty(value);

			// Procesarea actiunii prin server
			message = compute_post_request(server, REGISTER, JSON, data, NULL, NULL);
			send_to_server(sockfd, message);
		    response = receive_from_server(sockfd);
		    
		    // Verificarea erorilor
		    str = strstr(response, "error");
		    
		    if(str != NULL){
		    	snprintf(err, strlen(&str[8]) - 1, "%s", &str[8]);
		    	printf("Error: %s\n\n", err);
		    	close(sockfd);
		    	continue;
		    }

		    // Mesajul de succes
		    printf("Your registration with username %s was succesful\n\n", username);

		    // Dezalocari
			json_free_serialized_string(data);
			json_value_free(value);
		}

		// login
		if(strcmp(buffer, "login") == 0){
			// Citire acreditari
			printf("username=");
			scanf("%s", username);
			printf("password=");
			scanf("%s", password);

			// Creare JSON
			JSON_Value *value = json_value_init_object();
			JSON_Object *obj = json_value_get_object(value);
			json_object_set_string(obj, "username", username);
			json_object_set_string(obj, "password", password);
			data = json_serialize_to_string_pretty(value);

			// Procesarea actiunii prin server
			message = compute_post_request(server, LOGIN, JSON, data, NULL, NULL);
			send_to_server(sockfd, message);
		    response = receive_from_server(sockfd);

		    // Verificarea erorilor
		    str = strstr(response, "error");

		    if(str != NULL){
		    	snprintf(err, strlen(&str[8]) - 1, "%s", &str[8]);
		    	printf("Error: %s\n\n", err);
				json_free_serialized_string(data);
				json_value_free(value);
		    	close(sockfd);
		    	continue;
		    }

		    // Preluarea cookie-ului de sesiune
		    str = strstr(response, "Set-Cookie:");
		    str2 = strchr(str, ';');
		    snprintf(cookie, strlen(&str[12]) - strlen(str2) + 1, "%s", &str[12]);

		    // Mesajul de succes
		    printf("Credentials accepted and connection token achieved.\n\n");
			
		    // Dezalocari
			json_free_serialized_string(data);
			json_value_free(value);
		}

		// enter_library
		if(strcmp(buffer, "enter_library") == 0){
			// Procesarea actiunii prin server
			message = compute_get_request(server, ACCESS, NULL, NULL, cookie);
			send_to_server(sockfd, message);
		    response = receive_from_server(sockfd);

		    // Verificarea erorilor
		    str = strstr(response, "error");
		    if(str != NULL){
		    	snprintf(err, strlen(&str[8]) - 1, "%s", &str[8]);
		    	printf("Error: %s\n\n", err);
		    	close(sockfd);
		    	continue;
		    }

		    // Preluarea token-ului JWT
		    str = strstr(response, "token");
		    snprintf(JWT, strlen(&str[8]) + 21, "Authorization: Bearer %s", &str[8]);

		    // Mesajul de succes
		    printf("Token achieved and entry to the library has been granted.\n\n");
		}

		// get_books
		if(strcmp(buffer, "get_books") == 0){
			// Procesarea actiunii prin server
			message = compute_get_request(server, BOOKS, NULL, JWT, NULL);
			send_to_server(sockfd, message);
		    response = receive_from_server(sockfd);

		    // Verificarea erorilor
		    str = strstr(response, "error");
		    if(str != NULL){
		    	snprintf(err, strlen(&str[8]) - 1, "%s", &str[8]);
		    	printf("Error: %s\n\n", err);
		    	close(sockfd);
		    	continue;
		    }

		    // Mesajul de succes
		    printf("Lista de carti este urmatoarea:\n%s\n\n", strstr(response, "["));
		}

		// get_book
		if(strcmp(buffer, "get_book") == 0){
			// Verificarea autorizatiei
			if(JWT[0] == '\0'){
				printf("Error: Authorization token is missing\n\n");
				close(sockfd);
		    	continue;
			}

			// Citirea id-ului cartii
			printf("id=");
			scanf("%d", &id);

			// Setare URL_ului
			sprintf(the_book, "%s/%d", BOOKS, id);

			// Procesarea actiunii prin server
			message = compute_get_request(server, the_book, NULL, JWT, NULL);
			send_to_server(sockfd, message);
		    response = receive_from_server(sockfd);

		    // Verificarea erorilor
		    str = strstr(response, "error");
		    if(str != NULL){
		    	snprintf(err, strlen(&str[8]) - 1, "%s", &str[8]);
		    	printf("Error: %s\n\n", err);
		    	close(sockfd);
		    	continue;
		    }

		    // Mesajul de succes
		    printf("Book's information:\n%s\n\n", strstr(response, "["));
		}

		// add_book
		if(strcmp(buffer, "add_book") == 0){
			// Verificarea autorizatiei
			if(JWT[0] == '\0'){
				printf("Error: Authorization token is missing\n\n");
				close(sockfd);
		    	continue;
			}

			// Citire a datelor cartii
			printf("title=");
			scanf("%s", title);
			printf("author=");
			scanf("%s", author);
			printf("genre=");
			scanf("%s", genre);
			printf("publisher=");
			scanf("%s", publisher);
			printf("page_count=");
			scanf("%d", &page_count);

			// Creare JSON
			JSON_Value *value = json_value_init_object();
			JSON_Object *obj = json_value_get_object(value);
			json_object_set_string(obj, "title", title);
			json_object_set_string(obj, "author", author);
			json_object_set_string(obj, "genre", genre);
			json_object_set_number(obj, "page_count", page_count);
			json_object_set_string(obj, "publisher", publisher);
			data = json_serialize_to_string_pretty(value);

			// Procesarea actiunii prin server
			message = compute_post_request(server, BOOKS, JSON, data, JWT, NULL);
			send_to_server(sockfd, message);
		    response = receive_from_server(sockfd);
		    
		    // Verificarea erorilor
		    str = strstr(response, "error");
		    
		    if(str != NULL){
		    	snprintf(err, strlen(&str[8]) - 1, "%s", &str[8]);
		    	printf("Error: %s\n\n", err);
		    	close(sockfd);
		    	continue;
		    }

		    // Mesajul de succes
		    printf("Book succesfully added.\n\n");

		    // Dezalocari
			json_free_serialized_string(data);
			json_value_free(value);
		}

		// delete_book
		if(strcmp(buffer, "delete_book") == 0){
			// Verificarea autorizatiei
			if(JWT[0] == '\0'){
				printf("Error: Authorization token is missing\n\n");
				close(sockfd);
		    	continue;
			}

			// Citirea id-ului cartii
			printf("id: ");
			scanf("%d", &id);

			// Setare url
			sprintf(the_book, "%s/%d", BOOKS, id);

			// Procesarea actiunii prin server
			message = compute_delete_request(server, the_book, JWT);
			send_to_server(sockfd, message);
		    response = receive_from_server(sockfd);

		    // Verificarea erorilor
		    str = strstr(response, "error");
		    if(str != NULL){
		    	snprintf(err, strlen(&str[8]) - 1, "%s", &str[8]);
		    	printf("Error: %s\n\n", err);
		    	close(sockfd);
		    	continue;
		    }

		    // Mesajul de succes
		    printf("Book was deleted with success!\n\n");
		}

		// logout
		if(strcmp(buffer, "logout") == 0){
			// Procesarea actiunii prin server
			message = compute_get_request(server, LOGOUT, NULL, NULL, cookie);
			send_to_server(sockfd, message);
		    response = receive_from_server(sockfd);

		    // Verificarea erorilor
		    str = strstr(response, "error");
		    if(str != NULL){
		    	snprintf(err, strlen(&str[8]) - 1, "%s", &str[8]);
		    	printf("Error: %s\n\n", err);
		    	close(sockfd);
		    	continue;
		    }

		    // Veselul mesaj de succes
		    memset(JWT, 0, BUFLEN);
		    memset(cookie, 0, BUFLEN);
		    printf("User left the library. See ya later :)!\n\n");
		}

		// inchiderea conexiunii
		close(sockfd);
    }
}