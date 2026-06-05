package br.edu.ifpb.es.daw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SpringDocConfiguration {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce MarketPlace API")
                        .description("API REST para gerenciamento de um E-commerce MarketPlace, " +
                                "com suporte a clientes, vendedores, produtos, pedidos, " +
                                "pagamentos, entregas, avaliações e muito mais.\n\n" +
                                "**Desenvolvedores:**\n" +
                                "- Lucas Barbosa\n" +
                                "- Valdênio Pantaleão\n" +
                                "- Paulo Moura")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("IFPB - Instituto Federal da Paraíba")
                                .url("https://www.ifpb.edu.br")));
    }
}