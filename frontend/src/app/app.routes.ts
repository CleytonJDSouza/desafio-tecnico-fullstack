import { Routes } from '@angular/router';
import { UserListComponent } from './components/user-list/user-list';

export const routes: Routes = [
  { path: '', redirectTo: '/users', pathMatch: 'full' },
  { path: 'users', component: UserListComponent },
  // TODO: adicionar quando criarmos o componente de detalhes (TASK-33)
  // { path: 'users/:id', component: UserDetailComponent },
  // TODO: adicionar quando criarmos o componente de formulário (TASK-34)
  // { path: 'users/:id/edit', component: UserFormComponent },
];