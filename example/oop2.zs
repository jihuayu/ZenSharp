type C = function()void;

interface A{
    function pp(t as C) as void;
}

class B impl A{
    function pp(t as C) as void{
        print('111');
        t();
    }
}
var b = B();
b.pp(function() as void {print('222');});