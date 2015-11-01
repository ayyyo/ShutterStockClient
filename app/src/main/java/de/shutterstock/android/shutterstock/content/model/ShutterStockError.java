package de.shutterstock.android.shutterstock.content.model;

import java.util.List;

/**
 * Created by emanuele on 25.10.15.
 */
public class ShutterStockError {

    public static class Error {
        public String text;
        public String code;
        public String message;
    }

    public String message;
    public List<Error> errors;
}
