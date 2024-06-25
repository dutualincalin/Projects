#ifndef _HELPERS_H
#define _HELPERS_H 1

#include <stdio.h>
#include <stdlib.h>


#define DIE(assertion, call_description)	\
	do {									\
		if (assertion) {					\
			fprintf(stderr, "(%s, %d): ",	\
					__FILE__, __LINE__);	\
			perror(call_description);		\
			exit(EXIT_FAILURE);				\
		}									\
	} while(0)

#define BUFLEN 	100

#endif

typedef struct{
	char *id;
	int socket;
	int connected;
	int offline_msg;
	char buffer_offline[1500];
} TCP_client;

typedef struct Subscriber Subscriber;

struct Subscriber{
	TCP_client *client;
	int SF;
	Subscriber *next;
};

typedef struct Topic Topic;

struct Topic{
	char *name;
	Subscriber *subscribers;
	Topic *next;
};

typedef struct Client_List Client_List;

struct Client_List{
	TCP_client *client;
	Client_List *next;
};

typedef struct{
	char cmd[15];
	char topic_name[50];
	int SF;
} TCP_sub_msg;

typedef struct{
	char address[17];
	int port;
	int cmd;
	int type;
	char topic_name[50];
	int integer;
	double real_num;
	char text[1501];
} TCP_msg;

typedef struct{
	char topic_name[50];
	char tip_date;
	char data[1501];
} UDP_msg;
