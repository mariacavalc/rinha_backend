package org.madu.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Pessoa {

    private UUID id;

    private String apelido;

    private String nome;

    private LocalDate nascimento;

    private List<String> stack;
}
