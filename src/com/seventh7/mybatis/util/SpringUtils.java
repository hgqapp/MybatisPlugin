package com.seventh7.mybatis.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.spring.CommonSpringModel;
import com.intellij.spring.SpringModelVisitorUtils;
import com.intellij.spring.model.SpringBeanPointer;
import com.intellij.spring.model.utils.SpringPropertyUtils;
import com.intellij.spring.model.xml.beans.SpringPropertyDefinition;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

/**
 * @author yanglin
 */
public final class SpringUtils {

    private SpringUtils() {
        throw new UnsupportedOperationException();
    }

    public static void processSpringConfig(@NotNull Project project,
                                           @NotNull CommonSpringModel model,
                                           @NotNull String clazzName,
                                           @NotNull String prop,
                                           @NotNull Processor<SpringPropertyDefinition> processor) {
        Optional<PsiClass> clazzOpt = JavaUtils.findClazz(project, clazzName);
        if (!clazzOpt.isPresent()) {
            return;
        }
        Collection<SpringBeanPointer> domBeans = SpringModelVisitorUtils.getAllDomBeans(model);

        PsiClass clazz = clazzOpt.get();
        for (SpringBeanPointer pointer : domBeans) {
            PsiClass beanClass = pointer.getBeanClass();
            if (beanClass == null || !beanClass.equals(clazz)) {
                continue;
            }
            SpringPropertyDefinition def = SpringPropertyUtils.findPropertyByName(pointer.getSpringBean(), prop);
            if (def != null && !processor.process(def)) {
                break;
            }
        }
    }
}
