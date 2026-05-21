# TP-remise
# Rapport Synthétique – Projet de Calcul de Remise (Spring Boot)

## 1. Objectif du projet

Ce projet vise à implémenter une application Spring Boot capable de :

* Calculer une remise à partir de règles stockées en base de données.
* Enregistrer chaque calcul sous forme de transaction.
* Exposer une API REST permettant de créer et consulter ces transactions.

Le système repose sur une architecture en couches séparant les responsabilités (contrôleur, service, accès aux données).
---
2. Architecture (point clé)
Controller → Service → JdbcTemplate → Base de données
        ↑                        ↓
      DTO (entrée/sortie uniquement)

Important :

Pas de Repository
Service = logique métier + accès DB
---
3. Rôles essentiels
Controller → reçoit requêtes HTTP, appelle service
Service → logique + requêtes SQL (via JdbcTemplate)
Model → structure des données (Transaction, Remise)
DTO → interface API (entrée/sortie uniquement)
Exception → transforme erreurs en HTTP 400/500
---
4. Base de données
REMISE

Règles de calcul :

montant_min | montant_max | taux
TRANSACTION

Historique :

id | date | montant_avant | montant_apres | remise_id
---
5. Point technique important
JdbcTemplate
Exécute SQL manuellement
Remplace les Repository (JPA)
Nécessite un RowMapper pour convertir DB → objet

Idée clé :

JPA → automatique
JdbcTemplate → contrôle total
---
6. requètes actuelle
POST   /api/transactions          → create transaction 
GET    /api/transactions/{id}     → get by id         
GET    /api/transactions          → get all           
DELETE /api/transactions/{id}     → delete            
GET    /api/remises/calculer      → calculate only    
