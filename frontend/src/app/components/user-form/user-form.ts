import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { User } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { CepService } from '../../services/cep';
import { Toast } from '../../services/toast';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-form.html',
  styleUrl: './user-form.css'
})
export class UserFormComponent implements OnInit {
  formulario!: FormGroup;
  carregando = signal(false);
  salvando = signal(false);
  erroGeral = signal('');
  erroPorCampo = signal<Record<string, string>>({});
  modoEdicao = signal(false);
  titulo = signal('Cadastrar usuário');

constructor(
  private readonly fb: FormBuilder,
  private readonly route: ActivatedRoute,
  private readonly router: Router,
  private readonly userService: UserService,
  private readonly cepService: CepService,
  private readonly toast: Toast
) {
  this.criarFormulario();

}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      const id = Number(idParam);

      if (!Number.isNaN(id)) {
        this.modoEdicao.set(true);
        this.titulo.set('Editar usuário');
        this.carregarUsuario(id);
      }
    }
  }

  criarFormulario(): void {
    this.formulario = this.fb.group({
      nome: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      telefone: ['', [Validators.required]],
      enderecos: this.fb.array([this.criarEnderecoGrupo()])
    });
  }

  criarEnderecoGrupo(): FormGroup {
    return this.fb.group({
      cep: ['', [Validators.required]],
      numero: ['', [Validators.required]],
      rua: [''],
      bairro: [''],
      cidade: [''],
      estado: ['']
    });
  }

  get enderecos(): FormArray {
    return this.formulario.get('enderecos') as FormArray;
  }

  carregarUsuario(id: number): void {
    this.carregando.set(true);
    this.erroGeral.set('');

    this.userService.getById(id).subscribe({
      next: (usuario) => {
        this.formulario.patchValue({
          nome: usuario.nome,
          email: usuario.email,
          telefone: usuario.telefone
        });

        this.enderecos.clear();

        if (usuario.enderecos?.length) {
          usuario.enderecos.forEach((endereco) => {
            this.enderecos.push(
              this.fb.group({
                cep: [endereco.cep, [Validators.required]],
                numero: [endereco.numero, [Validators.required]],
                rua: [endereco.rua || ''],
                bairro: [endereco.bairro || ''],
                cidade: [endereco.cidade || ''],
                estado: [endereco.estado || '']
              })
            );
          });
        } else {
          this.enderecos.push(this.criarEnderecoGrupo());
        }

        this.carregando.set(false);
      },
      error: () => {
        this.carregando.set(false);
        this.erroGeral.set('Não foi possível carregar os dados do usuário para edição.');
      }
    });
  }

  adicionarEndereco(): void {
    this.enderecos.push(this.criarEnderecoGrupo());
  }

  removerEndereco(index: number): void {
    if (this.enderecos.length > 1) {
      this.enderecos.removeAt(index);
    }
  }

  buscarCep(index: number): void {
  const grupoEndereco = this.enderecos.at(index) as FormGroup;
  const cep = grupoEndereco.get('cep')?.value;

  if (!cep) {
    return;
  }

  grupoEndereco.get('rua')?.setValue('Buscando...');

  this.cepService.consultar(cep).subscribe({
    next: (resultado) => {
      grupoEndereco.patchValue({
        rua: resultado.rua,
        bairro: resultado.bairro,
        cidade: resultado.cidade,
        estado: resultado.estado
      });
      grupoEndereco.get('cep')?.setErrors(null);
    },
    error: () => {
      grupoEndereco.patchValue({
        rua: '',
        bairro: '',
        cidade: '',
        estado: ''
      });
      grupoEndereco.get('cep')?.setErrors({ cepInvalido: true });
    }
  });
}

  marcarCamposComoTocados(): void {
    this.formulario.markAllAsTouched();
    this.enderecos.controls.forEach((controle) => {
      if (controle instanceof FormGroup) {
        Object.values(controle.controls).forEach((campo) => campo.markAsTouched());
      }
    });
  }

  salvar(): void {
    if (this.formulario.invalid) {
      this.marcarCamposComoTocados();
      this.erroGeral.set('Preencha os campos obrigatórios antes de salvar.');
      return;
    }

    this.salvando.set(true);
    this.erroGeral.set('');
    this.erroPorCampo.set({});

    const payload: User = {
      nome: this.formulario.value.nome,
      email: this.formulario.value.email,
      telefone: this.formulario.value.telefone,
      enderecos: this.formulario.value.enderecos.map((endereco: any) => ({
        cep: endereco.cep,
        numero: endereco.numero,
        rua: endereco.rua || undefined,
        bairro: endereco.bairro || undefined,
        cidade: endereco.cidade || undefined,
        estado: endereco.estado || undefined
      }))
    };

    const operacao = this.modoEdicao()
      ? this.userService.update(Number(this.route.snapshot.paramMap.get('id')), payload)
      : this.userService.create(payload);

    operacao.subscribe({
  next: (usuarioSalvo) => {
    this.salvando.set(false);
    this.toast.sucesso(
      this.modoEdicao() ? 'Usuário atualizado com sucesso!' : 'Usuário criado com sucesso!'
    );
    this.router.navigate(['/users', usuarioSalvo.id]);
  },
  error: (erro) => {
    this.salvando.set(false);
    this.tratarErro(erro);
  }
});
  }

  tratarErro(erro: any): void {
    const mensagem = erro?.error?.message || erro?.error?.error || 'Erro ao salvar o usuário.';

    if (erro?.status === 400 && erro?.error?.messages) {
      this.erroGeral.set('Não foi possível salvar o usuário. Corrija os campos abaixo.');
      this.erroPorCampo.set(
        Object.fromEntries(
          Object.entries(erro.error.messages).map(([campo, mensagemCampo]) => [campo, String(mensagemCampo)])
        )
      );
      this.toast.erro('Corrija os campos indicados antes de salvar.');
      return;
    }

    if (erro?.status === 400 && mensagem) {
      this.erroGeral.set(mensagem);
      this.toast.erro(mensagem);
      return;
    }

    this.erroGeral.set('Não foi possível salvar o usuário. Tente novamente.');
    this.toast.erro('Não foi possível salvar o usuário. Tente novamente.');
  }

  voltarParaListagem(): void {
    this.router.navigate(['/users']);
  }
}