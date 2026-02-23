📌 Application de Gestion des Réservations
📖 Description

Cette application  permet la gestion :

des salles

des équipements

des utilisateurs

des réservations

Elle met en œuvre :

Relation OneToMany avec orphanRemoval = true

Relation ManyToMany entre Salle et Equipement

Gestion des contraintes SQL (clé étrangère, unique, cascade)

Validation des données avec javax.validation

🔁 Génération automatique des tables (Hibernate)

Hibernate génère automatiquement les tables et les contraintes :
📸 Création des tables

<img width="611" height="340" alt="Screenshot 2026-02-23 214523" src="https://github.com/user-attachments/assets/829c54c9-792c-46b2-bc95-59b81f506e32" />
<img width="556" height="221" alt="Screenshot 2026-02-23 214531" src="https://github.com/user-attachments/assets/0c800d53-acc8-4c1e-acb6-6ec47ada71a5" />
<img width="695" height="748" alt="Screenshot 2026-02-23 214545" src="https://github.com/user-attachments/assets/d86f51a8-8b50-4934-a955-2d4a66b0056c" />
<img width="806" height="691" alt="Screenshot 2026-02-23 214558" src="https://github.com/user-attachments/assets/26546bbc-102f-4b56-b25b-a7fe60e2a37f" />
<img width="806" height="691" alt="Screenshot 2026-02-23 214558" src="https://github.com/user-attachments/assets/7f949afb-13ca-45f3-bf19-c272f98b329b" />
<img width="557" height="338" alt="Screenshot 2026-02-23 214609" src="https://github.com/user-attachments/assets/2224988c-25e3-48e9-80e7-f7109c67373a" />
ette capture montre l’insertion automatique d’un utilisateur dans la base de données.
Hibernate génère la requête SQL correspondante et utilise des paramètres préparés pour sécuriser l’insertion.

<img width="648" height="259" alt="Screenshot 2026-02-23 214618" src="https://github.com/user-attachments/assets/724fc559-2cdb-4217-83f5-9e560899cb2e" />


🗑️ Test de suppression orpheline (orphanRemoval)
🎯 Objectif

Lorsque l’on supprime une réservation de la liste d’un utilisateur :

utilisateur.getReservations().remove(reservation);

Grâce à :

@OneToMany(mappedBy = "utilisateur",
cascade = CascadeType.ALL,
orphanRemoval = true)

👉 La réservation est automatiquement supprimée de la base de données.
<img width="838" height="608" alt="Screenshot 2026-02-23 214630" src="https://github.com/user-attachments/assets/4eedc94e-0ae3-4834-a8bc-7997db439d66" />
<img width="760" height="756" alt="Screenshot 2026-02-23 214654" src="https://github.com/user-attachments/assets/b9506e1b-d7dd-4c2d-8a07-dc1ba6bffad3" />
<img width="791" height="663" alt="Screenshot 2026-02-23 214717" src="https://github.com/user-attachments/assets/9b47ac31-be9f-4f35-a3d4-0a039a187a7b" />
<img width="602" height="381" alt="Screenshot 2026-02-23 214730" src="https://github.com/user-attachments/assets/81a3fe75-f77e-432a-a72c-e89870739269" />


🔗 Test Relation ManyToMany (Salle ↔ Equipement)
🎯 Objectif

Une salle peut avoir plusieurs équipements.
Un équipement peut appartenir à plusieurs salles.

Hibernate crée automatiquement une table de jointure :
<img width="867" height="760" alt="Screenshot 2026-02-23 214751" src="https://github.com/user-attachments/assets/7824b99e-ef2a-4e1a-8568-6ad4059216b7" />
<img width="679" height="785" alt="Screenshot 2026-02-23 214814" src="https://github.com/user-attachments/assets/9b9f6aa4-6847-4475-a721-c124c2f3328f" />
<img width="830" height="497" alt="Screenshot 2026-02-23 214829" src="https://github.com/user-attachments/assets/3cd001da-fbc9-44b7-8608-6de4b8925339" />
<img width="740" height="737" alt="Screenshot 2026-02-23 214847" src="https://github.com/user-attachments/assets/4411f754-5175-4073-a3b0-5dc515223547" />
📸 Suppression automatique des tables
<img width="582" height="359" alt="Screenshot 2026-02-23 214912" src="https://github.com/user-attachments/assets/3f4f87c0-6957-4a0d-9fac-1388cab35b30" />

