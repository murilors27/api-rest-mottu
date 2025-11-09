package com.mottu.rastreamento.service;

import com.mottu.rastreamento.dto.MotoDTO;
import com.mottu.rastreamento.models.*;
import com.mottu.rastreamento.models.enums.StatusMoto;
import com.mottu.rastreamento.models.enums.StatusAlocacao;
import com.mottu.rastreamento.models.enums.StatusManutencao;
import com.mottu.rastreamento.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MotoService {

    private final MotoRepository motoRepository;
    private final SensorUWBRepository sensorRepository;
    private final AlocacaoRepository alocacaoRepository;
    private final ManutencaoRepository manutencaoRepository;

    @Transactional
    public MotoDTO salvar(MotoDTO dto) {
        if (!dto.getIdentificadorUWB().matches("^UWB(?!000)\\d{3}$")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Identificador UWB inválido. Use o formato 'UWB001' até 'UWB999'."
            );
        }

        // Verifica duplicidade
        motoRepository.findByIdentificadorUWB(dto.getIdentificadorUWB())
                .ifPresent(m -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Já existe uma moto cadastrada com este Identificador UWB.");
                });

        Moto moto = new Moto();
        moto.setIdentificadorUWB(dto.getIdentificadorUWB());
        moto.setModelo(dto.getModelo());
        moto.setCor(dto.getCor());
        moto.setStatus(StatusMoto.DISPONIVEL);
        moto.setAtivo(true);

        if (dto.getSensorId() != null) {
            SensorUWB sensor = sensorRepository.findById(dto.getSensorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor não encontrado."));
            moto.setSensor(sensor);
        }

        return toDTO(motoRepository.save(moto));
    }

    public Page<MotoDTO> listar(Pageable pageable) {
        List<MotoDTO> list = motoRepository.findByAtivoTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new org.springframework.data.domain.PageImpl<>(list, pageable, list.size());
    }

    public List<MotoDTO> listarTodas() {
        return motoRepository.findByAtivoTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MotoDTO> listarTodasDisponiveis() {
        return motoRepository.findByAtivoTrue()
                .stream()
                .filter(m -> m.getStatus() == StatusMoto.DISPONIVEL)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MotoDTO buscarPorId(Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada."));
        return toDTO(moto);
    }

    public MotoDTO buscarPorIdentificadorUWB(String identificadorUWB) {
        Moto moto = motoRepository.findByIdentificadorUWB(identificadorUWB)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Moto com UWB '" + identificadorUWB + "' não encontrada."));
        return toDTO(moto);
    }

    @Transactional
    public MotoDTO atualizar(Long id, MotoDTO dto) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada."));

        if (!dto.getIdentificadorUWB().matches("^UWB(?!000)\\d{3}$")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Identificador UWB inválido. Use o formato 'UWB001' até 'UWB999'."
            );
        }

        motoRepository.findByIdentificadorUWB(dto.getIdentificadorUWB())
                .filter(m -> !m.getId().equals(id))
                .ifPresent(m -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Já existe uma moto cadastrada com este Identificador UWB.");
                });

        moto.setIdentificadorUWB(dto.getIdentificadorUWB());
        moto.setModelo(dto.getModelo());
        moto.setCor(dto.getCor());

        if (dto.getSensorId() != null) {
            SensorUWB sensor = sensorRepository.findById(dto.getSensorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor não encontrado."));
            moto.setSensor(sensor);
        }

        if (dto.getStatus() != null) {
            moto.setStatus(dto.getStatus());

            if (dto.getStatus() == StatusMoto.ALOCADA) {
                Alocacao nova = new Alocacao();
                nova.setMoto(motoRepository.getReferenceById(id));
                nova.setInicio(LocalDateTime.now());
                nova.setStatus(StatusAlocacao.ABERTA);
                alocacaoRepository.save(nova);
            } else if (dto.getStatus() == StatusMoto.MANUTENCAO) {
                Manutencao nova = new Manutencao();
                nova.setMoto(motoRepository.getReferenceById(id));
                nova.setDescricao("Gerada automaticamente ao alterar status para MANUTENCAO");
                nova.setDataInicio(LocalDateTime.now());
                nova.setStatus(StatusManutencao.ABERTA);
                manutencaoRepository.save(nova);
            }
        }

        return toDTO(motoRepository.save(moto));
    }

    @Transactional
    public void deletar(Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada."));

        if (moto.getStatus() == StatusMoto.MANUTENCAO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esta moto está em manutenção e não pode ser excluída.");
        }

        List<Alocacao> alocacoes = alocacaoRepository.findByMotoId(id);
        boolean temAberta = alocacoes.stream()
                .anyMatch(a -> a.getStatus() != null && a.getStatus().equals(StatusAlocacao.ABERTA));

        if (temAberta) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Esta moto não pode ser excluída pois está atualmente alocada em uma locação ativa.");
        }

        moto.setAtivo(false);
        motoRepository.save(moto);
    }

    private MotoDTO toDTO(Moto moto) {
        MotoDTO dto = new MotoDTO();
        dto.setId(moto.getId());
        dto.setModelo(moto.getModelo());
        dto.setCor(moto.getCor());
        dto.setIdentificadorUWB(moto.getIdentificadorUWB());
        dto.setStatus(moto.getStatus());

        if (moto.getSensor() != null) {
            dto.setSensorId(moto.getSensor().getId());
            dto.setSensorLocalizacao(moto.getSensor().getLocalizacao());
        }

        return dto;
    }
}
