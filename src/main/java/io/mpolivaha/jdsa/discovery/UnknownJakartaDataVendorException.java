package io.mpolivaha.jdsa.discovery;

public class UnknownJakartaDataVendorException extends RuntimeException {

    public UnknownJakartaDataVendorException() {
        super("Unable to discover the Jakarta Data Vendor in class-path. Is Hibernate or OpenLiberty or JNoSQL present in the class-path?");
    }
}
