import { Routes } from "@angular/router";
import { CreateProductFormPageComponent } from "./create-product-form-page/create-product-form-page.component";

export const routes: Routes = [
  {
    path: 'products',
    children: [
      {
        path: 'create',
        title: 'Create Product',
        component: CreateProductFormPageComponent,
      }
    ],
  },
];
