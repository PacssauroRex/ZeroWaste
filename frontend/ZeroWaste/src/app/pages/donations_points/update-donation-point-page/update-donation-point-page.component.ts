import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { API_URL } from '../../../utils/constants';
import { InputComponent } from '../../../components/form/input/input.component';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { TextareaComponent } from '../../../components/form/textarea/textarea.component';

@Component({
  selector: 'app-update-donation-point-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    InputComponent,
    TextareaComponent,
    ButtonComponent,
  ],
  templateUrl: './update-donation-point-page.component.html',
  styleUrl: './update-donation-point-page.component.css'
})

export class UpdateDonationPointPageComponent implements OnInit {

  private fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  private router = inject(Router);
  public route = inject(ActivatedRoute);

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
      const response = await this.updateDonationPoint();

      if (!response.ok) {
        switch (response.status) {
          case 401:
            alert('Você não tem permissão para atualizar pontos de doação');
            break;
          case 404:
            alert('Ponto de doação não encontrado');
            break;
          default:
            alert('Erro ao atualizar ponto de doação');
            break;
        }

        return;
      }

      alert('Ponto de doação atualizado com sucesso');

      this.router.navigate(['/donation-points/']);
    } catch (error) {
      console.error('Erro ao atualizar o ponto de doação', error);
      alert('Erro ao atualizar produto');
    }
  }

  public async ngOnInit(): Promise<void> {
    this.donationPointForm.disable();

    try {
      const response = await this.getDonationPoint();

      if (!response.ok) {
        switch (response.status) {
          case 401:
            alert('Você não tem permissão para atualizar pontos de doação');
            break;
          case 404:
            alert('Ponto de doação não encontrado');
            break;
          default:
            alert('Erro ao atualizar ponto de doação');
            break;
        }

        return;
      }


      const { donation_point } = await response.json();

      donation_point.openingTime = donation_point.openingTime.slice(0, 5);
      donation_point.closingTime = donation_point.closingTime.slice(0, 5);

      this.donationPointForm.setValue({
        name: donation_point.name,
        email: donation_point.email,
        street: donation_point.street,
        number: donation_point.number,
        city: donation_point.city,
        openingTime: donation_point.openingTime,
        closingTime: donation_point.closingTime,
      });

    } catch (error) {
      console.error('Erro ao buscar o ponto de doação', error);
      alert('Erro ao buscar o ponto de doação');
    } finally {
      this.donationPointForm.enable();
    }
  }

  public async getDonationPoint() {
    const donationPointId = this.route.snapshot.paramMap.get('id')!;

    return await fetch(API_URL + "/donation-points/" + donationPointId, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
    });
  }

  public async updateDonationPoint() {
    const donationPointId = this.route.snapshot.paramMap.get('id')!;

    return await fetch(API_URL + "/donation-points/" + donationPointId, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(this.donationPointForm.value),
    });

  }
}
