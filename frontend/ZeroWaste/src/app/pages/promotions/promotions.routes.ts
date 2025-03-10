import { Routes } from "@angular/router";
import { authGuard } from "../../auth/auth.guard";
import { CreatePromotionFormPageComponent } from "./create-promotion-form-page/create-promotion-form-page.component";
import { UpdatePromotionFormPageComponent } from "./update-promotion-form-page/update-promotion-form-page.component";
import { DetailProductPageComponent } from "../products/detail-product-page/detail-product-page.component";
import { DetailPromotionPageComponent } from "./detail-promotion-page/detail-promotion-page.component";

export const routes: Routes = [
  {
    path: 'promotions',
    children: [
      {
        path: 'create',
        title: 'Create Promotion',
        component: CreatePromotionFormPageComponent,
        canActivate: [authGuard],
      },
      {
        path: 'update/:id',
        title: 'Update Promotion',
        component: UpdatePromotionFormPageComponent,
        canActivate: [authGuard],
      },
      {
        path: ':id',
        title: 'Promotion Detailing',
        component: DetailPromotionPageComponent,
        canActivate: [authGuard],
      },
    ],
  },
];
