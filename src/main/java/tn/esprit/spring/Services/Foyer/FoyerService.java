package tn.esprit.spring.Services.Foyer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class FoyerService implements IFoyerService {
    private final FoyerRepository repo;
    private final UniversiteRepository universiteRepository;
    private final BlocRepository blocRepository;

    @Override
    public Foyer addOrUpdate(Foyer f) {
        return repo.save(f);
    }

    @Override
    public List<Foyer> findAll() {
        return repo.findAll();
    }

    @Override
    public Foyer findById(long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    /*@Override
    public void delete(Foyer f) {
        repo.delete(f); // Supprime le foyer
    }*/

    @Override
    public Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite) {
        Foyer f = findById(idFoyer);
        Universite u = universiteRepository.findByNomUniversite(nomUniversite);
        u.setFoyer(f);
        return universiteRepository.save(u);
    }

    @Override
    public Universite desaffecterFoyerAUniversite(long idUniversite) {
        Universite u = universiteRepository.findById(idUniversite).orElse(null);
        if (u != null) {
            u.setFoyer(null);
            return universiteRepository.save(u);
        }
        return null; // Renvoie null si l'université n'est pas trouvée
    }

    @Override
    public Foyer ajouterFoyerEtAffecterAUniversite(Foyer foyer, long idUniversite) {
        Foyer savedFoyer = repo.save(foyer);
        Universite u = universiteRepository.findById(idUniversite).orElse(null);
        if (u != null) {
            u.setFoyer(savedFoyer);
            universiteRepository.save(u);
        }
        return savedFoyer;
    }

   // @Override
    /*public Foyer ajoutFoyerEtBlocs(Foyer foyer) {
        //Foyer child / Bloc parent
        //Objet foyer = attribut objet foyer + les blocs associés
//        Foyer f = repo.save(foyer);
//        for (Bloc b : foyer.getBlocs()) {
//            b.setFoyer(f);
//            blocRepository.save(b);
//        }
//        return f;
        //-----------------------------------------
        List<Bloc> blocs = foyer.getBlocs();
        foyer = repo.save(foyer);
        for (Bloc b : blocs) {
            b.setFoyer(foyer);
            blocRepository.save(b);
        }
        return foyer;
    }*/

}
