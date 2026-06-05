package br.edu.ifpb.es.daw.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupListener {

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("✅ Aplicação iniciada com sucesso!");
        System.out.println("📄 Swagger disponível em: http://localhost:8080/swagger-ui/index.html");
        System.out.println("--------------------------------------------------\n");
    }
}