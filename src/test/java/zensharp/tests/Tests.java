package zensharp.tests;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zensharp.TestHelper;
import zensharp.TestAssertions;

import static zensharp.TestAssertions.assertCalculated;

@SuppressWarnings("WeakerAccess")
public class Tests {
    
    @BeforeAll
    public static void setupEnvironment() {
        TestHelper.setupEnvironment();
    }
    
    @BeforeEach
    public void beforeEach() {
        TestHelper.beforeEach();
    }
    
    @Test
    public void testCompile() {
        TestHelper.run("var x = 0; x += 5;");
    }
    
    @Test
    public void testPrint() {
        TestHelper.run("var x = 0; x += 5; print(x);");
        TestAssertions.assertOne("5", 0);
    }
    
    @Test
    public void testLoop() {
        TestHelper.run("for i in 0 .. 10 { print(i); } for i in 10 to 20 {print(i); }");
        
        TestAssertions.assertCalculated(Integer::toString, 20);
    }
    
    @Test
    public void testArrays() {
        TestHelper.run("val stringArray = [\"Hello\",\"World\",\"I\",\"am\"] as string[];\n\nprint(stringArray[0]);\n\n\n\nval stringArray1 = [\"Hello\",\"World\"] as string[];\nval stringArray2 = [\"I\",\"am\"] as string[];\nval stringArray3 = [\"a\",\"beautiful\"] as string[];\nval stringArrayAll = [stringArray1,stringArray2,stringArray3,[\"Butterfly\",\"!\"]] as string[][];\n\nprint(stringArrayAll[0][1]);");
        TestAssertions.assertOne("Hello", 0);
        TestAssertions.assertOne("World", 1);
    }
    
    @Test
    public void testArrayLoop() {
        TestHelper.run("val stringArray = [\"Hello\",\"World\",\"I\",\"am\"] as string[];\n\nprint(stringArray[0]);\n\n\n\nval stringArray1 = [\"Hello\",\"World\"] as string[];\nval stringArray2 = [\"I\",\"am\"] as string[];\nval stringArray3 = [\"a\",\"beautiful\"] as string[];\nval stringArrayAll = [stringArray1,stringArray2,stringArray3,[\"Butterfly\",\"!\"]] as string[][];\n\nprint(stringArrayAll[0][1]); for item in stringArray {print(item); }");
        
        TestAssertions.assertMany("Hello", "World", "Hello", "World", "I", "am");
    }
    
    @Test
    public void testArrayAddition() {
        TestHelper.run("var strArr = [\"hello\"] as string[]; strArr += \"world\"; for item in strArr {print(item);}");
        
        TestAssertions.assertMany("hello", "world");
    }
    
    @Test
    public void testMaps() {
        TestHelper.run("val assocWithStrings = {\n    //you can use \"\" if you want\n    \"one\" : \"1\",\n\n    //but you don't have to\n    two : \"2\"\n} as string[string];\n\n//You can either use the memberGetter\nprint(assocWithStrings.one);\n\n//Or the standard index Getter\nprint(assocWithStrings[\"two\"]);");
        
        TestAssertions.assertMany("1", "2");
    }
    
    @Test
    public void testCalculations() {
        TestHelper.run("print(\"Hello\" ~ \" \" ~ \"World\"); if(3+1 == 2*2) {print(\"Used a calculation!\");} print(0x7fffffffffffffff);");
        
        TestAssertions.assertOne("Hello World", 0);
        TestAssertions.assertOne("Used a calculation!", 1);
        TestAssertions.assertOne("9223372036854775807", 2);
    }
    
    @Test
    public void testConditionals() {
        TestHelper.run("val a = 0 as int; if(a==0){print(\"NumVal\");}  val b = 1; val c = 5; if(b+c==6){print(\"Num1!\");} if(b*c==5){print(\"Num2!\");} if(b/c==0.2){print(\"Num3!\");}  val d = \"Hello\"; val e = \"World\"; val f = d~e; if(d==\"Hello\" | e == \"Hello\"){print(\"OR1!\");} if(d==\"Hello\" | e == \"World\"){print(\"OR2!\");}  if(d==\"Hello\" ^ e == \"Hello\"){print(\"XOR1!\");} if(d==\"Hello\" ^ e == \"World\"){print(\"XOR2!\");}  if(d==\"Hello\" & e == \"Hello\"){print(\"AND1!\");} if(d==\"Hello\" & e == \"World\"){print(\"AND2!\");}");
        
        TestAssertions.assertOne("NumVal", 0);
        TestAssertions.assertOne("Num1!", 1);
        TestAssertions.assertOne("Num2!", 2);
        TestAssertions.assertOne("Num3!", 3);
        TestAssertions.assertOne("OR1!", 4);
        TestAssertions.assertOne("OR2!", 5);
        TestAssertions.assertOne("XOR1!", 6);
        TestAssertions.assertOne("AND2!", 7);
    }
    
    @Test
    public void testContains() {
        TestHelper.run("var checkThisString = \"Checking\" as string; var checkForThisString = \"ing\" as string; if (checkThisString in checkForThisString) { print(\"Yes\"); } else { print(\"No\"); }");
        
        TestAssertions.assertOne("Yes", 0);
        
    }
    
    @Test
    public void testWhile() {
        TestHelper.run("var i = 0; while i < 10 {print(i); i += 1;} print(\"After loop: \" + i); while (i > 0) {if i == 5 break; print(i); i -= 1;} print(\"After loop 2: \" + i);");
        
        TestAssertions.assertCalculated(Integer::toString, 10);
        
        
        TestAssertions.assertOne("After loop: 10", 10);
        for(int i = 10; i > 5; i--)
            TestAssertions.assertOne(Integer.toString(i), 21 - i);
        TestAssertions.assertOne("After loop 2: 5", 16);
    }
    
    
}
