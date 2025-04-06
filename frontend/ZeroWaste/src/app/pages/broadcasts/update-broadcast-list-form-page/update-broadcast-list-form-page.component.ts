import { Component, OnInit, inject, signal } from "@angular/core";
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";
import { ValidationErrorMessage } from "../../../services/ValidationErrorMessage";
import { InputComponent } from "../../../components/form/input/input.component";
import { TextareaComponent } from "../../../components/form/textarea/textarea.component";
import { SelectComponent } from "../../../components/form/select/select.component";
import { ButtonComponent } from "../../../components/form/button/button.component";
import { CommonModule } from "@angular/common";
import { BroadcastService } from "../../../services/BroadcastService";
import { Product } from "../../products/product";
import { UpdateBroadcastDTO } from "../broadcast";
import { ProductService } from "../../../services/ProductService";

@Component({
  selector: 'app-update-broadcast-list-form-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    InputComponent,
    TextareaComponent,
    SelectComponent,
    ButtonComponent,
  ],
  templateUrl: './update-broadcast-list-form-page.component.html',
  styleUrl: './update-broadcast-list-form-page.component.css',
})
export class UpdateBroadcastListFormPageComponent implements OnInit {
  private fb = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);
  private router = inject(Router);
  public route = inject(ActivatedRoute);
  private productsService = inject(ProductService);
  private broadcastsService = inject(BroadcastService);

  public broadcastListForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(255)]],
    sendType: ['', [Validators.required]],
    emails: this.fb.array([this.fb.control('', [Validators.required, Validators.email])]),
    productsIds: [[] as string[], [Validators.required, Validators.minLength(1)]],
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

    const broadcastListId = this.route.snapshot.paramMap.get('id')!;
    const requestBody = {
      ...this.broadcastListForm.value,
      productsIds: (this.broadcastListForm.value.productsIds as unknown as string[])!.map((id: string) => parseInt(id)),
    } as UpdateBroadcastDTO;

    try {
      await this.broadcastsService.updateBroadcast(
        Number(broadcastListId),
        requestBody,
      );

      this.router.navigate(['/broadcasts']);
    } catch (error) {
      console.error('Erro ao atualizar produto', error);
      alert('Erro ao atualizar produto');
    }
  }

  public async ngOnInit(): Promise<void> {
    const products = await this.productsService.getAllProducts();
    this.products.set(products);

    const broadcastListId = this.route.snapshot.paramMap.get('id')!;

    const broadcastListData = await this.broadcastsService.getBroadcastById(Number(broadcastListId));

    this.broadcastListForm.setValue({
      name: broadcastListData.name,
      description: broadcastListData.description,
      sendType: broadcastListData.sendType,
      emails: broadcastListData.emails,
      productsIds: broadcastListData.productsIds.map((id) => id.toString()),
    });
  }
}
