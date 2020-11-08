package zensharp;

import zensharp.annotations.ZenClass;
import zensharp.annotations.ZenConstructor;
import zensharp.annotations.ZenMethod;
import zensharp.annotations.ZenProperty;

@ZenClass("test.A")
public class ZenTestClassA<T extends String> {

    @ZenConstructor
    public ZenTestClassA(){
        System.out.println(1111);

    }
    @ZenMethod("p")
    public String p(T a){
        return "1112";
    }
}
