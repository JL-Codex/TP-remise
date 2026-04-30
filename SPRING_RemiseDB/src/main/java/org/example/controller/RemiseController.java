package org.example.controller;

import org.example.service.RemiseTauxBDD;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/remises")
public class RemiseController {

    private final RemiseTauxBDD remiseTauxBDD;

    public RemiseController(RemiseTauxBDD remiseTauxBDD) {
        this.remiseTauxBDD = remiseTauxBDD;
    }

    @GetMapping("/calculer")
    public Map<String, Object> calculerRemise(@RequestParam double montant) {
        double reduction = remiseTauxBDD.calculer(montant);
        return Map.of(
            "montantAvant", montant,
            "reduction", reduction,
            "montantApres", montant - reduction
        );
    }
}
