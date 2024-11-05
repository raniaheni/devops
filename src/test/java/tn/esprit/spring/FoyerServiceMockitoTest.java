package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.Services.Foyer.FoyerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




@ExtendWith(MockitoExtension.class)
public class FoyerServiceMockitoTest {

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private FoyerService foyerService;

    @Test
    public void testFindAll() {
        // Préparer les données simulées
        Foyer foyer1 = new Foyer();
        foyer1.setIdFoyer(1L);
        foyer1.setNomFoyer("Foyer 1");
        foyer1.setCapaciteFoyer(100);

        Foyer foyer2 = new Foyer();
        foyer2.setIdFoyer(2L);
        foyer2.setNomFoyer("Foyer 2");
        foyer2.setCapaciteFoyer(200);
        List<Foyer> foyers = Arrays.asList(foyer1, foyer2);

        // Simuler le comportement de la méthode findAll de foyerRepository
        when(foyerRepository.findAll()).thenReturn(foyers);

        // Appeler la méthode que vous testez
        List<Foyer> result = foyerService.findAll();

        // Vérifier les résultats
        assertEquals(2, result.size());
        assertEquals("Foyer 1", result.get(0).getNomFoyer());
        assertEquals("Foyer 2", result.get(1).getNomFoyer());
        verify(foyerRepository, times(1)).findAll(); // Vérifier que la méthode a été appelée une fois
    }

    @Test
    public void testFindById() {
        // Créer un foyer pour le test
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Test Foyer");

        // Simuler le comportement du repository
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        // Appel de la méthode de service
        Foyer foundFoyer = foyerService.findById(1L);

        // Vérifications
        assertNotNull(foundFoyer);
        assertEquals("Test Foyer", foundFoyer.getNomFoyer());
        verify(foyerRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateFoyerMock() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Test Foyer");

        when(foyerRepository.save(any(Foyer.class))).thenReturn(foyer);

        Foyer updatedFoyer = foyerService.addOrUpdate(foyer);

        assertNotNull(updatedFoyer);
        assertEquals("Test Foyer", updatedFoyer.getNomFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }

}
