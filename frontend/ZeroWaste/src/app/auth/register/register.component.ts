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
          <label>Nome</label>
          <input type="text" formControlName="nome" placeholder="Informe seu nome" required/>
          <div class="erros" *ngIf="registroForm.get('nome')?.invalid && registroForm.get('nome')?.touched">
            Nome é obrigatório.
          </div>

          <label>E-mail</label>
          <input type="email" formControlName="email" placeholder="Informe seu e-mail" required/>
          <div class="erros" *ngIf="registroForm.get('email')?.invalid && registroForm.get('email')?.touched">
            E-mail inválido.
          </div>

          <label>Senha</label>
          <input type="password" formControlName="senha" placeholder="Informe sua senha" required/>
          <div class="erros" *ngIf="registroForm.get('senha')?.invalid && registroForm.get('senha')?.touched">
            Senha é obrigatória.
          </div>

          <label>Confirmação de senha</label>
          <input type="password" formControlName="confirmSenha" placeholder="Confirme sua senha" required/>
          <div class="erros" *ngIf="registroForm.get('confirmSenha')?.invalid && registroForm.get('confirmSenha')?.touched">
            A confirmação de senha é obrigatória.
          </div>

          <div class="checkbox">
            <input type="checkbox" formControlName="role" class="checkbox">
            <label id="checkbox">ADMIN</label>
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

      console.log(user)

      this.authService.register(user)
      .then(() => this.router.navigate(['/login']))
      .catch(err => alert('Erro ao registrar novo usuário: ' + err));
    }
  }
}
