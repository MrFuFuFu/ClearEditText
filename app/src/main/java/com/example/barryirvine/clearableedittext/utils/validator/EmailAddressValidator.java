package com.example.barryirvine.clearableedittext.utils.validator;

import java.util.regex.Pattern;

public class EmailAddressValidator implements Validator<String> {

    private static final Pattern EMAIL_ADDRESS_PATTERN
            = Pattern.compile("(?!\\.)(?!.*\\.{2})(?!.*\\.@)(?!.*\\.[0-9]+[a-z]+$)[a-z0-9\\+\\._%\\$\\-]{1,256}@" +
            "[a-z0-9][a-z0-9\\-\\.]*" +
            "\\." +
            "([a-z]{2,25})+"
            , Pattern.CASE_INSENSITIVE);
    // First look ahead clause - prevent leading fullstop
    // Second look ahead clause - prevent consecutive fullstops
    // Third look ahead clause - prevent fullstop before @
    // Fourth look ahead clause - prevent TLD beginning with number

    @Override
    public final boolean isValid(final String emailAddress) {
        return !(emailAddress == null || emailAddress.length() == 0) && EMAIL_ADDRESS_PATTERN.matcher(emailAddress).matches();

    }

    @Override
    public final ValidatorError[] getErrors() {
        return new ValidatorError[]{EmailAddressValidatorError.EMAIL_INVALID};
    }

    public enum EmailAddressValidatorError implements ValidatorError {
        EMAIL_INVALID
    }
}
