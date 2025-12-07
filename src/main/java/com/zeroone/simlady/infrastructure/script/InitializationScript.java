package com.zeroone.simlady.infrastructure.script;

import com.zeroone.simlady.core.application.usecases.marca.CriarMarcaUseCase;
import com.zeroone.simlady.core.application.usecases.pedido.CriarPedidoUseCase;
import com.zeroone.simlady.core.application.usecases.produto.CriarProdutoUseCase;
import com.zeroone.simlady.core.application.usecases.usuario.BuscarUsuarioPorEmailUseCase;
import com.zeroone.simlady.core.application.usecases.usuario.CadastrarUsuarioUseCase;
import com.zeroone.simlady.core.application.usecases.venda.CriarVendaUseCase;
import com.zeroone.simlady.core.domain.marca.Marca;
import com.zeroone.simlady.core.domain.pedido.Pedido;
import com.zeroone.simlady.core.domain.pedido.PedidoItem;
import com.zeroone.simlady.core.domain.produto.Produto;
import com.zeroone.simlady.core.domain.usuario.Permissao;
import com.zeroone.simlady.core.domain.usuario.Usuario;
import com.zeroone.simlady.core.domain.venda.Venda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InitializationScript implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitializationScript.class);

    @Autowired
    private Environment environment;

    @Autowired
    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    @Autowired
    private BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase;

    @Autowired
    private CriarMarcaUseCase criarMarcaUseCase;

    @Autowired
    private CriarProdutoUseCase criarProdutoUseCase;

    @Autowired
    private CriarPedidoUseCase criarPedidoUseCase;

    @Autowired
    private CriarVendaUseCase criarVendaUseCase;

    // Maps para armazenar UUIDs das entidades criadas
    private Map<String, UUID> marcasMap = new HashMap<>();
    private Map<String, UUID> usuariosMap = new HashMap<>();
    private Map<String, UUID> produtosMap = new HashMap<>();
    private Map<Integer, UUID> pedidosMap = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        logger.info("Iniciando script de inicialização...");
        
        try {
            // Sempre criar usuário admin
            criarUsuarioAdminSeNaoExistir();
            
            // Verificar se deve inicializar dados de teste
            String initializeData = environment.getProperty("app.initialize-data", "false");
            if (!"true".equalsIgnoreCase(initializeData)) {
                logger.info("app.initialize-data não está habilitado. Pulando criação de dados iniciais.");
                logger.info("Script de inicialização concluído com sucesso!");
                return;
            }
            
            logger.info("app.initialize-data habilitado. Verificando se dados iniciais já existem...");
            
            // Verificar se os dados iniciais já foram criados tentando criar a primeira marca
            // Se ela já existir, significa que o script já foi executado anteriormente
            if (dadosIniciaisJaForamCriados()) {
                logger.info("Dados iniciais já foram criados anteriormente. Pulando criação.");
                logger.info("Script de inicialização concluído com sucesso!");
                return;
            }
            
            logger.info("Dados iniciais não encontrados. Iniciando criação...");
            criarMarcas();
            criarUsuarios();
            criarProdutos();
            criarPedidos();
            criarVendas();
            
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

    private boolean dadosIniciaisJaForamCriados() {
        try {
            // Tenta criar a primeira marca (O Boticário) como verificação
            Marca marcaTeste = Marca.newMarca(
                "O Boticário",
                "Maior rede de franquias de beleza do Brasil",
                "https://gkpb.com.br/wp-content/uploads/2020/08/novo-logo-o-boticario-2020.jpg"
            );
            Marca marcaCriada = criarMarcaUseCase.executar(marcaTeste);
            marcasMap.put("O Boticário", marcaCriada.getId());
            logger.info("Primeira marca criada com sucesso. Dados iniciais não existem.");
            return false; // Não existia, acabamos de criar
        } catch (Exception e) {
            logger.info("Marca de teste já existe. Dados iniciais já foram criados anteriormente.");
            return true; // Já existe, então dados já foram criados
        }
    }
    
    private void criarMarcas() {
        logger.info("Criando marcas no banco de dados...");
        
        // Pular O Boticário pois já foi criado na verificação
        String[][] marcasData = {
            {"Natura", "Cosméticos sustentáveis e naturais", "https://cdn.worldvectorlogo.com/logos/natura-1.svg"},
            {"Avon", "Cosméticos e perfumaria internacional", "https://gkpb.com.br/wp-content/uploads/2021/01/novo-logo-avon-png.png"},
            {"L'Oréal", "Líder mundial em beleza e cosméticos", "https://cdn-icons-png.flaticon.com/512/5968/5968613.png"}
        };
        
        int criadas = 1; // O Boticário já foi criado
        
        for (String[] data : marcasData) {
            try {
                Marca marca = Marca.newMarca(data[0], data[1], data[2]);
                Marca marcaCriada = criarMarcaUseCase.executar(marca);
                marcasMap.put(data[0], marcaCriada.getId());
                logger.info("Marca criada: {} (ID: {})", data[0], marcaCriada.getId());
                criadas++;
            } catch (Exception e) {
                logger.error("Erro ao criar marca {}: {}", data[0], e.getMessage());
            }
        }
        
        logger.info("Marcas criadas: {}", criadas);
    }

    private void criarUsuarios() {
        logger.info("Criando usuários no banco de dados...");
        
        Object[][] usuariosData = {
            {"Shirleide", "Santos", "554.062.260-02", "admin@admin.com", "12345678", "11987654321", Permissao.ADMIN},
            {"Maria", "Santos", "464.874.560-47", "maria@gmail.com", "senha123456", "21976543210", Permissao.COMUM},
            {"João", "Oliveira", "399.738.250-63", "joao@gmail.com", "minhasenha123", "31965432109", Permissao.COMUM},
            {"Ana", "Costa", "895.271.520-97", "ana@gmail.com", "password123", "41954321098", Permissao.COMUM}
        };
        
        int criados = 0;
        
        for (Object[] data : usuariosData) {
            String email = (String) data[3];
            try {
                Usuario usuario = Usuario.newUsuario(
                    (String) data[0],  // nome
                    (String) data[1],  // sobrenome
                    email,              // email
                    (String) data[4],  // senha
                    (String) data[5],  // celular
                    (Permissao) data[6] // permissao
                );
                
                Usuario usuarioCriado = cadastrarUsuarioUseCase.executar(usuario);
                usuariosMap.put(email, usuarioCriado.getId());
                logger.info("Usuário criado: {} (ID: {})", email, usuarioCriado.getId());
                criados++;
            } catch (Exception e) {
                logger.error("Erro ao criar usuário {}: {}", email, e.getMessage());
            }
        }
        
        logger.info("Usuários criados: {}", criados);
    }

    private void criarProdutos() {
        logger.info("Criando produtos no banco de dados...");
        
        Object[][] produtosData = {
            {"Malbec Perfume", "MAL-PER-100", "O melhor perfume do Brasil", "Lançamento", 12, 30.0, true, 50.0, "https://cdn.awsli.com.br/600x450/1000/1000152/produto/59005983/d4e8a3d63b.jpg", "O Boticário"},
            {"Natura Tododia", "NAT-TOD-250", "Hidratante corporal com fragrância suave", "Hidratante", 25, 15.5, true, 28.9, "https://www.natura.com.br/cdn/produtos/tododia-hidratante.jpg", "Natura"},
            {"Avon Color Trend", "AVN-CLR-001", "Batom cremoso longa duração", "Maquiagem", 50, 8.75, true, 15.9, "https://www.avon.com.br/cdn/produtos/color-trend-batom.jpg", "Avon"},
            {"L'Oréal Revitalift", "LOR-REV-050", "Creme anti-idade com ácido hialurônico", "Anti-idade", 18, 45.0, true, 89.9, "https://www.loreal.com.br/cdn/produtos/revitalift-creme.jpg", "L'Oréal"}
        };
        
        int criados = 0;
        
        for (Object[] data : produtosData) {
            String sku = (String) data[1];
            String nomeMarca = (String) data[9];
            
            try {
                UUID idMarca = marcasMap.get(nomeMarca);
                if (idMarca == null) {
                    logger.warn("Marca {} não encontrada para produto {}", nomeMarca, sku);
                    continue;
                }
                
                Produto produto = Produto.newProduto(
                    (String) data[0],   // nome
                    sku,                // sku
                    (String) data[2],   // descricao
                    (String) data[3],   // tag
                    (Integer) data[4],  // quantidade
                    (Double) data[5],   // precoUnitario
                    (Boolean) data[6],  // catalogo
                    (Double) data[7],   // valorVenda
                    (String) data[8],   // imagemUrl
                    idMarca             // idMarca
                );
                
                Produto produtoCriado = criarProdutoUseCase.executar(produto);
                produtosMap.put(sku, produtoCriado.getId());
                logger.info("Produto criado: {} (ID: {})", sku, produtoCriado.getId());
                criados++;
            } catch (Exception e) {
                logger.error("Erro ao criar produto {}: {}", sku, e.getMessage());
            }
        }
        
        logger.info("Produtos criados: {}", criados);
    }

    private void criarPedidos() {
        logger.info("Criando pedidos no banco de dados...");
        
        int criados = 0;
        
        // Pedido 1: maria@gmail.com
        criados += criarPedido("maria@gmail.com", 1,
            new String[]{"AVN-CLR-001", "3", "15.9"});
        
        // Pedido 2: joao@gmail.com
        criados += criarPedido("joao@gmail.com", 2,
            new String[]{"LOR-REV-050", "1", "89.9"});
        
        // Pedido 3: ana@gmail.com
        criados += criarPedido("ana@gmail.com", 3,
            new String[]{"NAT-TOD-250", "5", "25.0"},
            new String[]{"AVN-CLR-001", "2", "17.5"});
        
        // Pedido 4: maria@gmail.com
        criados += criarPedido("maria@gmail.com", 4,
            new String[]{"MAL-PER-100", "1", "49.99"});
        
        // Pedido 5: joao@gmail.com
        criados += criarPedido("joao@gmail.com", 5,
            new String[]{"NAT-TOD-250", "3", "28.9"});
        
        // Pedido 6: ana@gmail.com
        criados += criarPedido("ana@gmail.com", 6,
            new String[]{"LOR-REV-050", "2", "90.0"});
        
        // Pedido 7: maria@gmail.com
        criados += criarPedido("maria@gmail.com", 7,
            new String[]{"NAT-TOD-250", "2", "27.9"});
        
        // Pedido 8: joao@gmail.com
        criados += criarPedido("joao@gmail.com", 8,
            new String[]{"MAL-PER-100", "1", "48.5"});
        
        // Pedido 9: ana@gmail.com
        criados += criarPedido("ana@gmail.com", 9,
            new String[]{"AVN-CLR-001", "4", "14.9"});
        
        // Pedido 10: joao@gmail.com
        criados += criarPedido("joao@gmail.com", 10,
            new String[]{"NAT-TOD-250", "1", "29.5"},
            new String[]{"AVN-CLR-001", "3", "15.9"});
        
        // Pedido 11: maria@gmail.com
        criados += criarPedido("maria@gmail.com", 11,
            new String[]{"LOR-REV-050", "1", "88.0"});
        
        logger.info("Pedidos processados - Criados: {}", criados);
    }
    
    private int criarPedido(String emailUsuario, int numeroPedido, String[]... itensData) {
        try {
            UUID idUsuario = usuariosMap.get(emailUsuario);
            if (idUsuario == null) {
                logger.warn("Usuário {} não encontrado para pedido {}", emailUsuario, numeroPedido);
                return 0;
            }
            
            List<PedidoItem> itens = new ArrayList<>();
            for (String[] itemData : itensData) {
                String sku = itemData[0];
                Integer quantidade = Integer.parseInt(itemData[1]);
                String preco = itemData[2];
                
                UUID idProduto = produtosMap.get(sku);
                if (idProduto == null) {
                    logger.warn("Produto {} não encontrado para item do pedido {}", sku, numeroPedido);
                    continue;
                }
                
                PedidoItem item = PedidoItem.newPedidoItem(idProduto, quantidade, preco);
                itens.add(item);
            }
            
            if (itens.isEmpty()) {
                logger.warn("Pedido {} não possui itens válidos", numeroPedido);
                return 0;
            }
            
            Pedido pedidoCriado = criarPedidoUseCase.executar(null, idUsuario, itens);
            pedidosMap.put(numeroPedido, pedidoCriado.getId());
            logger.info("Pedido {} criado (ID: {})", numeroPedido, pedidoCriado.getId());
            return 1;
        } catch (Exception e) {
            logger.error("Erro ao criar pedido {}: {}", numeroPedido, e.getMessage());
            return 0;
        }
    }

    private void criarVendas() {
        logger.info("Criando vendas no banco de dados...");
        
        // Estrutura: desconto, pagamentoRealizado, lista de pedidos (índices), dataVenda
        Object[][] vendasData = {
            {15.5, false, new int[]{1}, "2025-05-28"},
            {0.0, true, new int[]{2}, "2025-05-18"},
            {12.0, true, new int[]{3, 4}, "2025-05-12"},
            {22.7, false, new int[]{5}, "2025-04-28"},
            {10.0, true, new int[]{6}, "2025-04-17"},
            {5.5, true, new int[]{7, 8}, "2025-03-23"},
            {0.0, false, new int[]{9}, "2025-03-10"},
            {18.0, false, new int[]{10, 11}, "2025-01-13"}
        };
        
        int criadas = 0;
        
        for (int i = 0; i < vendasData.length; i++) {
            try {
                Double desconto = (Double) vendasData[i][0];
                Boolean pagamentoRealizado = (Boolean) vendasData[i][1];
                int[] indicesPedidos = (int[]) vendasData[i][2];
                String dataVendaStr = (String) vendasData[i][3];
                
                List<UUID> pedidosIds = Arrays.stream(indicesPedidos)
                    .mapToObj(idx -> pedidosMap.get(idx))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
                
                if (pedidosIds.isEmpty()) {
                    logger.warn("Venda {} não possui pedidos válidos", i + 1);
                    continue;
                }
                
                // Calcular valor total baseado nos pedidos
                double valorTotal = 100.0; // Valor simplificado para teste
                
                LocalDate dataVenda = LocalDate.parse(dataVendaStr);
                
                Venda vendaCriada = criarVendaUseCase.executar(
                    String.valueOf(valorTotal),
                    String.valueOf(desconto),
                    dataVenda,
                    pedidosIds
                );
                
                // Atualizar status de pagamento se necessário
                if (pagamentoRealizado && !vendaCriada.getPagamentoRealizado()) {
                    vendaCriada.confirmarPagamento();
                }
                
                logger.info("Venda {} criada (ID: {})", i + 1, vendaCriada.getId());
                criadas++;
            } catch (Exception e) {
                logger.error("Erro ao criar venda {}: {}", i + 1, e.getMessage());
            }
        }
        
        logger.info("Vendas processadas - Criadas: {}", criadas);
    }
}
