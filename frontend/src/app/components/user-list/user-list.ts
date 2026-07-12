import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css'
})
export class UserListComponent implements OnInit {
  usuarios = signal<User[]>([]);
  carregando = signal(false);
  erro = signal('');

  constructor(private readonly userService: UserService) {}

  ngOnInit(): void {
    this.carregarUsuarios();
  }

  carregarUsuarios(): void {
    this.carregando.set(true);
    this.erro.set('');

    this.userService.getAll().subscribe({
      next: (usuarios) => {
        this.usuarios.set(usuarios);
        this.carregando.set(false);
      },
      error: () => {
        this.carregando.set(false);
        this.erro.set('Não foi possível carregar os usuários. Tente novamente mais tarde.');
      }
    });
  }

  confirmarExclusao(usuario: User): void {
    if (!usuario.id) {
      return;
    }

    const confirmado = window.confirm(`Deseja realmente excluir ${usuario.nome}?`);

    if (!confirmado) {
      return;
    }

    this.userService.delete(usuario.id).subscribe({
      next: () => {
        this.usuarios.update((lista) => lista.filter((item) => item.id !== usuario.id));
      },
      error: () => {
        this.erro.set(`Não foi possível excluir ${usuario.nome}.`);
      }
    });
  }
}