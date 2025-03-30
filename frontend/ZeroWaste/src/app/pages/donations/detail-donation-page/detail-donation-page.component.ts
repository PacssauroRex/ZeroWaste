import { CommonModule, formatDate } from '@angular/common';
import { Component, ViewChild, inject, signal } from '@angular/core';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Donation } from '../donation';
import { InputComponent } from "../../../components/form/input/input.component";
import { FormBuilder, ReactiveFormsModule} from '@angular/forms';
import { UserPayload } from '../../../auth/auth.service';
import { ModalComponent } from '../../../components/modal/modal.component';
import { CardComponent, CardContentComponent, CardFooterComponent, CardHeaderComponent } from '../../../components/card/card.component';
import { DonationService } from '../../../services/DonationService';

@Component({
  selector: 'app-detail-donation-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonComponent,
    InputComponent,
    RouterModule,
    ModalComponent,
    CardComponent,
    CardHeaderComponent,
    CardContentComponent,
    CardFooterComponent,
  ],
  templateUrl: './detail-donation-page.component.html',
  styleUrl: './detail-donation-page.component.css'
})
export class DetailDonationPageComponent {

  donationService: DonationService = inject(DonationService);
  fb = inject(FormBuilder);
  route: ActivatedRoute = inject(ActivatedRoute);
  private router = inject(Router);
  donation: Donation | undefined;
  public donations = signal<Donation[]>([]);
  @ViewChild(ModalComponent) modal!: ModalComponent;
  donationId = Number(this.route.snapshot.params['id']);

  public productsId: any[] = [];

  public donationForm = this.fb.group({
    name: [''],
    date: [''],
    productsId: [''],
  });

  public ngOnInit(): void {

    this.donationForm.disable();
    this.donationService.getDonationById(this.donationId).then((donationResponse => {
    this.donation = donationResponse;

      this.donationForm.setValue({
        name: this.donation.name,
        date: formatDate(this.donation.date, 'dd/MM/yyyy', 'en-US'),
        productsId: this.donation.products.map(productsId => productsId.name).join(', '),
      });
      this.donation.products.forEach(productsId => this.productsId.push({ name: productsId.name, id: productsId.id }));

    }));

  }

  public async onDeleteDonationConfirmation(id: number): Promise<void> {
    const user: UserPayload = JSON.parse(localStorage.getItem('user')!);

    if (user.role !== 'ADMIN') {
      alert('Você não tem permissão para deletar pontos de doações');
      this.modal.closeModal();
      return;
    }

    try {
      await this.donationService.deleteDonation(this.donationId);
      alert('Ponto de doação deletado com sucesso!');

      this.donations.set(this.donations().filter((donations) => donations.id !== this.donationId));
      this.router.navigate(['/donations']);
    } catch (error) {
      if ((error as Error).cause) {
        const { message } = (error as Error).cause as any;

        alert('Erro ao deletar ponto de doação: ' + message);
      }
    }
  }

}
