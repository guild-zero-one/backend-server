package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.storage.StorageDeleteResponseDto;
import com.zeroone.simlady.dto.storage.StorageUploadResponseDto;
import com.zeroone.simlady.exception.BadRequestException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.service.FornecedorService;
import com.zeroone.simlady.service.ProdutoService;
import com.zeroone.simlady.service.StorageService;
import com.zeroone.simlady.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storage")
@Tag(name = "Storage", description = "Gerenciamento de imagens")
public class StorageController {

    private static final long MAX_SIZE = 5L * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_FOLDERS = Set.of("produtos", "usuarios", "fornecedores");

    private final StorageService storageService;
    private final ProdutoService produtoService;
    private final UsuarioService usuarioService;
    private final FornecedorService fornecedorService;

    @Operation(summary = "Upload de imagem", description = "Envia uma imagem para o storage e vincula à entidade.")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem enviada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StorageUploadResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Arquivo inválido ou parâmetros incorretos",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Entidade não encontrada",
                    content = @Content()),
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StorageUploadResponseDto> upload(
            @RequestParam MultipartFile file,
            @RequestParam String folder,
            @RequestParam UUID entityId) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Nenhum arquivo enviado.");
        }
        if (!ALLOWED_FOLDERS.contains(folder)) {
            throw new BadRequestException("Pasta inválida. Use: produtos, usuarios ou fornecedores.");
        }

        String extension = extractExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("Formato de arquivo não suportado. Use: jpg, jpeg, png ou webp.");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new BadRequestException("O arquivo excede o tamanho máximo permitido de 5MB.");
        }

        String filename = getSingularPrefix(folder) + "-" + entityId + "." + extension;

        String existingUrl = getExistingImageUrl(folder, entityId);
        if (existingUrl != null) {
            String existingKey = folder + "/" + getSingularPrefix(folder) + "-" + entityId + "." + extractExtension(existingUrl);
            storageService.delete(existingKey);
        }

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Falha ao processar o arquivo.", e);
        }

        String mimeType = file.getContentType() != null ? file.getContentType() : "image/" + extension;
        String url = storageService.upload(folder, filename, bytes, mimeType);
        atualizarImageUrl(folder, entityId, url);

        return ResponseEntity.ok(new StorageUploadResponseDto("Imagem enviada com sucesso.", url, folder, entityId));
    }

    @Operation(summary = "Remover imagem", description = "Remove a imagem vinculada à entidade.")
    @SecurityRequirement(name = "Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem removida com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StorageDeleteResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Pasta inválida",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Entidade não encontrada ou sem imagem cadastrada",
                    content = @Content()),
    })
    @DeleteMapping("/{folder}/{entityId}")
    public ResponseEntity<StorageDeleteResponseDto> delete(
            @PathVariable String folder,
            @PathVariable UUID entityId) {

        if (!ALLOWED_FOLDERS.contains(folder)) {
            throw new BadRequestException("Pasta inválida. Use: produtos, usuarios ou fornecedores.");
        }

        String existingUrl = getExistingImageUrl(folder, entityId);
        if (existingUrl == null) {
            throw new ResourceNotFoundException("Esta entidade não possui imagem cadastrada.");
        }

        String fileKey = folder + "/" + getSingularPrefix(folder) + "-" + entityId + "." + extractExtension(existingUrl);
        storageService.delete(fileKey);
        atualizarImageUrl(folder, entityId, null);

        return ResponseEntity.ok(new StorageDeleteResponseDto("Imagem removida com sucesso.", folder, entityId));
    }

    private String getExistingImageUrl(String folder, UUID entityId) {
        return switch (folder) {
            case "produtos" -> produtoService.buscarPorId(entityId).getUrlImagem();
            case "usuarios" -> usuarioService.buscar(entityId).getUrlImagem();
            case "fornecedores" -> fornecedorService.buscarPorId(entityId).getImagemUrl();
            default -> null;
        };
    }

    private void atualizarImageUrl(String folder, UUID entityId, String url) {
        switch (folder) {
            case "produtos" -> produtoService.atualizarImageUrl(entityId, url);
            case "usuarios" -> usuarioService.atualizarImageUrl(entityId, url);
            case "fornecedores" -> fornecedorService.atualizarImageUrl(entityId, url);
        }
    }

    private String getSingularPrefix(String folder) {
        return switch (folder) {
            case "produtos" -> "produto";
            case "usuarios" -> "usuario";
            case "fornecedores" -> "fornecedor";
            default -> folder;
        };
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
