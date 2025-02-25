import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login/login.component';
import { RegisterComponent } from './auth/register/register.component';

export const routes: Routes = [
    {
        path: '', redirectTo: 'login', pathMatch:'full'
    },
    {
        path: 'login', component: LoginComponent, title: 'Login'
    },
    {
        path: 'registro', component: RegisterComponent, title: 'Cadastro'
    }
];
