import { Component, OnInit, inject, signal } from "@angular/core";
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router, RouterModule } from "@angular/router";
import { ValidationErrorMessage } from "../../../services/ValidationErrorMessage";
import { futureDateValidator } from "../../../utils/validators/future-date";
import { InputComponent } from "../../../components/form/input/input.component";
import { TextareaComponent } from "../../../components/form/textarea/textarea.component";
import { SelectComponent } from "../../../components/form/select/select.component";
import { ButtonComponent } from "../../../components/form/button/button.component";
import { CommonModule } from "@angular/common";
import { ProductService } from "../../../services/ProductService";
import { Product } from "../../products/product";
import { BroadcastService } from "../../../services/BroadcastService";
import { CreateBroadcastDTO } from "../broadcast";

@Component({
  selector: 'app-create-broadcast-list-form-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    InputComponent,
    TextareaComponent,
    SelectComponent,
    ButtonComponent,
  ],
  templateUrl: './create-broadcast-list-form-page.component.html',
  styleUrl: './create-broadcast-list-form-page.component.css',
})
export class CreateBroadcastListFormPageComponent implements OnInit {
  private fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  private router = inject(Router);
  private productsService = inject(ProductService);
  private broadcastsService = inject(BroadcastService);

  public broadcastListForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(255)]],
    sendType: ['', [Validators.required]],
    emails: this.fb.array([this.fb.control('', [Validators.required, Validators.email])]),
    productsIds: [[], [Validators.required, Validators.minLength(1)]],
  });
  public products = signal<Product[]>([]);

  public get emails() {
    return this.broadcastListForm.get('emails') as FormArray;
  }

  public set emails(value: FormArray) {
    this.broadcastListForm.setControl('emails', value);
  }

  public addEmail() {
    this.emails.push(
      this.fb.control('', [Validators.required, Validators.email])
    );
  }

  public removeEmail(index: number) {
    if (this.emails.length > 1) {
      this.emails = this.fb.array(this.emails.controls.filter((_, i) => i !== index));
    }
  }

  public getErrorMessage(controlName: string, index?: number): string | null {
    if (index !== undefined) {
      return this.validationErrorMessage.getValidationErrorMessage(
        this.broadcastListForm.get(controlName)!.get(index.toString())!,
      );
    }

    const validationErrorMessage = this.validationErrorMessage.getValidationErrorMessage(this.broadcastListForm.get(controlName)!);

    return validationErrorMessage;
  }

  public async onSubmit(event: SubmitEvent) {
    event.preventDefault();

    Object.values(this.broadcastListForm.controls).forEach(control => {
      control.markAsTouched();
    });

    if (this.broadcastListForm.invalid) {
      return;
    }

    console.log('Formulário válido', this.broadcastListForm.value);

    const requestBody = {
      ...this.broadcastListForm.value,
      productsIds: (this.broadcastListForm.value.productsIds as unknown as string[])!.map((id: string) => parseInt(id)),
    } as CreateBroadcastDTO;

    try {
      await this.broadcastsService.createBroadcast(requestBody);
      alert('Lista de transmissão salvo com sucesso');
      this.router.navigate(['/broadcasts']);
    } catch (error) {
      console.error('Erro ao salvar lista de transmissão', error);
      alert('Erro ao salvar lista de transmissão');
    }
  }

  public async ngOnInit(): Promise<void> {
    const products = await this.productsService.getAllProducts();
    this.products.set(products);
  }
}
