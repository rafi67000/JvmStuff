package xyz.rafi67000.agent;

import java.lang.instrument.Instrumentation;

public class JavacAgent {

    public static void premain(String args, Instrumentation inst) {
        System.out.println("Java Agent has been initialized!");
        inst.addTransformer(ClassTransformer.INSTANCE, true);
    }
}