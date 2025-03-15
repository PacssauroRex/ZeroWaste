import { Injectable } from "@angular/core";
import { AbstractControl } from "@angular/forms";

export const validationDictionary = {
  ['required']: 'Esse campo é obrigatório',
  ['minlength']: 'Esse campo deve ter no mínimo {requiredLength} caracteres',
  ['maxlength']: 'Esse campo deve ter no máximo {requiredLength} caracteres',
  ['min']: 'Esse campo deve ser no mínimo {min}',
  ['max']: 'Esse campo deve ser no máximo {max}',
  ['pattern']: 'Esse campo está em um formato inválido',
  ['email']: 'Esse campo deve ser um email válido',
  ['futureDate']: 'Essa data deve ser no futuro',
} as const;

export type ValidationDictionary = typeof validationDictionary;

@Injectable({
  providedIn: 'root',
})
export class ValidationErrorMessage {
  public getValidationErrorMessage(control: AbstractControl): string | null {
    switch (true) {
      case control.hasError('required'): {
        return validationDictionary.required;
      }

      case control.hasError('minlength'): {
        return validationDictionary.minlength
          .replace('{requiredLength}', control.getError('minlength')?.requiredLength);
      }

      case control.hasError('maxlength'): {
        return validationDictionary.maxlength
          .replace('{requiredLength}', control.getError('maxlength')?.requiredLength);
      }

      case control.hasError('min'): {
        return validationDictionary.min
          .replace('{min}', control.getError('min')?.min);
      }
      case control.hasError('max'): {
        return validationDictionary.max
          .replace('{max}', control.getError('max')?.max);
      }

      case control.hasError('pattern'): {
        return validationDictionary.pattern;
      }

      case control.hasError('email'): {
        return validationDictionary.email;
      }

      case control.hasError('futureDate'): {
        return validationDictionary.futureDate;
      }

      default: return null;
    }
  }
}
