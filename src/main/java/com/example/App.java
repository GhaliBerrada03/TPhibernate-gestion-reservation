package com.example;

import com.example.model.Equipement;
import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Création de l'EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion-reservations");

        try {
            // Test des relations et des opérations en cascade
            System.out.println("\n=== Test des relations et des opérations en cascade ===");
            testRelationsEtCascade(emf);

            // Test de la suppression orpheline
            System.out.println("\n=== Test de la suppression orpheline ===");
            testSuppressionOrpheline(emf);

            // Test de la relation ManyToMany avec Équipement
            System.out.println("\n=== Test de la relation ManyToMany avec Équipement ===");
            testRelationManyToMany(emf);

        } finally {
            // Fermeture de l'EntityManagerFactory
            emf.close();
        }
    }

    private static void testRelationsEtCascade(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Création des entités
            System.out.println("Création des entités...");

            // Création d'un utilisateur
            Utilisateur utilisateur = new Utilisateur("Berrada", "Ghali", "ghali.berrada@gmail.com");

            // Création d'une salle
            Salle salle = new Salle("Salle 01", 30);
            salle.setDescription("Salle de meeting équipée d'un datashow");

            // Création d'une réservation
            Reservation reservation = new Reservation(
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(1).plusHours(2),
                    "meeting d'équipe"
            );

            // Établissement des relations
            utilisateur.addReservation(reservation);
            salle.addReservation(reservation);

            // Persistance de l'utilisateur avec cascade sur la réservation
            em.persist(utilisateur);
            em.persist(salle);

            em.getTransaction().commit();
            System.out.println("Entités créées et liées avec succès !");

            // Vérification des entités persistées
            em.clear(); // Vider le contexte de persistance

            System.out.println("\nVérification des entités persistées :");
            Utilisateur utilisateurPersiste = em.find(Utilisateur.class, utilisateur.getId());
            System.out.println("Utilisateur : " + utilisateurPersiste);
            System.out.println("Nombre de réservations : " + utilisateurPersiste.getReservations().size());

            Salle sallePersistee = em.find(Salle.class, salle.getId());
            System.out.println("Salle : " + sallePersistee);
            System.out.println("Nombre de réservations : " + sallePersistee.getReservations().size());

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void testSuppressionOrpheline(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Utilisateur utilisateur = new Utilisateur("Mohamed", "MED", "Mohamed.MED@example.com");

            Salle salle1 = new Salle("Salle 02", 22);
            em.persist(salle1);

            Salle salle2 = new Salle("Salle 03", 25);
            em.persist(salle2);

            Reservation reservation1 = new Reservation(
                    LocalDateTime.now().plusDays(2),
                    LocalDateTime.now().plusDays(2).plusHours(1),
                    "Entretien"
            );

            Reservation reservation2 = new Reservation(
                    LocalDateTime.now().plusDays(3),
                    LocalDateTime.now().plusDays(3).plusHours(2),
                    "Formation"
            );

            // Établissement des relations
            utilisateur.addReservation(reservation1);
            utilisateur.addReservation(reservation2);
            salle1.addReservation(reservation1);
            salle2.addReservation(reservation2);

            em.persist(utilisateur);

            em.getTransaction().commit();
            System.out.println("Utilisateur avec 2 réservations créé !");

            em.getTransaction().begin();

            Utilisateur utilisateurAModifier = em.find(Utilisateur.class, utilisateur.getId());
            System.out.println("Nombre de réservations avant suppression : " + utilisateurAModifier.getReservations().size());

            Reservation reservationASupprimer = utilisateurAModifier.getReservations().get(0);
            utilisateurAModifier.removeReservation(reservationASupprimer);

            em.getTransaction().commit();

            // Vérification de la suppression
            em.clear();
            Utilisateur utilisateurApresModification = em.find(Utilisateur.class, utilisateur.getId());
            System.out.println("Nombre de réservations après suppression : " + utilisateurApresModification.getReservations().size());

            // Vérification que la réservation a bien été supprimée de la base de données
            Long reservationId = reservationASupprimer.getId();
            Reservation reservationSupprimee = em.find(Reservation.class, reservationId);
            System.out.println("La réservation existe-t-elle encore ? " + (reservationSupprimee != null));

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void testRelationManyToMany(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Création des équipements
            Equipement projecteur = new Equipement("DataShow", "DataShow 4K");
            Equipement ecran = new Equipement("Écran intelligente", "Écran avec 55 pouces");
            Equipement visioconference = new Equipement("Système visio", "Système inclus caméra 4k");

            // Création des salles
            Salle salleReunion = new Salle("Salle de conference 04", 65);
            Salle salleFormation = new Salle("Salle d'etudes 05", 50);

            salleReunion.addEquipement(projecteur);
            salleReunion.addEquipement(visioconference);

            salleFormation.addEquipement(projecteur);
            salleFormation.addEquipement(ecran);

            em.persist(salleReunion);
            em.persist(salleFormation);

            em.getTransaction().commit();
            System.out.println("Salles et équipements créés avec succès !");

            // Vérification des relations
            em.clear();

            System.out.println("\nVérification des relations ManyToMany :");

            // Récupération des salles
            Salle salleReunionPersistee = em.find(Salle.class, salleReunion.getId());
            System.out.println("Salle : " + salleReunionPersistee.getNom());
            System.out.println("Équipements :");
            for (Equipement equipement : salleReunionPersistee.getEquipements()) {
                System.out.println("- " + equipement.getNom());
            }

            Salle salleFormationPersistee = em.find(Salle.class, salleFormation.getId());
            System.out.println("\nSalle : " + salleFormationPersistee.getNom());
            System.out.println("Équipements :");
            for (Equipement equipement : salleFormationPersistee.getEquipements()) {
                System.out.println("- " + equipement.getNom());
            }

            // Récupération d'un équipement et affichage des salles associées
            Equipement projecteurPersiste = em.createQuery(
                            "SELECT e FROM Equipement e WHERE e.nom = :nom", Equipement.class)
                    .setParameter("nom", "Projecteur")
                    .getSingleResult();

            System.out.println("\nÉquipement : " + projecteurPersiste.getNom());
            System.out.println("Salles équipées :");
            for (Salle salle : projecteurPersiste.getSalles()) {
                System.out.println("- " + salle.getNom());
            }

            // Test de suppression d'un équipement d'une salle
            em.getTransaction().begin();

            salleReunionPersistee.removeEquipement(projecteurPersiste);

            em.getTransaction().commit();

            // Vérification après suppression
            em.clear();

            Salle salleApresModification = em.find(Salle.class, salleReunion.getId());
            System.out.println("\nSalle après suppression d'un équipement : " + salleApresModification.getNom());
            System.out.println("Équipements restants :");
            for (Equipement equipement : salleApresModification.getEquipements()) {
                System.out.println("- " + equipement.getNom());
            }

            // Vérification que l'équipement existe toujours
            Equipement projecteurApresModification = em.find(Equipement.class, projecteurPersiste.getId());
            System.out.println("\nL'équipement existe-t-il encore ? " + (projecteurApresModification != null));

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}