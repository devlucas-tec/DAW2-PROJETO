package br.edu.ifpb.es.daw.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CriarBancoSeNaoExistir implements BeanPostProcessor, EnvironmentAware {

    private static final Logger log = LoggerFactory.getLogger(CriarBancoSeNaoExistir.class);
    private static final Pattern URL_POSTGRES = Pattern.compile("^(jdbc:postgresql://[^/]+/)([^?]+)(\\?.*)?$");
    private static final Pattern NOME_VALIDO = Pattern.compile("[A-Za-z0-9_]+");

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof HikariDataSource dataSource
                && environment.getProperty("app.database.create-if-not-exists", Boolean.class, false)) {
            criarBanco(dataSource.getJdbcUrl(), dataSource.getUsername(), dataSource.getPassword());
        }
        return bean;
    }

    private void criarBanco(String url, String usuario, String senha) {
        Matcher matcher = URL_POSTGRES.matcher(url);
        if (!matcher.matches()) {
            return;
        }
        String nomeBanco = matcher.group(2);
        if (!NOME_VALIDO.matcher(nomeBanco).matches()) {
            log.warn("Nome de banco '{}' inválido para criação automática.", nomeBanco);
            return;
        }
        String urlAdministrativa = matcher.group(1) + "postgres" + (matcher.group(3) != null ? matcher.group(3) : "");

        try (Connection conexao = DriverManager.getConnection(urlAdministrativa, usuario, senha);
             PreparedStatement consulta = conexao.prepareStatement("SELECT 1 FROM pg_database WHERE datname = ?")) {
            consulta.setString(1, nomeBanco);
            try (ResultSet resultado = consulta.executeQuery()) {
                if (resultado.next()) {
                    return;
                }
            }
            try (Statement comando = conexao.createStatement()) {
                comando.executeUpdate("CREATE DATABASE \"" + nomeBanco + "\"");
                log.info("Banco de dados '{}' criado.", nomeBanco);
            }
        } catch (SQLException e) {
            log.warn("Não foi possível verificar/criar o banco '{}': {}", nomeBanco, e.getMessage());
        }
    }
}
