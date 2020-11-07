interface A{
    fun pp() as void;
}

class B impl A{
    fun pp() as void{
        println(111);
    }
}
var b = B();
b.pp();