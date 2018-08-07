package com.seventh7.mybatis.alias;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanglin
 */
public class AliasClassReference extends PsiReferenceBase<XmlAttributeValue> {
    public AliasClassReference(@NotNull XmlAttributeValue element) {
        super(element, true);
    }

    @Nullable
    @Override
    public PsiClass resolve() {
        XmlAttributeValue attributeValue = getElement();
        return AliasFacade.getInstance(attributeValue.getProject()).findPsiClass(attributeValue, attributeValue.getValue()).orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        AliasFacade aliasFacade = AliasFacade.getInstance(getElement().getProject());
        List<String> result = aliasFacade.getAliasDescs(getElement()).stream().map(AliasDesc::getAlias).collect(Collectors.toList());
        return result.toArray(new String[]{});
    }

}
