package com.codrest.teriser_java_connector;

import com.codrest.teriser_java_connector.annotation.UserDefinedClass;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassScanner {

    public static HashMap<String, Class<?>> classMap = new HashMap<>();

    public static void scanAllClass() {
        for (Package p : Package.getPackages()) {
            String packageName = p.getName().replace(".", "/");
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName);
            if (Objects.nonNull(stream)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                Set<Class<?>> set = reader.lines()
                        .filter(line -> line.endsWith(".class"))
                        .map(line ->
                                getClass(line, packageName))
                        .collect(Collectors.toSet());

                for (Class<?> c : set) {
                    if (c.isAnnotationPresent(UserDefinedClass.class)) {
                        System.out.println("Class "+c);
                        classMap.put(c.getSimpleName(), c);
                    }
                }
            }
        }
    }

    private static Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName.replace("/", ".") + "." + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
