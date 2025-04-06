import { Routes } from "@angular/router";
import { authGuard } from "../../auth/auth.guard";
import { ListBroadcastPageComponent } from "./list-broadcast-page/list-broadcast-page.component";
import { CreateBroadcastListFormPageComponent } from "./create-broadcast-list-form-page/create-broadcast-list-form-page.component";

export const routes: Routes = [
  {
    path: 'broadcasts',
    children: [
      {
        path: '',
        title: 'Listas de transmissão',
        component: ListBroadcastPageComponent,
        canActivate: [authGuard],
      },
      {
        path: 'create',
        title: 'Cadastrar lista de transmissão',
        component: CreateBroadcastListFormPageComponent,
        canActivate: [authGuard],
        data: { role: 'ADMIN' },
      },
    ]
  }
]
