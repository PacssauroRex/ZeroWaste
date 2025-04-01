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
import { UserPayload } from '../../../auth/auth.service';

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
    @ViewChild(ModalComponent) modal!: ModalComponent;
  
    public async ngOnInit(): Promise<void> {
      const broadcasts = await this.broadcastService.getAllBroadcasts();
      this.broadcasts.set(broadcasts);
    }

    public async onDeleteBroadcastConfirmation(id: number): Promise<void>{
      const user: UserPayload = JSON.parse(localStorage.getItem('user')!);
      if (user.role !== 'ADMIN') {
        alert('Você não tem permissão para deletar pontos de doações');
        this.modal.closeModal();
        return;
      }
      
      try {
        await this.broadcastService.deleteBroadcast(id);
      
        alert('Ponto de doação deletado com sucesso!');
      
        this.broadcasts.set(this.broadcasts().filter((broadcasts) => broadcasts.id !== id));
      } catch (error) {
        if ((error as Error).cause) {
          const { message } = (error as Error).cause as any;
      
          alert('Erro ao deletar ponto de doação: ' + message);
        }
      } 
    }
}
