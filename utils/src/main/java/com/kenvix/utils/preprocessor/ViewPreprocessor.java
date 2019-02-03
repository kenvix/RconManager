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
    public Set<String> getSupportedAnnotationTypes() {
        return new LinkedHashSet<String>() {{
            add(ViewAutoLoad.class.getCanonicalName());
        }};
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> rootElements = roundEnv.getRootElements();
        Map<Element, List<Element>> tasks = new HashMap<>();

        for (Element classElement : rootElements) {
            if(classElement.toString().startsWith(Environment.TargetAppPackage)) {
                List<? extends Element> enclosedElements = classElement.getEnclosedElements();

                for(Element enclosedElement : enclosedElements) {
                    List<? extends AnnotationMirror> annotationMirrors = enclosedElement.getAnnotationMirrors();

                    for (AnnotationMirror annotationMirror : annotationMirrors) {
                        if(ViewAutoLoad.class.getName().equals(annotationMirror.getAnnotationType().toString())) {
                            if(!tasks.containsKey(classElement))
                                tasks.put(classElement, new LinkedList<>());

                            tasks.get(classElement).add(enclosedElement);
                        }
                    }
                }
            }
        }

        tasks.forEach(this::processViewAutoLoad);

        if(roundEnv.processingOver()) {
            List<MethodSpec> methods = new LinkedList<>();

            getMethodBuffer().forEach((name, builderList) ->
                    builderList.forEach(methodBuilder -> methods.add(methodBuilder.build()))
            );

            TypeSpec viewToolset = TypeSpec.classBuilder("ViewToolset")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethods(methods)
                    .build();

            JavaFile javaFile = JavaFile.builder(Environment.TargetAppPackage + ".generated", viewToolset)
                    .addFileComment(getFileHeader())
                    .build();

            try {
                javaFile.writeTo(filer);
            } catch (IOException ex) {
                throw new IllegalStateException(ex.toString());
            }
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

                builders.forEach(builder -> builder
                        .addStatement("target.$N = target.findViewById($T.$N)",
                                fieldVarName,
                                RId,
                                RMemberName));
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
        TypeMirror typeMirror = clazz.asType();
        ClassName targetClassName = getTargetClassName(clazz);

        return new ArrayList<MethodSpec.Builder>() {{
            add(getCommonFormCheckBuilder(methodName, clazz).
                    addParameter(Object.class, "targetRaw"));
        }};
    }

    private ClassName getTargetClassName(Element clazz) {
        String fullName = clazz.toString();
        return ClassName.get(fullName.substring(0, fullName.indexOf(clazz.getSimpleName().toString())-1), clazz.getSimpleName().toString());
    }
}
