# Noviaal - Use Cases (Functioneel Ontwerp)

Dit document bevat de Use Cases die de functionaliteit van het BackEnd eindopdracht project "Noviaal" beschrijven.

Noviaal maakt het mogelijk voor geregistreerde gebruikers om aantekeningen, Notes, te maken, wijzigen, bekijken en verwijderen.

## Actoren

Er worden drie rollen van gebruikers (Person) onderscheiden in het Noviaal systeem:

1. __User__, de normale, geregistreerde gebruiker, die vrijwel alle functies kan uitvoeren.
2. __Admin__, de super gebruiker, die rapportages kan aanmaken en bekijken.
3. __Reader__, een niet-geregistreerde gebruiker, die alleen de publieke tijdlijn van Users kan zien.

Alles wat een User kan, kan een Admin ook.

Een Person wordt geïdentificeerd met zijn of haar email adres, dat dus uniek moet zijn binnen de bekende gebruikers van Noviaal.

## Notes (notities)

Een Note is een tekst met een titel. Een Note kan publiekelijk zichtbaar zijn, zodat ook Readers de Note kunnen lezen, alleen voor volgers (Followers) of privé (Private).

### Commentaar (comments)

Bij een Note kunnen Users aantekeningen maken, behalve als de Note privé is. Commentaar kan ook weer op commentaar gegeven worden, zodat er een hierarchie van commentaar kan zijn.

### Reminder

Een User kan een herinnering, datum en tijd, zetten op een bepaalde Note.

### Like

Een User kan een Note liken. Liked notes kunnen opgehaald worden. Liked notes kunnen ge-unliked worden.



## Use Cases

### Authenticatie

#### Links

* [Spring Security - Authentication](https://docs.spring.io/spring-security/site/docs/5.4.1/reference/html5/#servlet-authentication)
* [Registratie en Login](https://medium.com/@kamer.dev/spring-boot-user-registration-and-login-43a33ea19745)

ID | AC1
---:|:---
Naam | Registreer nieuwe gebruiker
Beschrijving | Om alle functionaliteit van Noviaal te gebruiken moet een persoon eerst registreren
Actoren | Reader
Voorwaarden | Een persoon registreert met een uniek email adres, een naam en een wachtwoord
Happy flow | Een nog niet geregistreerde gebruiker maakt duidelijk dat hij of zij wil registreren. De aanstaande User voert een email adres in, een naam (geheel vrij, maar wel verplicht) en een password (twee maal). De nieuwe User wordt vastgelegd in de database.

ID | AC2
---:|:---
Naam | Login
Beschrijving | Een eerder geregistreerde gebruiker logt in door zijn of haar email adres en password in te voeren.
Actoren | User
Voorwaarden | Om in te kunnen loggen moet een User eerder geregistreerd zijn en de juiste combinatie van email adres en password invoeren
Happy Flow |

