# JvmStuff

A collection of tools designed to manipulate and inspect JVM bytecode behavior, specifically targeting the Java compiler's generation of synthetic members.

## Components

### :javaagent
A Java instrumentation agent (targeting Java 25+) designed to hack into the Java compiler (`javac`) at runtime. It intercepts the `com.sun.tools.javac.jvm.Target` class to revert or modify bytecode generation strategies.

**What it does:**
- Uses the `java.lang.classfile` API (introduced in modern JDKs) to transform the compiler's behavior.
- Forces `optimizeOuterThis()` and `nullCheckOuterThisByDefault()` to return `false`.
- This effectively disables certain modern `javac` optimizations related to how inner classes handle references to their outer instances, which can be useful for maintaining compatibility with older JVM behaviors or specific bytecode analysis tools.

### :debug-tool
A Spigot plugin that allows for real-time inspection of classes loaded in the JVM.

**Features:**
- Provides a `/debug <class>` command.
- Displays class hierarchy (superclass, interfaces).
- Lists all declared fields, highlighting if they are **synthetic**.
- Lists all declared methods with their return types, parameters, and **access flags**.
- Useful for verifying the effects of the Java Agent or understanding how the compiler transformed your code.

## Getting Started

### Prerequisites
- JDK 25 or higher

### Building
Build both components using the Gradle wrapper:
```bash
./gradlew build
```

### Usage

#### Using the Java Agent
To use the agent when compiling your code with gradle:
```kotlin
tasks.withType<JavaCompile>().configureEach {
        options.isFork = true
        options.forkOptions.jvmArgs = listOf(
            "-javaagent:PATH TO AGENT",
            "--add-opens=jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED"
        )
}
```

#### Using the Debug Tool
1. Drop the `debug-tool.jar` into your Spigot server's `plugins` folder.
2. Start the server.
3. Use the command in-game or from console:
   ```
   /debug java.util.ArrayList
   ```
   *Permission required: `titan.debug` or OP.*