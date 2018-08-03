package com.seventh7.mybatis.action;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.CodeCompletionHandlerBase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.sql.psi.SqlFile;
import com.seventh7.mybatis.util.DomUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class MybatisTypedHandler extends TypedHandlerDelegate {

  @Override
  public Result checkAutoPopup(char charTyped, final Project project, final Editor editor, PsiFile file) {
    if (charTyped == '.' && DomUtils.isMybatisFile(file)) {
      autoPopupParameter(project, editor);
      return Result.STOP;
    }
    return super.checkAutoPopup(charTyped, project, editor, file);
  }

  @Override
  public Result charTyped(char c, final Project project, @NotNull final Editor editor, @NotNull PsiFile file) {
    if (c != '{') {
      return Result.CONTINUE;
    }
    int index = editor.getCaretModel().getOffset() - 2;
    if (index < 0) {
      return Result.CONTINUE;
    }
    char beginningChar = editor.getDocument().getText().charAt(index);
    if (beginningChar != '#' && beginningChar != '$') {
      return Result.CONTINUE;
    }

    PsiFile topLevelFile = InjectedLanguageManager.getInstance(project).getTopLevelFile(file);
    boolean parameterCase = file instanceof SqlFile &&
                            DomUtils.isMybatisFile(topLevelFile);
    if (parameterCase) {
      autoPopupParameter(project, editor);
      return Result.STOP;
    }
    return super.charTyped(c, project, editor, file);
  }

  private static void autoPopupParameter(final Project project, final Editor editor) {
    AutoPopupController.runTransactionWithEverythingCommitted(project, () -> new CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(project, editor, 1));
  }

}