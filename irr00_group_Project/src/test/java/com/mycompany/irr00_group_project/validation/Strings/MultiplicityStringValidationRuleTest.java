package com.mycompany.irr00_group_project.validation.Strings;

import com.mycompany.irr00_group_project.validation.ValidationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MultiplicityStringValidationRule and
 * verifies correct handling of UML multiplicity strings.
 *
 * @author Weiping Yan
 * @author Long Pham
 */
public class MultiplicityStringValidationRuleTest {

    private final MultiplicityStringValidationRule validator
            = new MultiplicityStringValidationRule();

    /**
     * Test that valid multiplicity strings pass validation.
     */
    @Test
    void testValidMultiplicities() {
        assertTrue(validator.validate("*").isValid());
        assertTrue(validator.validate("0").isValid());
        assertTrue(validator.validate("1").isValid());
        assertTrue(validator.validate("0..1").isValid());
        assertTrue(validator.validate("1..*").isValid());
        assertTrue(validator.validate("3..7").isValid());
    }

    /**
     * Test that empty or null multiplicities are invalid.
     */
    @Test
    void testEmptyOrNullMultiplicity() {
        assertFalse(validator.validate("").isValid());
        assertFalse(validator.validate(null).isValid());
    }

    /**
     * Test invalid formats like letters, wrong syntax, or missing dots.
     */
    @Test
    void testInvalidFormats() {
        assertFalse(validator.validate("abc").isValid());
        assertFalse(validator.validate("1..1..1").isValid());
        assertFalse(validator.validate("..1").isValid());
        assertTrue(validator.validate("1..*").isValid());
    }

    /**
     * Test that upper bound less than lower bound is caught.
     */
    @Test
    void testReversedRange() {
        ValidationResult result = validator.validate("5..1");
        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains(
                "lower bound must be less than or equal to upper bound")
        );
    }

    /**
     * Test that if numbers are invalid even though format looks right, it's caught.
     */
    @Test
    void testInvalidNumbersInRange() {
        ValidationResult result = validator.validate("1..X");
        assertFalse(result.isValid());
    }
}
