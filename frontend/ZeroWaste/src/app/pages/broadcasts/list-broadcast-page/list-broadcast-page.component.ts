import { CommonModule } from '@angular/common';
import { Component, inject, signal, ViewChild } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ButtonComponent } from '../../../components/form/button/button.component';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ModalComponent } from '../../../components/modal/modal.component';
import { CardComponent, CardContentComponent, CardFooterComponent, CardHeaderComponent } from '../../../components/card/card.component';
import { BroadcastService } from '../../../services/BroadcastService';
import { ValidationErrorMessage } from '../../../services/ValidationErrorMessage';
import { Broadcast } from '../broadcast';

@Component({
  selector: 'app-list-broadcast-page',
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
  templateUrl: './list-broadcast-page.component.html',
  styleUrl: './list-broadcast-page.component.css'
})
export class ListBroadcastPageComponent {
  public broadcastService: BroadcastService = inject(BroadcastService);
    public fb = inject(FormBuilder);
    public route = inject(ActivatedRoute);
    public broadcasts = signal<Broadcast[]>([]);
    @ViewChild(ModalComponent) modalConfirmBroadcastTrigger!: ModalComponent;
    @ViewChild(ModalComponent) modalDelete!: ModalComponent;

    public async ngOnInit(): Promise<void> {
      const broadcasts = await this.broadcastService.getAllBroadcasts();
      this.broadcasts.set(broadcasts);
    }

    public async onDeleteBroadcastConfirmation(id: number): Promise<void>{

    }

    public async onConfirmBroadcastTrigger(id: number): Promise<void> {
      await this.broadcastService.triggerBroadcast(id);

      this.modalConfirmBroadcastTrigger.closeModal();
    }
}
