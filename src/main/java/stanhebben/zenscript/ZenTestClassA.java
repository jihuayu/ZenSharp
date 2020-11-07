package stanhebben.zenscript;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenConstructor;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("test.A")
public class ZenTestClassA {
    @ZenMethod("p")
    public void p(){
        System.out.println(111111198);
    }

    @ZenClass("test.Pt")
    public static class PT extends ZenTestClassA{

        @ZenConstructor
        public PT(){

        }

        @ZenMethod("t")
        public void t(){
            System.out.println("114514");
        }
    }
}
