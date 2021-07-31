# 🖥️ Development of GraphQL Web Services in Spring and Adobe Experience Manager
Master thesis on topic "Developing GraphQL Web Services in Spring and Adobe Experience Manager". Project will contain REST api build in Spring framework for receiving data about earthquakes with possibility of local data caching. Local data cache will be achieved will background thread contacting external API and filling local database using prebuild REST service. Build web service will be used as a backbone service for GraphQL endpoint, which will provide specific information to received query. As an addition to the master thesis content, new technology will be used in comparison with spring, it's called Adobe Experience Manager [AEM]. AEM is an enterprise content managment system, with a rich technology stack and high level of customization.  
## 🍃 Spring framework
Usage of Spring framework for creation of component for filling up local database with earthquake information and providing REST api calls for CRUD operations for following data insertion and fetching from external API. REST api should provide data access layer for data fetching on top of GraphQL endpoint.
### 🔧 Prerequirements
Following dependencies are required to run Spring Boot.
* Lombok
* Spring Web
* ThymeLeaf
* Spring Data JPA
* MySQL Driver
### 🔚 REST APIs
#### Potresi
* **{GET}** /api/v1/potresi (limit) - dohvaćanje svih potresa uz mogućnost uvjetnog dohvaćanja limitiranog broja potresa
* **{GET}** /api/v1/potresi/_{eventId}_ - dohvaćanje jednog potresa prema ID
* **{PUT}** /api/v1/potresi/azuriraj - ažuriranje postojećeg potresa 
* **{DELETE}** /api/v1/potresi/_{eventId}_ - brisanje postojećeg potresa

#### Mjesta
* **{GET}** /api/v1/mjesto - dohvaćanje svih mjesta na kojim su se dogodili potresi
* **{GET}** /api/v1/mjesto/_{iso}_ - dohvaćanje mjesta na kojem se je dogodio potres

#### Korisnici 
* **{GET}** /api/v1/korisnici - dohvaćanje svih korisnika
* **{GET}** /api/v1/korisnici/_{email}_ - dohvaćanje korisnika po emailu
* **{POST}** /api/v1/korisnici/dodaj - dodavanje novog korisnika preko tijela zahtjeva
* **{POST}** /api/v1/korisnici/dodajNovog - dodavanje novog korisnika preka URI-a
* **{PUT}** /api/v1/korisnici - ažuriranje postojećeg korisnika
* **{DELETE}** /api/v1/korisnici/izbrisi/_{id}_ - brisanje postojećeg korisnika
### 💹 GraphQL endpoint
#### Spring GraphQL endpoint
* **{POST}** /api/v1/potresi/graphql - krajnja mrežna točka za dohvaćanje graphql odgovora
## Adobe Experience Manager
* **{GET}** /bin/v1/potresi/svi - dohvaćanje svih potresa
* **{GET}** /bin/v1/potres?id=_{id}_ - dohvaćanje potresa prema ID-u
* **{POST}** /bin/v1/potresi/brisanje - brisanje potresa prema ID-u
* **{POST}** /bin/v1/graphql - krajnja mrežna točka za dohvaćanje graphql odgovora
