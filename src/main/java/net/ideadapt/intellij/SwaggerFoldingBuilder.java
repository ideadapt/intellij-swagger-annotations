package net.ideadapt.intellij;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SwaggerFoldingBuilder extends FoldingBuilderEx implements DumbAware {

  public static final String API_OPERATION = "ApiOperation";
  public static final String API_IMPLICIT_PARAMS = "ApiImplicitParams";

  @NotNull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
    var annotations = PsiTreeUtil.findChildrenOfAnyType(root, PsiAnnotation.class);
    var descriptors = new ArrayList<FoldingDescriptor>();

    for (PsiAnnotation annotation : annotations) {

      if (API_OPERATION.equals(annotation.getQualifiedName())) {
        String value = AnnotationUtil.getStringAttributeValue(annotation, "value");

        if (value != null) {
          descriptors.add(new FoldingDescriptor(
                  annotation.getNode(),
                  annotation.getTextRange()));
        }
      }

      if (API_IMPLICIT_PARAMS.equals(annotation.getQualifiedName())) {
        descriptors.add(new FoldingDescriptor(
                annotation.getNode(),
                annotation.getTextRange()));
      }
    }

    return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
  }

  @Nullable
  @Override
  public String getPlaceholderText(@NotNull ASTNode node) {
    var annotation = (PsiAnnotation) node.getPsi();

    if (API_OPERATION.equals(annotation.getQualifiedName())) {
      String value = AnnotationUtil.getStringAttributeValue(annotation, "value");
      return "@Operation " + value.substring(0, Math.min(60, value.length()) - 1) + "...";
    }
    if (API_IMPLICIT_PARAMS.equals(annotation.getQualifiedName())) {
      return "@Param ...";
    }
    return null;
  }

  @Override
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    return true;
  }
}
