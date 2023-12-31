package com.example.meusgastos.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.meusgastos.domain.dto.usuario.UsuarioRequestDTO;
import com.example.meusgastos.domain.dto.usuario.UsuarioResponseDTO;
import com.example.meusgastos.domain.exception.ResourceBadRequestException;
import com.example.meusgastos.domain.exception.ResourceNotFoundException;
import com.example.meusgastos.domain.model.Usuario;
import com.example.meusgastos.domain.repository.UsuarioRepository;

@Service
public class UsuarioService implements ICRUDService {
@Autowired
private UsuarioRepository usuarioRepository;
@Autowired
private ModelMapper mapper; 

@Override
public List obterTodos() {
List usuarios = usuarioRepository.findAll();
return usuarios.stream()
.map(usuario -> mapper.map(usuario, UsuarioResponseDTO.class))
.collect(Collectors.toList());
}

@Override
    public UsuarioResponseDTO obterPorId(Long id) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);
        if(optUsuario.isEmpty()){
            throw new ResourceNotFoundExcpetion("não foi possível encontrar o usuário com id:"+ id);
        }
        return mapper.map(optUsuario.get(), UsuarioResponseDTO.class);
     }

@Override
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        Usuario usuario = mapper.map(dto, Usuario.class);
        //encriptografar senha será feito posteriormente     
        usuario = usuarioRepository.save(usuario);
        return mapper.map(usuario, UsuarioResponseDTO.class);
    }


@Override
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        obterPorId(id); // se ele não existir o obterPorId vai lançar a exceção
        Usuario usuario = mapper.map(dto, Usuario.class);
        usuario.setId(id);  // ele já tem id e uso o mesmo save.
        usuario = usuarioRepository.save(usuario);
        return mapper.map(usuario, UsuarioResponseDTO.class);
    }

@Override
    public void deletar(Long id) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);
        if(optUsuario.isEmpty()){
            throw new ResourceNotFoundExcpetion("não foi possível encontrar o usuário com id:"+ id);
        }
    
      Usuario usuario = optUsuario.get();
      usuario.setDataInativacao(new Date());
      usuarioRepository.save(usuario);
    }

}