package br.edu.ifpb.es.daw.seed;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seed")
@Tag(name = "Seed", description = "API de povoamento e limpeza")
public class SeedController {

    private final SeedService seedService;

    public SeedController(SeedService seedService) {
        this.seedService = seedService;
    }

    @PostMapping("/popular")
    public String popular() {
        seedService.popular();
        return "✅ Banco populado com sucesso!";
    }

    @DeleteMapping("/limpar")
    public String limpar() {
        seedService.limpar();
        return "🗑️ Banco limpo com sucesso!";
    }

    @PostMapping("/resetar")
    public String resetar() {
        seedService.limpar();
        seedService.popular();
        return "🔄 Banco resetado com sucesso!";
    }
}