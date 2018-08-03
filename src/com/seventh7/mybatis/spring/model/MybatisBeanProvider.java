package com.seventh7.mybatis.spring.model;

import com.intellij.openapi.module.Module;
import com.intellij.spring.model.CommonSpringBean;
import com.intellij.spring.model.SpringImplicitBeansProviderBase;
import com.intellij.util.containers.ContainerUtilRt;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.MapperUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author yanglin
 */
public class MybatisBeanProvider extends SpringImplicitBeansProviderBase {

    @Override
    protected Collection<CommonSpringBean> getImplicitBeans(@NotNull Module module) {
        Collection<Mapper> mappers = MapperUtils.findMappers(module.getProject());
        HashSet<CommonSpringBean> beans = ContainerUtilRt.newHashSet();
        Iterator var4 = mappers.iterator();
        while (var4.hasNext()) {
            Mapper mapper = (Mapper) var4.next();
            String className = mapper.getNamespace().getStringValue();
            if (className != null) {
                this.addImplicitLibraryBean(beans, module, className, beanNameOfMapper(className));
            }
        }
        return beans;
    }


    private static String beanNameOfMapper(String className) {
        int index = className.lastIndexOf(".");
        return index == -1 ? className : className.substring(0, index);
    }


    @NotNull
    @Override
    public String getProviderName() {
        return "Mybatis Mapper Bean Provider";
    }

}
