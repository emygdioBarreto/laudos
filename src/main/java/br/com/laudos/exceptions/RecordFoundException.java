package br.com.laudos.exceptions;

public class RecordFoundException extends RuntimeException {

    public RecordFoundException(String text) {
        super(text);
    }
}
