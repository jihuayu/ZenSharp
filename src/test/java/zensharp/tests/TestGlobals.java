package zensharp.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zensharp.TestHelper;
import zensharp.TestAssertions;

public class TestGlobals {

    @BeforeAll
    public static void setupEnvironment() {
        TestHelper.setupEnvironment();
    }

    @BeforeEach
    public void beforeEch() {
        TestHelper.beforeEach();
    }

    @Test
    public void testAssign() {
        TestHelper.run("static a as string = 'A'; print(a); a = 'B'; print(a);");
        TestAssertions.assertMany("A", "B");
    }
}
