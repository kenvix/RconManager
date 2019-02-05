package com.kenvix.utils.preprocessor;

import com.kenvix.utils.Environment;
import com.kenvix.utils.StringTools;
import com.kenvix.utils.annotation.ViewAutoLoad;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.kenvix.utils.PreprocessorName.getViewAutoLoaderMethodName;

public class ViewPreprocessor extends BasePreprocessor {

    @Override
    public Class[] getSupportedAnnotations() {
        return new Class[] {ViewAutoLoad.class};
    }

    @Override
    protected boolean onProcessingOver(Map<Element, List<Element>> filteredAnnotations, Set<? extends TypeElement> originalAnnotations, RoundEnvironment roundEnv) {
        List<MethodSpec> methods = new LinkedList<>();

        getMethodBuffer().forEach((name, builderList) ->
                builderList.forEach(methodBuilder -> methods.add(methodBuilder.build()))
        );

        TypeSpec viewToolset = TypeSpec.classBuilder("ViewToolset")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethods(methods)
                .build();

        JavaFile javaFile = getOutputJavaFileBuilder(viewToolset).build();

        try {
            saveOutputJavaFile(javaFile);
        } catch (IOException ex) {
            throw new IllegalStateException(ex.toString());
        }

        return true;
    }

    private void processViewAutoLoad(Element targetClass, List<Element> annotatedElements) {
        TypeMirror targetClassType = targetClass.asType();
        String targetClassFullName = getViewAutoLoaderMethodName(targetClass.toString());

        annotatedElements.forEach(annotatedElement -> {
            if(annotatedElement.getKind() == ElementKind.FIELD) {
                TypeMirror fieldType = annotatedElement.asType();

                ViewAutoLoad annotation = annotatedElement.getAnnotation(ViewAutoLoad.class);

                List<MethodSpec.Builder> builders = getMethodBuilder(targetClassFullName, targetClass);
                String RMemberName = StringTools.convertUppercaseLetterToUnderlinedLowercaseLetter(annotatedElement.getSimpleName().toString());
                Name fieldVarName = annotatedElement.getSimpleName();
                ClassName RId =  ClassName.get(Environment.TargetAppPackage, "R", "id");

                final boolean generateCodeForActivityClass = targetClassFullName.endsWith("Activity");

                if(generateCodeForActivityClass) {
                    builders.forEach(builder -> builder
                            .addStatement("target.$N = target.findViewById($T.$N)",
                                    fieldVarName,
                                    RId,
                                    RMemberName));
                } else {
                    builders.forEach(builder -> builder
                            .addStatement("target.$N = targetView.findViewById($T.$N)",
                                    fieldVarName,
                                    RId,
                                    RMemberName));
                }
            }
        });
    }

    private MethodSpec.Builder getCommonFormCheckBuilder(String methodName, Element clazz) {
        ClassName targetClassName = getTargetClassName(clazz);

        return MethodSpec
                .methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addStatement("$T target = ($T) targetRaw", targetClassName, targetClassName)
                .returns(void.class);
    }


    @Override
    protected List<MethodSpec.Builder> createMethodBuilder(String methodName, Element clazz) {
        //TypeMirror typeMirror = clazz.asType();
        final boolean generateCodeForActivityClass = methodName.endsWith("Activity");
        ClassName targetViewClassName = ClassName.get("android.view", "View");

        return new ArrayList<MethodSpec.Builder>() {{
            if(generateCodeForActivityClass) {
                add(getCommonFormCheckBuilder(methodName, clazz)
                        .addParameter(Object.class, "targetRaw"));
            } else {
                add(getCommonFormCheckBuilder(methodName, clazz)
                        .addParameter(Object.class, "targetRaw")
                        .addParameter(targetViewClassName, "targetView"));
            }
        }};
    }

    @Override
    protected boolean onProcess(Map<Element, List<Element>> filteredAnnotations, Set<? extends TypeElement> originalAnnotations, RoundEnvironment roundEnv) {
        filteredAnnotations.forEach(this::processViewAutoLoad);
        return true;
    }
}
