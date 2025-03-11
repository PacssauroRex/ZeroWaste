import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { RouterModule } from '@angular/router';
import { Product } from '../product';
import { ProductService } from '../../../services/ProductService';
import { ModalComponent } from "../../../components/modal/modal.component";
import { CardComponent, CardHeaderComponent, CardContentComponent, CardFooterComponent } from "../../../components/card/card.component";

@Component({
  selector: 'app-list-product-page',
  imports: [
    CommonModule,
    ButtonComponent,
    RouterModule,
    ModalComponent,
    CardComponent,
    CardHeaderComponent,
    CardContentComponent,
    CardFooterComponent
],
  templateUrl: './list-product-page.component.html',
  styleUrl: './list-product-page.component.css'
})
export class ListProductPageComponent implements OnInit {
  public productsService: ProductService = inject(ProductService);
  public products: Product[] = [];

  public async ngOnInit(): Promise<void> {
    this.products = await this.productsService.getAllProducts();
  }

  public async onDeleteProductConfirmation(productId: number): Promise<void> {
    try {
      await this.productsService.deleteProduct(productId);

      alert('Produto deletado com sucesso!');

      this.products = this.products.filter((product) => product.id !== productId);
    } catch (error) {
      if ((error as Error).cause) {
        const { message } = (error as Error).cause as any;

        alert('Erro ao deletar produto: ' + message);
      }
    }
  }
}
