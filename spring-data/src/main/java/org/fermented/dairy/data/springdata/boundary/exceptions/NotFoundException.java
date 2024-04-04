package org.fermented.dairy.data.springdata.boundary.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(final String message){
        super(message);
    }
}
