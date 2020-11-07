package zensharp.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import zensharp.TestHelper;
import zensharp.TestAssertions;

@SuppressWarnings("WeakerAccess")
public class TestLoops {
    @BeforeAll
    public static void setupEnvironment() {
        TestHelper.setupEnvironment();
    }

    @BeforeEach
    public void beforeEch() {
        TestHelper.beforeEach();
    }

    @Test
    public void testBreak() {
        TestHelper.run("val arr = ['a', 'b', 'c'] as string[]; for x in arr {print(x); if (x == 'b') break;}");
        TestAssertions.assertMany("a", "b");
    }

    @Test
    public void testContinue() {
        TestHelper.run("val arr = ['a', 'b', 'c'] as string[]; for x in arr {if (x == 'b') continue; print(x);}");
        TestAssertions.assertMany("a", "c");
    }

    @Test
    public void testBreakInner(){
        TestHelper.run("val arr = ['a', 'b', 'c'] as string[]; for x in arr {print(x); if(x == 'b') break; for y in arr { if(y=='c') break; print(x~y);}}");
        TestAssertions.assertMany("a", "aa", "ab", "b");
    }

    @Test
    public void testContinueInner() {
        TestHelper.run("val arr = ['a', 'b', 'c'] as string[]; for x in arr {print(x); if(x == 'b') continue; for y in arr { if(y=='c') continue; print(x~y);}}");
        TestAssertions.assertMany("a", "aa", "ab", "b", "c", "ca", "cb");
    }
}
