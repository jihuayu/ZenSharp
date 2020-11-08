class B{
    function pp() as void{
        print("233");
    }
}

class A<T extends B>{
    var i as T;
    constructor(a as T){
        i = a;
    }
}

val b = A(B()).i;
b.pp();