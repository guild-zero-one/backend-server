package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.dto.LoteProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.LoteProdutoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.entity.LoteProduto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.entity.Produto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.LoteProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoteProdutoService {

    @Autowired
    private LoteProdutoRepository loteProdutoRepository;

    @Autowired
    private ProdutoService produtoService;

    public LoteProdutoDTO cadastrarLote(LoteProdutoDTO loteProdutoDTO) {
        LoteProduto loteProduto = LoteProdutoMapper.toEntity(loteProdutoDTO);
        Produto produto = produtoService.buscarPorId(loteProdutoDTO.getProdutoId());
        loteProduto.setProduto(produto);
        loteProduto = cadastrarLote(loteProduto);
        return LoteProdutoMapper.toDTO(loteProduto);
    }

    public LoteProduto cadastrarLote(LoteProduto loteProduto) {
        return loteProdutoRepository.save(loteProduto);
    }

    public List<LoteProdutoDTO> listar() {
        return loteProdutoRepository.findAll().stream()
                .map(LoteProdutoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public LoteProduto buscarPorId(Integer id) {
        return loteProdutoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Fornecedor Não Encontrado"));
    }

    public void excluirPorId(Integer id) {
        loteProdutoRepository.deleteById(id);
    }

    public LoteProdutoDTO atualizar(Integer id, LoteProdutoDTO loteProdutoDTO){
        LoteProduto loteBuscado = buscarPorId(id);
        loteBuscado.setQtdLote(loteProdutoDTO.getQtdLote());
        loteBuscado.setValorUnitCompra(loteProdutoDTO.getValorUnitCompra());
        LoteProduto loteSalvo = loteProdutoRepository.save(loteBuscado);
        return LoteProdutoMapper.toDTO(loteSalvo);
    }
}
