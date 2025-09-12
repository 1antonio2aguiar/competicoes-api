package codiub.competicoes.api.service;

import codiub.competicoes.api.DTO.campeonato.DadosUpdateCampeonatoRcd;
import codiub.competicoes.api.DTO.empresa.DadosCadastroEmpresa;
import codiub.competicoes.api.DTO.empresa.DadosUpdateEmpresa;
import codiub.competicoes.api.entity.Campeonato;
import codiub.competicoes.api.entity.Empresa;
import codiub.competicoes.api.repository.EmpresaRepository;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    @Transactional
    public Empresa insert(DadosCadastroEmpresa dados) {
        // Validação para evitar CNPJ duplicado
        if (empresaRepository.existsByCnpj(dados.cnpj())) {
            throw new IllegalArgumentException("CNPJ já cadastrado no sistema.");
        }

        Empresa novaEmpresa = new Empresa();
        BeanUtils.copyProperties(dados, novaEmpresa);
        return empresaRepository.save(novaEmpresa);
    }

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com o ID: " + id));
    }

    @Transactional
    public Empresa update(Long id, DadosUpdateEmpresa dados){
        Empresa empresaUpd = empresaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Empresa não cadastrada. Id: " + id));
        BeanUtils.copyProperties(dados, empresaUpd, "id");

        return empresaRepository.save(empresaUpd);
    }

    @Transactional
    public void deletar(Long id) {
        // Verifica se a empresa existe antes de tentar deletar
        if (!empresaRepository.existsById(id)) {
            throw new EntityNotFoundException("Empresa não encontrada com o ID: " + id);
        }

        empresaRepository.deleteById(id);
    }
}
