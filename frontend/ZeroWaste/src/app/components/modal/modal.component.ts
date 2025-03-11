import { Component, ElementRef, Input, Output, ViewChild } from '@angular/core';
import { CardComponent } from "../card/card.component";

@Component({
  selector: 'app-modal',
  imports: [CardComponent],
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.css',
  standalone: true,
})
export class ModalComponent {
  @ViewChild('modal') modal!: ElementRef<HTMLDialogElement>;

  openModal() {
    this.modal.nativeElement.showModal();
  }

  closeModal() {
    this.modal.nativeElement.close();
  }
}
