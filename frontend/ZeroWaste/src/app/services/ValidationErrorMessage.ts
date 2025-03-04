import { Injectable } from "@angular/core";
import { AbstractControl } from "@angular/forms";

export const validationDictionary = {
  ['required']: 'This field is required',
  ['minlength']: 'This field must have at least {requiredLength} characters',
  ['maxlength']: 'This field must have at most {requiredLength} characters',
  ['min']: 'This field must be greater than or equal to {min}',
  ['pattern']: 'Invalid format',
  ['email']: 'Invalid email',
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

      case control.hasError('pattern'): {
        return validationDictionary.pattern;
      }

      case control.hasError('email'): {
        return validationDictionary.email;
      }

      default: return null;
    }
  }
}
