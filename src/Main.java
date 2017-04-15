import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Smali.SmaliClass;
import Smali.SmaliMethod;
import Smali.SmaliModifier.ModifierAttribute;
import Smali.SmaliModifier.ModifierPremission;
public class Main {
	public static String jar;
	public static String smaliDir;
	public static String enMtdSig;
	
	public static URLClassLoader clsLoader;
	public static Class deCls;
	public static Method deMtd;
	public static int num;
	
	public static void main(String args[]) throws Exception{
		jar = args[0];
		smaliDir = args[1];
		enMtdSig = args[2];
		
		Pattern sigPtn = Pattern.compile("L([a-zA-Z0-9/$_]{1,128});->([a-zA-Z0-9/$_]{1,128})");
		Matcher mtc = sigPtn.matcher(enMtdSig);
		if(!mtc.find()){
			System.out.println("not matche methodSig");
			return;
		}
		String className = mtc.group(1).replace("/", ".");
		String mtdName = mtc.group(2);
		URLClassLoader clsLoader = new URLClassLoader(new URL[]{new File(jar).toURI().toURL()});
		Class deCls = clsLoader.loadClass(className);
		deMtd = deCls.getMethod(mtdName, String.class);
		ArrayList<File> files = Utils.GetInDirFiles(new File(smaliDir));
		for(File file : files){
			SmaliClass cls = SmaliClass.PraseClass(file);
			restore(cls);
		}
		System.out.println("restored, restore num is " + num);
//		if(args[0].equals("-p")){
//			if(args.length>= 4 &&  args[2].equals("--f")){
//				SafeProject(new File(args[1]),args[3]);
//			}
//			else{
//				SafeProject(new File(args[1]),null);
//			}
//		}
//		
		
	}
	

	public static void restore(SmaliClass cls) throws Exception{
		ArrayList<String> codes = cls.getCodes();
		
		

		Pattern invokePtn = Pattern.compile("invoke-static \\{([vp\\d]{0,4})\\}, "+enMtdSig);
		for(int i = 0;i<codes.size();i++){
			String code = codes.get(i);
			if(code.indexOf(enMtdSig) != -1){
				Matcher mtc = invokePtn.matcher(code);
				if(!mtc.find()){
					continue;
				}
				
				String reg = mtc.group(1);
				Pattern constPtn = Pattern.compile("const-string "+reg+", \"(.*)\"");
				int i1 = i - 1;
				code = codes.get(i1);
				while(code.indexOf("const-string " + reg) == -1){
					i1--;
					code = codes.get(i1);
				}
				mtc = constPtn.matcher(code);
				if(!mtc.find()){
					continue;
				}
				String text = mtc.group(1);
				String deText = (String)deMtd.invoke(null,text);
				
				
				int i2 = i + 1;
				code = codes.get(i2);
				while(code.indexOf("move-result-object " + reg) == -1){
					i2++;
					code = codes.get(i2);
				}
				codes.set(i, "");
				codes.set(i1, "");
				codes.set(i2, "	const-string " + reg + ",\"" + Utils.gbEncoding(deText)+"\"");
				System.out.println(cls.toString() + ":" + i2 + "		" +text + " -> " + deText);
				num++;
			}
		}
		cls.saveChange();
	}
	
	
//	public static void restore(File dex,String methidSig){
//		File maniFile = new File(projectDir.toString() + "\\AndroidManifest.xml");
//		if(!maniFile.exists()){
//			System.out.println("[*SafeProjectError]AndroidManifest.xml is not found");
//			return;
//		}
//		String mainClassName = Utils.FindMainClass(maniFile);
//		if(mainClassName == null){
//			System.out.println("[*SafeProjectError]Launcher Class is not found");
//			return;
//		}
//		ArrayList<File> files = Utils.GetInDirFiles(new File(projectDir.toString() +"\\smali\\"));
//		if(files == null){
//			System.out.println("[*SafeProjectError]Files is null");
//			return;
//		}
//		for(File file : files){
//			SmaliClass cls = SmaliUtils.PraseClass(file);
//			if(cls == null || cls.isAbstract() || cls.isInterface() || cls.toString().indexOf("$") != -1){
//				continue;
//			}
//			if(cls.toString().startsWith("Landroid")){
//				continue;
//			}
//			String className = cls.toString();
//			className = className.startsWith("L") ? className.substring(1,className.length()-1) : className;
//			className = className.endsWith(";") ? className.substring(0,className.length()-1) : className;
//			className = className.replace("/",".");
//			if(filter != null){
//				if(className.indexOf(filter) == -1){
//					if(mainClassName.equals(className)){
//						SmaliClass.ClassTable.add(cls);
//						AddClinit(cls);
//						cls.saveChange();
//					}
//					continue;
//				}
//			}
//			SmaliClass.ClassTable.add(cls);
//			System.out.println("[I:ChangeSmali]: " + cls.toString());
//			ChangeSmali(cls);
//			if(mainClassName.equals(className)){
//				AddClinit(cls);
//			}
//			cls.saveChange();
//		}
//		NativeHelper helper = NativeHelper.getInstance();
//		helper.setOnLoadCode(helper.createOnLoadCode());
//		helper.writeSource();
//	}
//	
//	public static void ChangeSmali(SmaliClass cls){
//		String methodArrayName = Utils.GetRandomMethodName(0);  //�������
//		NativeHelper helper = NativeHelper.getInstance();
//		NativeArray<JNINativeMethod> array = new NativeArray<JNINativeMethod>(cls.toString(),methodArrayName);
//		ArrayList<SmaliMethod> smaliMethods = cls.getMethods();
//		ArrayList<SmaliMethod> addMethod = new ArrayList<SmaliMethod>();
//		for(int i = 0 ;i < smaliMethods.size() ; i++){
//			SmaliMethod method = smaliMethods.get(i);
//			String srcName = method.getMethodName();
//			if(method.isNative() || method.isSynthetic() || method.isFinal()){
//				continue;
//			}
//			//�����������;�̬�����
//			if(!"<init>".equals(srcName) && !"<clinit>".equals(srcName) ){
//				String reName = Utils.GetRandomMethodName(1);  //�������
//				method.rename(reName);
//				NativeMethod nativeMethod = NativeMethod.smaliMethod2NativeMethod(method,Utils.GetRandomMethodName(2));  //nativeName�������
//				helper.addMethod(nativeMethod);
//				array.add(new JNINativeMethod(srcName,method.getMethodSig().toString(),NativeHelper.SmaliType2NativeType(method.getReturnTypeName()),nativeMethod.getMethodName()));
//				ArrayList<ModifierAttribute> attributes = method.getAttributes();
//				attributes.add(ModifierAttribute.ATTRIBUTE_NATIVE);
//				addMethod.add(new SmaliMethod(method.getPremission(),attributes,method.getArgs(),method.getReturnTypeName(),method.getSuperClassName(),srcName,-1));
//			}
//		}
//		for(SmaliMethod method : addMethod){
//			cls.addMethod(method, "");
//		}
//		helper.addJNIMethod(array);
//	}
//
//	public static void AddClinit(SmaliClass cls){
//		ArrayList<ModifierAttribute> attr = new ArrayList<ModifierAttribute>();
//		attr.add(ModifierAttribute.ATTRIBUTE_STATIC);
//		attr.add(ModifierAttribute.ATTRIBUTE_CONSTRUCTOR);
//		SmaliMethod clinit = new SmaliMethod(ModifierPremission.PREMISSION_DEFAULT,attr,new ArrayList<String>(),"V",cls.toString(),"<clinit>",-1);
//		StringBuilder codes = new StringBuilder();
//		if(cls.findMethod(clinit) ==  null){
//			codes.append("    .locals 1\r\n");
//			codes.append("    const-string v0,\"smalisafe\"\r\n");
//			codes.append("    invoke-static {v0}, Ljava/lang/System;->loadLibrary(Ljava/lang/String;)V\r\n");
//			codes.append("    return-void\r\n");
//			cls.addMethod(clinit, codes.toString());
//		}
//		else{
//			clinit = cls.findMethod(clinit);
//			clinit.addLineCodeToEnd("   const-string v0,\"smalisafe\"");
//			clinit.addLineCodeToEnd("    invoke-static {v0}, Ljava/lang/System;->loadLibrary(Ljava/lang/String;)V");
//		}
//	}
}
