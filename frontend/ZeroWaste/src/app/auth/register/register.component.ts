import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [RouterModule, CommonModule, ReactiveFormsModule],
  template: `
    <div>
      <div class="registro-container">
      <form [formGroup]="registroForm" (ngSubmit)="onSubmit()">
          <h2>Registro</h2>
          <label>E-mail</label>
          <input type="email" formControlName="email" placeholder="Informe seu e-mail" required/>
          <div *ngIf="registroForm.get('email')?.invalid && registroForm.get('email')?.touched">
            E-mail inválido.
          </div>

          <label>Senha</label>
          <input type="password" formControlName="senha" placeholder="Informe sua senha" required/>
          <div *ngIf="registroForm.get('senha')?.invalid && registroForm.get('senha')?.touched">
            Senha é obrigatória.
          </div>

          <label>Confirmação de senha</label>
          <input type="password" formControlName="confirmSenha" placeholder="Confirme sua senha" required/>
          <div *ngIf="registroForm.get('confirmSenha')?.invalid && registroForm.get('confirmSenha')?.touched">
            A confirmação de senha é obrigatória.
          </div>
          
          <button type="submit" [disabled]="registroForm.invalid">Registrar</button>
        </form>
      </div>
    </div>
  `,
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private formBuilder = inject(FormBuilder)

  registroForm: FormGroup = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    senha: ['', [Validators.required, Validators.minLength(4)]],
    confirmSenha: ['', [Validators.required]]
  }, {validators: this.confirmaSenhaValidartor});

  confirmaSenhaValidartor (formGroup: FormGroup) {
    const senha = formGroup.get('senha')?.value;
    const confimacao = formGroup.get('confirmSenha')?.value;
    return senha === confimacao ? null : {passwordMismatch: true};
  }

  onSubmit() {
    if(this.registroForm.valid) {
      const { email, password } = this.registroForm.value;
      const user = {id: 0, email, password, role: 'USER'};

      this.authService.register(user)
      .then(() => this.router.navigate(['/login']))
      .catch(err => alert('Erro ao registrar novo usuário: ' + err));
    }
  }
}
