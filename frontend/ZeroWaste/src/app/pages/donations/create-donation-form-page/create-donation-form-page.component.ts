import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { InputComponent } from "../../../components/form/input/input.component";
import { ButtonComponent } from "../../../components/form/button/button.component";
import { API_URL } from '../../../utils/constants';
import { futureDateValidator } from '../../../utils/validators/future-date';
import { MultiSelectComponent } from '../../../components/form/multi-select/multi-select.component';

@Component({
  selector: 'app-create-donation-form-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    InputComponent,
    ButtonComponent,
    MultiSelectComponent,
  ],
  templateUrl: './create-donation-form-page.component.html',
  styleUrl: './create-donation-form-page.component.css'
})
export class CreateDonationFormPageComponent {

  private fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  private router = inject(Router);
  public route = inject(ActivatedRoute);

  public donationForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3) , Validators.maxLength(100)]],
    date: ['', [Validators.pattern(/^\d{4}-\d{2}-\d{2}$/), futureDateValidator()]],
    productsId: [<any>[], [Validators.required]],
  });

  public allProducts: any[] = [];
  public alreadyInListProducts: any[] = [];
  public selectedVariants: { [key: number]: any[] } = {};
  public productsId: number[] = [];

  public async ngOnInit(): Promise<void> {

    try {
      await this.loadProductData();
    }

    catch (error) {
      alert("Falha no carregamento: " + error);
    }
  }

  private async getAllProducts() {
    return await fetch(API_URL + "/products", {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
    });
  }

  private async loadProductData(): Promise<void> {
    const response = await this.getAllProducts();
    if (!response.ok) {
      this.handleError(response.status, 'produtos');
      return;
    }

    const { products } = await response.json();
    this.allProducts = products;
  }

  private handleError(status: number, type: string): void {
    let message = `Erro ao buscar ${type}`;
    switch (status) {
      case 401:
        message = `Você não tem permissão para buscar ${type}`;
        break;
      case 404:
        message = `${type.charAt(0).toUpperCase() + type.slice(1)} não encontrado`;
        break;
      case 400:
        message = `Erro ao buscar ${type}: Dados inválidos`;
        break;
      case 403:
        message = `Você não tem permissão para buscar ${type}`;
        break;
    }
    alert(message);
  }

  public onSelectionChange(selected: any[], product: any) {
    this.selectedVariants[product.id] = selected;

    this.productsId = Object.values(this.selectedVariants)
      .flatMap(items => items.map(item => item.id));

    this.donationForm.patchValue({
      productsId: this.productsId
    });
  }

  public getErrorMessage(controlName: string): string | null {
    const validationErrorMessage = this.validationErrorMessage.getValidationErrorMessage(this.donationForm.get(controlName)!);

    return validationErrorMessage;
  }

  public async onSubmit(event: SubmitEvent) {
    event.preventDefault();

    Object.values(this.donationForm.controls).forEach(control => {
      control.markAsTouched();
    });

    if (this.donationForm.invalid) {
      return;
    }

    try {
      await this.saveDonationPoint(this.donationForm.value)
      alert('Doação salva com sucesso');
      this.router.navigate(['/donations']);
    } catch (error) {
      console.error('Erro ao salvar doação', error);
      alert('Erro ao salvar doação');
    }
  }

  public async saveDonationPoint(data: typeof this.donationForm.value) {
    const response = await fetch(API_URL + "/donations", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
    const errorData = await response.json();
    console.error('Erro ao salvar doação:', errorData);
    throw new Error(errorData.message || 'Erro ao salvar doação');
  }

    return response.json();
  }
}

