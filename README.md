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
Spring boot, JavaFx, Css,  MySql, Protocol MQTT, JSON,  Simulateur de données IoT(IOT-DATA-SIMULATOR by IBA Group).

Architecture de l’application : 

![Archit3](https://user-images.githubusercontent.com/70114659/159466462-9cd2e8e9-d3ab-4944-a54c-5ceb4b825bde.png)

(1) - Dans le IOT-DATA-SIMULATOR, nous avons quatre types de capteurs qui sont 
simulés chaque type a son propre pattern pour générer des données : 
o Capteurs de température/humidité qui publient leurs données sur le topic “DbAir”.
o Capteurs de qualité de l'air qui publient leurs données sur le topic “DbAir”.
o Capteurs d'énergie qui publient leurs données sur le topic “DbEnergy”.
o Capteurs de résistance à la force qui publient leurs données sur le topic “DbCustomer” 
(2) - Lorsque le broker reçoit les payloads publiées dans les différents topics de l'IOT-DATASIMULATOR, il envoie le payload de chaque topic aux classes de l’application qui sont 
abonnées à ce topic.
(3) - Ensuite, lorsque chaque classe reçoit les données, elle les interprète et les affiche sur 
l'interface utilisateur sous une forme particulière (graphiques, tuiles, etc.…).

Présentation des interfaces : 


