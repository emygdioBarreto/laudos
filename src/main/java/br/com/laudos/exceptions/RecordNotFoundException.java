package br.com.laudos.exceptions;

import java.io.Serial;

public class RecordNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RecordNotFoundException(Long id) {
        super("Registro não encontrado com o id: " + id);
    }

    public RecordNotFoundException(Integer id) {
        super("Registro não encontrado com o id: " + id);
    }

    public RecordNotFoundException(String message) {
        super(message);
    }
}
