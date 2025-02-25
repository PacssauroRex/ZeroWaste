import {Component, inject} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, ReactivateFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../auth.service';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactivateFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  });

  onSubmit() {
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value;
      this.authService.login(email, password)
      .then(user => this.router.navigate(['/home']))
      .catch(err => alert('Login error: ' + err));
    }
  }
}
