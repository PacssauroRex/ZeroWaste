import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { inject } from '@angular/core';

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [],
  template: `
    <button (click)="logout()" *ngIf="isAuthenticated()">
      Logout
  </button>
  `,
  styleUrl: './logout.component.css'
})

export class LogoutComponent {
  private authService = inject(AuthService);

  logout() {
    this.authService.logout();
  }

  isAuthenticated() {
    return this.authService.isAuthenticated();
  }
}
