#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include "helpers.h"
#include "requests.h"

char *compute_get_request(char *host, char *url, char *query_params, char *header, char *cookie) {
    char *message = calloc(BUFLEN, sizeof(char));
    char *line = calloc(LINELEN, sizeof(char));

    // Adaugarea URL-ului
    if (query_params != NULL) {
        sprintf(line, "GET %s?%s HTTP/1.1", url, query_params);
    } 

    else sprintf(line, "GET %s HTTP/1.1", url);

    compute_message(message, line);

    // Adaugarea Host-ului
    sprintf(line, "Host: %s", host);
    compute_message(message, line);

    // Adaugarea Headerelor necesare
    sprintf(line, "Connection: keep-alive");
    compute_message(message, line);

    if(header != NULL){
        sprintf(line, "%s", header);
        compute_message(message, line);
    }

    // Adaugarea cookie-urilor
    if (cookie != NULL) {
        sprintf(line, "Cookie: ");
        sprintf(line, "%s%s", line, cookie);
        compute_message(message, line);
    }

    // Adaugarea ultimului spatiu
    compute_message(message, "");

    // Eliberarea memoriei si returnarea mesajului
    free(line);
    return message;
}

char *compute_post_request(char *host, char *url, char* content_type, char *body_data, char *header, char *cookie) {
    int len;
    char *message = calloc(BUFLEN, sizeof(char));
    char *line = calloc(LINELEN, sizeof(char));

    // Adaugarea URL-ului
    sprintf(line, "POST %s HTTP/1.1", url);
    compute_message(message, line);

    // Adaugarea Host-ului
    sprintf(line, "Host: %s", host);
    compute_message(message, line);

    // Adaugarea Headerelor necesare
    sprintf(line, "Connection: keep-alive");
    compute_message(message, line);

    sprintf(line, "Content-Type: %s", content_type);
    compute_message(message, line);

    if(header != NULL){
        sprintf(line, "%s", header);
        compute_message(message, line);
    }

    len = strlen(body_data);

    sprintf(line, "Content-Length: %d", len);
    compute_message(message, line);

    // Adaugarea cookie-urilor
    if(cookie != NULL) {
        sprintf(line, "Cookie: ");
        sprintf(line, "%s%s", line, cookie);
        compute_message(message, line);
    }

    // Adaugarea ultimului spatiu
    compute_message(message, "");

    // Adaugarea datelor in mesaj
    compute_message(message, body_data);

    // Eliberarea memoriei si returnarea mesajului
    free(line);
    return message;
}

char *compute_delete_request(char *host, char *url, char *header) {
    char *message = calloc(BUFLEN, sizeof(char));
    char *line = calloc(LINELEN, sizeof(char));

    // Adaugarea URL-ului
    sprintf(line, "DELETE %s HTTP/1.1", url);
    compute_message(message, line);

    // Adaugarea Host-ului
    sprintf(line, "Host: %s", host);
    compute_message(message, line);

    // Adaugarea Headerelor necesare
    sprintf(line, "Connection: keep-alive");
    compute_message(message, line);

    if(header != NULL){
        sprintf(line, "%s", header);
        compute_message(message, line);
    }

    // Adaugarea ultimului spatiu
    compute_message(message, "");

    // Eliberarea memoriei si returnarea mesajului
    free(line);
    return message;
}