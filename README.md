# API Produits

Petit projet pédagogique Spring Boot 3, Java 21, Maven et PostgreSQL. L'API gère une seule entité `Product` (`id`, `name`, `price`, `quantity`).

## Prérequis

- JDK 21
- Maven 3.6.3 ou plus récent
- PostgreSQL local accessible sur `localhost:5432`

## Préparer PostgreSQL

### macOS avec Homebrew (PostgreSQL 16)

À la racine du projet, exécuter :

```bash
bash run-local-mac.sh
```

Le script démarre PostgreSQL, demande un mot de passe local sans l'afficher, crée ou met à jour `productuser`, crée `productdb` si nécessaire, puis lance Spring Boot. Le mot de passe reste dans l'environnement du script pendant cette exécution. Relancer la même commande les fois suivantes ; arrêter l'application avec `Ctrl+C`.

Pour exécuter seulement le fichier SQL dans `psql` sur macOS :

```bash
brew services start postgresql@16
psql -d postgres
```

Puis, depuis la racine du projet :

```text
\set db_password 'votre-mot-de-passe-local'
\i sql/setup.sql
\q
```

Le fichier SQL peut être relancé : il crée les objets absents et met à jour le mot de passe de `productuser`. Pour lancer ensuite l'application, définir `DB_PASSWORD` avec le même mot de passe, puis exécuter `mvn spring-boot:run`.

### Préparation manuelle (exemple Ubuntu)

Démarrer le service PostgreSQL local selon votre installation, puis ouvrir un terminal avec un compte administrateur PostgreSQL. Sur Ubuntu, par exemple :

```bash
sudo systemctl start postgresql
sudo -u postgres psql
```

Dans `psql`, remplacer le mot de passe d'exemple par un mot de passe local personnel :

```sql
CREATE USER productuser WITH PASSWORD 'choisir-un-mot-de-passe-local';
CREATE DATABASE productdb OWNER productuser;
\q
```

La variable `DB_PASSWORD` doit contenir ce même mot de passe. Ne pas l'ajouter au dépôt :

```bash
export DB_PASSWORD='choisir-un-mot-de-passe-local'
```

L'application utilise `jdbc:postgresql://localhost:5432/productdb` et `productuser`. Hibernate met le schéma à jour au démarrage (`ddl-auto=update`), ce qui convient à ce projet pédagogique.

## Lancer l'application

```bash
mvn spring-boot:run
```

Swagger UI : <http://localhost:8080/swagger-ui.html>  
Description OpenAPI JSON : <http://localhost:8080/v3/api-docs>

## Endpoints

| Méthode | URL | Résultat |
| --- | --- | --- |
| GET | `/api/products` | Liste, `200` |
| GET | `/api/products/{id}` | Produit, `200`, ou `404` |
| POST | `/api/products` | Création, `201` et en-tête `Location` |
| PUT | `/api/products/{id}` | Modification, `200`, ou `404` |
| DELETE | `/api/products/{id}` | Suppression, `204`, ou `404` |

Exemple de corps JSON pour POST et PUT :

```json
{"name":"Stylo","price":2.50,"quantity":10}
```

`name` doit être renseigné ; `price` et `quantity` doivent être présents et supérieurs ou égaux à zéro. Une entrée invalide retourne `400`.

## Tests et JAR

Les tests du service utilisent un dépôt simulé et n'exigent pas de base PostgreSQL :

```bash
mvn test
mvn clean package
```

Le JAR exécutable est généré dans `target/products-api-0.0.1-SNAPSHOT.jar`. Pour le lancer avec PostgreSQL prêt et `DB_PASSWORD` défini :

```bash
java -jar target/products-api-0.0.1-SNAPSHOT.jar
```
