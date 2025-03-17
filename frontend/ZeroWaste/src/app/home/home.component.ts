import { Component } from '@angular/core';
import { LogoutComponent } from '../auth/logout/logout.component';
import { ButtonComponent } from "../components/form/button/button.component";
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [
    ButtonComponent,
    RouterModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
