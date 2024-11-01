package tn.esprit.tn;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.Services.Chambre.ChambreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class FoyerApplicationTest {
    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreService chambreService;

    private Chambre chambre;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chambre = Chambre.builder()
                .idChambre(1L)
                .numeroChambre(101)
                .typeC(TypeChambre.SIMPLE)
                .build();
    }

    @Test
    void testAddOrUpdate() {
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        Chambre result = chambreService.addOrUpdate(chambre);

        assertNotNull(result);
        assertEquals(chambre.getIdChambre(), result.getIdChambre());
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testFindAll() {
        List<Chambre> chambres = new ArrayList<>();
        chambres.add(chambre);

        when(chambreRepository.findAll()).thenReturn(chambres);

        List<Chambre> result = chambreService.findAll();

        assertEquals(1, result.size());
        assertEquals(chambre, result.get(0));
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(chambreRepository.findById(anyLong())).thenReturn(Optional.of(chambre));

        Chambre result = chambreService.findById(1L);

        assertNotNull(result);
        assertEquals(chambre.getIdChambre(), result.getIdChambre());
        verify(chambreRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        doNothing().when(chambreRepository).deleteById(anyLong());

        chambreService.deleteById(1L);

        verify(chambreRepository, times(1)).deleteById(1L);
    }

}
