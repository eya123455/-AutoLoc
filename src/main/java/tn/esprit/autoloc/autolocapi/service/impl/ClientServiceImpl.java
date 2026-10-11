package tn.esprit.autoloc.autolocapi.service.impl;

import tn.esprit.autoloc.autolocapi.domain.Client;
import tn.esprit.autoloc.autolocapi.exception.ConflictException;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.ClientRepository;
import tn.esprit.autoloc.autolocapi.service.IClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new ConflictException("Un client avec l'email '" + client.getEmail() + "' existe déjà");
        }
        if (clientRepository.existsByNumPermis(client.getNumPermis())) {
            throw new ConflictException("Un client avec le permis '" + client.getNumPermis() + "' existe déjà");
        }
        client.setIdClient(null);
        client.setDateInscription(LocalDate.now());
        return clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Override
    public Client update(Long id, Client data) {
        Client existing = findById(id);
        if (clientRepository.existsByEmailAndIdClientNot(data.getEmail(), id)) {
            throw new ConflictException("Un autre client utilise déjà l'email '" + data.getEmail() + "'");
        }
        if (clientRepository.existsByNumPermisAndIdClientNot(data.getNumPermis(), id)) {
            throw new ConflictException("Un autre client utilise déjà le permis '" + data.getNumPermis() + "'");
        }
        existing.setNom(data.getNom());
        existing.setPrenom(data.getPrenom());
        existing.setEmail(data.getEmail());
        existing.setTelephone(data.getTelephone());
        existing.setNumPermis(data.getNumPermis());
        return clientRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        clientRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'email '" + email + "'"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> search(String texte) {
        return clientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(texte, texte);
    }
}
