import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router, RouterModule } from '@angular/router';
import { InputComponent } from '../../components/form/input/input.component';
import { ButtonComponent } from '../../components/form/button/button.component';
import { ValidationErrorMessage } from '../../services/ValidationErrorMessage';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [
    RouterModule, 
    CommonModule, 
    ReactiveFormsModule,
    InputComponent,
    ButtonComponent
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private formBuilder = inject(FormBuilder);
  private validationErrorMessage = inject(ValidationErrorMessage);

  registroForm: FormGroup = this.formBuilder.group({
    nome: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    senha: ['', [Validators.required, Validators.minLength(8)]],
    confirmSenha: ['', [Validators.required]],
    role: ['']
  }, {validators: this.confirmaSenhaValidartor});

  confirmaSenhaValidartor (formGroup: FormGroup) {
    const senha = formGroup.get('senha')?.value;
    const confimacao = formGroup.get('confirmSenha')?.value;
    return senha === confimacao ? null : {passwordMismatch: true};
  }

  onSubmit() {
    if(this.registroForm.valid) {
      const { nome, email, senha, role } = this.registroForm.value;

      var finalRole = 'USER';
      if(role == true)
        finalRole = 'ADMIN';

      const user = {name: nome, email, password: senha, role: finalRole};

      try {
        this.authService.register(user)
        this.router.navigate(['/login'])
        alert('Usuário cadastrado com sucesso')  
      }
      catch(err) {
        alert('Erro ao registrar novo usuário: ' + err); 
      }
    }
  }

  public getErrorMessage(controlName: string): string | null {
    const validationErrorMessage = this.validationErrorMessage.getValidationErrorMessage(this.registroForm.get(controlName)!);

    return validationErrorMessage;
  }
}
