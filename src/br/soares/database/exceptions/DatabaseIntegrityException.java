package br.soares.database.exceptions;

import java.io.Serial;

public class DatabaseIntegrityException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    public DatabaseIntegrityException(String exception) {
        super(exception);
    }
}
