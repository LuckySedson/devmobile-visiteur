package com.visiteur.visiteur_api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visiteur.visiteur_api.model.Visiteur;
import com.visiteur.visiteur_api.service.VisiteurService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/visiteurs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VisiteurController {

    private final VisiteurService service;

    @GetMapping
    public List<Visiteur> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Visiteur getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Visiteur create(@RequestBody Visiteur v) {
        return service.create(v);
    }

    @PutMapping("/{id}")
    public Visiteur update(@PathVariable Long id, @RequestBody Visiteur v) {
        return service.update(id, v);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public Map<String, BigDecimal> getStats() {
        return service.getStats();
    }
}