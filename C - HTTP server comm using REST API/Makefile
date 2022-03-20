client: client.c requests.c helpers.c buffer.c parson.c
	gcc -g -o client client.c requests.c helpers.c buffer.c parson.c -Wall

run: client
	./client

clean:
	rm -f *.o client
