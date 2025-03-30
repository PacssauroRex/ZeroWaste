import { Routes } from "@angular/router";
import { authGuard } from "../../auth/auth.guard";
import { CreateDonationPointFormPageComponent } from "./create-donation-point-form-page/create-donation-point-form-page.component";
import { ListDonationPointPageComponent } from "./list-donation-point-page/list-donation-point-page.component";
import { DetailDonationPointPageComponent } from "./detail-donation-point-page/detail-donation-point-page.component";
import { UpdateDonationPointPageComponent } from "./update-donation-point-page/update-donation-point-page.component";

export const routes: Routes = [
  {
    path: 'donation-points',
    children: [
      {
        path: 'create',
        title: 'Create Donation Point',
        component: CreateDonationPointFormPageComponent,
        canActivate: [authGuard],
        data: { role: 'ADMIN' },
      },
      {
        path: '',
        title: 'Donation Points Listing',
        component: ListDonationPointPageComponent,
        canActivate: [authGuard],
      },
      {
        path: ':id',
        title: 'Donation Point Details',
        component: DetailDonationPointPageComponent,
        canActivate: [authGuard],
        data: { role: 'ADMIN' },
      },
      {
        path: 'update/:id',
        title: 'Update Point Details',
        component: UpdateDonationPointPageComponent,
        canActivate: [authGuard],
        data: { role: 'ADMIN' },
      }
    ],
  },
];
