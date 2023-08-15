package org.madu.util;

import org.madu.entities.Pessoa;
import org.madu.exception.BadRequestException;
import org.madu.exception.UnprocessableEntityException;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;

public class RequestValidator {

    public static void validate(Pessoa pessoa) {
        if (Stream.of(pessoa.getApelido(), pessoa.getNome(), pessoa.getNascimento()).anyMatch(Objects::isNull)) {
            throw new UnprocessableEntityException();
        }

        if (!pessoa.getNome().matches("^(?=.*[a-zA-Z]).{1,100}$")) {
            throw new BadRequestException();
        }

        if (!pessoa.getApelido().matches("^(?=.*[a-zA-Z]).{1,32}$")) {
            throw new BadRequestException();
        }

        if (isNull(pessoa.getStack())) {
            return;
        }

        if (stream(pessoa.getStack()).anyMatch(x -> isNull(x) || !x.matches("^(?=.*[a-zA-Z]).{1,32}$"))) {
            throw new BadRequestException();
        }
    }
}
