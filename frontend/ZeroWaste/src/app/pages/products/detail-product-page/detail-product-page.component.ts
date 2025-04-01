import { CommonModule, formatDate } from '@angular/common';
import { Component, inject, ViewChild } from '@angular/core';
import { ProductService } from '../../../services/ProductService';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Product } from '../product';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { InputComponent } from '../../../components/form/input/input.component';
import { TextareaComponent } from '../../../components/form/textarea/textarea.component';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ModalComponent } from '../../../components/modal/modal.component';
import { CardComponent, CardContentComponent, CardFooterComponent, CardHeaderComponent } from '../../../components/card/card.component';
import { SelectComponent } from '../../../components/form/select/select.component';

@Component({
  selector: 'app-detail-product-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonComponent,
    InputComponent,
    TextareaComponent,
    ModalComponent,
    SelectComponent,
    CardComponent,
    CardHeaderComponent,
    CardContentComponent,
    CardFooterComponent,
    RouterModule
  ],
  templateUrl: './detail-product-page.component.html',
  styleUrl: './detail-product-page.component.css'
})
export class DetailProductPageComponent {
  fb = inject(FormBuilder);
  productService: ProductService = inject(ProductService);
  route: ActivatedRoute = inject(ActivatedRoute);
  private router: Router = inject(Router);
  product: Product | undefined;
  public productId: number = 0;
  @ViewChild(ModalComponent) modal!: ModalComponent;

  //Amostragem de informações
  public productForm = this.fb.group({
    name: [''],
    description: [''],
    brand: [''],
    category: [''],
    unitPrice: [''],
    promotionPrice: [''],
    stock: [''],
    expiresAt: [''],
    status: [''],
  });

  //Mudança de status
  public statusForm = this.fb.group({
    status: ['', Validators.required]
  });

  public ngOnInit(): void {
    this.productForm.disable();

    this.productId = Number(this.route.snapshot.params['id'])
    this.productService.getProductById(this.productId).then((productResponse => {
      this.product = productResponse;

      var promoPrice;
      if (this.product.promotionPrice == null) 
        promoPrice = "Sem preço promocional"
      else
        promoPrice = 'R$' + this.product.promotionPrice;

      var statusFinal = "Disponível"
      if (this.product.status == "DONATED")
        statusFinal = "Doado"
      else if (this.product.status == "DISCARDED")
        statusFinal = "Descartado"
      
      this.productForm.setValue({
        name: this.product.name,
        description: this.product.description,
        brand: this.product.brand,
        category: this.product.category,
        unitPrice: 'R$' + this.product.unitPrice,
        promotionPrice: promoPrice,
        stock: String(this.product.stock),
        expiresAt: formatDate(this.product.expiresAt, 'dd/MM/yyyy', 'en-US'),
        status: statusFinal
      });
    }))
  }

  public async onChangeStatusConfirmation(): Promise<void> {
    try {
      if(this.statusForm.get('status')?.value == "DONATED")
        await this.productService.setDonatedStatusProduct(this.productId);
  
      else if(this.statusForm.get('status')?.value == "DISCARDED")
        await this.productService.setDiscardedStatusProduct(this.productId);
      
      this.modal.closeModal();
      alert("Disponibilidade modificada!");
      window.location.reload();
    }
    catch(error) {
      console.error('Erro ao modificar disponilbilidade: ', error);
      alert('Erro ao modificar disponilbilidade');
    }
  }
}
