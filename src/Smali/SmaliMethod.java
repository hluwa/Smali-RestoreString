package Smali;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Smali.SmaliClass.SmaliType;
import Smali.SmaliModifier.ModifierAttribute;
import Smali.SmaliModifier.ModifierPremission;
import Smali.SmaliModifier.ModifierType;

public class SmaliMethod extends SmaliObject{
	private String methodName;
	private ArrayList<String> args;
	private String superClass;
	private String returnType;
	
	public static final String MethodRegEx = ModifierType.TYPE_METHOD +" ([a-z ]{0,64}) ([a-zA-Z0-9_$<>]{0,128})\\(([a-zA-Z0-9_$;/\\[]{0,512})\\)([a-zA-Z0-9_$;/\\[]{0,256})";
	public static final String MethodRegEx_NotPre = ModifierType.TYPE_METHOD +" ([a-zA-Z0-9_$<>]{0,128})\\(([a-zA-Z0-9_$;\\[/]{0,512})\\)([a-zA-Z0-9_$;/\\[]{0,256})";
	
	
	
	/**
	 * @param premission: ModifierPremission
	 * **/
	public SmaliMethod(ModifierPremission premission,ArrayList<ModifierAttribute> attributes,ArrayList<String> args,String returnType,String superCls,String name,int lineInFile){
		super(premission,attributes,lineInFile);
		this.args = args;
		this.returnType = returnType;
		this.methodName = name;
		this.superClass = superCls;
	}
	
	public SmaliMethod(String name,ArrayList<String> args,String returnType){
		this(null,null,args,returnType,null,name,-1);
	}
	
	/**
	 * @return ClassName+"->"+MethodName
	 * **/
	public String toString(){
		return superClass+"->"+methodName;
	}
	
	public boolean equals(SmaliMethod method){
		if(method == null){
			return false;
		}
		if(!this.methodName.equals(method.methodName)){
			return false;
		}
		if(!this.getMethodSig().equals(method.getMethodSig())){
			return false;
		}
		return true;
	}
	
	private void setMethodName(String name){
		this.methodName = name;
	}
	
	public void setSuperClass(String clsName){
		this.superClass = clsName;
	}

	public boolean isSynthetic(){
		if(getAttributes().indexOf(ModifierAttribute.ATTRIBUTE_SYNTHETIC) == -1  && getAttributes().indexOf(ModifierAttribute.ATTRIBUTE_DECLARED_SYNCHRONIZED) == -1){
			return false;
		}
		return true;
	}
	
	public boolean isNative(){
		if(getAttributes().indexOf(ModifierAttribute.ATTRIBUTE_NATIVE) != -1){
			return true;
		}
		return false;
	}
	
	public String getMethodName(){
		return methodName;
	}
			
	public ArrayList<String> getArgs(){
		return args;
	}
	
	public String getMethodSig(){
		StringBuilder sig =  new StringBuilder("(");
		for(String arg : args){
			sig.append(arg);
		}
		sig.append(")");
		sig.append(returnType);
		return sig.toString();
	}
	
	public SmaliClass getReturnType(){
		return SmaliClass.FindClass(returnType);
	}

	public SmaliClass getSuperClass(){
		return SmaliClass.FindClass(superClass);
	}
	
	public String getReturnTypeName(){
		return returnType;
	}
	
	public String getSuperClassName(){
		return superClass;
	}
	
	public int rename(String text){
		ArrayList<String> codes = getSuperClass().getCodes();
		String line = codes.get(getLineInFile()).toString();
		if(line == null){
			return -1;
		}
		boolean havePre = true;
		Pattern pattern = Pattern.compile(MethodRegEx);
		Matcher matcher = pattern.matcher(line);
		if(!matcher.find()){
			pattern = Pattern.compile(MethodRegEx_NotPre);
			matcher = pattern.matcher(line);
			if(!matcher.find()){
				return -2;	
			}
			havePre = false;
		}
		if(havePre){
			codes.set(getLineInFile(),SmaliModifier.ModifierType.TYPE_METHOD +" " + matcher.group(1) + " " + text + "(" + matcher.group(3) + ")" +matcher.group(4)) ;
		}
		else{
			codes.set(getLineInFile(),SmaliModifier.ModifierType.TYPE_METHOD+" " + text + "(" + matcher.group(2) + ")" +matcher.group(3));
		}
		setMethodName(text);
		return 0;
	}
	
	public void addLineCodeToEnd(String code){
		int startLine = getLineInFile();
		ArrayList<String> codes = getSuperClass().getCodes();
		while(!codes.get(startLine + 1).equals(ModifierType.TYPE_METHOD_END.toString())){
			startLine++;	
		}
		codes.add(startLine,code);
		setOverAddLine(startLine);
	}
	
	public static ArrayList<String> PraseMethodArgs(String text){
		char[] c = text.toCharArray();
		ArrayList<String> args = new ArrayList<String>();
		int fristi = 0;
		int endi = 0;
		for(int i = 0;i<c.length ;i++){
			if(endi == 0){
				SmaliType type = SmaliType.GetBaseType(c[i]);
				if(type != null){
					args.add(type.toString());
					continue;
				}
				fristi = i;
				endi++;
			}
			else{
				if(c[i] == ';'){
					args.add(text.substring(fristi,fristi + endi + 1));
					endi = 0;
					continue;
				}
				endi++;
				
			}
		}
		return args;
	}
}
