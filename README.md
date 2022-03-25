# Management-of-a-Connected-Restaurant-Spring-JavaFx-

Présentation du projet : 

- Ce projet vise à développer une plateforme/solution IoT pour la gestion d'un restaurant 
équipé de dispositifs IoT, et permet plus particulièrement : 
  1. La gestion des commandes.
  
  2. La détection des clients dans l'espace, qui consiste à savoir si un nouveau client est 
arrivé ou si un autre est parti. 
  
  3. La gestion de la température, qui consiste à visualiser la température pour pouvoir 
l'ajuster en fonction de nos besoins. 
  
  4. La gestion de l'énergie, qui consiste à visualiser et à contrôler la consommation 
d'énergie.
  
  5. Surveillance de la qualité de l'air.

Les technologies utilisées : 
Spring boot, JavaFx, Css,  MySql, Protocol MQTT, JSON,  [Simulateur de données IoT(IOT-DATA-SIMULATOR by IBA Group)](https://github.com/IBA-Group-IT/IoT-data-simulator).

Architecture de l’application : 

![Archit3](https://user-images.githubusercontent.com/70114659/159466462-9cd2e8e9-d3ab-4944-a54c-5ceb4b825bde.png)

(1) - Dans le IOT-DATA-SIMULATOR, nous avons quatre types de capteurs qui sont 
simulés chaque type a son propre pattern pour générer des données : 
o Capteurs de température/humidité qui publient leurs données sur le topic “DbAir”.
o Capteurs de qualité de l'air qui publient leurs données sur le topic “DbAir”.
o Capteurs d'énergie qui publient leurs données sur le topic “DbEnergy”.
o Capteurs de résistance à la force qui publient leurs données sur le topic “DbCustomer” 

(2) - Lorsque le broker reçoit les payloads publiées dans les différents topics de l'IOT-DATA-SIMULATOR, il envoie le payload de chaque topic aux classes de l’application qui sont abonnées à ce topic.

(3) - Ensuite, lorsque chaque classe reçoit les données, elle les interprète et les affiche sur l'interface utilisateur sous une forme particulière (graphiques, tuiles, etc.…).

#### Présentation des interfaces

  1. IOT-DATA-SIMULATOR

https://user-images.githubusercontent.com/60849617/160182273-3438fb95-d22e-4345-991e-4999f4b52a28.mp4

2. Authentification 

https://user-images.githubusercontent.com/60849617/160182379-602db010-0e8a-48dd-b662-6bae1663aef0.mp4

3. Customer Area

https://user-images.githubusercontent.com/60849617/160182594-1cc0ddfc-0885-4c2f-96c1-60c91aa71bcf.mp4

4. Air Quality Dashboard

https://user-images.githubusercontent.com/60849617/160182772-f499afb6-fdfb-4ccb-b37e-030b462edde7.mp4

5. Energy Dashboard

https://user-images.githubusercontent.com/60849617/160182909-320e7e55-a9ba-45d1-ae7e-fdf28312ca2c.mp4

6. Statistics Dashboard

https://user-images.githubusercontent.com/60849617/160183097-3236ef79-54be-487b-a4f8-4c1b2266a78f.mp4

7. Alertes example

https://user-images.githubusercontent.com/60849617/160183411-1eb85c24-3c50-4d7f-8d65-fb3b9c5c2c05.mp4

