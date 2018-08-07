package com.seventh7.mybatis.alias;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import com.seventh7.mybatis.annotation.Annotation;
import com.seventh7.mybatis.util.JavaUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanglin
 */
public class AnnotationAliasResolver extends AliasResolver {

    public AnnotationAliasResolver(Project project) {
        super(project);
    }

    public static AnnotationAliasResolver getInstance(@NotNull Project project) {
        return project.getComponent(AnnotationAliasResolver.class);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Set<AliasDesc> getClassAliasDescriptions(@Nullable PsiElement element) {
        Optional<PsiClass> clazz = Annotation.ALIAS.toPsiClass(project);
        if (clazz.isPresent()) {
            Collection<PsiClass> res = AnnotatedElementsSearch.searchPsiClasses(clazz.get(), GlobalSearchScope.allScope(project)).findAll();
            return res.stream().map(v -> {
                Optional<String> txt = JavaUtils.getAnnotationValueText(v, Annotation.ALIAS);
                if (!txt.isPresent()) return null;
                AliasDesc ad = new AliasDesc();
                ad.setAlias(txt.get());
                ad.setClazz(v);
                return ad;
            }).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

}
