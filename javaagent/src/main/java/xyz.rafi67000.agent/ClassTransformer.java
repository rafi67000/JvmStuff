package xyz.rafi67000.agent;

import java.lang.classfile.ClassFile;
import java.lang.classfile.MethodModel;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class ClassTransformer  implements ClassFileTransformer {
    public static ClassTransformer INSTANCE = new ClassTransformer();

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer){

        if (!className.equals("com/sun/tools/javac/jvm/Target")) {
            return null;
        }

        var classModel = ClassFile.of().parse(classfileBuffer);

        return ClassFile.of().build(classModel.thisClass().asSymbol(), cb -> {
            for (var element : classModel) {
                if (element instanceof MethodModel method) {

                    String mName = method.methodName().stringValue();

                    if ((mName.equals("optimizeOuterThis") ||
                            mName.equals("nullCheckOuterThisByDefault"))
                            && method.methodType().stringValue().equals("()Z")) {

                        cb.withMethod(mName, method.methodTypeSymbol(), method.flags().flagsMask(), methodBuilder -> methodBuilder.withCode(code -> {
                            code.iconst_0(); // false
                            code.ireturn();
                        }));
                        System.out.println("JavaAgent transformed " + mName);

                    } else {
                        cb.with(method); // keep original
                    }

                } else {
                    cb.with(element);
                }
            }
        });
    }
}
