package zensharp.definitions;

import zensharp.ZenTokener;
import zensharp.compiler.IEnvironmentGlobal;
import zensharp.parser.Token;
import zensharp.parser.expression.ParsedExpression;
import zensharp.type.ZenType;
import zensharp.util.ZenPosition;

public class ParsedGlobalValue {
	private final ZenPosition position;
	private final String name;
	private final ZenType type;
	private final ParsedExpression value;
	private final String owner;
	private final boolean global;
	
	ParsedGlobalValue(ZenPosition position, String name, ZenType type, ParsedExpression value, String owner, boolean global){
		this.position = position;
		this.name = name;
		this.type = type;
		this.value = value;
		this.owner = owner;
        
        this.global = global;
    }

	public String getName() {
		return name;
	}

	public ZenPosition getPosition() {
		return position;
	}
	
	public ZenType getType() {
		return type;
	}
	
	public ParsedExpression getValue() {
		return value;
	}
	
	public String getOwner() {
		return owner;
	}
	

	public static ParsedGlobalValue parse(ZenTokener parser, IEnvironmentGlobal environment, String owner, boolean global) {
		//Start ("GLOBAL")
		Token startingPoint = parser.next();
		
		//Name ("globalName", "test")
		String name = parser.required(ZenTokener.T_ID, "Global value requires a name!").getValue();
		
		//Type ("as type", "as IItemStack")
		ZenType type = ZenType.ANY;
		Token nee = parser.optional(ZenTokener.T_AS);
		if(nee!=null) {
			type = ZenType.read(parser, environment);
		}
		
		//"="
		parser.required(ZenTokener.T_ASSIGN, "Global values have to be initialized!");
		
		//"value, <minecraft:dirt>"
		ParsedExpression value = ParsedExpression.read(parser, environment);
		
		//";"
		parser.required(ZenTokener.T_SEMICOLON, "; expected");
		
		//throw it together
		return new ParsedGlobalValue(startingPoint.getPosition(), name, type, value, owner, global);
	}
    
    public boolean isGlobal() {
        return global;
    }
}
