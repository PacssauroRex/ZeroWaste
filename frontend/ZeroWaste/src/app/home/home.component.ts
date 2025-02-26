import { Component } from '@angular/core';
import { LogoutComponent } from '../auth/logout/logout.component';

@Component({
  selector: 'app-home',
  imports: [LogoutComponent],
  template: `
    <h1>Home Page</h1>
    <app-logout></app-logout>
  `,
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
