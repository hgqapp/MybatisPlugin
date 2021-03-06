package com.seventh7.mybatis.alias;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.spring.CommonSpringModel;
import com.intellij.spring.SpringManager;
import com.intellij.spring.model.xml.beans.SpringPropertyDefinition;
import com.intellij.util.Processor;
import com.intellij.util.xml.GenericDomValue;
import com.seventh7.mybatis.util.SpringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yanglin
 */
public class BeanAliasResolver extends PackageAliasResolver {

    private static final String MAPPER_ALIAS_PACKAGE_CLASS = "org.mybatis.spring.SqlSessionFactoryBean";
    private static final String MAPPER_ALIAS_PROPERTY = "typeAliasesPackage";
    private ModuleManager moduleManager;
    private SpringManager springManager;
    private Project project;

    public BeanAliasResolver(Project project) {
        super(project);
        this.moduleManager = ModuleManager.getInstance(project);
        this.springManager = SpringManager.getInstance(project);
        this.project = project;
    }

    @NotNull
    @Override
    public Collection<String> getPackages(@Nullable PsiElement element) {
        Set<String> res = new HashSet<>();
        for (Module module : moduleManager.getModules()) {
            for (CommonSpringModel springModel : springManager.getCombinedModel(module).getRelatedModels()) {
                addPackages(res, springModel);
            }
        }
        return res;
    }

    private void addPackages(final Set<String> res, CommonSpringModel springModel) {
        Processor<SpringPropertyDefinition> processor = def -> {
            GenericDomValue<?> valueElement = def.getValueElement();
            if (valueElement != null) {
                final String value = valueElement.getStringValue();
                if (value != null) {
                    Collections.addAll(res, value.split(",|;"));
                }
            }
            return true;
        };
        SpringUtils.processSpringConfig(project, springModel, MAPPER_ALIAS_PACKAGE_CLASS, MAPPER_ALIAS_PROPERTY, processor);
    }

}
