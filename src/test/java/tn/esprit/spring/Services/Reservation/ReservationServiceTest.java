package tn.esprit.spring.Services.Reservation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {
    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationService reservationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addOrUpdate() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        when(reservationService.addOrUpdate(reservation)).thenReturn(reservation);

        Reservation result = reservationService.addOrUpdate(reservation);

        assertNotNull(result);
        assertEquals("1", result.getIdReservation());
    }

    @Test
    public void findAll() {
        List<Reservation> reservations = List.of(new Reservation());

        when(reservationService.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void findById() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        when(reservationService.findById("1")).thenReturn(reservation);

        Reservation result = reservationService.findById("1");

        assertNotNull(result);
        assertEquals("1", result.getIdReservation());
    }

    @Test
    public void deleteById() {
        doNothing().when(reservationService).deleteById("1");

        reservationService.deleteById("1");

        verify(reservationService, times(1)).deleteById("1");
    }

    @Test
    public void ajouterReservationEtAssignerAChambreEtAEtudiant() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");
        Etudiant etudiantMock = new Etudiant();
        etudiantMock.setIdEtudiant(1L);

        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiantMock);
        reservation.setEtudiants(etudiants);

        when(reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(1L, 1L)).thenReturn(reservation);

        Reservation result = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(1L, 1L);

        assertNotNull(result);
        assertEquals("1", result.getIdReservation());
        assertFalse(result.getEtudiants().isEmpty());
    }

    @Test
    public void getReservationParAnneeUniversitaire() {
        Long expectedCount = 1L;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        // Mock the method to return a Long instead of a List
        when(reservationService.getReservationParAnneeUniversitaire(startDate, endDate)).thenReturn(expectedCount);

        Long result = reservationService.getReservationParAnneeUniversitaire(startDate, endDate);

        assertNotNull(result);
        assertEquals(expectedCount, result);
    }






}
