Nume: Dutu Alin Calin
Grupa: 336CC

	Tema 2 APD

Tema contine:
- Tema2.java
- Map.java
- MapDicitonary.java
- Reduce.java
- Reduced.java
- ReduceResult.java

Explicatii: Pentru ca am explicat, zic eu, destul de detaliat in comentariile din cod,
am sa spun aici in cateva cuvinte, care a fost ideea pentru fiecare clasa.

1)Map.java - Clasa task-urilor Map. Aici se va citi cate un fragment din fisier caracter
           cu caracter si se va forma un dictionar partial.

2)MapDictionary.java - Clasa dictionarelor Map. Serveste pe post de structura pentru
                     retinerea dictionarelor partiale.

3)Reduce.java - Clasa task-urilor Reduce. Aici se va citi ansamblul de dictionare create
              de ooperatiile Map si se vor calcula rangul, lungimea maxima si numarul
              maxim de cuvinte pentru un fisier

4)ReducedDictionary.java - Clasa dictionarelor Reduce. Folosita ca structura pentru a aduna
                         dictionarele Map intr-un singur loc. Folosit in pregatirea task-urilor
                         Reduce.

5)ReduceResult.java - Clasa rezultatelor Reduce. Folosita ca structura pentru rezultatele task-urilor
                    Reduce. Vor fi folosite la crearea output-ului.

6)Tema2.java - Clasa principala. Se citesc datele de intrare, se creeaza dictionare partiale
		pe baza fisierelor citite cu ajutorul operatiilor de Map, se grupeaza dictionarele
		partiale pentru fiecare fisier, se apeleaza task-urile de Reduce pe grupurile de
		dictionare si se scrie rezultatul.