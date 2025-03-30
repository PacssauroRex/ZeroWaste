import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild, inject, signal } from '@angular/core';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { DonationPoint } from '../donationPoint';
import { ModalComponent } from "../../../components/modal/modal.component";
import { CardComponent, CardHeaderComponent, CardContentComponent, CardFooterComponent } from "../../../components/card/card.component";
import { FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { UserPayload } from '../../../auth/auth.service';
import { DonationPointService } from '../../../services/DonationPointService';

@Component({
  selector: 'app-list-donation-point-page',
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
  ],
  templateUrl: './list-donation-point-page.component.html',
  styleUrl: './list-donation-point-page.component.css'
})

export class ListDonationPointPageComponent implements OnInit {
  public donationPointService: DonationPointService = inject(DonationPointService);
  public fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  public route = inject(ActivatedRoute);
  public donationsPoints = signal<DonationPoint[]>([]);
  public filtersID = this.fb.group({
    id: [null, Validators.min(0)],
  });
  @ViewChild(ModalComponent) modal!: ModalComponent;

  public async ngOnInit(): Promise<void> {
    const donationsPoints = await this.donationPointService.getAllDonationPoints();
    this.donationsPoints.set(donationsPoints);
  }

  public async onDeleteDonationPointConfirmation(id: number): Promise<void> {
    const user: UserPayload = JSON.parse(localStorage.getItem('user')!);

    if (user.role !== 'ADMIN') {
      alert('Você não tem permissão para deletar pontos de doações');

      this.modal.closeModal();

      return;
    }

    try {
      await this.donationPointService.deleteDonationPoint(id);

      alert('Ponto de doação deletado com sucesso!');

      this.donationsPoints.set(this.donationsPoints().filter((donation_points) => donation_points.id !== id));
    } catch (error) {
      if ((error as Error).cause) {
        const { message } = (error as Error).cause as any;

        alert('Erro ao deletar ponto de doação: ' + message);
      }
    }
  }

  public getErrorMessageId(controlName: string): string | null {
    const validationErrorMessage = this.validationErrorMessage.getValidationErrorMessage(this.filtersID.get(controlName)!);

    return validationErrorMessage;
  }

}

