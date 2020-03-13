[ het klinkt als een excuus en dat is het ook, maar het logboek heb ik bijgehouden in een apart word-document. Ik stond er niet bij stil dat het handig was geweest na elke ‘werkdag’ een update te schrijven en in de repo te plaatsen]

07.11.2019 
* Er staat inderdaad november, dit was nog van de keer dat ik het vak eerder volgde. Ik ben namelijk verder gegaan met het idee wat ik had.

Nagedacht over het gebruiken van FireBase omdat ik dat de vorige keer ook heb gedaan maar uiteindelijk toch niet omdat het waarschijnlijk meer kost dan het oplevert. Daarom kijken naar Room.

03.02.2020
Ervoor gekozen toch niet Room te gebruiken. Het ziet er wel goed uit, maar   heb zelf meer ervaring met SQLite wat het makkelijk maakt ermee om te gaan.

07.02.2020
Verder: besloten de MVP te veranderen. Na bespreking toch best wel veel, vandaar dat inperking beter is. Kan altijd nog meer doen als het meevalt. 
TODO: readme en designdoc updaten

16.02.2020
In eerste instantie, geen rekening houden met keukens, maar alleen met venues die rekening houden met de dieetvoorkeuren weergeven (misschien dat het later weer toegevoegd kan worden).

Een andere strategie bedacht voor het handiger extraheren van benodigde data om dat ik erachter kwam dat er eigenlijk twee requests nodig zijn.
1st request: voor alle venues_ids, en lat, lon en distance verkrijgen
2nd request (gebaseerd op venue_id): meer details over de specifieke venue

22.02.2020
Erachter gekomen hoe het filter werkt. Dacht eerst dat voorkeuren vielen onder ‘diets’, maar dat bleek ‘tags’ te zijn. Dit hielp erg met het in orde maken van de zoekrequest.

26.02.2020
Iets met: gebruikers ook uniek maken op gebruikersnaam. Dit wordt best vaak ook in andere apps gedaan en maakt het wat makkelijker met het ophalen van gebruikersgegevens.

01.03.2020
Besloten om na het inloggen of registreren meteen al een zoekopdracht te doen gebaseerd op de locatie van de gebruiker

02.03.2020
Geen e-mail voor nodig voor wat het doet om eerlijk te zijn. Eruit gehaald.
Geen ‘deel’ functie wegens te weinig tijd. Wel kun je het bellen makkelijker maken, dan hoef je niet ongemakkelijk zo’n heel nummer te onthouden.

05.03.2020
Besloten meteen al een request te maken op MapsActivity. Zie geen aanleiding om ‘helemaal’ nieuwe schermen te maken als het ook zo kan.

Gebruikersnamen zijn uniek. Dit is best gewoon en sneller bij het extraheren van records uit de database.

Afhankelijk van de combinatie tussen locatie en eetwensen kan het voorkomen dat er geen resultaten zijn die voldoen aan het filter. Wat zou er dan moeten worden weergeven? Denk hierover na. Verder de rest van de app ook waterdicht maken e.d.

07.03.2020
Presentatie hoeft waarschijnlijk niet. Dus extra tijd voor de screencast en het netjes maken van de repository. 

Met voorkeuren aanpassen en een filter voor keukens moet het in principe voldoende zijn. (Martijn heeft dat ook gezegd).

Functie: wachtwoord wijzigen is eruit gehaald omdat het een erg makkelijke en in die zin toegankelijk applicatie is waardoor het naar mijn inziens niet veel zin heeft deze functionaliteit in de app te hebben.

09.03.2020
Had wat struggles met het opslaan van preferences. Idealiter gezien wilde ik het opslaan in een lijst. Maar SQLite werkt niet zo goed met lijsten in een database opslaan. Deze string wordt zo opgeslagen in de databasetabel, ook als User instance worden de preferenties opgeslagen als string. Alleen wanneer de preferenties weergeven worden is het makkelijker de string eerst te converteren naar lijst, dat is ook gedaan.
