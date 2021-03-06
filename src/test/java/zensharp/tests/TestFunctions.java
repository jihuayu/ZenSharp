package zensharp.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zensharp.TestHelper;
import zensharp.TestAssertions;

import static zensharp.TestAssertions.assertCalculated;

@SuppressWarnings("WeakerAccess")
public class TestFunctions {
    
    @BeforeAll
    public static void setupEnvironment() {
        TestHelper.setupEnvironment();
    }
    
    @BeforeEach
    public void beforeEach() {
        TestHelper.beforeEach();
    }
    
    @Test
    public void Test_NestedFunctions() {
        TestHelper.run("tens();  realTens(\"Hello World!\"); function tens(){     realTens(\"a\"); }  function realTens(a as string){     for i in 1 to 11{         print(a);     } }");
        
        TestAssertions.assertCalculated(value -> value < 10 ? "a" : "Hello World!");
    }
    
    @Test
    public void Test_FunctionDeclaredAfterCall() {
        TestHelper.run("val result = add(1,99); print(result);  print(add(2,64));  function add(a as int,b as int) as int{     return a+b; }");
        
        TestAssertions.assertMany("100", "66");
    }
    
    @Test
    public void Test_FunctionLargeType() {
        TestHelper.run("function test(a as double, b as bool) as double {return b ? a : 10.0D;} print(test(3.0D, true)); print(test(3.0D, false));");
        
        TestAssertions.assertMany("3.0", "10.0");
    }
}
