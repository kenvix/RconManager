package com.kenvix.utils.preprocessor;

import com.kenvix.utils.Environment;
import com.kenvix.utils.StringTools;
import com.kenvix.utils.annotation.ErrorPrompt;
import com.kenvix.utils.annotation.form.*;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.kenvix.utils.PreprocessorName.getFormEmptyCheckerMethodName;

public class FormPreprocessor extends BasePreprocessor {

    private void processFormNotEmpty(Element targetClass, List<Element> annotatedElements) {
        TypeMirror targetClassType = targetClass.asType();
        String targetClassFullName = getFormEmptyCheckerMethodName(targetClass.toString());

        annotatedElements.forEach(annotatedElement -> {
            if(annotatedElement.getKind() == ElementKind.FIELD) {
                TypeMirror fieldType = annotatedElement.asType();

                FormNotEmpty annotation = annotatedElement.getAnnotation(FormNotEmpty.class);

                List<MethodSpec.Builder> builders = getMethodBuilder(getFormEmptyCheckerMethodName(targetClassFullName), targetClass);
                String RMemberName = StringTools.convertUppercaseLetterToUnderlinedLowercaseLetter(annotatedElement.getSimpleName().toString());
                Name fieldVarName = annotatedElement.getSimpleName();
                ClassName RId =  ClassName.get(Environment.TargetAppPackage, "R", "id");

                builders.forEach(builder -> builder
                        .addStatement("$T $N = target.findViewById($T.$N)",
                                fieldType,
                                fieldVarName,
                                RId,
                                RMemberName)
                        .addStatement("assert $N != null", fieldVarName)
                        .beginControlFlow("if($N.getText().toString().isEmpty())", fieldVarName)
                        .addStatement("$N.setError($L) ", fieldVarName, "promptText")
                        .addStatement("return false")
                        .endControlFlow());

                FormNumberLess formNumberLess = annotatedElement.getAnnotation(FormNumberLess.class);
                FormNumberMore formNumberMore = annotatedElement.getAnnotation(FormNumberMore.class);
                FormNumberLessOrEqual formNumberLessOrEqual = annotatedElement.getAnnotation(FormNumberLessOrEqual.class);
                FormNumberMoreOrEqual formNumberMoreOrEqual = annotatedElement.getAnnotation(FormNumberMoreOrEqual.class);

                if(formNumberLess != null || formNumberLessOrEqual != null || formNumberMore != null || formNumberMoreOrEqual != null) {
                    builders.forEach(builder -> builder
                            .beginControlFlow("try")
                            .addStatement("int number = Integer.parseInt($N.getText().toString())", fieldVarName));

                        String lessConditionStatement = null;

                        if(formNumberLess != null) {
                            lessConditionStatement = "number >= " + formNumberLess.value();
                        } else if(formNumberLessOrEqual != null) {
                            lessConditionStatement = "number > " + formNumberLessOrEqual.value();
                        }

                        String moreConditionStatement = null;

                        if(formNumberMore != null) {
                        moreConditionStatement = "number <= " + formNumberMore.value();
                    } else if(formNumberMoreOrEqual != null) {
                        moreConditionStatement = "number < " + formNumberMoreOrEqual.value();
                    }

                    String conditionStatement;

                    if(moreConditionStatement != null && lessConditionStatement != null)
                        conditionStatement = lessConditionStatement + " || " + moreConditionStatement;
                    else
                        conditionStatement = lessConditionStatement == null ? moreConditionStatement : lessConditionStatement;

                    builders.forEach(builder -> builder
                            .beginControlFlow("if ($L)", conditionStatement)
                            .addStatement("$N.setError($S) ", fieldVarName, getErrorPromptStatement(annotatedElement, "You can't let " + conditionStatement.replace("||", "or")))
                            .addStatement("return false")
                            .endControlFlow()
                            .endControlFlow()
                            .beginControlFlow("catch (Exception ex)")
                            .addStatement("$N.setError($L) ", fieldVarName, "ex.getMessage()")
                            .addStatement("return false")
                            .endControlFlow());
                }
            }
        });
    }

    private MethodSpec.Builder getCommonFormCheckBuilder(String methodName, Element clazz) {
        ClassName targetClassName = getTargetClassName(clazz);

        return MethodSpec
                .methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(boolean.class)
                .addParameter(String.class, "promptText")
                .addStatement("assert targetRaw instanceof $T", targetClassName)
                .addStatement("$T target = ($T) targetRaw", targetClassName, targetClassName);
    }

    @Override
    protected List<MethodSpec.Builder> createMethodBuilder(String methodName, Element clazz) {
        //TypeMirror typeMirror = clazz.asType();
        //ClassName targetClassName = getTargetClassName(clazz);

        return new ArrayList<MethodSpec.Builder>() {{
            add(getCommonFormCheckBuilder(methodName, clazz)
                    .addParameter(Object.class, "targetRaw"));
        }};
    }

    @Override
    protected boolean onProcess(Map<Element, List<Element>> filteredAnnotations, Set<? extends TypeElement> originalAnnotations, RoundEnvironment roundEnv) {
        filteredAnnotations.forEach(this::processFormNotEmpty);
        return true;
    }

    @Override
    protected Class[] getSupportedAnnotations() {
        return new Class[] {FormNotEmpty.class};
    }

    @Override
    protected boolean onProcessingOver(Map<Element, List<Element>> filteredAnnotations, Set<? extends TypeElement> originalAnnotations, RoundEnvironment roundEnv) {
        List<MethodSpec> methods = new LinkedList<>();

        getMethodBuffer().forEach((name, builderList) ->
                builderList.forEach(methodBuilder -> methods.add(methodBuilder.addStatement("return true").build()))
        );

        TypeSpec formChecker = TypeSpec.classBuilder("FormChecker")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethods(methods)
                .build();

        JavaFile javaFile = getOutputJavaFileBuilder(formChecker).build();

        try {
            saveOutputJavaFile(javaFile);
        } catch (IOException ex) {
            throw new IllegalStateException(ex.toString());
        }

        return true;
    }
}
