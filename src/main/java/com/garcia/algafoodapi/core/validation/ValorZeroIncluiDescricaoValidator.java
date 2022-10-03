package com.garcia.algafoodapi.core.validation;

import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.Locale;

public class ValorZeroIncluiDescricaoValidator
    implements ConstraintValidator<ValorZeroIncluiDescricao, Object> {

  private String valorField;
  private String descricaoField;
  private String descricaoObrigatoria;

  @Override
  public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
    this.valorField = constraintAnnotation.valorField();
    this.descricaoField = constraintAnnotation.descricaoField();
    this.descricaoObrigatoria = constraintAnnotation.descricaoObrigatoria();
  }

  @Override
  public boolean isValid(
      Object oojetoValidacao, ConstraintValidatorContext constraintValidatorContext) {

    boolean valido = true;
    try {
      BigDecimal valor =
          (BigDecimal)
              BeanUtils.getPropertyDescriptor(oojetoValidacao.getClass(), valorField)
                  .getReadMethod()
                  .invoke(oojetoValidacao);
      String descricao =
          (String)
              BeanUtils.getPropertyDescriptor(oojetoValidacao.getClass(), descricaoField)
                  .getReadMethod()
                  .invoke(oojetoValidacao);

      if (valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null) {
        valido =
            descricao
                .toLowerCase(Locale.ROOT)
                .contains(this.descricaoObrigatoria.toLowerCase(Locale.ROOT));
      }

      return valido;
    } catch (Exception e) {
      throw new ValidationException(e);
    }
  }
}
