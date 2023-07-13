# ZadanieING

Aplikacja została napisana przy użyciu:
    framework: SpringBoot
    db: PostgreSQL
    framework: Hibernate

# Uruchomienie projektu
W celu poprawnego uruchomienia projektu trzeba najpierw utworzyc kontener z bazą danych
poprzez polecenie:
    docker-compose up -d

Teraz można już uruchomić projekt InteliJ tworzący serwer

# Krótki opis:
Aplikacja tworzy 3 tabele:
    client - przechowuje dane klientów np. nazwę firmy
    daydata - przechowującą dane z konkretnego dnia, odwołuje się do tabeli client
    notes - przechowującą notatki, odwołuje się do tabeli client

Kontroler obsługuje wymagane żądania wykrzystując do tego celu klasy usługowe typu:
    FetchingDataService - slużąca do fetch'owania danych z pliku,
        obsługująca logikę biznesową oraz wydająca dane do odpowiedzi do kontrolera
    NotesService - obsługująca tworzenie notatek, zapewniająca do nich dostęp
    Zdefiniowana jest też klasa EmailGenerateService umożliwiająca generowanie ewentualnych wiadomości email

Powyższe klasy wykorzystują obiekt DAO DataAccessServicePostgres implementujący połączenie z bazą danych.
Obsługuje on wymianę danych z aplikacji do bazy danych i na odwrót

