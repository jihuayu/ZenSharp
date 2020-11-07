package zensharp;

import zensharp.annotations.ZenClass;
import zensharp.annotations.ZenConstructor;
import zensharp.annotations.ZenMethod;
import zensharp.annotations.ZenProperty;

@ZenClass("test.A")
public class ZenTestClassA {
    @ZenMethod("p")
    public void p(){
        System.out.println(111111198);
    }

    @ZenClass("test.Pt")
    public static class PT extends ZenTestClassA{

        @ZenProperty("oou")
        public int i =100;

        @ZenConstructor
        public PT(){
            this.i = 1001;
        }

        @ZenMethod("t")
        public void t(){
            System.out.println("114514");
        }
    }

    @ZenClass("test.Pi")
    public interface PI{
        @ZenMethod
        int a();
    }

    @ZenClass("test.Pi2")
    public interface PI2{
        @ZenMethod
        int b();
    }
}
