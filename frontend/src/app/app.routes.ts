import { Routes } from '@angular/router';
import { UserDetailComponent } from './components/user-detail/user-detail';
import { UserListComponent } from './components/user-list/user-list';

export const routes: Routes = [
  { path: '', redirectTo: '/users', pathMatch: 'full' },
  { path: 'users', component: UserListComponent },
  { path: 'users/:id', component: UserDetailComponent },
  // TODO: adicionar quando criarmos o componente de formulário (TASK-34)
  // { path: 'users/:id/edit', component: UserFormComponent },
];