import { CommonModule, formatDate } from '@angular/common';
import { Component, forwardRef, inject } from '@angular/core';
import { FormBuilder, NG_VALUE_ACCESSOR, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { InputComponent } from '../../../components/form/input/input.component';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { API_URL } from '../../../utils/constants';
import { MultiSelectComponent } from '../../../components/form/multi-select/multi-select.component';
import { futureDateValidator } from '../../../utils/validators/future-date';

@Component({
  selector: 'app-update-donation-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    InputComponent,
    ButtonComponent,
    MultiSelectComponent
  ],
  templateUrl: './update-donation-page.component.html',
  styleUrl: './update-donation-page.component.css'
})
export class UpdateDonationPageComponent {

  private fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  private router = inject(Router);
  public route = inject(ActivatedRoute);

  public donationForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    date: ['', [Validators.required, Validators.pattern(/^\d{4}-\d{2}-\d{2}$/), futureDateValidator]],
    productsId: [<any>[], [Validators.required]],
  });

  public allProducts: any[] = [];
  public alreadyInListProducts: any[] = [];
  public selectedVariants: { [key: number]: any[] } = {};
  public productsId: number[] = [];

  public isLoading = true;

  public async ngOnInit(): Promise<void> {
    this.donationForm.disable();

    try {
      await this.loadDonationData();
      await this.loadProductData();
    }

    catch (error) {
      alert("Falha no carregamento: " + error);
    }

    finally {
      this.isLoading = false;
      this.donationForm.enable();
      this.donationForm.get('productsId')?.disable();
    }
  }

  private async loadDonationData(): Promise<void> {
    const response = await this.getDonation();
    if (!response.ok) {
      this.handleError(response.status, 'doação');
      return;
    }

    const { donation } = await response.json();
    this.alreadyInListProducts.push(...donation.products);

    this.donationForm.patchValue({
      name: donation.name,
      date: formatDate(donation.date, 'yyyy-MM-dd', 'en-US'),
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
    console.log('Seleção Atualizada:', this.selectedVariants);

    this.productsId = Object.values(this.selectedVariants)
      .flatMap(items => items.map(item => item.id));

    this.donationForm.patchValue({
      productsId: this.productsId
    });
    console.log('Produto recebido no onSelectionChange:', product);
  }

  public getErrorMessage(controlName: string): string | null {
    return this.validationErrorMessage.getValidationErrorMessage(this.donationForm.get(controlName)!);
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
      const response = await this.updateDonation();

      if (!response.ok) {
        this.handleError(response.status, 'doação');
        return;
      }

      alert('Doação atualizada com sucesso');
      this.router.navigate(['/donations']);

    }

    catch (error) {
      console.error('Erro ao atualizar Doação', error);
      alert('Erro ao atualizar Doação');
    }

  }

  private async getDonation() {
    const donationId = this.route.snapshot.paramMap.get('id')!;
    return await fetch(API_URL + "/donations/" + donationId, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
    });
  }

  private async updateDonation() {
    const donationId = this.route.snapshot.paramMap.get('id')!;
    const dateValue = this.donationForm.value.date;
    if (dateValue !== null && dateValue !== undefined) {
      this.donationForm.value.date = formatDate(dateValue, 'yyyy-MM-dd', 'en-US');
    } else {
      console.error('Date value is null or undefined');
    }
    this.donationForm.value.productsId = this.productsId;

    return await fetch(API_URL + "/donations/" + donationId, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(this.donationForm.value),
    });
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
}

