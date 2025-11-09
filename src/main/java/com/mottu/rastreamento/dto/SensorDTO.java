package com.mottu.rastreamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SensorDTO {

    private Long id;

    @NotBlank(message = "A localização do sensor é obrigatória.")
    @Pattern(
            regexp = "^Setor [A-Z] - Coluna [1-9][0-9]*$",
            message = "Formato inválido. Use o padrão 'Setor X - Coluna Y' (ex: Setor A - Coluna 1)."
    )
    private String localizacao;
}
