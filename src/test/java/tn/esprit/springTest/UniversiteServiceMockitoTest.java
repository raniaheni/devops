package tn.esprit.springTest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.FoyerApplication;
import tn.esprit.spring.Services.Universite.IUniversiteService;
import tn.esprit.spring.Services.Universite.UniversiteService;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = FoyerApplication.class)
class UniversiteServiceMockitoTest {
    @InjectMocks
    private UniversiteService universiteService;
    @Mock
    private UniversiteRepository universiteRepository;

    Universite u = new Universite();

    @BeforeEach
    public void setUp() {
        // Creating a Foyer object
        Foyer foyer = Foyer.builder()
                .nomFoyer("Foyer Central")
                .build();

        // Creating a Universite object
        this.u = Universite.builder()
                .nomUniversite("Esprit University")
                .adresse("Ariana, Tunisia")
                .foyer(foyer)
                .build();
    }

    @Test
    @Order(1)
    public void TestaddOrUpdate() {

        Mockito.when(universiteRepository.save(Mockito.any(Universite.class)))
                .thenReturn(u);

        this.u = universiteService.addOrUpdate(this.u);

        Mockito.verify(universiteRepository).save(Mockito.any(Universite.class));

    }

    @Test
    @Order(3)
    public void TestfindAll() {
        universiteService.findAll();
    }

    @Test
    @Order(2)
    public void TestfindById() {
        Mockito.when(universiteRepository.save(Mockito.any(Universite.class)))
                .thenReturn(u);

        this.u = universiteService.addOrUpdate(this.u);

        Mockito.when(universiteRepository.findById(u.getIdUniversite()))
                .thenReturn(Optional.of(u));

        universiteService.findById(this.u.getIdUniversite());

        Mockito.verify(universiteRepository).save(Mockito.any(Universite.class));
    }

    @Test
    @Order(4)
    public void TestdeleteById() {
        universiteService.deleteById(this.u.getIdUniversite());
    }

    @Test
    @Order(5)
    public void Testdelete() {
        universiteService.delete(this.u);
    }
}
