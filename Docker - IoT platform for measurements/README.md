## Dutu Alin Calin
## 342 C1

# Tema 3 SPRC

## Rulare
Pentru o prima rulare a serviciilor, trebuie rulat in terminal:

```
. ./run.sh
```

Scriptul verifica daca variabila de mediu `SPRC_DVP` (aceasta reprezentand folderul unde se salveaza volumele de
persistenta ale serviciilor), asigneaza variabilei o cale data de utilizator dupa caz, creeaza folderele pentru
volume, asigneaza permisiunile necesare urmand sa construiasca si sa porneasca stack-ul cu servicii.

Note: De retinut ca definirea variabilei `SPRC_DVP` cu `run.sh` se face doar la nivelul terminalului in care a fost
rulat scriptul. Pentru definirea globala a variabilei de mediu se poate folosi comanda:

```
export SPRC_DVP=<cale_volume_Docker>
```

Dupa ce imaginea serverului a fost creata, aplicatia poate fi pornita cu comanda:
```
docker stack deploy -c stack.yml <nume_aplicatie>
```

Aplicatia poate fi oprita folosind comanda:
```
docker stack rm <nume_aplicatie>
```


## Ierarhie fisiere
```
.
├── grafana
│   ├── grafana.env
│   └── provisioning
│       ├── dashboards
│       │   ├── Battery-Dashboard.json
│       │   ├── grafana-dashboards.yml
│       │   └── UPB-Iot-Data-Dashboard.json
│       └── datasources
│           └── datasource.yml
├── influxdb
│   └── influxdb.env
├── mosquitto
│   └── config
│       └── mosquitto.conf
├── mqtt-adapter
│   ├── adapter.env
│   ├── Dockerfile
│   ├── requirements.txt
│   └── src
│       └── mqtt-adapter.py
├── mqtt-client
│   └── mqtt-client.py
├── README.md
├── run.sh
└── stack.yml

10 directories, 17 files
```

## Detalii servicii

### Broker-ul de mesaje

Pentru broker-ul de mesaje am folosit o imagine de `eclipse-mosquitto`, un broker de mesaje care foloseste
protocolul MQTT. Fiind familiar cu acest broker de la laboratorul 5 de SPRC, am decis sa il folosesc in tema.

In cadrul folderului `mosquitto` se gaseste in folderul `config` fisierul de configurare care permite utilizatorilor
neautentificati sa trimita mesaje catre broker utilizand portul 1883.


### Adaptorul de mesaje

Adaptorul de mesaje se regaseste in folderul `mqtt-adapter` sub forma unei implementari in Python in care acesta se
conecteaza atat la broker cat si la baza de date si apoi asteapta sa primeasca mesaje de la orice topic. Fiecare mesaj
trimis de clienti va fi preluat de adaptor, convertit in json, urmand ca fiecare pereche de tip cheie - valoare numerica
sa fie prelucrata intr-o intrare de tipul urmator:

```
{
    "measurement": statie.cheie (string),
    "tags": {
        "location": locatie (string),
        "station": statie (string)
    },

    "time": timestamp (string),
    "fields": {
        "value": valoare (float) 
    },
}

```

- Locatia si statia sunt preluate din topicul mesajului
- Cheia reprezinta ce se masoara (ex: BAT, TEMP, HUMID) si este preluat din payload-ul mesajului
- Valoarea reprezinta valoarea masuratorii si e preluata din payload-ul mesajului
- Timestamp-ul sau timpul cand a fost inregistrata masuratoarea este preluat din payload

Aceasta reprezentare este conforma cu exemplele din documentatia celor de la InfluxDB.

In final se va obtine dintr-un mesaj o lista de json-uri precum cele explicate mai sus. Aceasta lista se va trimite
catre baza de date.

Adaptorul are si o functionalitate de logging care functioneaza numai daca variabila de mediu `DEBUG_DATA_FLOW` este
setata pe "true". Varibila este definita in fisierul `adapter.env` din folderul adaptorului de mesaje.


### Baza de date

Pentru baza de date a fost folosita o imagine de `influx:1.8-alpine` pentru ca imaginile mai noi au probleme mari cu
procesul de autentificare si cu cel de retinere al volumelor de date

Numele bazei de date a fost setat folosind variabila de mediu `INFLUXDB_DB` care se gaseste in fisierul
`influxdb.env` din folderul dedicat bazei de date numit  `influxdb`


### Componenta de vizualizare a datelor

Componenta de vizualizare a datelor e reprezentata de o imagine de `grafana`. Am decis ca credentialele de autentificare
sa fie definite in variabilele de mediu din fisierul `grafana.env` din folderul `grafana`.

Tot in folderul grafana se gaseste folderul provisioning care contine configuratiile pentru conexiunea directa cu baza
de date si pentru dashboard-urile folosite la vizualizarea datelor.

### Retele Docker
-  mqtt-protocol: mosquitto(1883), adaptor
-  db-network: adaptor, influxdb(8086)
-  gui-network: influxdb(8086), grafana(3000)

## Testare

Partea de testare a implementarii este insotita de si de un client care se gaseste in folderul `mqtt-client` in cadrul
caruia se genereaza mesaje cu date alese random si care sunt trimise la un interval de la o jumatate de secunda pana la
maxim 2 secunde.

Note: Am pus sleep intre request-uri deoarece, intr-o prima faza de testare request-urile se trimiteau extrem de repede,
mult mai repede fata de cat de repede a putut procesa adaptorul de mesaje. Totusi, la o verificare amanuntita am vazut
ca toate datele trimise de clienti au fost procesate cu succes.


## Bibliografie

1. Laborator 5 - MQTT, Echipa SPRC 2022, Link: https://curs.upb.ro/2022/mod/resource/view.php?id=45681
(Ultima accesare: 24.12.2022, 12:23 AM)

2. Eclipse-mosquitto Docker Image, Eclipse Foundation, Link: https://hub.docker.com/_/eclipse-mosquitto
(Ultima accesare: 24.12.2022, 12:23 AM)

3. InfluxDB Docker Image, InfluxData, Link: https://hub.docker.com/_/influxdb
(Ultima accesare: 24.12.2022, 12:23 AM)

4. InfluxDB python examples, InfluxData, Link: https://influxdb-python.readthedocs.io/en/latest/examples.html
(Ultima accesare: 24.12.2022, 12:23 AM)

5. Grafana Docker Image, GrafanaLabs, Link: https://hub.docker.com/r/grafana/grafana
(Ultima accesare: 24.12.2022, 12:23 AM)

6. Docker Swarm Docs, Docker, Link: https://docs.docker.com/engine/swarm/stack-deploy/
(Ultima accesare: 24.12.2022, 12:23 AM)