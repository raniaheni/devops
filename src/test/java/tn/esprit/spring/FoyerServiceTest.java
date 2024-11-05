package tn.esprit.spring;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Foyer.FoyerService;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer. OrderAnnotation.class)
@SpringBootTest
public class FoyerServiceTest {

    @Autowired
    private FoyerRepository foyerRepository;
    private FoyerService foyerService;

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired
    private BlocRepository blocRepository;

    @BeforeEach
    public void setUp() {
        foyerService = new FoyerService(foyerRepository, universiteRepository, blocRepository);
    }

    @Test
    public void testAddOrUpdateFoyer() {
        // Arrange
        Foyer foyer = Foyer.builder()
                .nomFoyer("Foyer Test Junit")
                .capaciteFoyer(50)
                .build();

        // Act
        Foyer savedFoyer = foyerService.addOrUpdate(foyer);

        // Assert
        assertTrue(savedFoyer.getIdFoyer() > 0, "L'ID du foyer enregistré doit être supérieur à 0");
        assertEquals(foyer.getNomFoyer(), savedFoyer.getNomFoyer());
        assertEquals(foyer.getCapaciteFoyer(), savedFoyer.getCapaciteFoyer());
    }

    @Test
    public void testDeleteById() {
        // Arrange
        Foyer foyer = Foyer.builder()
                .nomFoyer("Foyer à supprimer")
                .capaciteFoyer(50)
                .build();
        Foyer savedFoyer = foyerService.addOrUpdate(foyer);

        // Act
        foyerService.deleteById(savedFoyer.getIdFoyer());

        // Assert
        assertFalse(foyerRepository.findById(savedFoyer.getIdFoyer()).isPresent());
    }


    @Test
    public void testFindByIdNotFound() {
        // Act
        Foyer foundFoyer = foyerService.findById(999L); // ID qui n'existe pas

        // Assert
        assertNull(foundFoyer);
    }

    @Test
    public void testAffecterFoyerAUniversite() {
        // Arrange
        Universite universite = Universite.builder()
                .nomUniversite("Université Test")
                .adresse("test")
                .build();
        Universite savedUniversite = universiteRepository.save(universite); // Enregistrement de l'université

        Foyer foyer = Foyer.builder()
                .nomFoyer("Foyer à affecter")
                .capaciteFoyer(100)
                .build();
        Foyer savedFoyer = foyerRepository.save(foyer); // Enregistrement du foyer

        // Act
        Universite updatedUniversite = foyerService.affecterFoyerAUniversite(savedFoyer.getIdFoyer(), savedUniversite.getNomUniversite());

        // Assert
        assertNotNull(updatedUniversite, "L'université ne doit pas être null après l'affectation.");
        assertEquals(savedFoyer.getIdFoyer(), updatedUniversite.getFoyer().getIdFoyer(),
                "Le foyer affecté à l'université doit être le foyer enregistré.");
    }


}
