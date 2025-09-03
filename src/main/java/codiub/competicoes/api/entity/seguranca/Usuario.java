package codiub.competicoes.api.entity.seguranca;

import codiub.competicoes.api.entity.Empresa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "Usuario")
@Table(name = "USUARIOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String nome;

    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "EMPRESA_ID")
    private Empresa empresa;

    @ManyToMany(fetch = FetchType.EAGER) // EAGER para carregar os perfis junto com o usuário
    @JoinTable(
            name = "USUARIO_ROLES",
            joinColumns = @JoinColumn(name = "USUARIO_ID"),
            inverseJoinColumns = @JoinColumn(name = "PERFIL_ID")
    )
    private List<Perfil> perfis = new ArrayList<>();

    // Métodos da interface UserDetails que o Spring Security precisa

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Transforma a lista de Perfis em uma lista que o Spring Security entende
        return this.perfis.stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.getNome()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email; // Usamos o email como username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Lógica para expiração de conta pode ser adicionada aqui
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Lógica para bloqueio de conta
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Lógica para expiração de credenciais
    }

    @Override
    public boolean isEnabled() {
        return this.ativo; // Usa o campo 'ativo' do banco
    }
}