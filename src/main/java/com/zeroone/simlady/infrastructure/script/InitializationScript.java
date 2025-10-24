package com.zeroone.simlady.infrastructure.script;

import com.zeroone.simlady.core.application.usecases.usuario.BuscarUsuarioPorEmailUseCase;
import com.zeroone.simlady.core.application.usecases.usuario.CadastrarUsuarioUseCase;
import com.zeroone.simlady.core.domain.usuario.Permissao;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class InitializationScript implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitializationScript.class);

    @Autowired
    private Environment environment;

    @Autowired
    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    @Autowired
    private BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Iniciando script de inicialização...");
        
        try {
            criarUsuarioAdminSeNaoExistir();
            logger.info("Script de inicialização concluído com sucesso!");
        } catch (Exception e) {
            logger.error("Erro durante a inicialização: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void criarUsuarioAdminSeNaoExistir() {
        String sysadminUser = environment.getProperty("SYSADMIN_USER");
        String sysadminEmail = environment.getProperty("SYSADMIN_EMAIL");
        String sysadminPassword = environment.getProperty("SYSADMIN_PASSWORD");

        // Validar se as variáveis de ambiente estão definidas
        if (sysadminUser == null || sysadminUser.trim().isEmpty()) {
            logger.warn("Variável SYSADMIN_USER não definida. Pulando criação do usuário admin.");
            return;
        }

        if (sysadminEmail == null || sysadminEmail.trim().isEmpty()) {
            logger.warn("Variável SYSADMIN_EMAIL não definida. Pulando criação do usuário admin.");
            return;
        }

        if (sysadminPassword == null || sysadminPassword.trim().isEmpty()) {
            logger.warn("Variável SYSADMIN_PASSWORD não definida. Pulando criação do usuário admin.");
            return;
        }

        try {
            // Verificar se o usuário já existe
            try {
                buscarUsuarioPorEmailUseCase.executar(sysadminEmail);
                logger.info("Usuário admin já existe com o email: {}", sysadminEmail);
                return;
            } catch (Exception e) {
                // Usuário não existe, continuar com a criação
            }

            // Criar novo usuário admin
            Usuario usuarioAdmin = Usuario.newUsuario(
                sysadminUser,
                "", // sobrenome padrão
                sysadminEmail,
                sysadminPassword,
                null, // celular opcional
                Permissao.ADMIN
            );

            Usuario usuarioCriado = cadastrarUsuarioUseCase.executar(usuarioAdmin);
            
            logger.info("Usuário admin criado com sucesso!");
            logger.info("ID: {}", usuarioCriado.getId());
            logger.info("Nome: {}", usuarioCriado.getNome());
            logger.info("Email: {}", usuarioCriado.getEmail());
            logger.info("Permissão: {}", usuarioCriado.getPermissao());

        } catch (Exception e) {
            logger.error("Erro ao criar usuário admin: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao criar usuário admin durante a inicialização", e);
        }
    }
}
