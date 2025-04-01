import { Routes } from "@angular/router";
import { authGuard } from "../../auth/auth.guard";
import { ListBroadcastPageComponent } from "./list-broadcast-page/list-broadcast-page.component";

export const routes: Routes = [
  {
    path: 'broadcasts',
    children: [
      {
        path: '',
        title: 'Broadcasts',
        component: ListBroadcastPageComponent,
        canActivate: [authGuard],
      },
    ]
  }
]