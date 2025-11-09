package com.mottu.rastreamento.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "sensores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorUWB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Localização é obrigatória")
    @Pattern(
            regexp = "^Setor [A-Z] - Coluna [1-9][0-9]*$",
            message = "A localização deve seguir o formato 'Setor X - Coluna Y'. Exemplo: Setor A - Coluna 1"
    )
    @Column(nullable = false, unique = true)
    private String localizacao;

    @OneToMany(mappedBy = "sensor", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Moto> motos;
}