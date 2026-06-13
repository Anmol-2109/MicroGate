package com.Anmol.Gateway.Exception;


public class NoHealthyInstanceException
        extends RuntimeException {

    public NoHealthyInstanceException(
            String serviceName
    ) {
        super(
                "No healthy instances found for "
                        + serviceName
        );
    }
}