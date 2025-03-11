import { Routes } from "@angular/router";
import { CreateProductFormPageComponent } from "./create-product-form-page/create-product-form-page.component";
import { authGuard } from "../../auth/auth.guard";
import { ListProductPageComponent } from "./list-product-page/list-product-page.component";
import { DetailProductPageComponent } from "./detail-product-page/detail-product-page.component";
import { UpdateProductFormPageComponent } from "./update-product-form-page/update-product-form-page.component";

export const routes: Routes = [
  {
    path: 'products',
    children: [
      {
        path: '',
        title: 'Products',
        component: ListProductPageComponent,
        data: { role: 'ADMIN' },
      },
      {
        path: 'create',
        title: 'Create Product',
        component: CreateProductFormPageComponent,
        canActivate: [authGuard],
        data: { role: 'ADMIN' },
      },
      {
        path: ':id',
        title: 'Product detailing',
        component: DetailProductPageComponent,
        canActivate: [authGuard],
        data: { role: 'ADMIN' },
      },
      {
        path: 'update/:id',
        title: 'Update Product',
        component: UpdateProductFormPageComponent,
        canActivate: [authGuard],
        data: { role: 'ADMIN' },
      }
    ],
  }
];
