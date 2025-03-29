import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { LogoutComponent } from '../../auth/logout/logout.component';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, LogoutComponent],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  private authService = inject(AuthService);

  links = [
    { name: 'Produtos', url: '/products' },
    { name: 'Promoções', url: '/promotions' }
  ];

  isAuthenticated() {
    return this.authService.isAuthenticated();
  }
}
