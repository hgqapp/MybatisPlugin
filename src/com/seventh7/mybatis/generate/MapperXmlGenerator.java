package com.seventh7.mybatis.generate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.codeInsight.hint.HintUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.awt.RelativePoint;
import com.seventh7.mybatis.service.EditorService;
import com.seventh7.mybatis.template.MybatisFileTemplateDescriptorFactory;
import com.seventh7.mybatis.ui.ClickableListener;
import com.seventh7.mybatis.ui.ListSelectionListener;
import com.seventh7.mybatis.ui.UiComponentFacade;
import com.seventh7.mybatis.util.CollectionUtils;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MapperUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanglin
 */
public final class MapperXmlGenerator {

    private static final MapperXmlGenerator instance = new MapperXmlGenerator();

    private MapperXmlGenerator() {
    }

    public static MapperXmlGenerator getInstance() {
        return instance;
    }

    public void generate(@NotNull PsiClass clazz) {
        Project project = clazz.getProject();
        Collection<PsiDirectory> directories = MapperUtils.findMapperDirectories(project);
        if (CollectionUtils.isEmpty(directories)) {
            handleChooseNewFolder(project, clazz);
        } else {
            handleMutilDirectories(project, clazz, directories);
        }
    }

    private void handleMutilDirectories(Project project,
                                        final PsiClass clazz,
                                        Collection<PsiDirectory> directories) {
        final Map<String, PsiDirectory> pathMap = getPathMap(directories);
        final ArrayList<String> keys = Lists.newArrayList(pathMap.keySet());
        ListSelectionListener popupListener = new ListSelectionListener() {
            @Override
            public void selected(int index) {
                processGenerate(clazz, pathMap.get(keys.get(index)));
            }

            @Override
            public boolean isWriteAction() {
                return true;
            }
        };
        UiComponentFacade uiComponentFacade = UiComponentFacade.getInstance(project);
        uiComponentFacade.showListPopupWithSingleClickable("Choose folder",
                popupListener,
                "Choose another folder",
                getChooseFolderListener(clazz),
                getPathTextForShown(project, keys, pathMap));
    }

    private ClickableListener getChooseFolderListener(final PsiClass clazz) {
        final Project project = clazz.getProject();
        return new ClickableListener() {
            @Override
            public void clicked() {
                handleChooseNewFolder(project, clazz);
            }
        };
    }

    private void handleChooseNewFolder(Project project, PsiClass clazz) {
        UiComponentFacade uiComponentFacade = UiComponentFacade.getInstance(project);
        VirtualFile baseDir = project.getBaseDir();
        VirtualFile vf = uiComponentFacade.showSingleFolderSelectionDialog("Select target folder",
                baseDir,
                baseDir);
        if (null != vf) {
            processGenerate(clazz, PsiManager.getInstance(project).findDirectory(vf));
        }
    }

    private String[] getPathTextForShown(Project project, List<String> paths, final Map<String, PsiDirectory> pathMap) {
        final String projectBasePath = project.getBasePath();
        List<String> result = paths.stream().sorted().map(v -> {
            String relativePath = FileUtil.getRelativePath(projectBasePath, v, File.separatorChar);
            Module module = ModuleUtil.findModuleForPsiElement(pathMap.get(v));
            return null == module ? relativePath : ("[" + module.getName() + "] " + relativePath);
        }).collect(Collectors.toList());
        return result.toArray(new String[]{});
    }

    private Map<String, PsiDirectory> getPathMap(Collection<PsiDirectory> directories) {
        Map<String, PsiDirectory> result = Maps.newHashMap();
        for (PsiDirectory directory : directories) {
            String presentableUrl = directory.getVirtualFile().getPresentableUrl();
            result.put(presentableUrl, directory);
        }
        return result;
    }

    private void processGenerate(PsiClass clazz, PsiDirectory directory) {
        if (null == directory) {
            return;
        }
        if (!directory.isWritable()) {
            showError("Target directory is not writable");
            return;
        }
        try {
            Properties properties = new Properties();
            properties.setProperty("NAMESPACE", clazz.getQualifiedName());
            PsiElement psiFile = MapperUtils.createMapperFromFileTemplate(
                    MybatisFileTemplateDescriptorFactory.MYBATIS_MAPPER_XML_TEMPLATE,
                    clazz.getName(), directory, properties);
            EditorService.getInstance(clazz.getProject()).scrollTo(psiFile, 0);
            generateStatement(clazz);
        } catch (Exception e) {
            showError("Failed: " + e.getCause());
        }
    }

    private static void showError(String msg) {
        final HyperlinkLabel link = new HyperlinkLabel("");
        HintManager.getInstance().showHint(HintUtil.createErrorLabel(msg),
                RelativePoint.getSouthWestOf(link),
                HintManager.HIDE_BY_ANY_KEY | HintManager.HIDE_BY_TEXT_CHANGE,
                -1);
    }

    private void generateStatement(final PsiClass clazz) {
        final List<PsiMethod> methods = JavaUtils.findAllMethodsWithoutRootParent(clazz);
        if (methods.isEmpty()) {
            return;
        }

        UiComponentFacade.getInstance(clazz.getProject()).showListPopup(
                "Select statements to generate",
                new ListSelectionListener() {
                    @Override
                    public void selected(int index) {
                        // Do nothing
                    }

                    @Override
                    public void selected(int[] indexes) {
                        if (indexes == null) {
                            return;
                        }
                        for (int index : indexes) {
                            StatementGenerator.applyGenerate(methods.get(index), false);
                        }
                    }
                },
                methods,
                PsiMethod::getName);

    }

}
