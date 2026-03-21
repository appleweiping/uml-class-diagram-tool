package com.mycompany.irr00_group_project.validation.Strings;

import com.mycompany.irr00_group_project.validation.Severity;
import com.mycompany.irr00_group_project.validation.ValidationResult;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A container for all validation rule  for sting .
 * 
 * @author Anas Mohammad Jebril Yousef Noufal
 */
class StringValidationTests {

    @Nested
    class CamelCaseFormatValidationRuleTest {

        @Test
        void testValidCamelCase() {
            CamelCaseFormatValidationRule rule = new CamelCaseFormatValidationRule();
            assertTrue(rule.validate("myAttributeName").isValid());
            assertTrue(rule.validate("customer").isValid());
            assertTrue(rule.validate("order1Processor").isValid());
        }

        @Test
        void testInvalidStartsWithUppercase() {
            CamelCaseFormatValidationRule rule = new CamelCaseFormatValidationRule("Attribute");
            ValidationResult result = rule.validate("MyAttribute");
            assertFalse(result.isValid());
            assertEquals(Severity.WARNING, result.getSeverity());
            assertTrue(result.getMessage().contains("must be in camelCase"));
        }

        @Test
        void testInvalidWithSpecialChars() {
            CamelCaseFormatValidationRule rule = new CamelCaseFormatValidationRule();
            assertFalse(rule.validate("my attribute").isValid());
            assertFalse(rule.validate("my-attribute").isValid());
        }

        @Test
        void testNullOrEmptyInput() {
            CamelCaseFormatValidationRule rule = new CamelCaseFormatValidationRule("Field");

            ValidationResult nullResult = rule.validate(null);
            assertFalse(nullResult.isValid());
            assertEquals("Field cannot be empty.", nullResult.getMessage());
            assertEquals(Severity.WARNING, nullResult.getSeverity());

            ValidationResult emptyResult = rule.validate(" ");
            assertFalse(emptyResult.isValid());
            assertEquals("Field cannot be empty.", emptyResult.getMessage());
        }

        @Test
        void testDefaultFieldNameInMessage() {
            CamelCaseFormatValidationRule rule = new CamelCaseFormatValidationRule();
            ValidationResult result = rule.validate("");
            assertTrue(result.getMessage().startsWith("Input text"));
        }
    }
    
    @Nested
    class MultiplicityStringValidationRuleTest {

        private final MultiplicityStringValidationRule rule 
            = new MultiplicityStringValidationRule();

        @Test
        void testValidMultiplicityFormats() {
            assertTrue(rule.validate("*").isValid());
            assertTrue(rule.validate("1").isValid());
            assertTrue(rule.validate("0..1").isValid());
            assertTrue(rule.validate("1..*").isValid());
            assertTrue(rule.validate("5..10").isValid());
            assertTrue(rule.validate("1..1").isValid());
        }

        @Test
        void testInvalidRangeOrder() {
            ValidationResult result = rule.validate("5..1");
            assertFalse(result.isValid());
            assertEquals(Severity.ERROR, result.getSeverity());
            assertTrue(result.getMessage().contains(
                    "lower bound must be less than or equal to upper bound"));
        }

        @Test
        void testInvalidFormats() {
            assertFalse(rule.validate("1-*").isValid());
            assertFalse(rule.validate("..").isValid());
            assertFalse(rule.validate("a..b").isValid());
            assertFalse(rule.validate("1.. 2").isValid());
        }

        @Test
        void testNullOrEmptyInput() {
            ValidationResult nullResult = rule.validate(null);
            assertFalse(nullResult.isValid());
            assertEquals("Multiplicity cannot be empty.", nullResult.getMessage());
            assertEquals(Severity.ERROR, nullResult.getSeverity());

            ValidationResult emptyResult = rule.validate("  ");
            assertFalse(emptyResult.isValid());
            assertEquals("Multiplicity cannot be empty.", emptyResult.getMessage());
        }
    }

    @Nested
    class PascalCaseFormatValidationRuleTest {

        @Test
        void testValidPascalCase() {
            PascalCaseFormatValidationRule rule = new PascalCaseFormatValidationRule("ClassName");
            assertTrue(rule.validate("MyClass").isValid());
            assertTrue(rule.validate("User").isValid());
            assertTrue(rule.validate("Order1Detail").isValid());
        }

        @Test
        void testInvalidStartsWithLowercase() {
            PascalCaseFormatValidationRule rule = new PascalCaseFormatValidationRule("ClassName");
            ValidationResult result = rule.validate("myClass");
            assertFalse(result.isValid());
            assertEquals(Severity.WARNING, result.getSeverity());
            assertTrue(result.getMessage().contains("must be in PascalCase"));
        }

        @Test
        void testInvalidWithSpecialChars() {
            PascalCaseFormatValidationRule rule = new PascalCaseFormatValidationRule("ClassName");
            assertFalse(rule.validate("My Class").isValid());
            assertFalse(rule.validate("My-Class").isValid());
        }
    }

    @Nested
    class StereotypeFormatValidationRuleTest {

        private final StereotypeFormatValidationRule rule = new StereotypeFormatValidationRule();

        @Test
        void testValidStereotype() {
            assertTrue(rule.validate("<<Interface>>").isValid());
            assertTrue(rule.validate("<<Entity_User>>").isValid());
            assertTrue(rule.validate("<<Service1>>").isValid());
        }

        @Test
        void testInvalidMissingBrackets() {
            ValidationResult result = rule.validate("Interface");
            assertFalse(result.isValid());
            assertEquals(Severity.ERROR, result.getSeverity());
            assertTrue(result.getMessage().contains("Invalid stereotype format"));
        }

        @Test
        void testInvalidFormatWithSpacesOrPartialBrackets() {
            assertFalse(rule.validate("<<My Stereo>>").isValid());
            assertFalse(rule.validate("<<Interface>").isValid());
            assertFalse(rule.validate("<Interface>>").isValid());
        }

        @Test
        void testNullOrEmptyInput() {
            ValidationResult nullResult = rule.validate(null);
            assertFalse(nullResult.isValid());
            assertEquals("Stereotype text cannot be empty.", nullResult.getMessage());
            assertEquals(Severity.ERROR, nullResult.getSeverity());

            ValidationResult emptyResult = rule.validate(" ");
            assertFalse(emptyResult.isValid());
        }
    }

    @Nested
    class VisibilityMarkerValidationRuleTest {

        private final VisibilityMarkerValidationRule rule = new VisibilityMarkerValidationRule();

        @Test
        void testValidMarkers() {
            assertTrue(rule.validate("+").isValid());
            assertTrue(rule.validate("-").isValid());
            assertTrue(rule.validate("#").isValid());
            assertTrue(rule.validate("~").isValid());
        }

        @Test
        void testInvalidMarkers() {
            ValidationResult result = rule.validate("*");
            assertFalse(result.isValid());
            assertEquals("Invalid visibility marker. Must be one of: +, -, #, ~",
                result.getMessage());
            assertEquals(Severity.WARNING, result.getSeverity());

            assertFalse(rule.validate("+-").isValid());
            assertFalse(rule.validate("a").isValid());
        }

        @Test
        void testNullOrEmptyInput() {
            ValidationResult nullResult = rule.validate(null);
            assertFalse(nullResult.isValid());
            assertEquals("Visibility marker cannot be empty.", nullResult.getMessage());
            assertEquals(Severity.WARNING, nullResult.getSeverity());

            ValidationResult emptyResult = rule.validate("\t");
            assertFalse(emptyResult.isValid());
        }
    }
}