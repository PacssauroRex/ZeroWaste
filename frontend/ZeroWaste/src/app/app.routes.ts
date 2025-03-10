import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { HomeComponent } from './home/home.component';

import { routes as productsRoutes } from './pages/products/products.routes';
import { routes as promotionsRoutes } from './pages/promotions/promotions.routes';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
    title: 'Login'
  },
  {
    path: 'registro',
    component: RegisterComponent,
    title: 'Cadastro'
  },
  {
    path: 'home',
    component: HomeComponent,
    title: "Homepage"
  },
  ...productsRoutes,
  ...promotionsRoutes
];

export default routes;
