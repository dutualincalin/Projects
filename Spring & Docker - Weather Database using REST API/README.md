Dutu Alin Calin
342 C1


# Tema 2 SPRC

## Continut arhiva
```
.
├── docker-compose.yml
├── .env
├── README.md
└── Spring-app
    ├── Dockerfile
    ├── .dockerignore
    ├── .idea
    ├── .mvn
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    └── src
```

## Framework-uri

- Pentru acest proiect s-a folosit pentru crearea aplicatiei de tip REST API
framework-ul Spring cu instrumentul de dezvoltare Maven
- Baza de date a fost creata cu PostgreSQL
- Utilitarul de gestiune open source folosit este pgAdmin


## Informatii Utile

- Link pgAdmin: `http://localhost:5050`
- Credentiale pentru logare in pgAdmin
  - User: sprc@sprc.com
  - Password: sprc
  
- Host:Port conectare baza de date: postgres:5432
- Credentiale implicite conectare baza de date:
  - User: postgres
  - Password: postgres

- Fisierul de variabile de mediu (.env) se afla in acelasi folder cu docker-compose.
El contine credentialele pentru utilitar si pentru baza de date

- Fisierul .dockerignore se gaseste in folderul src impreuna cu dockerfile-ul pentru
microserviciul de REST api. Am ales sa ignor fisiere create de Intellij Idea precum
si cele de la Maven
