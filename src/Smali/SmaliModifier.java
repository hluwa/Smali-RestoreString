package Smali;

public class SmaliModifier {
	private String modifierText;
	
	public static class ModifierPremission extends SmaliModifier{
		public ModifierPremission(String modifierText){
			super(modifierText);
		}
		public static final ModifierPremission PREMISSION_PUBLIC = new ModifierPremission("public");
		public static final ModifierPremission PREMISSION_PRIVATE = new ModifierPremission("private");
		public static final ModifierPremission PREMISSION_PROTECTED = new ModifierPremission("protected");
		public static final ModifierPremission PREMISSION_DEFAULT = PREMISSION_PUBLIC;
		public static ModifierPremission Get(String text){
			if(text.equals(PREMISSION_PUBLIC.toString())){
				return PREMISSION_PUBLIC;
			}
			else if(text.endsWith(PREMISSION_PRIVATE.toString())){
				return PREMISSION_PRIVATE;
			}
			else if(text.endsWith(PREMISSION_PROTECTED.toString())){
				return PREMISSION_PROTECTED;
			}
			return PREMISSION_DEFAULT;
		}
	}
	
	public static class ModifierAttribute extends SmaliModifier{
		public ModifierAttribute(String modifierText){
			super(modifierText);
		}
		public static final ModifierAttribute ATTRIBUTE_STATIC = new ModifierAttribute("static");;
		public static final ModifierAttribute ATTRIBUTE_FINAL = new ModifierAttribute("final"); 
		public static final ModifierAttribute ATTRIBUTE_SYNTHETIC = new ModifierAttribute("synthetic");
		public static final ModifierAttribute ATTRIBUTE_DECLARED_SYNCHRONIZED = new ModifierAttribute("declared-synchronized");
		public static final ModifierAttribute ATTRIBUTE_NATIVE = new ModifierAttribute("native");
		public static final ModifierAttribute ATTRIBUTE_CONSTRUCTOR = new ModifierAttribute("constructor");
		public static final ModifierAttribute ATTRIBUTE_INTERFACE = new ModifierAttribute("interface");
		public static final ModifierAttribute ATTRIBUTE_ABSTRACT = new ModifierAttribute("abstract");
		public static ModifierAttribute Get(String text){
			if(text.equals(ATTRIBUTE_STATIC.toString())){
				return ATTRIBUTE_STATIC;
			}
			else if(text.equals(ATTRIBUTE_FINAL.toString())){
				return ATTRIBUTE_FINAL;
			}
			else if(text.equals(ATTRIBUTE_SYNTHETIC.toString())){
				return ATTRIBUTE_SYNTHETIC;
			}
			else if(text.equals(ATTRIBUTE_NATIVE.toString())){
				return ATTRIBUTE_NATIVE;
			}
			else if(text.equals(ATTRIBUTE_CONSTRUCTOR.toString())){
				return ATTRIBUTE_CONSTRUCTOR;
			}
			else if(text.equals(ATTRIBUTE_INTERFACE.toString())){
				return ATTRIBUTE_INTERFACE;
			}
			else if(text.equals(ATTRIBUTE_ABSTRACT.toString())){
				return ATTRIBUTE_ABSTRACT;
			}
			else if(text.equals(ATTRIBUTE_DECLARED_SYNCHRONIZED.toString())){
				return ATTRIBUTE_DECLARED_SYNCHRONIZED;
			}
			return new ModifierAttribute(text);
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return super.toString();
		}
	}
	
	public static class ModifierType extends SmaliModifier{
		public ModifierType(String modifierText){
			super(modifierText);
		}
		public static final ModifierType TYPE_CLASS = new ModifierType(".class");
		public static final ModifierType TYPE_METHOD = new ModifierType(".method");
		public static final ModifierType TYPE_SUPER = new ModifierType(".super");
		public static final ModifierType TYPE_SOURCE = new ModifierType(".source");
		public static final ModifierType TYPE_METHOD_END = new ModifierType(".end method");
		public static final ModifierType TYPE_FIELD = new ModifierType(".field");
		
		public static ModifierType Get(String text){
			if(text.equals(TYPE_CLASS.toString())){
				return TYPE_CLASS;
			}
			else if(text.endsWith(TYPE_METHOD.toString())){
				return TYPE_METHOD;
			}
			else if(text.endsWith(TYPE_SUPER.toString())){
				return TYPE_SUPER;
			}
			else if(text.endsWith(TYPE_SOURCE.toString())){
				return TYPE_SOURCE;
			}
			else if(text.endsWith(TYPE_METHOD_END.toString())){
				return TYPE_METHOD_END;
			}
			else if(text.endsWith(TYPE_FIELD.toString())){
				return TYPE_FIELD;
			}
			return new ModifierType(text);
		}
	}
	
	
	public SmaliModifier(String modifierText){
		this.modifierText = modifierText;
	}

	
	public String toString(){
		return modifierText;
	}
	
	public boolean equals(SmaliModifier modifier){
		return this.toString().equals(modifier.toString());
	}
}
