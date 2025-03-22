package fpt.swp.pcols.validation;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ValidationResult {
    private boolean hasErrors;
    private Map<String, String> errors;

    public ValidationResult() {
        this.hasErrors = false;
        this.errors = new HashMap<>();
    }

    public void addError(String field, String message) {
        this.errors.put(field, message);
        this.hasErrors = true;
    }
}