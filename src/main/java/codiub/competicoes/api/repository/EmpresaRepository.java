package codiub.competicoes.api.repository;

import codiub.competicoes.api.entity.Categoria;
import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.repository.categoria.CategoriaRepositoryQuery;
import codiub.competicoes.api.repository.empresa.EmpresaRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>, EmpresaRepositoryQuery {

    // Método para verificar se já existe uma empresa com o CNPJ informado
    boolean existsByCnpj(String cnpj);
}
