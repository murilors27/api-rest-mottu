package com.mottu.rastreamento.service;

import com.mottu.rastreamento.dto.SensorDTO;
import com.mottu.rastreamento.models.SensorUWB;
import com.mottu.rastreamento.repository.SensorUWBRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorUWBService {

    private final SensorUWBRepository repository;

    public SensorDTO salvar(SensorDTO dto) {
        if (repository.existsByLocalizacaoIgnoreCase(dto.getLocalizacao())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um sensor cadastrado nessa localização."
            );
        }

        SensorUWB sensor = new SensorUWB();
        sensor.setLocalizacao(dto.getLocalizacao());
        return toDTO(repository.save(sensor));
    }

    public Page<SensorDTO> listar(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::toDTO);
    }

    public List<SensorDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public SensorDTO buscarPorId(Long id) {
        SensorUWB sensor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sensor não encontrado"));
        return toDTO(sensor);
    }

    public SensorDTO atualizar(Long id, SensorDTO dto) {
        SensorUWB sensor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sensor não encontrado"));

        if (repository.existsByLocalizacaoIgnoreCase(dto.getLocalizacao())
                && !sensor.getLocalizacao().equalsIgnoreCase(dto.getLocalizacao())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe outro sensor com essa localização."
            );
        }

        sensor.setLocalizacao(dto.getLocalizacao());
        return toDTO(repository.save(sensor));
    }

    public void deletar(Long id) {
        SensorUWB sensor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sensor não encontrado"));

        if (sensor.getMotos() != null && !sensor.getMotos().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir o sensor '" + sensor.getLocalizacao() +
                            "', pois está vinculado a uma ou mais motos."
            );
        }

        repository.delete(sensor);
    }


    private SensorDTO toDTO(SensorUWB sensor) {
        SensorDTO dto = new SensorDTO();
        dto.setId(sensor.getId());
        dto.setLocalizacao(sensor.getLocalizacao());
        return dto;
    }
}