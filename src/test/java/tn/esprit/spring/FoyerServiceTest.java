package tn.esprit.spring;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Foyer.FoyerService;
import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FoyerServiceTest {

    @Autowired
    private FoyerRepository foyerRepository; // Injecter le repository

    private FoyerService foyerService; // Votre service à tester

    @Autowired
    private UniversiteRepository universiteRepository; // Inject UniversiteRepository

    @Autowired
    private BlocRepository blocRepository; // Inject BlocRepository

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

}
