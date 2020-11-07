package zensharp.tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zensharp.TestHelper;
import zensharp.TestAssertions;

@SuppressWarnings("WeakerAccess")
public class TestClasses {
    
    @BeforeAll
    public static void setupEnvironment() {
        TestHelper.setupEnvironment();
    }
    
    @BeforeEach
    public void beforeEach() {
        TestHelper.beforeEach();
    }
    
    @Test
    public void testWhole() {
        TestHelper.run("zenClass testWhole {\n" + "\tstatic myStatic as string = \"value\";\n" +
                "\tstatic otherStatic as string = \"value\";\n" +
                "\tval nonStatic as string = \"123\";\n" +
                "\tval nonStaticTwo as string;\n" + "\n" +
                "\tzenConstructor(parameter as string, parameter2 as string) {\n" +
                "\t\tprint(\"TETETE\");\n" +
                "\t\tprint(parameter);\n" +
                "\t\tnonStaticTwo = parameter2;\n" +
                "\t}\n" +
                "\n" + "\tzenConstructor(parameter as string) {\n" +
                "\t\tprint(\"FFFFFF\");\n" + "\t}\n" +
                "\tfunction myMethod(arg as string, arg1 as string) as string {\n" +
                "\t\treturn \"value\" + arg ~ arg1;\n" + "\t}\n" + "\n" + "}\n" +
                "var test = testWhole(\"NOPE\");\n" + "test = testWhole(\"nope\", \"noper\");\n" +
                "print(test.myMethod(\"one\", \"two\"));\n" + "print(testWhole.myStatic);\n" +
                "print(testWhole(\"parameter1\", \"parameter2\").nonStatic);\n" +
                "val ttt = testWhole(\"t\");\n" +
                "ttt.myStatic = \"1\";\n" + "print(ttt.myStatic);\n" +
                "ttt.nonStatic = \"0\";\n" + "print(ttt.nonStatic);\n" +
                "print(testWhole(\"MYParam1\", \"MyPAram2\").nonStaticTwo);");
        
        TestAssertions.assertMany("FFFFFF", "TETETE", "nope", "valueonetwo", "value", "TETETE", "parameter1", "123", "FFFFFF", "1", "0", "TETETE", "MYParam1", "MyPAram2");
    }
    
    @Test
    public void testConstructorCall() {
        TestHelper.run("zenClass testConstructorCall { zenConstructor(a as int, b as int) {print(a); print(b);}} testConstructorCall(10, 20);");

        TestAssertions.assertMany("10", "20");
    }

    @Test
    public void testConstructorCallLarge() {
        TestHelper.run("zenClass testConstructorCallLarge { zenConstructor(a as long, b as long) {print(a); print(b);}} testConstructorCallLarge(10, 20);");

        TestAssertions.assertMany("10", "20");
    }

    @Test
    public void testMethod_empty() {
        TestHelper.run("zenClass testMethod_empty {zenConstructor() {} function method() {}} testMethod_empty().method();");
    }

    @Test
    public void testMethod() {
        TestHelper.run("zenClass testMethod {zenConstructor() {} function method(a as int, b as int) {print(a); print(b);}} testMethod().method(10, 20);");

        TestAssertions.assertMany("10", "20");
    }

    @Test
    public void testMethod_large() {
        TestHelper.run("zenClass testMethod_large {zenConstructor() {} function method(a as long, b as long) {print(a); print(b);}} testMethod_large().method(10, 20);");

        TestAssertions.assertMany("10", "20");
    }

    @Test
    public void testMethod_overloading() {
        TestHelper.run("zenClass testMethod_overloading {zenConstructor() {} \n"
                + "function method(a as int) {print('a: ' ~ a);} \n"
                + "function method(a as int, b as int) {print('b: ' ~ a ~ b);}} \n"
                + "testMethod_overloading().method(30); \n"
                + "testMethod_overloading().method(10, 20);");
        TestAssertions.assertMany("a: 30", "b: 1020");
    }
}
