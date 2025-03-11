import { CommonModule } from '@angular/common';
import { Component, forwardRef, inject } from '@angular/core';
import { FormBuilder, NG_VALUE_ACCESSOR, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { InputComponent } from '../../../components/form/input/input.component';
import { InputWithSymbolComponent } from '../../../components/form/input-with-symbol/input-with-symbol.component';
import { TextareaComponent } from '../../../components/form/textarea/textarea.component';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { API_URL } from '../../../utils/constants';
import { MultiSelectComponent } from '../../../components/form/multi-select/multi-select.component';

@Component({
  selector: 'app-update-promotion-form-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    InputComponent,
    InputWithSymbolComponent,
    ButtonComponent,
    MultiSelectComponent
  ],
  templateUrl: './update-promotion-form-page.component.html',
  styleUrl: './update-promotion-form-page.component.css'
})
export class UpdatePromotionFormPageComponent {

  private fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  private router = inject(Router);
  public route = inject(ActivatedRoute);

  public promotionForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    percentage: ['', [Validators.required, Validators.min(1), Validators.max(100)]],
    startsAt: ['', [Validators.required, Validators.pattern(/^\d{4}-\d{2}-\d{2}$/)]],
    endsAt: ['', [Validators.required, Validators.pattern(/^\d{4}-\d{2}-\d{2}$/)]],
    productIds: [<any>[], [Validators.required]],
  });

  public allProducts: any[] = [];
  public alreadyInListProducts: any[] = [];
  public selectedVariants: { [key: number]: any[] } = {};
  public productIds: number[] = [];

  public isLoading = true; // Página começa carregando

  public async ngOnInit(): Promise<void> {
    this.promotionForm.disable();

    try {
      await this.loadPromotionData();
      await this.loadProductData();
    }

    catch (error) {
      alert("Falha no carregamento: " + error);
    }

    finally {
      this.isLoading = false; // Apenas agora os dados estão prontos
      this.promotionForm.enable();
      this.promotionForm.get('productIds')?.disable();
    }
  }

  private async loadPromotionData(): Promise<void> {
    const response = await this.getPromotion();
    if (!response.ok) {
      this.handleError(response.status, 'promoção');
      return;
    }

    const { promotion } = await response.json();
    this.alreadyInListProducts.push(...promotion.products);

    this.promotionForm.patchValue({
      name: promotion.name,
      percentage: (promotion.percentage * 100).toString(),
      startsAt: promotion.startsAt,
      endsAt: promotion.endsAt,
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
    // console.log('Seleção Atualizada:', this.selectedVariants);

    this.productIds = Object.values(this.selectedVariants)
      .flatMap(items => items.map(item => item.id));

    this.promotionForm.patchValue({
      productIds: this.productIds
    });
  }

  public getErrorMessage(controlName: string): string | null {
    return this.validationErrorMessage.getValidationErrorMessage(this.promotionForm.get(controlName)!);
  }

  private async updatePromotion() {
    const promotionId = this.route.snapshot.paramMap.get('id')!;

    this.promotionForm.value.percentage = (Number(this.promotionForm.value.percentage) / 100).toString();
    this.promotionForm.value.productIds = this.productIds;

    return await fetch(API_URL + "/promotions/" + promotionId, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(this.promotionForm.value),
    });
  }

  public async onSubmit(event: SubmitEvent) {
    event.preventDefault();

    Object.values(this.promotionForm.controls).forEach(control => {
      control.markAsTouched();
    });

    if (this.promotionForm.invalid) {
      return;
    }

    try {
      const response = await this.updatePromotion();

      if (!response.ok) {
        this.handleError(response.status, 'promoção');
        return;
      }

      alert('Promoção atualizada com sucesso');
      this.router.navigate(['/home']);

    }

    catch (error) {
      console.error('Erro ao atualizar promoção', error);
      alert('Erro ao atualizar promoção');
    }
  }

  private async getPromotion() {
    const promotionId = this.route.snapshot.paramMap.get('id')!;
    return await fetch(API_URL + "/promotions/" + promotionId, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
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
