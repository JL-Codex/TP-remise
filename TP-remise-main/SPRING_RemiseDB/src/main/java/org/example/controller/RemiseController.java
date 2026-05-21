package org.example.controller;

import java.util.Map;

import org.example.service.IRemise;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing the discount calculation endpoint.
 *
 * SOLID compliance:
 *  D — depends on IRemise abstraction, not on RemiseTauxBDD or any other
 *      concrete implementation. Spring injects whichever bean is active.
 */
@RestController
@RequestMapping("/api/remises")
public class RemiseController {

    private final IRemise remiseService;

    public RemiseController(IRemise remiseService) {
        this.remiseService = remiseService;
    }

    @GetMapping("/calculer")
    public Map<String, Object> calculerRemise(@RequestParam double montant) {
        double reduction = remiseService.calculer(montant);
        return Map.of(
            "montantAvant", montant,
            "reduction",    reduction,
            "montantApres", montant - reduction
        );
    }
}
