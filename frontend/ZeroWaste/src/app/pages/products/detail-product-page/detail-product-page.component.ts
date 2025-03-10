import { CommonModule, formatDate } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ProductService } from '../../../services/ProductService';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Product } from '../product';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { InputComponent } from '../../../components/form/input/input.component';
import { TextareaComponent } from '../../../components/form/textarea/textarea.component';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-detail-product-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonComponent,
    InputComponent,
    TextareaComponent,
    RouterModule
  ],
  templateUrl: './detail-product-page.component.html',
  styleUrl: './detail-product-page.component.css'
})
export class DetailProductPageComponent {
  fb = inject(FormBuilder);
  productService: ProductService = inject(ProductService);
  route: ActivatedRoute = inject(ActivatedRoute);
  product: Product | undefined;

  public productForm = this.fb.group({
    name: [''],
    description: [''],
    brand: [''],
    category: [''],
    unitPrice: [''],
    promotionPrice: [''],
    stock: [''],
    expiresAt: [''],
  });

  public ngOnInit(): void {
    this.productForm.disable();

    const productId = Number(this.route.snapshot.params['id'])
    this.productService.getProductById(productId).then((productResponse => {
      this.product = productResponse;

      var promoPrice;
      if (this.product.promotionPrice == null) 
        promoPrice = "Sem preço promocional"
      else
        promoPrice = 'R$' + this.product.promotionPrice;
      
      this.productForm.setValue({
        name: this.product.name,
        description: this.product.description,
        brand: this.product.brand,
        category: this.product.category,
        unitPrice: 'R$' + this.product.unitPrice,
        promotionPrice: promoPrice,
        stock: String(this.product.stock),
        expiresAt: formatDate(this.product.expiresAt, 'dd/MM/yyyy', 'en-US')
      });
    }))


  }
}
