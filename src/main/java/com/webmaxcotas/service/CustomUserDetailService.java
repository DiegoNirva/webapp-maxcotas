package com.webmaxcotas.service;

import com.webmaxcotas.model.Usuario;
import com.webmaxcotas.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CustomUserDetailService  implements UserDetailsService {
    @Autowired
    private  UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username);

        //se verifica si el usuario existe
        if(usuario == null){
            throw new UsernameNotFoundException("Usuario no encontrado: ".concat(username));
        }

        return User.withUsername(usuario.getUsername())
                .password(usuario.getContrasenia())
                .authorities(List.of(new SimpleGrantedAuthority(usuario.getRol())))
                .build();
    }

    public Usuario saveUsuario(Usuario usuario){
            usuario.setContrasenia(passwordEncoder().encode(usuario.getContrasenia()));
            return usuarioRepository.save(usuario);
    }

    public Usuario findByIdUsuario(Long idUsuario){
        return usuarioRepository.findById(idUsuario).orElseThrow(()-> new RuntimeException("No se encontro el usuario"+idUsuario));
    }
    public Usuario upDateUsuario(Long idUsuario, String nuevoRol){

        Usuario usuario = findByIdUsuario(idUsuario);

        if(usuario !=null){
            throw new UsernameNotFoundException("Usuario no encontrado:"+idUsuario);
        }

        usuario.setRol(nuevoRol);
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Long idUsuario){
        usuarioRepository.deleteById(idUsuario);
    }

    public List<Usuario> findAllUsuario(){
        return usuarioRepository.findAll();
    }



    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
