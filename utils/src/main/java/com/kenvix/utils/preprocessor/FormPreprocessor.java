package com.kenvix.utils.preprocessor;

import com.kenvix.utils.Environment;
import com.kenvix.utils.StringTools;
import com.kenvix.utils.annotation.form.FormNotEmpty;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class FormPreprocessor extends BasePreprocessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new LinkedHashSet<String>() {{
            add(FormNotEmpty.class.getCanonicalName());
        }};
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(FormNotEmpty.class).forEach(this::processFormNotEmpty);

        if(roundEnv.processingOver()) {
            List<MethodSpec> methods = new LinkedList<>();

            getMethodBuffer().forEach((name, builderList) ->
                    builderList.forEach(methodBuilder -> methods.add(methodBuilder.addStatement("return true").build()))
            );

            TypeSpec formChecker = TypeSpec.classBuilder("FormChecker")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethods(methods)
                    .build();

            JavaFile javaFile = JavaFile.builder(Environment.TargetAppPackage, formChecker)
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

    private void processFormNotEmpty(Element annotatedElement) {
        if(annotatedElement.getKind() == ElementKind.FIELD) {
            TypeMirror fieldType = annotatedElement.asType();
            DeclaredType declared = (DeclaredType) fieldType;

            FormNotEmpty annotation = annotatedElement.getAnnotation(FormNotEmpty.class);

            List<MethodSpec.Builder> builders = getMethodBuilder(annotation.value());
            String RMemberName = StringTools.convertUppercaseLetterToUnderlinedLowercaseLetter(annotatedElement.getSimpleName().toString());
            Name fieldVarName = annotatedElement.getSimpleName();
            ClassName RId =  ClassName.get(Environment.TargetAppPackage, "R", "id");

            builders.forEach(builder -> builder
                    .addStatement("$T $N = target.findViewById($T.$N)",
                            fieldType,
                            fieldVarName,
                            RId,
                            RMemberName)
                    .beginControlFlow("if($N.getText().toString().isEmpty())", fieldVarName)
                    .addStatement("$N.setError($L) ", fieldVarName, "promptText")
                    .addStatement("return false")
                    .endControlFlow());
        }
    }

    private MethodSpec.Builder getCommonFormCheckBuilder(String methodName) {
        return MethodSpec
                .methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(boolean.class)
                .addParameter(String.class, "promptText");
    }

    @Override
    protected List<MethodSpec.Builder> createMethodBuilder(String methodName) {
        final boolean generateCodeForViewClass = !methodName.endsWith("Activity");
        final boolean generateCodeForActivityClass = !methodName.endsWith("View");

        ClassName appCompatClass = ClassName.get("android.support.v7.app", "AppCompatActivity");
        ClassName viewClass = ClassName.get("android.view", "View");

        return new ArrayList<MethodSpec.Builder>() {{
            if(generateCodeForActivityClass)
                add(getCommonFormCheckBuilder(methodName).
                        addTypeVariable(TypeVariableName.get("T", appCompatClass)).
                        addParameter(TypeVariableName.get("T"), "target"));

            if(generateCodeForViewClass)
                add(getCommonFormCheckBuilder(methodName).
                        addTypeVariable(TypeVariableName.get("T", viewClass)).
                        addParameter(TypeVariableName.get("T"), "target"));
        }};
    }
}
