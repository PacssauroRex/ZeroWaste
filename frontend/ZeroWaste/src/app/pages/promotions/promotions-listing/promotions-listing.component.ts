import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { RouterModule } from '@angular/router';
import { Promotion } from '../promotion';
import { PromotionService } from '../../../services/PromotionService';
import { ModalComponent } from "../../../components/modal/modal.component";
import { CardComponent, CardHeaderComponent, CardContentComponent, CardFooterComponent } from "../../../components/card/card.component";
import { InputComponent } from "../../../components/form/input/input.component";
import { FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';

@Component({
  selector: 'app-promotions-listing',
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
  templateUrl: './promotions-listing.component.html',
  styleUrl: './promotions-listing.component.css'
})

export class PromotionsListingComponent implements OnInit {
  public promotionService: PromotionService = inject(PromotionService);
  public fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  public promotions = signal<Promotion[]>([]);
  public filtersPercentage = this.fb.group({
    percentage: [null, Validators.min(0)],
  });
  public filtersID = this.fb.group({
    id: [null, Validators.min(0)],
  });

  public async ngOnInit(): Promise<void> {
    const promotions = await this.promotionService.getAllPromotions();
    this.promotions.set(promotions);
  }

  public async onSubmitFilterFormPercentage(event: SubmitEvent) {
    event.preventDefault();

    if (this.filtersPercentage.invalid) {
      return;
    }

    try {
      const percentage = this.filtersPercentage.get('percentage')?.value;
      const filteredPromotions = await this.promotionService.getPromotionByPercentage(percentage);
      this.promotions.set(filteredPromotions);
    } catch (error) {
      console.error('Erro ao buscar promoções', error);
      alert('Erro ao buscar promoções');
    }
  }

  public async onSubmitFilterFormProduct(event: SubmitEvent) {
    event.preventDefault();

    if (this.filtersID.invalid) {
      return;
    }

    try {
      const id = this.filtersID.get('id')?.value;
      const filteredPromotions = await this.promotionService.getPromotionByProductId(id);
      this.promotions.set(filteredPromotions);
    } catch (error) {
      console.error('Erro ao buscar promoções', error);
      alert('Erro ao buscar promoções');
    }
  }

  public async onDeletePromotionConfirmation(id: number): Promise<void> {
    try {
      await this.promotionService.deletePromotion(id);

      alert('Promoção deletada com sucesso!');

      this.promotions.set(this.promotions().filter((promotion) => promotion.id !== id));
    } catch (error) {
      if ((error as Error).cause) {
        const { message } = (error as Error).cause as any;

        alert('Erro ao deletar promoção: ' + message);
      }
    }
  }

  public getErrorMessagePercentage(controlName: string): string | null {
    const validationErrorMessage = this.validationErrorMessage.getValidationErrorMessage(this.filtersPercentage.get(controlName)!);

    return validationErrorMessage;
  }
  public getErrorMessageId(controlName: string): string | null {
    const validationErrorMessage = this.validationErrorMessage.getValidationErrorMessage(this.filtersID.get(controlName)!);

    return validationErrorMessage;
  }

}

