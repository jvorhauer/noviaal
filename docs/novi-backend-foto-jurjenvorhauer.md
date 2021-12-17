| <!-- --> | <!-- --> |
|----------|----------|
| Instelling    | NOVI HBO Software Development |
| Leerlijn      | BackEnd |
| Document      | Functioneel en Technisch Ontwerp voor de Eindopdracht |
| Docent        | Nick Stuivenberg |
| Datum         | 20 november 2020 |
| Auteur        | Jurjen Vorhauer |
| Studentnummer | 800009793 |
| GitHub        | https://github.com/jvorhauer/noviaal |
| __Noviaal__   | Een Twiiter-achtig platformm, een samentrekking van NOVI en Sociaal |

<div style="page-break-after: always;"></div>

## Functioneel Ontwerp (FO)

Een *platform* om:

* notities op te slaan,
* om die notities te kunnen vinden, lezen, taggen en liken.
* om andere gebruikers te volgen
* om direct geïnformeerd te worden over nieuwe notities van gevolgde gebruikers.
* om herinnerd te worden aan notities.

De doelgroep is **iedereen** op het internet.

### Actoren

1. Geregistreerde gebruiker (User),
2. Administrator (Admin) is een speciale geregistreerde gebruiker die niet-admin gebruikers kan blokkeren,
3. Anonieme gebruiker (Reader) kan een indruk krijgen van wat er allemaal gebeurd, maar kan alleen lezen.

### Entiteiten

De belangrijkste entiteiten in Noviaal zijn:

* Users, gebruikers en
* Notes, notities

__NB__: de termen worden in dit document door elkaar gebruikt.

Daarnaast zijn er een aantal aanvullende en/of ondersteunende entiteiten:

* Tag, groepering van notities in een bepaalde categorie/met een bepaald label,
* Reminder, een Note kan in de toekomst weer aandacht nodig hebben
* Comment, commentaar van gebruikers op notities
* Like, een gebruiker vind een notitie leuk, mooi, goed of zo.

### Startpunten

De geregistreerde gebruiker heeft vier hoofdingangen:

1. `/home` is de TimeLine pagina waarop alle notities van gevolgde gebruikers en de eigen notities worden getoond,
2. `/mine` geeft alle eigen notities met aantallen likes weer en de gebruikers gegevens,
3. `/likes` geeft alle ge-like-te (??) notities weer,
4. `/reminders` geeft alle notities waaraan de huidige gebruiker herinnerd wil worden,
5. `/followers` geeft alle mij volgende gebruikers weer,
6. `/following` geeft alle door mij gevolgde gebruikers weer.

Vanuit ieder van die pagina's kan men naar de details:
* Voor gebruikers is dat een profielpagina, gelijk aan `/mine`, maar dan voor die andere gebruiker en
  zonder de mogelijkheden om de notities te wijzigen, maar wel te liken/unliken en reminders te zetten.
  De gebruiker kan op deze pagina gevolgd worden
* Voor notities worden de titel, body tekst, aanmaak- en laatste wijzigingsdatums en de auteur getoond.
  De notitie van jezelf kun je wijzigen en een reminder op zetten; op notities van anderen kun je liken/unliken, reminder zetten

Bovenaan iedere (startpunt) pagina is een zoekveld, waarmee in gebruikersnamen, tags en notities gezocht kan worden.
Resultaten worden getoond op `/search`.

### Use Cases

![Use Cases diagram](FO/use-cases.png)

# Noviaal - Use Cases (Functioneel Ontwerp)

Dit document bevat de Use Cases die de functionaliteit van het BackEnd eindopdracht project "Noviaal" beschrijven.

Noviaal maakt het mogelijk voor geregistreerde gebruikers om aantekeningen, Notes, te maken, wijzigen, bekijken en verwijderen.

## Actoren

Er worden drie rollen van gebruikers (Person) onderscheiden in het Noviaal systeem:

1. __User__, de geregistreerde gebruiker, die vrijwel alle functies kan uitvoeren.
2. __Admin__, de super gebruiker, die rapportages kan aanmaken en bekijken.
3. __Reader__, een niet-geregistreerde, dus *anonieme* gebruiker met zeer beperkte functionaliteit ter beschikking.

Alles wat een User kan, kan een Admin ook.

Een Person wordt geïdentificeerd met zijn of haar email adres, dat dus uniek moet zijn binnen de bekende gebruikers van Noviaal.

## Notes (notities)

Een Note is een tekst met een titel. Een Note kan publiekelijk zichtbaar zijn, zodat ook Readers de Note kunnen lezen, alleen voor volgers (Followers) of privé (Private).

### Commentaar (comments)

Bij een Note kunnen Users aantekeningen maken, behalve als de Note privé is. Bij een notitie wordt het commentaar getoond, dat bestaat uit tekst, de datum/tijd en de auteur van het commentaar.

### Reminder

Een User kan een herinnering, in de vorm van datum/tijd, zetten op een bepaalde notitie.

### Like

Een User kan een Note liken. Liked notes kunnen opgehaald worden. Liked notes kunnen ge-unliked worden.

## Use Cases

### Authenticatie

AC1 | Registreer nieuwe gebruiker
---:|---
Beschrijving | Om alle functionaliteit van Noviaal te gebruiken moet een persoon eerst registreren
Actoren | Reader
Voorwaarden | Een persoon registreert met een uniek email adres, een naam en een wachtwoord
Happy flow | Een nog niet geregistreerde gebruiker maakt duidelijk dat hij of zij wil registreren. De aanstaande User voert een email adres in, een naam (geheel vrij, maar wel verplicht) en een password (twee maal). De nieuwe User wordt vastgelegd in de database. De nieuwe gebruiker kan meteen inloggen en aan de slag (om een afhankelijkheid van een email server of ander bevestigingssignaal kanaal te voorkomen).

AC2 | Login
---:|---
Beschrijving | Een eerder geregistreerde gebruiker logt in door zijn of haar email adres en password in te voeren.
Actoren | User en Admin
Voorwaarden | Om in te kunnen loggen moet een User eerder geregistreerd zijn en de juiste combinatie van email adres en password invoeren
Happy Flow | Gebruiker voert email en password in en logt in
Alternatieve flow | Verkeerde email en/of password ingevoerd, de gebruiker keert terug naar een leeg inlog scherm met een foutmelding.
__NB__ | van gebruikers wordt bijgehouden wanneer ze hebben ingelogd.


AC3 | Password vergeten
---:|---
Beschrijving | Een eerder geregistreerde gebruiker is het password van het account vergeten. Het password wordt op verzoek gemaild naar het email adres van het account
Actoren | User en Admin
Voorwaarden | zelfde als inloggen
Happy Flow | De gebruiker geeft aan dat het password opgestuurd moet worden en dan wordt het password opgestuurd.
__NB__ | Dit is onveilig. Zie ook de niet-functionele eisen: omdat de applicatie alleen lokaal draait en niet bedoeld is om publiekelijk te delen, is een complete en vergaande veiligheid niet een vereiste.

AC4 | Uitloggen
---:|---
Beschrijving | een ingelogde gebruiker kan uitloggen en daarmee de lopende sessie beeindigen.
Actoren | User en Admin
Voorwaarden | de gebruiker is ingelogd
Happy Flow | Ingelogde gebruiker geeft aan dat uitgelogd moet worden. De gebruiker-sessie wordt beeindigd. De gebruiker is nu een Reader

AC5 |  Forget Me (beeindig account)
---:|---
Beschrijving | Indien een geregistreerde gebruiker geen account meer wil bij Noviaal dan kan dat account vergeten worden: ieder spoor van dat account wordt dan verwijderd uit de persistente opslag (de database).
Actoren | User
Voorwaarden | de gebruiker heeft een account en is ingelogd
Happy Flow | de ingelogde gebruiker geeft aan dat het account vergeten moet worden. Alle notities, likes, reminders en comments worden eerst verwijderd, waarna het account zelf uit de database verwijderd wordt. Ook de
__NB__ | het email adres van het vergeten account kan daarna hergebruikt worden voor een nieuw account (met een nieuw id)

ID  | AC6
---:|---
Naam | Disable account
Beschrijving | een account kan gedisabled worden, die gebruiker kan dan niet meer inloggen.
Actoren | Admin
Voorwaarden | de gebruiker waarvan het account ge-disabled gaat worden, mag niet ingelogd zijn, omdat dan de state van de lopende sessie onzeker wordt.
Happy Flow | een admin gebruiker zoekt een gebruiker op email adres op en krijgt de mogelijkheid om die gebruiker te disablen.
Alternatieve Flow | de gebruiker is ingelogd en dus krijgt de admin geen mogelijkheid om het betreffende account de disablen.
__NB__ | deze use case doet geen uitspraak over waarom een account disabled gaat worden.

GC1 | Toon gebruiker
---:|---
Beschrijving | Toon alle detailinformatie van een gebruiker
Actoren | Gebruiker
Voorwaarden | De gebruiker is ingelogd
Happy Flow | Vanuit bijv. zoekresultaten kan een gebruiker een (andere) gebruiker selecteren en daarvan de detailinformatie zien. Ook worden de notities van die gebruiker getoond.
Alternatieve Flow | een mogelijkheid wordt geboden aan ingelogde gebruikers om hun eigen details in te zien.

### Notities

NC1  | Creer nieuwe notitie
---:|---
Beschrijving | Maak een nieuwe Notitie en informeer alle volgers van dit feit
Actoren | Gebruikers: auteur en volgers.
Voorwaarden | Gebruiker is ingelogd.
Happy Flow | de gebruiker vult een titel en tekst in en geeft aan dat een nieuwe notitie aangemaakt moet worden. De applicatie slaat de notitie op in het permanente geheugen (database) en stuurt een event aan de volgers van de ingelogde gebruiker dat er een nieuwe notitie ter beschikking is.
Alternative Flow | het lukt niet om de nieuwe notitie in de database op te slaan: de auteur wordt geinformeerd over de fout, de volgers niet.
Uitzondering | Indien de auteur aangeeft dat de notitie privé is, dan worden volgers niet geinformeerd over deze notitie

NC2  | Lees een notitie
---:|---
Beschrijving | Een gebruiker heeft een notitie geselecteerd uit de TimeLine (zie Use Case IC6) of uit zoek resultaten (zie Use Case IC3). De notitie wordt gepresenteerd met auteur, datum & tijd van aanmaken en datum & tijd van laatste wijziging en titel en body.
Actoren | Gebruiker, Reader
Voorwaarden | gebruiker is ingelogd en heeft een notitie gekozen
Happy Flow | een gevonden notitie wordt getoond

NC3  | Update een notitie
---:|---
Beschrijving | Een auteur kan de titel en/of de body tekst van een bestaande notitie aanpassen en weer opslaan.
Actoren | Gebruiker (auteur en volgers)
Voorwaarden | gebruiker is ingelogd en heeft aangegeven, bijv. vanuit NC2, dat de betreffende notitie gewijzigd gaat worden
Happy Flow | gebruiker wijzigt titel en/of body tekst en slaat deze op in de databse, volgers worden geinformeerd.
Alternate Flow | gebruiker geeft aan de wijzigingsactie niet uit te willen voeren (cancel)
Uitzondering | Indien de auteur aangeeft dat de notitie privé is, dan worden volgers niet geinformeerd over deze notitie

NC4 | Verwijder notitie
---:|---
Beschrijving | Een auteur kan een bestaande notitie verwijderen
Actoren | Gebruiker (auteur)
Voorwaarden | gebruiker is ingelogd en heeft een notitie geselecteerd
Happy Flow | de gebruiker geeft aan de notitie te willen verwijderen; de notitie, commentaar, likes en reminders woorder uit de permanente opslag (database) verwijderd.

NC5 | Tag notitie
---:|---
Beschrijving | Een auteur kan een notitie voorzien van één of meer tags
Actoren | gebruiker (auteur)
Voorwaarden | gebruiker is ingelogd en heeft een notitie geselecteerd
Happy Flow | gebruiker kiest één of meer tags uit de lijst van beschikbare tags en geeft aan deze selectie bij de notitie te willen opslaan. De keuze voor 0 of meer tags wordt opgeslagen in de permanente opslag (database).
__NB__ | er wordt een beperkt aantal tags beschikbaar gesteld in Noviaal. In een latere versie kan dat aantal veranderen en/of onderhoudbaar gemaakt worden.

### Interactie

IC1 | Follow (volg)
---:|---
Beschrijving | Een gebruiker kan een andere gebruiker gaan volgen. De volgende gebruiker wordt op de hoogte gehouden van nieuwe en gewijzigde notities van de gevolgde gebruiker
Actoren | Gebruiker (volger en gevolgde)
Voorwaarden | gebruiker die wil gaan volgen is ingelogd en heeft een andere gebruiker geselecteerd.
Happy Flow | Uit de zoekresultaten of via de auteur-link van een notitie is een te volgen gebruiker geselecteerd. De gebruiker geeft aan dat deze gebruiker gevolgd moet gaan worden.

IC2 | Unfollow (beeindig volgen)
---:|---
Beschrijving | Een gebruiker kan een andere, gevolgde gebruiker unfollowen, waardoor deze gebruiker niet meer op hoogte wordt gehouden van events van die gebruiker
Actoren | Gebruiker (volger, gevolgde)
Voorwaarden | Gebruiker is ingelogd en volgt de andere gebruiker
Happy Flow | De gebruiker geeft aan de gevolgde gebruiker niet meer te willen volgen. Dit kan bijv. vanaf OC5

IC3 | Zoek
---:|---
Beschrijving | Zoek naar notities en gebruikers op basis van ingevoerde woorden.
Actoren | Gebruiker, Reader
Voorwaarden | Geen
Happy Flow | De gebruiker of reader voert één zoekwoord in, de applicatie toont alle notities met dat woord in titel en/of body en gebruikers met dat woord in hun email adres of naam.

IC4 | Like
---:|---
Beschrijving | 'Like' een getoonde notitie
Actoren | Gebruiker
Voorwaarden | Gebruiker is ingelogd en heeft een notitie geselecteerd (middels NC2)
Happy Flow | De gebruiker geeft aan dat een getoonde notitie ge-liked moet worden. Noviaal slaat deze keuze op in de database.

IC5 | Unlike
---:|---
Beschrijving | Stop met 'like'n van een notitie
Actoren | Gebruiker
Voorwaarden | gebruiker is ingelogd en heeft een gelikede notitie geselecteerd
Happy Flow | De gebruiker geeft aan dat de getoonde, gelikede notitie niet meer geliked hoeft te worden. Noviaal verwijdert de like uit de database.

IC6 | TimeLine
---:|---
Beschrijving | Toon notities van gevolgde gebruikers en eigen notities op volgorde van laatste wijzigingsdatum.
Actoren | Gebruiker, Reader
Voorwaarden | Geen
Happy Flow | Dit is 'home' voor alle gebruikers en readers: meest recente notities bovenaan.

IC7 | Remind
---:|---
Beschrijving | Bij een getoonde notitie wordt een reminder aangemaakt
Actoren | Gebruiker
Voorwaarden | Gebruiker is ingelogd en een notitie werd geselecteerd.
Happy Flow | Bij de getoonde notitie wordt een reminder gezet: een datum en tijd waarop de gebruiker een melding krijgt dat deze notitie aandacht nodig heeft.
__NB__ | hoe de reminder aan de gebruiker wordt gecommuniceerd weet ik nog niet... Ik denk nu door een update voor timeline te sturen en dan in de timeline de notitie laten opvallen, zodat duidelijk is dat er iets bijzonders mee aan de hand is.


### Overzicht

OC1 | Zoekresultaten
---:|---
Beschrijving | Toon notities en gebruikers die gevonden werden naar aanleiding van een zoek-actie (IC3)
Actoren | Gebruiker
Voorwaarden | Een gebruiker (ingelogd of anoniem) heeft een zoekopdracht gegeven.
Happy Flow | Met het ingevoerde zoekwoord wordt door de titels en bodies van notities en emails en namen van gebruikers gezocht. De lijst met resultaten wordt getoond.
Alternatieve Flow | als er geen resultaten werden gevonden, dan wordt dat gemeld.

OC2 | Eigen notities
---:|---
Beschrijving | De eigen notities worden getoond op het detailinformatie scherm van de ingelogde gebruiker
Actoren | Gebruiker
Voorwaarden | Gebruiker is ingelogd en heeft gekozen voor het tonen van zijn eigen profiel (GC1)
Happy Flow | Alle eigen notities worden getoond, volgorde kan op datum/tijd of titel zijn.
Alternatieve Flow | Als de huidige gebruiker nog geen notities heeft, dan wordt dat gemeld.

OC3 | Liked notities
---:|---
Beschrijving | Toon alle notities die de huidige gebruiker heeft geliked
Actoren | Gebruiker
Voorwaarden | Gerbuiker is ingelogd
Happy Flow | De notities die geliked zijn door de huidige gebruiker worden getoond
Alternatieve Flow | Als er nog geen gelikede notities zijn, dan wordt dat gemeld.

OC4 | Reminders
---:|---
Beschrijving | Toon alle notities waarvoor de huidige gebruiker een reminder heeft gezet
Actoren | Gebruiker
Voorwaarden | Gebruiker is ingelogd
Happy Flow | De gebruiker selecteert deze optie, waarna alle notities met reminders getoond worden.
Alternatieve Flow | als er nog geen reminders ingesteld zijn, dan wordt dat gemeld.

OC5 | Gevolgden
---:|---
Beschrijving | Toon een lijst met alle gebruikers die de ingelogde gebruiker volgen
Actoren | Gebruiker (auteur, volgers)
Voorwaarden | De gebruiker is ingelogd
Happy Flow | De gebruiker kiest voor de lijst met volgers. Van iedere volger op de lijst zijn de details in te zien
Alternatieve flow | Als nog niemand gevolgd wordt, dan wordt dat gemeld.

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

<div style="page-break-after: always;"></div>

## Technisch Ontwerp (TO)

### Classes

![Class diagram](TO/class-diagram.png)

NB: met de eerste versie is het niet mogelijk om Tags te bewerken.

### C4 diagrammen

Zie vooral eerst [C4 Architectuur Model](https://c4model.com).

![Context diagram](FO/c4-context-diagram.png)

---

![Container diagram](FO/c4-container-diagram.png)

---

![Componenten diagram](TO/c4-component-diagram.png)

---

### Sequence diagrammen

![Nieuwe notitie](TO/seq-diags/seq-new-note.png)

Update notitie gaat vergelijkbaar als deze nieuwe notitie sequentie, maar dan met een bestaande notitie.

![Registreer nieuwe gebruiker](TO/seq-diags/registreer.png)

![Zoek](TO/seq-diags/zoek.png)

De meeste lijsten van notities en gebruikers werken op dezelfde manier: op basis van een query wordt een lijst met resultaten terug gegeven.

![Like](TO/seq-diags/like.png)

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
* zet de tag(s) van een eigen notitie (NC5).

#### Users

Voor alle gebruikers (ingelogd):
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

Voor alle ingelogde gebruikers:
* Toon de TimeLine: notities van gebruikers die ik (de huidige, ingelogde gebruiker) volg of ik een reminder voor heb gezet én mijn eigen notities.

### Voorwaarden

1. Installatie van Java 11 (meest recente versie) is aanwezig.
2. Docker is geinstalleerd en werkzaam.
3. Maven is geinstalleerd en werkzaam
4. het project Noviaal is ge-cloned van Github of
gedownload (als ZIP archief) van Teams.

### Hulpmiddelen etc.

[Java](https://jdk.java.net/11/), meest recente Long Term Supported (LTS) versie (nu: 11)
[Spring Boot](https://spring.io/projects/spring-boot), versie 2.4.0
[Spring Security](https://spring.io/projects/spring-security), versie 5.4.1

[Maven](https://maven.apache.org), versie 3.6.3
