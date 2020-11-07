import test.Pt;
import test.Pi;
import test.Pi2;

class A extends Pt implements Pi , Pi2
{
    function a() as int
    {
        return 1;
    }

    function b() as int
    {
        return 2;
    }
}

var tt = A();
tt.t();
println(tt.oou)
println(tt.a())
println(tt.b())