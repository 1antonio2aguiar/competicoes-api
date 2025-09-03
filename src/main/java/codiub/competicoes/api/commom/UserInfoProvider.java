package codiub.competicoes.api.commom;

import codiub.competicoes.api.entity.seguranca.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
@Service
public class UserInfoProvider {

    public Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }
        // Lança uma exceção se não houver usuário logado, para evitar erros de null pointer
        throw new IllegalStateException("Nenhum usuário autenticado encontrado no contexto de segurança.");
    }

    public Long getEmpresaId() {
        Usuario usuario = getUsuarioLogado();
        if (usuario.getEmpresa() != null) {
            return usuario.getEmpresa().getId();
        }
        throw new IllegalStateException("O usuário autenticado não está associado a nenhuma empresa.");
    }
}