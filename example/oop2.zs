interface A{
    function pp() as void;
}

class B impl A{
    function pp() as void{
        print('111');
    }
}
var b = B();
b.pp();