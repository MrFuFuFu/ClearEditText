package com.example.barryirvine.clearableedittext.utils.validator;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class EmailAddressValidatorTest {

    private static final String[] GOOD_EMAILS = {
            "foo@bar.com",
            "foo1@bar.com",
            "foo12@bar.com",
            "1foo@bar.com",
            "12foo@bar.com",
            "foo-1@bar.com",
            "foo-12@bar.com",
            "foo.1@bar.com",
            "foo.12@bar.com",
            "1.foo@bar.com",
            "12.foo@bar.com",
            "foo@bar1.com",
            "foo@bar.1.com",
            "foo@bar.1a.com",
            "foo.foo.1@bar.com",
            "$A12345@example.com",
            "_foo@bar.com",
            "FOO@BAR.COM",
            "foo@bar.cancerresearch",
            "foo@1.com"
    };

    private static final String[] BAD_EMAILS = {
            null,
            "",
            "foo",
            "foo.com",
            "foo@foo@bar.com",
            "foo@foo..com",
            ".foo@bar.com",
            "foo@.com",
            "foo@.bar.com",
            "foo@bar.com.",
            "foo@bar.1com",
            "foo@bar.",
            "foo@..com",
            "foo@*.com",
            "foo@%.com",
            "foo@bar.a",
            "foo@bar.1a",
            "foo@-bar.com",
            "foo..123@bar.com",
            "foo.@bar.com",
            "foo@com.",
            "foo@com",
    };

    @Test
    public void testValidatorValidatesGoodEmails() {

        final Validator<String> v = new EmailAddressValidator();

        for (final String email : GOOD_EMAILS) {
            assertTrue("Failed to validate " + email, v.isValid(email));
        }

    }

    @Test
    public void testValidatorDoesNotValidatesBadEmails() {

        final Validator<String> v = new EmailAddressValidator();

        for (final String email : BAD_EMAILS) {
            assertFalse("Erroneously validated " + email, v.isValid(email));
        }

    }

}