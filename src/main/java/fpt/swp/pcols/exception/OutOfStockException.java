package fpt.swp.pcols.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class OutOfStockException extends RuntimeException {
    private final Map<Long, String> errorMap;

    public OutOfStockException(Map<Long, String> errorMap) {
        // Constructor của java.lang.RuntimeException có sẵn dạng RuntimeException(String message)
        super("Not enough stock for one or more products");
        this.errorMap = errorMap;
    }
}
