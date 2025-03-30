import { CommonModule } from '@angular/common';
import { Component, ViewChild, inject, signal } from '@angular/core';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { DonationPoint } from '../donationPoint';
import { InputComponent } from "../../../components/form/input/input.component";
import { FormBuilder, ReactiveFormsModule} from '@angular/forms';
import { DonationPointService } from '../../../services/DonationPointService';
import { UserPayload } from '../../../auth/auth.service';
import { ModalComponent } from '../../../components/modal/modal.component';
import { CardComponent, CardContentComponent, CardFooterComponent, CardHeaderComponent } from '../../../components/card/card.component';

@Component({
  selector: 'app-detail-donation-point-page',
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
  templateUrl: './detail-donation-point-page.component.html',
  styleUrl: './detail-donation-point-page.component.css'
})
export class DetailDonationPointPageComponent {

  donationPointService: DonationPointService = inject(DonationPointService);
  fb = inject(FormBuilder);
  route: ActivatedRoute = inject(ActivatedRoute);
  private router = inject(Router);
  donation_points: DonationPoint | undefined;
  public donationsPoints = signal<DonationPoint[]>([]);
  @ViewChild(ModalComponent) modal!: ModalComponent;
  donationPointId = Number(this.route.snapshot.params['id']);

  public donationPointForm = this.fb.group({
    name: [''],
    email: [''],
    street: [''],
    number: [''],
    city: [''],
    openingTime: [''],
    closingTime: [''],
  });

  public ngOnInit(): void {

    this.donationPointForm.disable();
    this.donationPointService.getDonationPointById(this.donationPointId).then((donationPointResponse => {
    this.donation_points = donationPointResponse;

      this.donationPointForm.setValue({
        name: this.donation_points.name,
        email: this.donation_points.email,
        street: this.donation_points.street,
        number: this.donation_points.number.toString(),
        city: this.donation_points.city,
        openingTime: this.donation_points.openingTime,
        closingTime: this.donation_points.closingTime,
      });

    }));

  }

  public async onDeleteDonationPointConfirmation(id: number): Promise<void> {
    const user: UserPayload = JSON.parse(localStorage.getItem('user')!);

    if (user.role !== 'ADMIN') {
      alert('Você não tem permissão para deletar pontos de doações');
      this.modal.closeModal();
      return;
    }

    try {
      await this.donationPointService.deleteDonationPoint(this.donationPointId);
      alert('Ponto de doação deletado com sucesso!');

      this.donationsPoints.set(this.donationsPoints().filter((donationPoint) => donationPoint.id !== this.donationPointId));
      this.router.navigate(['/donation-points/']);
    } catch (error) {
      if ((error as Error).cause) {
        const { message } = (error as Error).cause as any;

        alert('Erro ao deletar ponto de doação: ' + message);
      }
    }
  }

}
