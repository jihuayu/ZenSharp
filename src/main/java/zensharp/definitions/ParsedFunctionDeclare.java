package zensharp.definitions;

import zensharp.ZenTokener;
import zensharp.compiler.IEnvironmentGlobal;
import zensharp.parser.Token;
import zensharp.statements.Statement;
import zensharp.type.ZenType;
import zensharp.type.ZenTypeAny;
import zensharp.util.ZenPosition;

import java.util.ArrayList;
import java.util.List;

public class ParsedFunctionDeclare {

    private final ZenPosition position;
    private final String name;
    private final List<ParsedFunctionArgument> arguments;
    private final ZenType returnType;
    private final String signature;


    public ParsedFunctionDeclare(ZenPosition position, String name, List<ParsedFunctionArgument> arguments, ZenType returnType) {
        this.position = position;
        this.name = name;
        this.arguments = arguments;
        this.returnType = returnType;

        StringBuilder sig = new StringBuilder();
        sig.append("(");
        for(ParsedFunctionArgument argument : arguments) {
            sig.append(argument.getType().getSignature());
        }
        sig.append(")");
        sig.append(returnType.getSignature());
        signature = sig.toString();
    }


    public static ParsedFunctionDeclare parse(ZenTokener parser, IEnvironmentGlobal environment) {
        parser.next();
        Token tName = parser.required(ZenTokener.T_ID, "identifier expected");

        // function (argname [as type], argname [as type], ...) [as type] {
        // ...contents... }
        parser.required(ZenTokener.T_BROPEN, "( expected");

        List<ParsedFunctionArgument> arguments = new ArrayList<>();
        if(parser.optional(ZenTokener.T_BRCLOSE) == null) {
            Token argName = parser.required(ZenTokener.T_ID, "identifier expected");
            ZenType type = ZenTypeAny.INSTANCE;
            if(parser.optional(ZenTokener.T_AS) != null) {
                type = ZenType.read(parser, environment);
            }

            arguments.add(new ParsedFunctionArgument(argName.getValue(), type));

            while(parser.optional(ZenTokener.T_COMMA) != null) {
                Token argName2 = parser.required(ZenTokener.T_ID, "identifier expected");
                ZenType type2 = ZenTypeAny.INSTANCE;
                if(parser.optional(ZenTokener.T_AS) != null) {
                    type2 = ZenType.read(parser, environment);
                }

                arguments.add(new ParsedFunctionArgument(argName2.getValue(), type2));
            }

            parser.required(ZenTokener.T_BRCLOSE, ") expected");
        }

        ZenType type = ZenTypeAny.INSTANCE;
        if(parser.optional(ZenTokener.T_AS) != null) {
            type = ZenType.read(parser, environment);
        }
        parser.required(ZenTokener.T_SEMICOLON, "; expected");
        return new ParsedFunctionDeclare(tName.getPosition(), tName.getValue(), arguments, type);
    }

    public ZenPosition getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getSignature() {
        return signature;
    }

    public ZenType getReturnType() {
        return returnType;
    }

    public List<ParsedFunctionArgument> getArguments() {
        return arguments;
    }

    public ZenType[] getArgumentTypes() {
        ZenType[] result = new ZenType[arguments.size()];
        for(int i = 0; i < arguments.size(); i++) {
            result[i] = arguments.get(i).getType();
        }
        return result;
    }
}
