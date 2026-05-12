package com.visiteur.visiteur_api.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.visiteur.visiteur_api.model.Visiteur;
import com.visiteur.visiteur_api.repository.VisiteurRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisiteurService {

    private final VisiteurRepository repository;

    public List<Visiteur> getAll() {
        return repository.findAll();
    }

    public Visiteur getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visiteur non trouvé"));
    }

    public Visiteur create(Visiteur v) {
        return repository.save(v);
    }

    public Visiteur update(Long id, Visiteur updated) {
        Visiteur existing = getById(id);
        existing.setNom(updated.getNom());
        existing.setNombreJours(updated.getNombreJours());
        existing.setTarifJournalier(updated.getTarifJournalier());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Map<String, BigDecimal> getStats() {
        Map<String, BigDecimal> stats = new HashMap<>();
        stats.put("total", repository.findTarifTotal());
        stats.put("min", repository.findTarifMin());
        stats.put("max", repository.findTarifMax());
        return stats;
    }
}