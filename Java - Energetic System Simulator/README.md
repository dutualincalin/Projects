# Proiect Energy System Etapa 2

## Despre

Proiectare Orientata pe Obiecte, Seriile CA, CD
2020-2021

<https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa2>

Student: Dutu Alin Calin, 323 CD

## Rulare teste

Clasa Test#main
  * ruleaza solutia pe testele din checker/, comparand rezultatele cu cele de referinta
  * ruleaza checkstyle

Detalii despre teste: checker/README

Biblioteci necesare pentru implementare:
* Jackson Core 
* Jackson Databind 
* Jackson Annotations

Tutorial Jackson JSON: 
<https://ocw.cs.pub.ro/courses/poo-ca-cd/laboratoare/tutorial-json-jackson>

## Implementare

### Entitati

ContractActions
- Pentru gestionarea contractelor
    
Financial
- Pentru tot ce tine de plati si salarii
    
InstanceProcedures
- Pentru statistici si pentru cautarea noilor producatori

Update
- Pentru actualizarile datelor la fiecare runda

Comparators
- Pentru sortarea datelor

Constants
- Contine constante
  
Bill
- Reprezinta factura unui consumator pentru un distribuitor

Contract
- Reprezinta contractul unui consumator cu un distribuitor

Debt 
- Reprezinta datoria unui consumator fata de un distribuitor 
  
DistributorPrice 
- Pretul unui contract la un distribuitor

Clasele din pachetul de input
- Folosite pentru parsarea datelor de intrare dintr-un fisier JSON folosind metoda Jackson

LoaderIn
- Retine toate datele parsate in program prin metoda Jackson

Client
- Retine datele unui client
- Contine toate metodele prin care se pot manipula datele unui client

Distributor
- Retine datele unui distribuitor
- Contine toate metodele pentru manipularea datelor unui distribuitor
- Este implementata cu Design Pattern-ul Observer si are rolul de Observer

MonthUpdate
- Retine datele de actualizare pentru toate rundele (lunile)

Producer
- Retine datele unui producator
- Contine toate metodele necesare pentru manipularea datelor unui producator
- Este implementata cu Design Pattern-ul Observer si are rolul de Observable

Input 
- Folosit la citirea datelor cu metoda Jackson
- Implementat cu Design Pattern-ul Factory

Output 
- Folosit la scrierea datelor folosind metoda jackson
- Implementat cu Design Pattern-ul Factory

IOFactory 
- Clasa folosita pentru generarea unei instante de tip Input sau Output
- Implementeaza Design Pattern-ul Factory

IO 
- Contine metodele pentru tipurile de instante generate de clasa IOFactory
- Implementeaza Design Pattern-ul Factory

ObservableProducer
- Contine metode pentru obiectele observate (producatori)
- Implementeaza Design Pattern-ul Observer

Observer 
- Contine metodele pentru Observatori (distribuitori)
- Implementeaza Design Pattern-ul Observer

Clasele din pachetul de output
- Folosite pentru parsarea datelor de iesire intr-un fisier JSON folosind metoda Jackson

LoaderOut
- Retine toate datele de iesire si le parseaza catre output folosind metoda Jackson 

MonthlyStats 
- Clasa retine id-urile distribuitorilor abonati la un producator pentru o luna

ChoosingStrategy 
- Contine metodele pentru strategiile de alegere a producatorilor
- Implementeaza Design Pattern-ul Strategy

GreenStrategy 
- Strategie care prioritizeaza producatorii cu energie regenerabila
- Implementeaza Design Pattern-ul Strategy 

PriceStrategy 
- Strategie care prioritizeaza producatorii cu cel mai mic pret per KW
- Implementeaza Design Pattern-ul Strategy

QuantityStrategy 
- Strategie care prioritizeaza producatorii cu cel mai mare debit de energie per producator
- Implementeaza Design Pattern-ul Strategy

SystemRounds
- Centrul de prelucrare a datelor pentru fiecare runda(luna)
- Se executa toate comenzile pentru toate rundele

### Flow

Programul incepe din Main unde se apeleaza Clasa IOFactory care creeaza o instanta de tip
Input. Cu ajutorul ei se va apela metoda Jackson care parseaza datele intr-o instanta de 
tip LoaderIn (se folosesc toate clasele din pachetul input si clasa LoaderIn). Folosind 
clasa JacksontoDataConverter se vor extrage datele din instanta de LoaderIn in HashMap-uri 
de clienti, distribuitori, producatori si ArrayList-uri de producatori, preturi si date de 
actualizare. Urmeaza apelarea metodei systemExecute din clasa sistem care va incepe 
rundele programului.

Se creeaza instantele pentru clasele Update, Financial, Actions si instanceProcedures 
si se intra in runda initiala unde distribuitorii vor alege producatorii folosind clasa 
instanceProcedures si Clasele din pachetul strategy, in functie de strategiile 
distribuitorilor, urmand sa se calculeze preturile distribuitorilor folosind clasele 
Financial si Distributors si clientii sa isi primeasca salariile folosind clasele 
Financial si Client.Urmeaza crearea contractelor clientilor catre distribuitori si 
verificarea distribuitorilor de eventuale falimente. Aceste actiuni se vor efectua cu 
ajutorul claselor contractActions, Distributor pentru verificarea distribuitorilor si 
crearea si retinerea contractelor clientilor si Client pentru retinerea contractelor.
Urmatoarele actiuni care se vor face in runda initiala sunt platile clientilor catre 
distribuitori si platile distribuitorilor, actiuni care acceseaza clasele Financial, 
Distributor si Client si in cele din urma verificarea clientilor care necesita doar 
clasa Client.

Pentru celelalte runde se va face o scurta verificare a distribuitorilor pentru cazul 
in care toti au dat faliment apoi urmeaza actualizarea datelor pentru clienti si 
distribuitori (folosind clasele Update, Client si Distributor) si verificarea 
clientilor in caz de faliment. Urmeaza sa se efectueze actiuni asemanatoare cu cele 
din runda initiala: Calcularea preturilor distribuitorilor, clientii primesc salarii,
verificarea distribuitorilor de faliment, actualizarea situatiei contractelor 
clienti-distribuitori, clientii platesc facturile la distribuitori, distribuitorii 
isi platesc cheltuielile si o reverificare a clientilor. Si dupa aceste actiuni se 
vor actualiza datele producatorilor (cu ajutorul claselor Update si Producer), 
distribuitorii isi vor alege noii producatori (instanceProcedures, Producer Distributor
si clasele din pachetul strategy) si se vor adauga producatorilor liste cu id-urile 
distribuitorilor abonati la ei(MonthlyStats, Distributor, Producer).

In final datele prelucrate vor fi salvate intr-o instanta de tip LoaderOut (folosind clasa
DataToJacksonConverter si LoaderOut) si parsate catre fisierul de output folosind metoda 
Jackson(LoaderOut si o instanta de Output generata cu FactoryIO).

### Elemente de design OOP

S-a folosit incapsulare pentru aproape toate clasele astfel incat sa nu 
existe coliziuni de date, suprascrieri de date sau modificarea datelor 
folosind alte metode decat cele din clasele cu acest rol.

Am folosit abstractizare pentru Design Pattern-urile Observer si Factory,
insa cel mai bine se observa la Factory. In clasa abstracta IO exista 
metoda execute care este prezenta in clasele Input si Output care implementeaza IO.
Implementarile in Input si Output sunt diferite, insa antetul metodei este acelasi.

In proiect a fost implementat si conceptul de Polimorfism la clasele de distribuitori
si producatori unde exista 2 constructori care au acelasi nume, dar parametrii de
intrare diferiti (Overloading/ Supraincarcare). Primul constructor din ambele clase
este folosit pentru instante care contin toate datele necesare unui distribuitor
respectiv unui producator, iar al doilea constructor creeaza instante pentru actualizarile
rundelor.

### Design patterns

Singleton
- Folosit in clasele de LoaderIn si LoaderOut
- Scopul este de a apela aceleasi instante de tip
  LoaderIn si LoaderOut cu aceleasi date de intrare
  respectiv iesire din mai multe locuri.

Factory
- Folosit pentru clasele Input si Output
- Pentru a crea instante si de a le apela
  metoda execute mult mai usor
  
Observer
- Folosit in clasele Producers si Distributors
- Producers este obiectul observabil (Observable)
- Distributors este observatorul (Observer)
- S-a implementat in asa fel incat distribuitorii sa 
  fie notificati cand unul din producatorii lor isi
  schimba debitul de energie per distribuitor

Strategy.
- Folosit pentru Clasa Distributors
- Exista 3 tipuri de strategii care pot fi apelate:
  * Green - criteriile de alegere: energie regenerabila, 
  pret, cantitate, id
  * Price - criteriile de alegere: pret, cantitate, id
  * Quantity - criteriile de alegere: cantitate, id
- Pentru fiecare tip de strategie se sorteaza producatorii 
  in functie de strategia selectata si se adauga producatori
  (de la stanga la dreapta listei) pana cand necesarul de
  energie al distribuitorului este satisfacut.

### Dificultati intalnite, limitari, probleme

In principiu, nu au fost probleme sau dificultati privind scrierea
programelor, debugging-ul testelor sau checkstyle.

## Feedback, comments
Mi-a placut aceasta tema foarte mult! As putea spune ca fata de alte
teme aceasta a fost chiar relaxanta si usor de implementat pentru ca
am reusit sa fac o implementare solida. Din punctul meu de vedere,
cred ca am reusit sa imi modulez codul foarte bine si asta a dus la
rezolvarea rapida a temei.
