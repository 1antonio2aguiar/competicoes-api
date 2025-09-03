package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.empresa.DadosCadastroEmpresa;
import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.repository.EmpresaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public Empresa cadastrar(DadosCadastroEmpresa dados) {
        // Você pode adicionar uma verificação aqui para ver se o CNPJ já existe
        // if (empresaRepository.existsByCnpj(dados.cnpj())) {
        //     throw new IllegalArgumentException("CNPJ já cadastrado.");
        // }

        Empresa novaEmpresa = new Empresa();
        BeanUtils.copyProperties(dados, novaEmpresa);
        return empresaRepository.save(novaEmpresa);
    }

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    // Adicione outros métodos conforme necessário (atualizar, buscar por id, etc.)
}
