# Noviaal - Novi Sociaal

Een *platform* om:

* notities op te slaan,
* om die notities te kunnen vinden, lezen, taggen en liken.
* om andere gebruikers te volgen
* om direct geïnformeerd te worden over nieuwe notities van gevolgde gebruikers.
* om herinnerd te worden aan notities.

De doelgroep is *iedereen*.

## FO

![Use Cases diagram](documentatie/FO/use-cases.png)

De Use Cases zijn uitgewerkt in het MarkDown document [Use Cases](./documentatie/FO/use-cases.md)

De broncode van het Use Case diagram is PlantUML, zie [Use Case diagram](https://plantuml.com/use-case-diagram)

### Actoren

1. Geregistreerde gebruiker (User),
2. Administrator (Admin) is een speciale geregistreerde gebruiker die niet-admin gebruikers kan blokkeren,
3. Anonieme gebruiker (Reader) kan een indruk krijgen van wat er allemaal gebeurd, maar kan alleen lezen.

### Entiteiten

De belangrijkste entiteiten in Noviaal zijn:

* Users, gebruikers en
* Notes, notities

Daarnaast zijn er een aantal aanvullende en/of ondersteunende entiteiten:

* Tag, groepering van notities in een bepaalde categorie/met een bepaald label,


### Startpunten

De geregistreerde gebruiker heeft vier hoofdingangen:

1. /home is de TimeLine pagina waarop alle notities van gevolgde gebruikers en de eigen notities worden getoond,
2. /mine geeft alle eigen notities met aantallen likes weer,
3. /likes geeft alle ge-like-te (??) notities weer,
4. /reminders geeft alle notities waaraan de huidige gebruiker herinnerd wil worden.

Bovenaan iedere (startpunt) pagina is een zoekveld, waarmee in gebruikersnamen, tags en notities gezocht kan worden.
Resultaten worden getoond op /search

### Flows

Vanuit de verschillende startpunt pagina's kan een geregistreerde gebruiker naar een specifieke note.
Die gebruiker kan iedere note liken, er een een reminder aan toe voegen en commentaar geven de note.

Alleen de eigenaar, degene die de notitie gemaakt heeft, kan de notitie ook wijzigen.

## TO

### Classes

![Class diagram](documentatie/TO/class-diagram.png)

NB: met de eerste versie is het niet mogelijk om Tags te bewerken.

### C4 diagrammen

Zie vooral eerst [C4 Architectuur Model](https://c4model.com).

![Context diagram](documentatie/FO/c4-context-diagram.png)

---

![Container diagram](documentatie/FO/c4-container-diagram.png)

---

![Componenten diagram](documentatie/TO/c4-component-diagram.png)

### Sequence diagrammen

![Nieuwe notitie](documentatie/TO/seq-diags/seq-new-note.png)

Update notitie gaat vergelijkbaar als deze nieuwe notitie sequentie, maar dan met een bestaande notitie.

![Registreer nieuwe gebruiker](documentatie/TO/seq-diags/registreer.png)

![Zoek](documentatie/TO/seq-diags/zoek.png)

De meeste lijsten van notities en gebruikers werken op dezelfde manier: op basis van een query wordt een lijst met resultaten terug gegeven.

![Like](documentatie/TO/seq-diags/like.png)

Het zetten van een reminder werkt vergelijkbaar met het liken van een notitie.

NB: om sequence diagram overload te voorkomen zijn de eenvoudiger use cases niet gedaan.

### EndPoints

Een overzicht van de endpoints die extern beschikbaar worden gemaakt voor gebruikers (Web, http/html/css/js) en andere apps (API, http/json).

#### Notities

Voor alle gebruikers (ingelogd of anoniem), behalve privé notities: deze zijn alleen zichtbaar voor de eigenaar/auteur
* lijst van alle notities van een bepaalde gebruiker (OC2)
* lijst van alle gelikede (door huidige gebruiker) notities (OC3)
* lijst van alle notities met reminder (van huidige gebruiker) (OC4)
* lijst van alle notities met een zoekwoord in titel of body (IC3 -> OC1)

Alleen voor ingelogde, huidige gebruiker:
* maak een nieuwe notitie (NC1),
* wijzig een bestaande, eigen notitie (titel en/of body) (NC3),
* like een bepaalde notitie (IC4),
* unlike een bepaalde notitie (IC5),
* zet een reminder voor een bepaalde notitie (IC7),
* zet de tag(s) van een bepaalde, eigen notitie (NC5).

#### Users

Voor alle gebruikers (ingelogd of anoniem):
* lijst van alle gebruikers
* lijst van gebruikers die ik (de ingelogde, huidige gebruiker) volg (OC6)
* lijst van gebruikers die mij (de ingelogde, huidige gebruiker) volgen (OC5)
* lijst van gebruikers met een zoekwoord in email adres of naam (IC3 -> OC1)
* toon details, inclusief alle niet-privé notities (NC2)

Voor anonieme gebruikers:
* registreer als gebruiker (AC1)

Voor geregistreerde gebruilkers:
* Vergeet mij, verwijder mij en mijn notities, etc. uit Noviaal (AC5)
* Volg een gebruiker (IC1)
* Stop met volgen van een gebruiker (IC2)

#### TimeLine

Voor alle gebruikers (ingelogd of anoniem):
* Toon de TimeLine: notities van gebruikers die ik (de huidige, ingelogde gebruiker) volg of ik een reminder voor heb gezet én mijn eigen notities.

## Niet-Functionele Eisen

### Kwaliteit

Unit test coverage moet boven de 80% zijn. Dat wil zeggen dat 80% van de regels code in de applicatie automatisch getest moeten worden.
Voor het rapporteren van de test coverage gaat gebruik gemaakt worden van [JaCoCo](https://www.eclemma.org/jacoco/). De test coverage wordt iedere keer dat de applicatie gebouwd wordt, getest en over gerapporteerd.

Het project zal alleen release versies van gebruikte dependencies gebruiken. Deze dependencies zullen actief gecheckt worden, zodat mogelijke (veiligsheid) fouten direct opgelost worden, zodra een verbeterde versie ter beschikking is. Hiervoor wordt gebruik gemaakt van DependaBot van GitHub.

Door het gebruik van Continuous Integration (CI) om het project te bouwen, iedere keer dat er een update gepushed wordt, is de kwaliteitsbewaking continue.

### Internet

De Noviaal Applicatie kan lokaal op een PC of laptop gedraaid worden zonder internet verbinding.

Voor het bouwen van het project tot een werkende applicatie is een internet verbinding nodig. De build tool (Maven) gaat de dependencies van repositories op internet ophalen.

Aangezien er van Docker compose gebruik gemaakt gaat worden om de database en de applicatie op te starten, is er eenmalig
een internet verbinding nodig om de PostgreSQL image en een standaard Linux image met Java te downloaden.

Dus: voor bouwen en installeren is wel een internet verbinding nodig, voor het draaien van de Noviaal applicatie is geen internet verbinding nodig.

### Veiligheid

Zolang de applicatie alleen lokaal gedraaid gaat worden is het niet nodig om SSL voor hhtps in te richten en is ook MultiFactor Authenticatioon (MFA) niet nodig.
Inloggen is bedoeld om onderscheid te kunnen maken tussen de verschillende gebruikers, zodat de interactie tussen die gebruikers zichtbaar gemaakt kan worden.

### Performance

Er zijn geen eisen gesteld aan de performance van het systeem. Er wordt dan ook vanuit gegaan dat er voldoende CPU en intern geheugen (RAM) in de machine waarop de Noviaal applicatie gedraaid wordt, aanwezig zijn.

Ook voor response tijden zijn geen eisen gegeven. Er zijn dus geen performance tests, laat staan performance optimalisaties gepland voor Noviaal.

### Data recovery

Er is geen backup van de database gepland.

### Voorwaarden

1. Installatie van JDK 11 (meest recente versie) is aanwezig.
2. Docker is geinstalleerd en werkzaam.

### Hulpmiddelen etc.

[Java](https://jdk.java.net/11/), meest recente Long Term Supported (LTS) versie (nu: 11)
[Spring Boot](https://spring.io/projects/spring-boot), versie 2.4.0
[Spring Security](https://spring.io/projects/spring-security), versie 5.4.1

[Maven](https://maven.apache.org), versie 3.6.3

[TravisCI](https://travis-ci.com/getting_started)

## Persoonlijk

Dit zijn mijn eigen notities en deze zijn niet officieel onderdeel van de project documentatie.

De lol zit 'm in de event publisher en listener(s) combinatie: als een nieuwe notitie succesvol is bewaard in de database,
dan wordt een event uitgestuurd om alle volgers van de schrijver van deze note te informeren.
Daarna kunnen de 'luisteraars' naar zo'n event actie ondernemen, door bijvoorbeeld de lijst van meest recente notities automatisch op te halen.

Ditzelfde mechanisme kan ook gebruikt worden om updates van notities, likes van notities en wellicht meer te luisteren.

Dus:
* Notitie aanmaken
* Selecteer uit de database welke volgers van de auteur (User) van de note genotificeerd moeten worden
* Event uitsturen

Query: haal user van note op, haal alle volgers op van gebruiker, stuur id van note naar al die volgers.

### Mogelijk nuttige links

- [SSE using Spring](https://dzone.com/articles/server-sent-events-using-spring)
- [Spring Events](https://www.baeldung.com/spring-events)
- [Spring Security - Sample](https://github.com/spring-projects/spring-security/blob/5.4.1/samples/boot/helloworld/)
- [Spring Security - JPA](https://www.codejava.net/frameworks/spring-boot/spring-boot-security-authentication-with-jpa-hibernate-and-mysql)
- [Spring Security - Authentication](https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#servlet-authentication)
- [Registratie en Login](https://medium.com/@kamer.dev/spring-boot-user-registration-and-login-43a33ea19745)
- [Web en API security mix](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#multiple-httpsecurity)

- [ManyToOne done right](https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/)
