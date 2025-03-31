import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductService } from '../services/ProductService';
import { PromotionService } from '../services/PromotionService';
import { CardComponent, CardContentComponent, CardFooterComponent, CardHeaderComponent } from '../components/card/card.component';
import { Promotion } from '../pages/promotions/promotion';
import { Product } from '../pages/products/product';
import { SelectComponent } from '../components/form/select/select.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    RouterModule,
    CommonModule,
    CardComponent,
    CardHeaderComponent,
    CardContentComponent,
    SelectComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})

export class HomeComponent implements OnInit {

  // Injeção dos serviços
  public productService: ProductService = inject(ProductService);
  public promotionService: PromotionService = inject(PromotionService);

  // Atributos da página
  public expiringProducts = signal<number>(0);
  public activePromotions = signal<Promotion[]>([]);
  public ready = signal<boolean>(false);

  public async ngOnInit(): Promise<void> {

    try {

      const [expiring, promotions] = await Promise.all([
        this.productService.getExpiringProducts(),
        this.promotionService.getActivePromotions()
      ]);

      this.expiringProducts.set(expiring);
      this.activePromotions.set(promotions);
    }

    catch (error) {
      console.error("Erro ao carregar dados:", error);
    }

    finally {
      this.ready.set(true);
    }
  }

  // Método trackBy para promoções
  public trackByPromotionId(index: number, item: Promotion): number {
    return item.id;
  }

  // Método trackBy para produtos dentro das promoções
  public trackByProductId(index: number, item: Product): number {
    return item.id;
  }
}
