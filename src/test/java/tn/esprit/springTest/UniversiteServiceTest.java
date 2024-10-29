package tn.esprit.springTest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.FoyerApplication;
import tn.esprit.spring.Services.Universite.IUniversiteService;
import tn.esprit.spring.Services.Universite.UniversiteService;

import java.util.List;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = FoyerApplication.class)
public class UniversiteServiceTest {
    @Autowired
    IUniversiteService universiteService;
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

        this.u = universiteService.addOrUpdate(this.u);
    }

    @Test
    @Order(3)
    public void TestfindAll() {
        universiteService.addOrUpdate(this.u);
        universiteService.findAll();
    }

    @Test
    @Order(2)
    public void TestfindById() {
        universiteService.addOrUpdate(this.u);
        universiteService.findById(this.u.getIdUniversite());
    }

    @Test
    @Order(4)
    public void TestdeleteById() {
        universiteService.addOrUpdate(this.u);
        universiteService.deleteById(this.u.getIdUniversite());
    }

    @Test
    @Order(5)
    public void Testdelete() {
        universiteService.addOrUpdate(this.u);
        universiteService.delete(this.u);
    }
}
