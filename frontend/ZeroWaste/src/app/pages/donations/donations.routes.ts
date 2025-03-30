import { Routes } from "@angular/router";
import { authGuard } from "../../auth/auth.guard";
import { CreateDonationFormPageComponent } from "./create-donation-form-page/create-donation-form-page.component";
import { ListDonationPageComponent } from "./list-donation-page/list-donation-page.component";
import { DetailDonationPageComponent } from "./detail-donation-page/detail-donation-page.component";
import { UpdateDonationPageComponent } from "./update-donation-page/update-donation-page.component";

export const routes: Routes = [
  {
    path: 'donations',
    children: [
      {
        path: 'create',
        title: 'Create Donation',
        component: CreateDonationFormPageComponent,
        canActivate: [authGuard],
      },
      {
        path: '',
        title: 'Donation Listing',
        component: ListDonationPageComponent,
        canActivate: [authGuard],
      },
      {
        path: ':id',
        title: 'Donation Details',
        component: DetailDonationPageComponent,
        canActivate: [authGuard],
      },
      {
        path: 'update/:id',
        title: 'Update Donation Details',
        component: UpdateDonationPageComponent,
        canActivate: [authGuard],
      },
    ],
  },
];
