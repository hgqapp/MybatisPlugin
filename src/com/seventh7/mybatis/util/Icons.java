package com.seventh7.mybatis.util;

import com.intellij.icons.AllIcons.Gutter;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.PlatformIcons;

import javax.swing.*;

/**
 * @author yanglin
 */
public interface Icons {

    Icon MYBATIS_LOGO = IconLoader.getIcon("/images/logo_icon.png");

    Icon PARAM_COMPLETION_ICON = PlatformIcons.PARAMETER_ICON;

    Icon MAPPER_LINE_MARKER_ICON = Gutter.ImplementedMethod;

    Icon STATEMENT_LINE_MARKER_ICON = Gutter.ImplementingMethod;

    Icon SPRING_INJECTION_ICON = IconLoader.getIcon("/images/injection.png");
}
