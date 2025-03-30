import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild, inject, signal } from '@angular/core';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Donation } from '../donation';
import { ModalComponent } from "../../../components/modal/modal.component";
import { CardComponent, CardHeaderComponent, CardContentComponent, CardFooterComponent } from "../../../components/card/card.component";
import { FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { UserPayload } from '../../../auth/auth.service';
import { DonationService } from '../../../services/DonationService';

@Component({
  selector: 'app-list-donation-page',
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
  templateUrl: './list-donation-page.component.html',
  styleUrl: './list-donation-page.component.css'
})

export class ListDonationPageComponent implements OnInit {

  public donationService: DonationService = inject(DonationService);
  public fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  public route = inject(ActivatedRoute);
  public donations = signal<Donation[]>([]);
  public filtersID = this.fb.group({
    id: [null, Validators.min(0)],
  });
  @ViewChild(ModalComponent) modal!: ModalComponent;

  public async ngOnInit(): Promise<void> {
    const donations = await this.donationService.getAllDonations();
    this.donations.set(donations);
  }

  public async onDeleteDonationConfirmation(id: number): Promise<void> {
    const user: UserPayload = JSON.parse(localStorage.getItem('user')!);

    if (user.role !== 'ADMIN') {
      alert('Você não tem permissão para deletar pontos de doações');

      this.modal.closeModal();

      return;
    }

    try {
      await this.donationService.deleteDonation(id);

      alert('Ponto de doação deletado com sucesso!');

      this.donations.set(this.donations().filter((donation) => donation.id !== id));
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

