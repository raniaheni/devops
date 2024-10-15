package tn.esprit.spring.Services.Etudiant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Used for testing JPA repositories
class EtudiantRepositoryTest {

    @Autowired
    private EtudiantRepository etudiantRepository;

    private Etudiant etudiant;

    @BeforeEach
    void setup() {
        // Initialize a new Etudiant entity
        etudiant = Etudiant.builder()
                .nomEt("John")
                .prenomEt("Doe")
                .cin(12345678L)
                .ecole("ESPRIT")
                .dateNaissance(LocalDate.of(1995, 10, 10))
                .build();
    }

    @Test
    void testSaveEtudiant() {
        // Test saving the entity
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);

        assertThat(savedEtudiant.getIdEtudiant()).isNotNull();
        assertThat(savedEtudiant.getNomEt()).isEqualTo("John");
        assertThat(savedEtudiant.getPrenomEt()).isEqualTo("Doe");
        assertThat(savedEtudiant.getCin()).isEqualTo(12345678L);
    }

    @Test
    void testFindById() {
        // Test finding an entity by ID
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);

        Etudiant foundEtudiant = etudiantRepository.findById(savedEtudiant.getIdEtudiant()).orElse(null);
        assertThat(foundEtudiant).isNotNull();
        assertThat(foundEtudiant.getNomEt()).isEqualTo("John");
        assertThat(foundEtudiant.getPrenomEt()).isEqualTo("Doe");
    }

    @Test
    void testUpdateEtudiant() {
        // Save initial Etudiant
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);

        // Update some fields
        savedEtudiant.setNomEt("Jane");
        Etudiant updatedEtudiant = etudiantRepository.save(savedEtudiant);

        assertThat(updatedEtudiant.getNomEt()).isEqualTo("Jane");
        assertThat(updatedEtudiant.getPrenomEt()).isEqualTo("Doe");
    }

    @Test
    void testDeleteEtudiant() {
        // Save Etudiant
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);

        // Delete the Etudiant
        etudiantRepository.deleteById(savedEtudiant.getIdEtudiant());

        // Verify that the Etudiant no longer exists
        assertThat(etudiantRepository.findById(savedEtudiant.getIdEtudiant())).isEmpty();
    }

    @Test
    void testFindAll() {
        // Save multiple Etudiant entities
        etudiantRepository.save(etudiant);
        etudiantRepository.save(Etudiant.builder().nomEt("Alice").prenomEt("Wonder").cin(98765432L).ecole("UML").dateNaissance(LocalDate.of(1998, 3, 15)).build());

        List<Etudiant> etudiants = etudiantRepository.findAll();

        assertThat(etudiants).hasSize(2);
    }
}
