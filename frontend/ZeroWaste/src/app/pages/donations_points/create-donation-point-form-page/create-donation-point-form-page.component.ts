import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { InputComponent } from "../../../components/form/input/input.component";
import { ButtonComponent } from "../../../components/form/button/button.component";
import { API_URL } from '../../../utils/constants';

@Component({
  selector: 'app-create-donation-point-form-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    InputComponent,
    ButtonComponent,
  ],
  templateUrl: './create-donation-point-form-page.component.html',
  styleUrl: './create-donation-point-form-page.component.css'
})
export class CreateDonationPointFormPageComponent {

  private fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  private router = inject(Router);

  public donationPointForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3) , Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.email]],
    street: ['', [Validators.required, Validators.minLength(3) , Validators.maxLength(100)]],
    number: ['', [Validators.required, Validators.min(0)]],
    city: ['', [Validators.required, Validators.minLength(3) , Validators.maxLength(100)]],
    openingTime: ['', [Validators.required, Validators.pattern(/^([01]\d|2[0-3]):[0-5]\d$/)]],
    closingTime: ['', [Validators.required, Validators.pattern(/^([01]\d|2[0-3]):[0-5]\d$/)]],
  });

  public getErrorMessage(controlName: string): string | null {
    const validationErrorMessage = this.validationErrorMessage.getValidationErrorMessage(this.donationPointForm.get(controlName)!);

    return validationErrorMessage;
  }

  public async onSubmit(event: SubmitEvent) {
    event.preventDefault();

    Object.values(this.donationPointForm.controls).forEach(control => {
      control.markAsTouched();
    });

    if (this.donationPointForm.invalid) {
      return;
    }

    try {
      await this.saveDonationPoint(this.donationPointForm.value)
      alert('Ponto de doação salvo com sucesso');
      this.router.navigate(['/donation-points/']);
    } catch (error) {
      console.error('Erro ao salvar ponto de doação', error);
      alert('Erro ao salvar ponto de doação');
    }
  }

  public async saveDonationPoint(data: typeof this.donationPointForm.value) {
    const response = await fetch(API_URL + "/donation-points/", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(data),
    });

    return response.json();
  }
}
