package com.kenvix.utils.preprocessor;

import com.kenvix.utils.annotation.form.FormNotEmpty;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.*;

public class FormPreprocessor extends AbstractProcessor {

    private Types typeUtil;
    private Elements elementUtil;
    private Filer filer;
    private Messager messager;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new LinkedHashSet<String>() {{
            add(FormNotEmpty.class.getCanonicalName());
        }};
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtil = processingEnv.getTypeUtils();
        elementUtil = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        throw new IllegalArgumentException("fuck");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(FormNotEmpty.class).forEach(this::processFormNotEmpty);
        return true;
    }

    private void processFormNotEmpty(Element annotatedElement) {
        if(annotatedElement.getKind() == ElementKind.FIELD) {
            throw new IllegalArgumentException("fuck");
        }
    }
}
