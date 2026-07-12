import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-detail.html',
  styleUrl: './user-detail.css'
})
export class UserDetailComponent implements OnInit {
  usuario = signal<User | null>(null);
  carregando = signal(false);
  erro = signal('');

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly userService: UserService
  ) {}

  ngOnInit(): void {
    this.carregarUsuario();
  }

  carregarUsuario(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = Number(idParam);

    if (!idParam || Number.isNaN(id)) {
      this.erro.set('ID do usuário inválido.');
      return;
    }

    this.carregando.set(true);
    this.erro.set('');

    this.userService.getById(id).subscribe({
      next: (usuario) => {
        this.usuario.set(usuario);
        this.carregando.set(false);
      },
      error: () => {
        this.carregando.set(false);
        this.erro.set('Não foi possível carregar os dados do usuário.');
      }
    });
  }

  voltarParaListagem(): void {
    this.router.navigate(['/users']);
  }

  navegarParaEdicao(): void {
    const usuarioAtual = this.usuario();

    if (usuarioAtual?.id) {
      this.router.navigate(['/users', usuarioAtual.id, 'edit']);
    }
  }
}
