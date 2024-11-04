
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.Services.Bloc.BlocService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class BlocServiceTest {

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private BlocService blocService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddOrUpdate() {
        Bloc bloc = new Bloc();
        List<Chambre> chambres = new ArrayList<>();
        chambres.add(new Chambre());
        bloc.setChambres(chambres);

        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.addOrUpdate(bloc);

        assertNotNull(result);
        verify(blocRepository, times(1)).save(bloc);
        verify(chambreRepository, times(1)).save(any(Chambre.class));
    }

    @Test
    public void testFindAll() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(new Bloc());
        when(blocRepository.findAll()).thenReturn(blocs);

        List<Bloc> result = blocService.findAll();

        assertEquals(1, result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        long id = 1L;
        Bloc bloc = new Bloc();
        when(blocRepository.findById(id)).thenReturn(Optional.of(bloc));

        Bloc result = blocService.findById(id);

        assertNotNull(result);
        verify(blocRepository, times(1)).findById(id);
    }

    @Test
    public void testDeleteById() {
        long id = 1L;

        blocService.deleteById(id);

        verify(blocRepository, times(1)).deleteById(id);
    }

    @Test
    public void testAffecterChambresABloc() {
        List<Long> numChambre = List.of(1L, 2L);
        String nomBloc = "Bloc A";
        Bloc bloc = new Bloc();
        bloc.setNomBloc(nomBloc);

        when(blocRepository.findByNomBloc(nomBloc)).thenReturn(bloc);
        when(chambreRepository.findByNumeroChambre(anyLong())).thenReturn(new Chambre());

        Bloc result = blocService.affecterChambresABloc(numChambre, nomBloc);

        assertNotNull(result);
        verify(chambreRepository, times(numChambre.size())).save(any(Chambre.class));
    }

    @Test
    public void testAffecterBlocAFoyer() {
        String nomBloc = "Bloc A";
        String nomFoyer = "Foyer A";
        Bloc bloc = new Bloc();
        Foyer foyer = new Foyer();

        when(blocRepository.findByNomBloc(nomBloc)).thenReturn(bloc);
        when(foyerRepository.findByNomFoyer(nomFoyer)).thenReturn(foyer);
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.affecterBlocAFoyer(nomBloc, nomFoyer);

        assertNotNull(result);
        assertEquals(foyer, result.getFoyer());
        verify(blocRepository, times(1)).save(bloc);
    }
}
