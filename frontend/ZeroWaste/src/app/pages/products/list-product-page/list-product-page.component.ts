import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild, inject, signal } from '@angular/core';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Product } from '../product';
import { ProductService } from '../../../services/ProductService';
import { ModalComponent } from "../../../components/modal/modal.component";
import { CardComponent, CardHeaderComponent, CardContentComponent, CardFooterComponent } from "../../../components/card/card.component";
import { InputComponent } from "../../../components/form/input/input.component";
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';

@Component({
  selector: 'app-list-product-page',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    ButtonComponent,
    RouterModule,
    ModalComponent,
    CardComponent,
    CardHeaderComponent,
    CardContentComponent,
    CardFooterComponent,
    InputComponent,
  ],
  templateUrl: './list-product-page.component.html',
  styleUrl: './list-product-page.component.css'
})
export class ListProductPageComponent implements OnInit {
  public productsService: ProductService = inject(ProductService);
  public fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  private router = inject(Router);
  public route = inject(ActivatedRoute);
  public products = signal<Product[]>([]);
  public filters = this.fb.group({
    daysToExpire: [null, Validators.min(0)],
  });

  @ViewChild(ModalComponent) modal!: ModalComponent;

  public async onSubmitFilterForm(event: SubmitEvent) {
    event.preventDefault();

    if (this.filters.invalid) {
      return;
    }

    try {
      const daysToExpire = this.filters.get('daysToExpire')?.value;

      const filteredProducts = await this.productsService.getAllProducts(daysToExpire);
      this.products.set(filteredProducts);
    } catch (error) {
      console.error('Erro ao buscar produtos', error);
      alert('Erro ao buscar produtos');
    }
  }

  public async ngOnInit(): Promise<void> {
    const products = await this.productsService.getAllProducts();
    this.products.set(products);
  }

  public async onDeleteProductConfirmation(productId: number): Promise<void> {
    if (this.route.snapshot.data['role'] !== 'ADMIN') {
      alert('Você não tem permissão para deletar produtos');

      this.modal.closeModal();

      return;
    }

    try {
      await this.productsService.deleteProduct(productId);

      alert('Produto deletado com sucesso!');

      this.products.set(this.products().filter((product) => product.id !== productId));
    } catch (error) {
      if ((error as Error).cause) {
        const { message } = (error as Error).cause as any;

        alert('Erro ao deletar produto: ' + message);
      }
    }
  }

  public getErrorMessage(controlName: string): string | null {
    const validationErrorMessage = this.validationErrorMessage.getValidationErrorMessage(this.filters.get(controlName)!);

    return validationErrorMessage;
  }
}
