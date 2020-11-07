package zensharp.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zensharp.TestHelper;
import zensharp.annotations.ZenClass;
import zensharp.annotations.ZenMethod;
import zensharp.annotations.ZenProperty;
import zensharp.TestAssertions;

@SuppressWarnings("WeakerAccess")
public class TestZenProperty {
    
    
    @BeforeAll
    public static void setupEnvironment() {
        TestHelper.setupEnvironment();
        TestHelper.registry.registerGlobal("testerInstance", TestHelper.registry.getStaticFunction(PropertyTester.class, "getInstance"));
    }
    
    
    @BeforeEach
    public void beforeEach() {
        TestHelper.beforeEach();
        PropertyTester.instance = null;
    }
    
    
    @Test
    public void testSet_NoParameters() {
        TestHelper.run("testerInstance().a = 'abc';");
        
        Assertions.assertNotNull(PropertyTester.instance);
        Assertions.assertEquals("abc", PropertyTester.getInstance().a);
    }
    
    @Test
    public void testSetMethod_NoParameters() {
        TestHelper.run("testerInstance().setA('abc');");
        
        Assertions.assertNotNull(PropertyTester.instance);
        Assertions.assertEquals("abc", PropertyTester.getInstance().a);
    }
    
    @Test
    public void testGet_NoParameters() {
        PropertyTester.getInstance().a = "someValue";
        TestHelper.run("print(testerInstance().a);");
        
        TestAssertions.assertMany("someValue");
    }
    
    @Test
    public void testGetMethod_NoParameters() {
        PropertyTester.getInstance().a = "someValue";
        TestHelper.run("print(testerInstance().getA());");
        
        TestAssertions.assertMany("someValue");
    }
    
    @Test
    public void testSet_Methods() {
        TestHelper.run("testerInstance().b = 'abc';");
        
        Assertions.assertNotNull(PropertyTester.instance);
        Assertions.assertEquals("abc", PropertyTester.getInstance().b);
        TestAssertions.assertMany("setB");
    }
    
    @Test
    public void testSetMethod_Methods() {
        TestHelper.run("testerInstance().setB('abc');");
        
        Assertions.assertNotNull(PropertyTester.instance);
        Assertions.assertEquals("abc", PropertyTester.getInstance().b);
        TestAssertions.assertMany("setB");
    }
    
    @Test
    public void testGet_Methods() {
        PropertyTester.getInstance().b = "someValue";
        TestHelper.run("print(testerInstance().b);");
        
        TestAssertions.assertMany("getB", "someValue");
    }
    
    @Test
    public void testGetMethod_Methods() {
        PropertyTester.getInstance().b = "someValue";
        TestHelper.run("print(testerInstance().getB());");
        
        TestAssertions.assertMany("getB", "someValue");
    }
    
    @Test
    public void testSet_MethodReRouting() {
        TestHelper.run("testerInstance().c = 'abc';");
        
        Assertions.assertNotNull(PropertyTester.instance);
        Assertions.assertEquals("abc", PropertyTester.getInstance().c);
        TestAssertions.assertMany("setC2");
    }
    
    @Test
    public void testGet_MethodReRouting() {
        PropertyTester.getInstance().c = "someValue";
        TestHelper.run("print(testerInstance().c);");
        
        TestAssertions.assertMany("getC2", "someValue");
    }
    
    
    //No ZenRegister because registered natively
    @ZenClass("tests.internal.PropertyTester")
    @SuppressWarnings("unused")
    public static final class PropertyTester {
        
        
        public static PropertyTester instance;
        @ZenProperty
        public String a;
        
        @ZenProperty
        public String b;
        
        @ZenProperty(getter = "getC2", setter = "setC2")
        public String c;
        
        
        @ZenMethod
        public static PropertyTester getInstance() {
            return instance != null ? instance : (instance = new PropertyTester());
        }
        
        @ZenMethod
        public String getB() {
            TestHelper.print("getB");
            return b;
        }
        
        @ZenMethod
        public void setB(String b) {
            TestHelper.print("setB");
            this.b = b;
        }
        
        public String getC2() {
            TestHelper.print("getC2");
            return c;
        }
        
        public void setC2(String c) {
            TestHelper.print("setC2");
            this.c = c;
        }
    }
}
