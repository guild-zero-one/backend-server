package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.LoteProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.LoteProdutoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.ProdutoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.LoteProduto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Produto;
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

    public LoteProdutoDTO atualizar(Integer id, LoteProduto loteProduto){
        LoteProduto loteBuscado = buscarPorId(id);
        loteProduto.setId(id);
        LoteProduto loteSalvo = loteProdutoRepository.save(loteProduto);
        return LoteProdutoMapper.toDTO(loteSalvo);
    }
}
