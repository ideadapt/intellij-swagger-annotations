package net.ideadapt.intellij;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * based on https://jetbrains.org/intellij/sdk/docs/tutorials/custom_language_support/folding_builder.html
 */
public class SimpleFoldingBuilder extends FoldingBuilderEx implements DumbAware {

  @NotNull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
    FoldingGroup group = FoldingGroup.newGroup("mygrp");
    List<FoldingDescriptor> descriptors = new ArrayList<>();
    // Get a collection of the literal expressions in the document below root
    Collection<PsiLiteralExpression> literalExpressions = PsiTreeUtil.findChildrenOfType(root, PsiLiteralExpression.class);
    for (final PsiLiteralExpression literalExpression : literalExpressions) {
      String value = literalExpression.getValue() instanceof String ? (String) literalExpression.getValue() : null;
      if (value != null && value.startsWith("http://")) {

        descriptors.add(new FoldingDescriptor(literalExpression.getNode(),
                new TextRange(
                        literalExpression.getTextRange().getStartOffset() + 1,
                        literalExpression.getTextRange().getEndOffset() - 1),
                group));
      }
    }
    return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
  }

  /**
   * Gets the Simple Language 'value' string corresponding to the 'key'
   *
   * @param node Node corresponding to PsiLiteralExpression containing a string in the format
   *             SIMPLE_PREFIX_STR + SIMPLE_SEPARATOR_STR + Key, where Key is
   *             defined by the Simple language file.
   */
  @Nullable
  @Override
  public String getPlaceholderText(@NotNull ASTNode node) {
    String retTxt = "...";
    if (node.getPsi() instanceof PsiLiteralExpression) {
      PsiLiteralExpression nodeElement = (PsiLiteralExpression) node.getPsi();
      return ((String) nodeElement.getValue()).substring("http://".length());
    }
    return retTxt;
  }

  @Override
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    return true;
  }

}
