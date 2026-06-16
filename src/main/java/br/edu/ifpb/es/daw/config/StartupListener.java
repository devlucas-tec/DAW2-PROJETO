package br.edu.ifpb.es.daw.config;

import br.edu.ifpb.es.daw.model.Admin;
import br.edu.ifpb.es.daw.model.enums.Role;
import br.edu.ifpb.es.daw.repository.AdminRepository;
import br.edu.ifpb.es.daw.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StartupListener {

    private static final String ADMIN_EMAIL = "admin@marketplace.com";
    private static final String ADMIN_SENHA = "admin123";

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        criarAdminPadraoSeNaoExistir();

        System.out.println("\n--------------------------------------------------");
        System.out.println("✅ Aplicação iniciada com sucesso!");
        System.out.println("📄 Swagger disponível em: http://localhost:8080/swagger-ui/index.html");
        System.out.println("👤 Admin padrão: " + ADMIN_EMAIL + " / " + ADMIN_SENHA);
        System.out.println("--------------------------------------------------\n");
    }

    public void criarAdminPadraoSeNaoExistir() {
        if (usuarioRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
            Admin admin = new Admin();
            admin.setNome("Administrador");
            admin.setEmail(ADMIN_EMAIL);
            admin.setSenha(passwordEncoder.encode(ADMIN_SENHA));
            admin.setRole(Role.ADMIN);
            adminRepository.save(admin);
            System.out.println("🔐 Admin padrão criado.");
        }
    }
}